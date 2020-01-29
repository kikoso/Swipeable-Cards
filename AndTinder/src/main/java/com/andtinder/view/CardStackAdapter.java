package com.andtinder.view;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.andtinder.R;
import com.andtinder.model.CardModel;

import java.util.ArrayList;
import java.util.Collection;

public abstract class CardStackAdapter extends BaseCardStackAdapter {
	private final Context context;

	/**
	 * Lock used to modify the content of {@link #data}. Any write operation
	 * performed on the deque should be synchronized on this lock.
	 */
	private final Object lock = new Object();
	private ArrayList<CardModel> data;

    private boolean shouldFillCardBackground = false;

    public CardStackAdapter(Context context) {
		this.context = context;
		data = new ArrayList<CardModel>();
	}

	public CardStackAdapter(Context context, Collection<? extends CardModel> items) {
		this.context = context;
		data = new ArrayList<CardModel>(items);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		FrameLayout wrapper = (FrameLayout) convertView;
		FrameLayout innerWrapper;
		View cardView;
		View convertedCardView;
		if (wrapper == null) {
			wrapper = new FrameLayout(context);
			wrapper.setBackgroundResource(R.drawable.card_bg);
			if (shouldFillCardBackground()) {
				innerWrapper = new FrameLayout(context);
				innerWrapper.setBackgroundColor(context.getResources().getColor(R.color.card_bg));
				wrapper.addView(innerWrapper);
			} else {
				innerWrapper = wrapper;
			}
			cardView = getCardView(position, getCardModel(position), null, parent);
			innerWrapper.addView(cardView);
		} else {
			if (shouldFillCardBackground()) {
				innerWrapper = (FrameLayout) wrapper.getChildAt(0);
			} else {
				innerWrapper = wrapper;
			}
			cardView = innerWrapper.getChildAt(0);
			convertedCardView = getCardView(position, getCardModel(position), cardView, parent);
			if (convertedCardView != cardView) {
				wrapper.removeView(cardView);
				wrapper.addView(convertedCardView);
			}
		}

		return wrapper;
	}

	protected abstract View getCardView(int position, CardModel model, View convertView, ViewGroup parent);

    public void setShouldFillCardBackground(boolean isShouldFillCardBackground) {
        this.shouldFillCardBackground = isShouldFillCardBackground;
    }

    public boolean shouldFillCardBackground() {
        return shouldFillCardBackground;
    }

    public void add(CardModel item) {
		synchronized (lock) {
			data.add(item);
		}
		notifyDataSetChanged();
	}

	public CardModel pop() {
		CardModel model;
		synchronized (lock) {
			model = data.remove(data.size() - 1);
		}
		notifyDataSetChanged();
		return model;
	}

	@Override
	public Object getItem(int position) {
		return getCardModel(position);
	}

	public CardModel getCardModel(int position) {
		synchronized (lock) {
			return data.get(data.size() - 1 - position);
		}
	}

	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public long getItemId(int position) {
		return getItem(position).hashCode();
	}

	public Context getContext() {
		return context;
	}
}
