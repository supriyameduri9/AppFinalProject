package com.example.pocketnews_277.viewmodel;


import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pocketnews_277.R;
import com.example.pocketnews_277.adapter.NewsListAdapter;
import com.example.pocketnews_277.adapter.TrendingListAdapter;
import com.example.pocketnews_277.model.NewsDataModel;

public class HomeFragment extends Fragment {
	final String TAG = "HomeFragment";
	private SearchView simpleSearchView;
	private NewsDataModel newsDataModel;
	private NewsDataModel trendingDataModel;
	private NewsListAdapter adapter;
	private TrendingListAdapter trendingListAdapter;
	private NewsDataViewModel viewModel;
	private RelativeLayout trendingRelativeLayout;
	private RelativeLayout searchRelativeLayout;

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_home,container,false);
	}

	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		searchRelativeLayout = (RelativeLayout) view.findViewById(R.id.searchBar);
		trendingRelativeLayout = (RelativeLayout) view.findViewById(R.id.trendingRelativeLayout);

		RecyclerView recyclerView = view.findViewById(R.id.newsList);
		LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
		recyclerView.setLayoutManager(layoutManager);
		newsDataModel = new NewsDataModel();
		adapter = new NewsListAdapter(getActivity(), newsDataModel.getArticles());
		recyclerView.setAdapter(adapter);
		viewModel = ViewModelProviders.of(this).get(NewsDataViewModel.class);
		viewModel.getNewsDataObserver().observe(getActivity(), new Observer<NewsDataModel>() {
			@Override
			public void onChanged(NewsDataModel newsDataModel) {
				if(newsDataModel != null){
					HomeFragment.this.newsDataModel = newsDataModel;
					adapter.setNewsList(newsDataModel.getArticles());
				} else{
					Toast.makeText(getActivity(), "Could not retrieve data!", Toast.LENGTH_SHORT).show();
				}
			}
		});

		viewModel.makeApiCall();

		RecyclerView trendingRecyclerView = view.findViewById(R.id.trendingNewsList);
		LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false);
		trendingRecyclerView.setLayoutManager(linearLayoutManager);
		trendingDataModel = new NewsDataModel();
		trendingListAdapter = new TrendingListAdapter(getActivity(), trendingDataModel.getArticles());
		trendingRecyclerView.setAdapter(trendingListAdapter);
		viewModel.getTrendingNewsDataObserver().observe(getActivity(), new Observer<NewsDataModel>() {
			@Override
			public void onChanged(NewsDataModel trendingDataModel) {
				if(trendingDataModel != null){
					HomeFragment.this.trendingDataModel = trendingDataModel;
					trendingListAdapter.setTrendingNewsList(trendingDataModel.getArticles());
				} else{
					Toast.makeText(getActivity(), "Could not retrieve trending news!", Toast.LENGTH_SHORT).show();
				}
			}
		});

		viewModel.makeTrendingApiCall("general");


		simpleSearchView = (SearchView) view.findViewById(R.id.searchInput);
		simpleSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
			@Override
			public boolean onQueryTextSubmit(String query) {
				Log.i(TAG, "Search input: " + query);
				viewModel.makeApiCall(query);
				trendingRelativeLayout.setVisibility(View.GONE);
				return false;
			}

			@Override
			public boolean onQueryTextChange(String newText) {
				if (simpleSearchView.getQuery().length() == 0) {
					Log.i(TAG, "Search got cleared");
					viewModel.makeApiCall();
					trendingRelativeLayout.setVisibility(View.VISIBLE);
				}
				return false;
			}
		});

	}

	@Override
	public void onResume() {
		super.onResume();
		searchRelativeLayout.requestFocus();
	}


}
