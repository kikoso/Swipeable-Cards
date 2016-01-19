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

package com.andtinder.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import com.andtinder.R;
import com.andtinder.model.CardModel;

import org.junit.Before;
import org.junit.Test;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;
import org.junit.runner.RunWith;
import static org.mockito.Matchers.anyObject;

@RunWith(PowerMockRunner.class)
@PrepareForTest(LayoutInflater.class)
public class SimpleCardStackAdapterTest {

    @Test
    public void testGetCardViewNullViewCheck(){
        //given
        CardModel cardModel=initializeCardModel();
        SimpleCardStackAdapter simpleCardStackAdapter=initializeSimpleCardStackAdapter();
        View view=initializeView();
        ViewGroup parent=mock(ViewGroup.class);
        initializeInflater(parent,view);

        //when
       View cardView=simpleCardStackAdapter.getCardView(1, cardModel, null, parent);

        //then
       assertEquals("view instances must be equal", view, cardView);
    }

    @Test
    public void testGetCardView(){
        //given
        ViewGroup parent=mock(ViewGroup.class);
        ImageView imageView=mock(ImageView.class);
        TextView titleTextView=mock(TextView.class);
        TextView descriptionTextView=mock(TextView.class);
        CardModel cardModel=initializeCardModel();
        View convertView=initializeView(imageView, titleTextView, descriptionTextView);
        SimpleCardStackAdapter simpleCardStackAdapter=initializeSimpleCardStackAdapter();

        //when
        simpleCardStackAdapter.getCardView(1, cardModel, convertView, parent);

        //then
       verify(imageView).setImageDrawable(cardModel.getCardImageDrawable());
       verify(titleTextView).setText(cardModel.getTitle());
       verify(descriptionTextView).setText(cardModel.getDescription());
    }

    private CardModel initializeCardModel(){
        String title="New Year Greeting Card";
        String description= "Happy New Year";
        Drawable drawable=mock(Drawable.class);
        CardModel cardModel=new CardModel(title, description, drawable);
        return cardModel;
    }

    private View initializeView(){
        ImageView imageView=mock(ImageView.class);
        TextView titleTextView=mock(TextView.class);
        TextView descriptionTextView=mock(TextView.class);
        return initializeView(imageView,titleTextView,descriptionTextView);
    }

    private View initializeView(ImageView imageView,TextView titleTextView, TextView descriptionTextView){
        View view=mock(View.class);
        when(view.findViewById(R.id.image)).thenReturn(imageView);
        when(view.findViewById(R.id.title)).thenReturn(titleTextView);
        when(view.findViewById(R.id.description)).thenReturn(descriptionTextView);
        return view;
    }

    private SimpleCardStackAdapter initializeSimpleCardStackAdapter(){
        Context mContext=mock(Context.class);
        SimpleCardStackAdapter simpleCardStackAdapter=new SimpleCardStackAdapter(mContext);
        return simpleCardStackAdapter;
    }

    private void initializeInflater(ViewGroup parent, View convertView){
        PowerMockito.mockStatic(LayoutInflater.class);
        LayoutInflater inflater=mock(LayoutInflater.class);
        when(LayoutInflater.from((Context) anyObject())).thenReturn(inflater);
        when(inflater.inflate(R.layout.std_card_inner, parent, false)).thenReturn(convertView);
    }
}
