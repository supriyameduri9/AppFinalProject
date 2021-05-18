package com.example.pocketnews_277.viewmodel;

import android.content.ClipData;
import android.media.MediaRouter;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.pocketnews_277.R;
import com.example.pocketnews_277.adapter.NewsListAdapter;
import com.example.pocketnews_277.model.ArticleModel;
import com.example.pocketnews_277.model.NewsDataModel;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class SavedFragment extends Fragment {
	private final static String TAG = "SavedFragment";

	public NewsDataViewModel viewModel;
	public NewsListAdapter newsListAdapter;
	private NewsDataModel newsDataModel;
	private RecyclerView saverecyclerView;
	private List<ArticleModel> articles;

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_saved, container, false);
	}
	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		articles = new ArrayList<>();
		viewModel = ViewModelProviders.of(this, new NewsDataViewModelProviderFactory(getActivity())).get(NewsDataViewModel.class);

		saverecyclerView = (RecyclerView) view.findViewById(R.id.rvSavedNews);
		viewModel.getSavedNews().observe(getActivity(), new Observer<List<ArticleModel>>() {
			@Override
			public void onChanged(List<ArticleModel> articlesSnapshot) {
				if(articlesSnapshot != null){
					articles = articlesSnapshot;
					Log.i(TAG, "Inside retrofit: " + articles.size());
					newsListAdapter.setNewsList(articlesSnapshot);
				} else{
					Toast.makeText(getActivity(), "Could not retrieve data!", Toast.LENGTH_SHORT).show();
				}
			}
		});
		setupRecyclerView();

		ItemTouchHelper helper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(
				ItemTouchHelper.UP | ItemTouchHelper.DOWN,
				ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT
		) {
			@Override
			public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
				return true;
			}

			@Override
			public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
					int position = viewHolder.getAbsoluteAdapterPosition();
					Log.i(TAG, "position: " + position + " articles size: " + articles.size());
					ArticleModel articleModel = articles.get(position);
					Log.i(TAG, "article: " + Integer.toString(position));
					viewModel.deleteArticle(articleModel);
					Snackbar mySnackBar = Snackbar.make(view,"Successfully deleted article",Snackbar.LENGTH_LONG);
					mySnackBar.setAction("UNDO",new View.OnClickListener(){
						@Override
						public void onClick(View view){
							viewModel.saveArticle(articleModel);
						}
					});
					mySnackBar.show();
			}
		});
		helper.attachToRecyclerView(saverecyclerView);
	}

	private void setupRecyclerView(){
		/*newsDataModel = new NewsDataModel();
		newsListAdapter = new NewsListAdapter(getActivity(), newsDataModel.getArticles());
*/		newsListAdapter = new NewsListAdapter(getActivity(),articles);
		LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
		saverecyclerView.setLayoutManager(layoutManager);
		saverecyclerView.setAdapter(newsListAdapter);
	}
}