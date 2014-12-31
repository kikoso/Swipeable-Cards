package com.andtinder.view;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.andtinder.model.Cardable;

public final class SimpleCardStackAdapter extends CardStackAdapter {

	public SimpleCardStackAdapter(Context mContext) {
		super(mContext);
	}

	@Override
	public View getCardView(int position, Cardable cardable, View convertView, ViewGroup parent) {
        return cardable.getView(getContext(), convertView, parent);
	}
}
