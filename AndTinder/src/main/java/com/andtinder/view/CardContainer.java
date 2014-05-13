package com.andtinder.view;


import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import com.andtinder.exceptions.CardContainerNotEmptyException;

import com.andtinder.model.Orientations.Orientation;

import java.util.ArrayList;


public class CardContainer extends RelativeLayout {

	private Orientation mOrientation;
	private ArrayList<CardView> mCardContainer = new ArrayList<CardView>();

	public CardContainer(Context context) {
		super(context);
		mOrientation = Orientation.Disordered;
	}

	public CardContainer(Context context, AttributeSet attrs) {
		super(context, attrs);
		mOrientation = Orientation.Disordered;
	}

	public CardContainer(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mOrientation = Orientation.Disordered;
	}

	public Orientation getOrientation() {
		return mOrientation;
	}

	public void setOrientation(Orientation orientation) {
		this.mOrientation = orientation;
	}

	@Override
	public void addView(View child) {
		super.addView(child);
		if ( child instanceof CardView) {
			mCardContainer.add( (CardView) child );
		}
	}
	
	public void addViewOnlyLayout(View child) {
		super.addView(child);
	}

	public void displayAgain() throws CardContainerNotEmptyException {
		if ( getChildCount() != 0 ) {
			throw new CardContainerNotEmptyException("The container is not empty");
		} else {
			for (CardView card : mCardContainer) {
				super.addView(card);
				card.translateCardToCenter();
			}
		}
	}
}