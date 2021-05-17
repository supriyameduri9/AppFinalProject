package com.example.pocketnews_277.viewmodel;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class NewsDataViewModelProviderFactory implements ViewModelProvider.Factory {
	Context context;

	public NewsDataViewModelProviderFactory(Context context) {
		this.context = context;
	}

	@NonNull
	@Override
	public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
		return (T) new NewsDataViewModel(context);
	}
}
