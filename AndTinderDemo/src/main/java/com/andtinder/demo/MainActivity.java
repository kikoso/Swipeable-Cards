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
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;

import com.andtinder.model.CardModel;
import com.andtinder.model.Orientations;
import com.andtinder.view.CardContainer;
import com.andtinder.view.CardView;


public class MainActivity extends Activity {

    /**
     * This variable is the container that will host our cards
     */
	private CardContainer mCardContainer;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.mainlayout);

		mCardContainer = (CardContainer) findViewById(R.id.layoutview);

        /**
         * The order of the cards can be either ordered or disordered
         */
        //mCardContainer.setOrientation(Orientations.Orientation.Ordered);
        /**
         * We need first to create our cards. The constructor needs to know which image will assign to the card
         */
		CardView card1 = new CardView(mCardContainer,new CardModel(R.drawable.picture1));
        /**
         * Afterwards, we need to add the car to the Card Container
         */
		mCardContainer.addView(card1);
		CardView card2 = new CardView(mCardContainer,new CardModel(R.drawable.picture2));
        /**
         * We can add a delegate to know when the card is being dismissed or liked and take an action
         */
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
        /**
         * We can set an Intent that will be trif
         */
        card3.getCardModel().setIntent(new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/kikoso/Swipeable-Cards")));
		mCardContainer.addView(card3);

	}
}
