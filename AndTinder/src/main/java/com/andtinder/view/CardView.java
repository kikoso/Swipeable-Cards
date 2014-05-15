package com.andtinder.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.andtinder.R;
import com.andtinder.Utils;
import com.andtinder.model.CardModel;
import com.andtinder.model.Likes.Like;
import com.andtinder.model.Orientations.Orientation;

public class CardView extends ViewGroup {
	/**
	 * The model of our car
	 * @see #CardModel
	 */
	public CardModel cardModel;
	
	/**
	 * Current status of the liked card.
	 */
	private Like mLiked = Like.None;
	
	/**
	 * The CardContainer to which the card is attached
	 */
	public CardContainer parentView;
	
	/**
	 * mAlphaValue represents the amount of transparency the image will have
	 */
	private float mAlphaValue = 0;

    private float rotation;
	private int lastX = 0, lastY = 0;
	private int xCoord, yCoord;
	private int screenCenter;
	
	private Button buttonLike, buttonDislike;
    private LinearLayout informationContainerLayout;
    private LinearLayout imageContainerLayout;

    private TextView firstTextView, secondTextView;
    private ImageView cardImage, firstImageView, secondImageView;

    private TextView mTextViewInformationText;

    private OnCardDimissedDelegate mOnCardDimissedDelegate = null;

    public interface OnCardDimissedDelegate {
        void onLike(CardView cardView);
        void onDislike(CardView cardView);
    }

	public CardView(CardContainer parentView, CardModel cardModel) {
		super(parentView.getContext());
		this.parentView = parentView;
		this.cardModel = cardModel;
		initCard();
	}

