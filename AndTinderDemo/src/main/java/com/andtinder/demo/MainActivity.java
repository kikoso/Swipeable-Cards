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
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;

import com.andtinder.model.CardModel;
import com.andtinder.view.CardContainer;
import com.andtinder.view.SimpleCardStackAdapter;

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

		Resources r = getResources();

		SimpleCardStackAdapter adapter = new SimpleCardStackAdapter(this);

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
		adapter.add(new CardModel("Title1", "Description goes here", r.getDrawable(R.drawable.picture1)));
		adapter.add(new CardModel("Title2", "Description goes here", r.getDrawable(R.drawable.picture2)));
		adapter.add(new CardModel("Title3", "Description goes here", r.getDrawable(R.drawable.picture3)));
		adapter.add(new CardModel("Title4", "Description goes here", r.getDrawable(R.drawable.picture1)));
		adapter.add(new CardModel("Title5", "Description goes here", r.getDrawable(R.drawable.picture2)));

        CardModel cardModel = new CardModel("Title1", "Description goes here", r.getDrawable(R.drawable.picture1));
        cardModel.setOnClickListener(new CardModel.OnClickListener() {
           @Override
           public void OnClickListener() {
               Log.i("Swipeable Cards","I am pressing the card");
           }
        });

        cardModel.setOnCardDismissedListener(new CardModel.OnCardDismissedListener() {
            @Override
            public void onLike() {
                Log.i("Swipeable Cards","I like the card");
            }

            @Override
            public void onDislike() {
                Log.i("Swipeable Cards","I dislike the card");
            }
        });

        adapter.add(cardModel);

		mCardContainer.setAdapter(adapter);
	}
}
