package com.andtinder.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
import android.widget.ListAdapter;

import com.andtinder.R;
import com.andtinder.model.CardModel;
import com.andtinder.model.Orientations.Orientation;

import java.util.Random;

public class CardContainer extends AdapterView<ListAdapter> {
    public static final int INVALID_POINTER_ID = -1;
    private int activePointerId = INVALID_POINTER_ID;
    private static final double DISORDERED_MAX_ROTATION_RADIANS = Math.PI / 64;
    private int numberOfCards = -1;

    private final DataSetObserver dataSetObserver = new DataSetObserver() {
        @Override
        public void onChanged() {
            super.onChanged();
            clearStack();
            ensureFull();
        }

        @Override
        public void onInvalidated() {
            super.onInvalidated();
            clearStack();
        }
    };

    private final Random random = new Random();
    private final Rect boundsRect = new Rect();
    private final Rect childRect = new Rect();
    private final Matrix matrix = new Matrix();


    //TODO: determine max dynamically based on device speed
    private int maxVisible = 10;
    private GestureDetector gestureDetector;
    private int flingSlop;
    private Orientation orientation;
    private ListAdapter listAdapter;
    private float lastTouchX;
    private float lastTouchY;
    private View topCard;
    private int touchSlop;
    private int gravity;
    private int nextAdapterPosition;
    private boolean dragging;

    private boolean locked = false;

    public CardContainer(Context context) {
        super(context);
        setOrientation(Orientation.Disordered);
        setGravity(Gravity.CENTER);
        init();
    }

    public CardContainer(Context context, AttributeSet attrs) {
        super(context, attrs);
        initFromXml(attrs);
        init();
    }

    public CardContainer(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initFromXml(attrs);
        init();
    }

    private void init() {
        ViewConfiguration viewConfiguration = ViewConfiguration.get(getContext());
        flingSlop = viewConfiguration.getScaledMinimumFlingVelocity();
        touchSlop = viewConfiguration.getScaledTouchSlop();
        gestureDetector = new GestureDetector(getContext(), new GestureListener());
    }

    private void initFromXml(AttributeSet attr) {
        TypedArray a = getContext().obtainStyledAttributes(attr,
                R.styleable.CardContainer);

        setGravity(a.getInteger(R.styleable.CardContainer_android_gravity, Gravity.CENTER));
        int orientation = a.getInteger(R.styleable.CardContainer_orientation, 1);
        setOrientation(Orientation.fromIndex(orientation));

        a.recycle();
    }

    @Override
    public ListAdapter getAdapter() {
        return listAdapter;
    }

    @Override
    public void setAdapter(ListAdapter adapter) {
        if (listAdapter != null)
            listAdapter.unregisterDataSetObserver(dataSetObserver);

        clearStack();
        topCard = null;
        listAdapter = adapter;
        nextAdapterPosition = 0;
        adapter.registerDataSetObserver(dataSetObserver);

        ensureFull();

        if (getChildCount() != 0) {
            topCard = getChildAt(getChildCount() - 1);
            topCard.setLayerType(LAYER_TYPE_HARDWARE, null);
        }
        numberOfCards = getAdapter().getCount();
        requestLayout();
    }

