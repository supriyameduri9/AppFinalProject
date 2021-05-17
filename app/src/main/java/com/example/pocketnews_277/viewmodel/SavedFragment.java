package com.example.pocketnews_277.viewmodel;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.pocketnews_277.R;
import com.example.pocketnews_277.adapter.NewsListAdapter;
import com.example.pocketnews_277.model.NewsDataModel;

public class SavedFragment extends Fragment {


	public NewsDataViewModel viewModel;
	public NewsListAdapter newsListAdapter;
	private NewsDataModel newsDataModel;
	private RecyclerView saverecyclerView;

	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		viewModel = ViewModelProviders.of(this, new NewsDataViewModelProviderFactory(getActivity())).get(NewsDataViewModel.class);

		saverecyclerView = (RecyclerView) view.findViewById(R.id.rvSavedNews);
		setupRecyclerView();


	}

	private void setupRecyclerView(){
		newsDataModel = new NewsDataModel();
		newsListAdapter = new NewsListAdapter(getActivity(), newsDataModel.getArticles());
		LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
		saverecyclerView.setLayoutManager(layoutManager);
		saverecyclerView.setAdapter(newsListAdapter);
	}
}