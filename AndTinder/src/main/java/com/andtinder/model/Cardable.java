package com.andtinder.model;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

public interface Cardable {
    public View getView(Context context, View convertView, ViewGroup parent);
    public CardModel.OnCardDismissedListener getOnCardDismissedListener();
    public CardModel.OnClickListener getOnClickListener();
}