    private void ensureFull() {
        while (nextAdapterPosition < listAdapter.getCount() && getChildCount() < maxVisible) {
            View view = listAdapter.getView(nextAdapterPosition, null, this);
            view.setLayerType(LAYER_TYPE_SOFTWARE, null);
            if (orientation == Orientation.Disordered) {
                view.setRotation(getDisorderedRotation());
            }
            addViewInLayout(view, 0, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT,
                    listAdapter.getItemViewType(nextAdapterPosition)), false);

            requestLayout();

            nextAdapterPosition += 1;
        }
    }

    private void clearStack() {
        removeAllViewsInLayout();
        nextAdapterPosition = 0;
        topCard = null;
    }

    public Orientation getOrientation() {
        return orientation;
    }

    public void setOrientation(Orientation orientation) {
        if (orientation == null)
            throw new NullPointerException("Orientation may not be null");
        if (this.orientation != orientation) {
            this.orientation = orientation;
            if (orientation == Orientation.Disordered) {
                for (int i = 0; i < getChildCount(); i++) {
                    View child = getChildAt(i);
                    child.setRotation(getDisorderedRotation());
                }
            } else {
                for (int i = 0; i < getChildCount(); i++) {
                    View child = getChildAt(i);
                    child.setRotation(0);
                }
            }
            requestLayout();
        }

    }

    private float getDisorderedRotation() {
        return (float) Math.toDegrees(random.nextGaussian() * DISORDERED_MAX_ROTATION_RADIANS);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int requestedWidth = getMeasuredWidth() - getPaddingLeft() - getPaddingRight();
        int requestedHeight = getMeasuredHeight() - getPaddingTop() - getPaddingBottom();
        int childWidth, childHeight;

        if (orientation == Orientation.Disordered) {
            int R1, R2;
            if (requestedWidth >= requestedHeight) {
                R1 = requestedHeight;
                R2 = requestedWidth;
            } else {
                R1 = requestedWidth;
                R2 = requestedHeight;
            }
            childWidth = (int) ((R1 * Math.cos(DISORDERED_MAX_ROTATION_RADIANS) - R2 * Math.sin(DISORDERED_MAX_ROTATION_RADIANS)) / Math.cos(2 * DISORDERED_MAX_ROTATION_RADIANS));
            childHeight = (int) ((R2 * Math.cos(DISORDERED_MAX_ROTATION_RADIANS) - R1 * Math.sin(DISORDERED_MAX_ROTATION_RADIANS)) / Math.cos(2 * DISORDERED_MAX_ROTATION_RADIANS));
        } else {
            childWidth = requestedWidth;
            childHeight = requestedHeight;
        }

        int childWidthMeasureSpec, childHeightMeasureSpec;
        childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(childWidth, MeasureSpec.AT_MOST);
        childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(childHeight, MeasureSpec.AT_MOST);

        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            assert child != null;
            child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);

        for (int i = 0; i < getChildCount(); i++) {
            boundsRect.set(0, 0, getWidth(), getHeight());

            View view = getChildAt(i);
            int w, h;
            w = view.getMeasuredWidth();
            h = view.getMeasuredHeight();

            Gravity.apply(gravity, w, h, boundsRect, childRect);
            view.layout(childRect.left, childRect.top, childRect.right, childRect.bottom);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (topCard == null) {
            return false;
        }
        if (gestureDetector.onTouchEvent(event)) {
            return true;
        }
        Log.d("Touch Event", MotionEvent.actionToString(event.getActionMasked()) + " ");
        final int pointerIndex;
        final float x, y;
        final float dx, dy;
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                topCard.getHitRect(childRect);
                pointerIndex = event.getActionIndex();
                try {
                    x = event.getX(pointerIndex);
                    y = event.getY(pointerIndex);
                } catch (Exception e) {
                    return false;
                }

                if (!childRect.contains((int) x, (int) y)) {
                    return false;
                }
                lastTouchX = x;
                lastTouchY = y;
                activePointerId = event.getPointerId(pointerIndex);


                float[] points = new float[]{x - topCard.getLeft(), y - topCard.getTop()};
                topCard.getMatrix().invert(matrix);
                matrix.mapPoints(points);
                topCard.setPivotX(points[0]);
                topCard.setPivotY(points[1]);

                break;
            case MotionEvent.ACTION_MOVE:

                pointerIndex = event.findPointerIndex(activePointerId);
                x = event.getX(pointerIndex);
                y = event.getY(pointerIndex);

                dx = x - lastTouchX;
                dy = y - lastTouchY;

                if (Math.abs(dx) > touchSlop || Math.abs(dy) > touchSlop) {
                    dragging = true;
                }

                if (!dragging) {
                    return true;
                }

                topCard.setTranslationX(topCard.getTranslationX() + dx);
                topCard.setTranslationY(topCard.getTranslationY() + dy);

                topCard.setRotation(40 * topCard.getTranslationX() / (getWidth() / 2.f));

                lastTouchX = x;
                lastTouchY = y;
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if (!dragging) {
                    return true;
                }
                dragging = false;
                activePointerId = INVALID_POINTER_ID;
                ValueAnimator animator = ObjectAnimator.ofPropertyValuesHolder(topCard,
                        PropertyValuesHolder.ofFloat("translationX", 0),
                        PropertyValuesHolder.ofFloat("translationY", 0),
                        PropertyValuesHolder.ofFloat("rotation", (float) Math.toDegrees(random.nextGaussian() * DISORDERED_MAX_ROTATION_RADIANS)),
                        PropertyValuesHolder.ofFloat("pivotX", topCard.getWidth() / 2.f),
                        PropertyValuesHolder.ofFloat("pivotY", topCard.getHeight() / 2.f)
                ).setDuration(250);
                animator.setInterpolator(new AccelerateInterpolator());
                animator.start();
                break;
            case MotionEvent.ACTION_POINTER_UP:
                pointerIndex = event.getActionIndex();
                final int pointerId = event.getPointerId(pointerIndex);

                if (pointerId == activePointerId) {
                    final int newPointerIndex = pointerIndex == 0 ? 1 : 0;
                    lastTouchX = event.getX(newPointerIndex);
                    lastTouchY = event.getY(newPointerIndex);

                    activePointerId = event.getPointerId(newPointerIndex);
                }
                break;
        }

        return true;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (topCard == null) {
            return false;
        }
        if (gestureDetector.onTouchEvent(event)) {
            return true;
        }
        final int pointerIndex;
        final float x, y;
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                topCard.getHitRect(childRect);

                CardModel cardModel = (CardModel) getAdapter().getItem(getChildCount() - 1);

                if (cardModel.getOnClickListener() != null) {
                    cardModel.getOnClickListener().OnClickListener();
                }
                pointerIndex = event.getActionIndex();
                try {
                    x = event.getX(pointerIndex);
                    y = event.getY(pointerIndex);
                } catch (Exception e) {
                    return false;
                }

                if (!childRect.contains((int) x, (int) y)) {
                    return false;
                }

                lastTouchX = x;
                lastTouchY = y;
                activePointerId = event.getPointerId(pointerIndex);
                break;
            case MotionEvent.ACTION_MOVE:
                pointerIndex = event.findPointerIndex(activePointerId);
                try {
                    x = event.getX(pointerIndex);
                    y = event.getY(pointerIndex);
                } catch (Exception e) {
                    return false;
                }
                if (Math.abs(x - lastTouchX) > touchSlop || Math.abs(y - lastTouchY) > touchSlop) {
                    float[] points = new float[]{x - topCard.getLeft(), y - topCard.getTop()};
                    topCard.getMatrix().invert(matrix);
                    matrix.mapPoints(points);
                    topCard.setPivotX(points[0]);
                    topCard.setPivotY(points[1]);
                    return true;
                }
        }

        return false;
    }

    @Override
    public View getSelectedView() {
        return topCard;
    }

    @Override
    public void setSelection(int position) {
        throw new UnsupportedOperationException();
    }

    public int getGravity() {
        return gravity;
    }

    public void setGravity(int gravity) {
        this.gravity = gravity;
    }

    public static class LayoutParams extends ViewGroup.LayoutParams {

        int viewType;

        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
        }

        public LayoutParams(int width, int height) {
            super(width, height);
        }

        public LayoutParams(ViewGroup.LayoutParams source) {
            super(source);
        }

        public LayoutParams(int w, int h, int viewType) {
            super(w, h);
            this.viewType = viewType;
        }
    }

    private class GestureListener extends SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            Log.d("Fling", "Fling with " + velocityX + ", " + velocityY);

            float dx = e2.getX() - e1.getX();
            if (Math.abs(dx) > touchSlop &&
                    Math.abs(velocityX) > Math.abs(velocityY) &&
                    Math.abs(velocityX) > flingSlop * 3) {

                leave(velocityX, velocityY);

                return true;
            } else
                return false;
        }
    }

    /**
     * Public function for allow dismiss cards directly from the CardContainer
     * IE: mCardContainer.leave(-1000, 45);
     *
     * @param velocityX
     * @param velocityY
     */
    public void leave(float velocityX, float velocityY) {
        if (!locked) {
            locked = true; // Lock swipe until current card is dismissed

            final View topCard = this.topCard;

            float targetX = topCard.getX();
            float targetY = topCard.getY();
            long duration = 0;

            boundsRect.set(0 - topCard.getWidth() - 100, 0 - topCard.getHeight() - 100, getWidth() + 100, getHeight() + 100);

            while (boundsRect.contains((int) targetX, (int) targetY)) {
                targetX += velocityX / 10;
                targetY += velocityY / 10;
                duration += 100;
            }

            duration = Math.min(500, duration);

            final Handler h = new Handler();
            h.postDelayed(new Runnable() {
                @Override
                public void run() {
                    locked = false; // Unlock swipe
                }
            }, duration + 200);

            this.topCard = getChildAt(getChildCount() - 2);
            CardModel cardModel = (CardModel) getAdapter().getItem(0);

            if (this.topCard != null)
                this.topCard.setLayerType(LAYER_TYPE_HARDWARE, null);

            if (cardModel.getOnCardDismissedListener() != null) {
                if (targetX < 0) {
                    cardModel.getOnCardDismissedListener().onDislike();
                } else {
                    cardModel.getOnCardDismissedListener().onLike();
                }
            }

            topCard.animate()
                    .setDuration(duration)
                    .alpha(.75f)
                    .setInterpolator(new LinearInterpolator())
                    .x(targetX)
                    .y(targetY)
                    .rotation(Math.copySign(45, velocityX))
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            removeViewInLayout(topCard);
                            ensureFull();
                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {
                            onAnimationEnd(animation);
                        }
                    });
        }
    }

    public int getNumberOfCards() {
        return numberOfCards;
    }
}
