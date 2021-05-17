package com.example.pocketnews_277.viewmodel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.pocketnews_277.R;
import com.example.pocketnews_277.model.ArticleModel;
import com.example.pocketnews_277.model.StoriesDocument;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class NotificationHandlerActivity extends AppCompatActivity {

	private FirebaseAuth mAuth;
	private FirebaseFirestore db;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_notification);

		Intent intent = getIntent();
		String articleId = intent.getStringExtra("articleId");

		db = FirebaseFirestore.getInstance();
		mAuth = FirebaseAuth.getInstance();

		loadStoryFromFirestore(articleId);
	}

	private void loadStoryFromFirestore(String articleId){
		Log.i("NotificationHandlerActivity", "loading article for: " + articleId);
		FirebaseUser user = mAuth.getCurrentUser();
		db.collection("stories")
				.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
			@Override
			public void onComplete(@NonNull Task<QuerySnapshot> task) {
				if (task.isSuccessful()){
					ArticleModel articleModel = null;
					for (QueryDocumentSnapshot document : task.getResult()) {
						List<ArticleModel> currUserNewsList = document.toObject(StoriesDocument.class).articles;
						if(currUserNewsList != null) {
							Log.i("NotificationHandlerActivity", "currUserNewsList size: " + currUserNewsList.size());
							for (ArticleModel currArticle : currUserNewsList){
								if (currArticle.getArticleId().equals(articleId)){
									articleModel = currArticle;
								}
							}
						}
					}
					if (articleModel != null){
						Intent intent = new Intent(NotificationHandlerActivity.this, NewsDetailView.class);
						intent.putExtra("item", articleModel);
						startActivity(intent);
					} else {
						Toast.makeText(NotificationHandlerActivity.this, "Sorry, requested article is missing",
								Toast.LENGTH_SHORT).show();
					}
				}
			}
		});
	}
}