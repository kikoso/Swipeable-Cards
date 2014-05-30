/**
 * AndTinder v0.1 for Android
 *
 * @Author: Enrique López Mañas <eenriquelopez@gmail.com>
 * http://www.lopez-manas.com
 *
 * TAndTinder is a native library for Android that provide a
 * Tinder card like effect. A card can be constructed using an
 * image and displayed with animation effects, dismiss-to-like
 * and dismiss-to-unlike, and use different sorting mechanisms.
 *
 * AndTinder is compatible with API Level 13 and upwards
 *
 * @copyright: Enrique López Mañas
 * @license: Apache License 2.0
 */

package com.andtinder.demo;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.Window;

import com.andtinder.model.CardModel;
import com.andtinder.view.CardContainer;
import com.andtinder.view.SimpleCardStackAdapter;

public class MainActivity extends Activity {

    /**
     * This variable is the container that will host our cards
     */
	private CardContainer mCardContainer;
	private CardContainer mCardContainer2;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.mainlayout);

		mCardContainer = (CardContainer) findViewById(R.id.layoutview);
		//mCardContainer2 = (CardContainer) findViewById(R.id.layoutview2);

		Resources r = getResources();

		SimpleCardStackAdapter adapter = new SimpleCardStackAdapter(this);
		adapter.add(new CardModel("Title1", "Description goes here Description goes here Description goes here Description goes here", r.getDrawable(R.drawable.picture1)));
		adapter.add(new CardModel("Title2", "Description goes here", r.getDrawable(R.drawable.picture2)));
		adapter.add(new CardModel("Title3", "Description goes here", r.getDrawable(R.drawable.picture3)));
		adapter.add(new CardModel("Title4", "Description goes here", r.getDrawable(R.drawable.picture1)));
		adapter.add(new CardModel("Title5", "Description goes here", r.getDrawable(R.drawable.picture2)));
		adapter.add(new CardModel("Title6", "Description goes here", r.getDrawable(R.drawable.picture3)));
		adapter.add(new CardModel("Title1", "Description goes here", r.getDrawable(R.drawable.picture1)));
		adapter.add(new CardModel("Title2", "Description goes here", r.getDrawable(R.drawable.picture2)));
		adapter.add(new CardModel("Title3", "Description goes here", r.getDrawable(R.drawable.picture3)));
		adapter.add(new CardModel("Title4", "Description goes here", r.getDrawable(R.drawable.picture1)));
		adapter.add(new CardModel("Title5", "Description goes here", r.getDrawable(R.drawable.picture2)));
		adapter.add(new CardModel("Title6", "Description goes here", r.getDrawable(R.drawable.picture3)));
		adapter.add(new CardModel("Title1", "Description goes here", r.getDrawable(R.drawable.picture1)));
		adapter.add(new CardModel("Title2", "Description goes here", r.getDrawable(R.drawable.picture2)));
		adapter.add(new CardModel("Title3", "Description goes here", r.getDrawable(R.drawable.picture3)));
		adapter.add(new CardModel("Title4", "Description goes here", r.getDrawable(R.drawable.picture1)));
		adapter.add(new CardModel("Title5", "Description goes here", r.getDrawable(R.drawable.picture2)));
		adapter.add(new CardModel("Title6", "Description goes here", r.getDrawable(R.drawable.picture3)));
		adapter.add(new CardModel("Title1", "Description goes here", r.getDrawable(R.drawable.picture1)));
		adapter.add(new CardModel("Title2", "Description goes here", r.getDrawable(R.drawable.picture2)));
		adapter.add(new CardModel("Title3", "Description goes here", r.getDrawable(R.drawable.picture3)));
		adapter.add(new CardModel("Title4", "Description goes here", r.getDrawable(R.drawable.picture1)));
		adapter.add(new CardModel("Title5", "Description goes here", r.getDrawable(R.drawable.picture2)));
		adapter.add(new CardModel("Title6", "Description goes here", r.getDrawable(R.drawable.picture3)));
		adapter.add(new CardModel("Title1", "Description goes here", r.getDrawable(R.drawable.picture1)));
		adapter.add(new CardModel("Title2", "Description goes here", r.getDrawable(R.drawable.picture2)));
		adapter.add(new CardModel("Title3", "Description goes here", r.getDrawable(R.drawable.picture3)));
		adapter.add(new CardModel("Title4", "Description goes here", r.getDrawable(R.drawable.picture1)));
		adapter.add(new CardModel("Title5", "Description goes here", r.getDrawable(R.drawable.picture2)));
		adapter.add(new CardModel("Title6", "Description goes here", r.getDrawable(R.drawable.picture3)));
		mCardContainer.setAdapter(adapter);

		//mCardContainer2.setAdapter(adapter);
		/*
        *//**
         * The order of the cards can be either ordered or disordered
         *//*
        //mCardContainer.setOrientation(Orientations.Orientation.Ordered);
        *//**
         * We need first to create our cards. The constructor needs to know which image will assign to the card
         *//*
		CardView card1 = new CardView(mCardContainer,new CardModel(r.getDrawable(R.drawable.picture1)));
        *//**
         * Afterwards, we need to add the car to the Card Container
         *//*
		mCardContainer.addView(card1);
		CardView card2 = new CardView(mCardContainer,new CardModel(R.drawable.picture2));
        *//**
         * We can add a delegate to know when the card is being dismissed or liked and take an action
         *//*
        card2.setOnCardDimissedDelegate(new CardView.OnCardDimissedDelegate() {
            @Override
            public void onLike(CardView cardView) {
                Log.d("AndTinder", "I liked it");
            }

            @Override
            public void onDislike(CardView cardView) {
                Log.d("AndTinder", "I did not liked it");
            }
        });
		mCardContainer.addView(card2);

		CardView card3 = new CardView(mCardContainer,new CardModel(R.drawable.picture3));
        *//**
         * We can set an Intent that will be trif
         *//*
        card3.getCardModel().setIntent(new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/kikoso/Swipeable-Cards")));
        card3.setText("This is an example text");
		mCardContainer.addView(card3);*/

	}
}
