package com.example.pocketnews_277.viewmodel;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.pocketnews_277.R;
import com.example.pocketnews_277.adapter.NewsListAdapter;
import com.example.pocketnews_277.model.ArticleModel;
import com.example.pocketnews_277.model.StoriesDocument;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;


public class MyStoriesFragment extends Fragment {

	private final static String TAG = "MyStoriesFragment";
	private NewsListAdapter newsListAdapter;
	private FirebaseAuth mAuth;
	private FirebaseFirestore db;

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_my_stories, container, false);
	}

	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		mAuth = FirebaseAuth.getInstance();
		db = FirebaseFirestore.getInstance();

		loadMyStoriesFromFirestore();
	}

	private void loadMyStoriesFromFirestore(){
		FirebaseUser user = mAuth.getCurrentUser();
		db.collection(user.getUid()).document("stories")
				.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
			@Override
			public void onComplete(@NonNull Task<DocumentSnapshot> task) {
				if (task.isSuccessful()) {
					DocumentSnapshot document = task.getResult();
					if (document.exists()) {
						Log.i(TAG, document.getId() + " => " + document.toObject(StoriesDocument.class));
						List<ArticleModel> newsList = document.toObject(StoriesDocument.class).articles;
						for(ArticleModel newsListItem: newsList) {
							Log.i(TAG, newsListItem.toString());
						}
						setUpRecyclerView(newsList);
					}
				}
			}
		});

	}

	public void setUpRecyclerView(List<ArticleModel> newsList){
		RecyclerView recyclerView = getView().findViewById(R.id.myStoriesList);
		LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
		recyclerView.setLayoutManager(layoutManager);

		newsListAdapter = new NewsListAdapter(getActivity(), newsList);
		recyclerView.setAdapter(newsListAdapter);
	}
}