	private void initCard() {
		screenCenter = getScreenSize().x / 2;

		LayoutInflater inflate = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflate.inflate(R.layout.card_layout, this, true);

        initGUI();

		setLayoutParams(new LayoutParams((getScreenSize().x - 80), getScreenSize().y / 2));

		setX(40);
		setY(40);
		cardImage.setBackgroundResource(cardModel.getCardImageResource());

		if (((CardContainer)parentView).getOrientation() == Orientation.Disordered ) {
			int rotationGradient = parentView.getChildCount() % 5;
			if (rotationGradient == 0) {
                rotation = -1;
		    } else if (rotationGradient == 1) {
                rotation = -5;
            } else if (rotationGradient == 2) {
                rotation = -3;
            } else if (rotationGradient == 3) {
                rotation = 7;
            } else if (rotationGradient == 4) {
                rotation = -2;
            } else if (rotationGradient == 5) {
                rotation = 5;
            }
            setRotation(rotation);
        }
		
		buttonLike = new Button(getContext());
		buttonLike.setLayoutParams(new LayoutParams(100, 50));
		buttonLike.setBackgroundResource(R.drawable.like);
		buttonLike.setX(20);
		buttonLike.setY(-250);
		buttonLike.setAlpha(mAlphaValue);
		imageContainerLayout.addView(buttonLike);
		
		buttonDislike = new Button(getContext());
		buttonDislike.setLayoutParams(new LayoutParams(100, 50));
		buttonDislike.setBackgroundResource(R.drawable.dislike);
		buttonDislike.setX(getScreenSize().x - 120 - 120);
		buttonDislike.setY(-300);
		buttonDislike.setAlpha(0);
		imageContainerLayout.addView(buttonDislike);

		informationContainerLayout.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (cardModel.getIntent() != null) {
                    getContext().startActivity(cardModel.getIntent());
                }
            }
        });

		imageContainerLayout.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				xCoord = (int) event.getRawX();
				yCoord = (int) event.getRawY();

				if (lastX == 0 || lastY == 0) {
					lastX = xCoord;
					lastY = yCoord;
				}

				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					break;
				case MotionEvent.ACTION_MOVE:
					xCoord = (int) event.getRawX();
					yCoord = (int) event.getRawY();
					setX(getX() + (xCoord - lastX));
					setY(getY() + (yCoord - lastY));
					if (xCoord >= screenCenter) {
						temporalMovementToTheRight();
					} else {
						temporalMovementToTheLeft();
					}
					lastX = xCoord;
					lastY = yCoord;
					break;
				case MotionEvent.ACTION_UP:
					xCoord = (int) event.getRawX();
					yCoord = (int) event.getRawY();
					lastX = 0;
					lastY = 0;
					buttonDislike.setAlpha(0);
					buttonLike.setAlpha(0);

					if (mLiked == Like.None) {
						translateCardToCenter();
					} else if (mLiked == Like.Liked) {
						translateCardToLeft();
                        if (mOnCardDimissedDelegate != null) {
                            mOnCardDimissedDelegate.onLike(getInstance());
                        }
					} else if (mLiked == Like.Disliked) {
						translateCardToRight();
                        if (mOnCardDimissedDelegate != null) {
                            mOnCardDimissedDelegate.onDislike(getInstance());
                        }
					}
					break;
				default:
					break;
				}
				return true;
			}
		});
	}

	private View getView() {
		return this;
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		int itemWidth = (r-l)/getChildCount();
		for(int i=0; i< this.getChildCount(); i++){
			View v = getChildAt(i);
			v.layout(itemWidth*i, t, (i+1)*itemWidth, b);
			v.layout(itemWidth*i, 0, (i+1)*itemWidth, b-t);
		}
	}
	
	@Override

	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
	      super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	      int wspec = MeasureSpec.makeMeasureSpec(
	                  getMeasuredWidth()/getChildCount(), MeasureSpec.EXACTLY);
	      int hspec = MeasureSpec.makeMeasureSpec(
	                  getMeasuredHeight(), MeasureSpec.EXACTLY);
	      for(int i=0; i<getChildCount(); i++){
	         View v = getChildAt(i);
	         v.measure(wspec, hspec);
	      }
	}
	
	private Point getScreenSize() {
		Display display = ((Activity)getContext()).getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		return size;
	}
	
	public void translateCardToLeft() {
		TranslateAnimation anim = new TranslateAnimation(0,
				-500, 0, 0);
		anim.setDuration(600);
		startAnimation(anim);
		parentView.removeView(getView());
	}
	
	public void translateCardToRight() {
		TranslateAnimation anim = new TranslateAnimation(0,
				+500, 0, 0);
		anim.setDuration(600);
		startAnimation(anim);
		parentView.removeView(getView());
	}
	
	public void translateCardToCenter() {
	     TranslateAnimation anim = new TranslateAnimation(0,
				-getX(), 0, -getY());
		anim.setDuration(600);
		anim.setAnimationListener(new TranslateAnimation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                setX(0);
                setY(0);
                clearAnimation();
            }
        });
       	startAnimation(anim);
	}
	
	public void temporalMovementToTheRight() {
		setRotation((float) ((xCoord - screenCenter) * (Math.PI / 32)));
		if (xCoord > screenCenter) {
			buttonLike.setAlpha(Utils.functionNormalize(
					screenCenter * 2, screenCenter, xCoord));
			if (xCoord > (getScreenSize().x - (screenCenter / 4))) {
				mLiked = Like.Disliked;
			} else {
				mLiked = Like.None;
			}
		} else {
			mLiked = Like.None;
			buttonLike.setAlpha(0);
		}
		buttonDislike.setAlpha(0);
	}
	
	public void temporalMovementToTheLeft() {
		setRotation((float) ((xCoord - screenCenter) * (Math.PI / 32)));
		if (xCoord < (screenCenter)) {
			buttonDislike.setAlpha(Utils.functionNormalize(screenCenter,
					0, xCoord));
			if (xCoord < screenCenter / 4) {
				mLiked = Like.Liked;
			} else {
				mLiked = Like.None;
			}
		} else {
			mLiked = Like.None;
			buttonDislike.setAlpha(0);
		}
		buttonLike.setAlpha(0);
	}

    public void setOnCardDimissedDelegate( OnCardDimissedDelegate delegate ) {
        this.mOnCardDimissedDelegate = delegate;
    }

    private CardView getInstance() {
        return this;
    }

    public CardModel getCardModel() {
        return this.cardModel;
    }

    public void setVisibilityBottomMenu(int visibilityBottomMenu) {
        informationContainerLayout.setVisibility(visibilityBottomMenu);
    }

    /**
     * Changes the text at the bottom of the card
     * @param text The new string
     */
    public void setText(String text) {
        mTextViewInformationText.setText(text);
    }

    /**
     * Gets the text of the card
     */
    public String getText() {
        return mTextViewInformationText.getText().toString();
    }

    public void setFirstText(String text) {
        firstTextView.setText(text);
    }

    public String getFirstText() {
        return firstTextView.getText().toString();
    }

    public void setSecondText(String text) {
        secondTextView.setText(text);
    }

    public String getSecondText() {
        return secondTextView.getText().toString();
    }

    public void setFirstImageView(Drawable image) {
        firstImageView.setImageDrawable(image);
    }

    public void setSecondImageView(Drawable image) {
        secondImageView.setImageDrawable(image);
    }

    private void initGUI() {
        cardImage = (ImageView) findViewById(R.id.tc_image);

        imageContainerLayout = (LinearLayout) findViewById(R.id.image_container);
        informationContainerLayout = (LinearLayout) findViewById(R.id.information_container);
        mTextViewInformationText = (TextView) findViewById(R.id.textViewInformationText);

        firstTextView   = (TextView) findViewById(R.id.firstTextView);
        secondTextView  = (TextView) findViewById(R.id.secondTextView);

        firstImageView  = (ImageView) findViewById(R.id.firstImageView);
        secondImageView = (ImageView) findViewById(R.id.secondImageView);
    }
}
