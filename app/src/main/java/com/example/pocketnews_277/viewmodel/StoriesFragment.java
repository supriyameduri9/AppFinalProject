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
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


public class StoriesFragment extends Fragment {

	private final static String TAG = "StoriesFragment";
	private NewsListAdapter newsListAdapter;
	private FirebaseAuth mAuth;
	private FirebaseFirestore db;

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_stories, container, false);
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
		db.collection("stories")
				.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
			@Override
			public void onComplete(@NonNull Task<QuerySnapshot> task) {
				if (task.isSuccessful()){
					List<ArticleModel> allNewsList = new ArrayList<>();
					for (QueryDocumentSnapshot document : task.getResult()) {
						List<ArticleModel> currUserNewsList = document.toObject(StoriesDocument.class).articles;
						if(currUserNewsList != null) {
							allNewsList.addAll(currUserNewsList);
						}
					}
					setUpRecyclerView(allNewsList);
				}
			}
		});
	}

	public void setUpRecyclerView(List<ArticleModel> newsList){
		RecyclerView recyclerView = getView().findViewById(R.id.allStoriesList);
		LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
		recyclerView.setLayoutManager(layoutManager);

		newsListAdapter = new NewsListAdapter(getActivity(), newsList);
		recyclerView.setAdapter(newsListAdapter);
	}
}