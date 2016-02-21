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

package com.andtinder.model;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import static org.powermock.api.mockito.PowerMockito.mock;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

/**
 * Test for the CarModel
 */
public class CardModelTest {

    @Test
    public void testConstructorDefaultValueCheck(){
        //when
        CardModel cardModel=new CardModel();

        //then
        assertNull("title property value must be null for zero argument constructor", cardModel.getTitle() );
        assertNull("description property value must be null for zero argument constructor",
                cardModel.getDescription() );
        assertNull("cardImageDrawable property value must be null for zero argument constructor",
                cardModel.getCardImageDrawable() );
        assertNull("cardLikeImageDrawable property value must be null for zero argument constructor",
                cardModel.getCardLikeImageDrawable() );
        assertNull("cardDislikeImageDrawable property value must be null for zero argument constructor",
                cardModel.getCardDislikeImageDrawable() );
        assertNull("mOnCardDismissedListener property value must be null for zero argument constructor",
                cardModel.getOnCardDismissedListener());
        assertNull("mOnClickListener property value must be null for zero argument constructor",
                cardModel.getOnClickListener() );
    }

    @Test
    public void testConstructorValueCheck(){
        //given
        String title="New Year Greeting Card";
        String description= "Happy New Year";
        Drawable drawable=mock(Drawable.class);

        //when
        CardModel cardModel=new CardModel(title, description, drawable);

        //then
        assertEquals("title property value must be equal to 'New Year Greeting Card' ", title, cardModel.getTitle() );
        assertEquals("description property value must be equal to 'Happy New Year'", description,
                cardModel.getDescription() );
        assertEquals("cardLikeImageDrawable property instances must be equal", drawable,
                cardModel.getCardImageDrawable() );
    }

    @Test
    public void testConstructorBitmapValueCheck(){
        //given
        String title="New Year Greeting Card";
        String description= "Happy New Year";
        Bitmap bitMap=mock(Bitmap.class);

        //when
        CardModel cardModel=new CardModel(title, description, bitMap);

        //then
        assertEquals("title property value must be equal to 'New Year Greeting Card' ", title, cardModel.getTitle() );
        assertEquals("description property value must be equal to 'Happy New Year'", description,
                cardModel.getDescription() );
        assertNotNull("cardImageDrawable property value must be not null", cardModel.getCardImageDrawable() );
    }

    @Test
    public void testSetTitleAndDescriptionProperties() {
        //given
        CardModel cardModel=new CardModel();

        //when
        cardModel.setTitle("New Year Greeting Card");

        //then
        assertEquals("title property value must be equal to 'New Year Greeting Card' ", "New Year Greeting Card",
                cardModel.getTitle());

        //when
        cardModel.setDescription("Happy New Year");

        //then
        assertEquals("description property value must be equal to 'Happy New Year'", "Happy New Year",
                cardModel.getDescription());
     }

    @Test
    public void testSetCardProperties(){
        //given
        CardModel cardModel=new CardModel();
        Drawable cardImageDrawable=mock(Drawable.class);

        //when
        cardModel.setCardImageDrawable(cardImageDrawable);

        //then
        assertEquals("cardImageDrawable property instances must be equal", cardImageDrawable,
                cardModel.getCardImageDrawable() );

        //given
        Drawable cardLikeImageDrawable=mock(Drawable.class);

        //when
        cardModel.setCardLikeImageDrawable(cardLikeImageDrawable);

        //then
        assertEquals("cardLikeImageDrawable property instances must be equal", cardLikeImageDrawable,
                cardModel.getCardLikeImageDrawable() );

        //given
        Drawable cardDislikeImageDrawable=mock(Drawable.class);

        //when
        cardModel.setCardDislikeImageDrawable(cardDislikeImageDrawable);

        //then
        assertEquals("cardDislikeImageDrawable property instances must be equal", cardDislikeImageDrawable,
                cardModel.getCardDislikeImageDrawable() );
    }

    @Test
    public void testSetPropertyListeners(){
        //given
        CardModel cardModel=new CardModel();
        CardModel.OnCardDismissedListener onCardDismissedListener=mock(CardModel.OnCardDismissedListener.class);

        //when
        cardModel.setOnCardDismissedListener(onCardDismissedListener);

        //then
        assertEquals("mOnCardDismissedListener property instances must be equal", onCardDismissedListener,
                cardModel.getOnCardDismissedListener() );

        //given
        CardModel.OnClickListener onClickListener=mock(CardModel.OnClickListener.class);

        //when
        cardModel.setOnClickListener(onClickListener);

        //then
        assertEquals("mOnClickListener property instances must be equal", onClickListener,
                cardModel.getOnClickListener() );
    }
}