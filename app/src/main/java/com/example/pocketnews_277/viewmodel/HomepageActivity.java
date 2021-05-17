package com.example.pocketnews_277.viewmodel;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pocketnews_277.R;
import com.example.pocketnews_277.adapter.NewsListAdapter;
import com.example.pocketnews_277.adapter.TrendingListAdapter;
import com.example.pocketnews_277.model.NewsDataModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class HomepageActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener{
    final String TAG = "HomepageActivity";
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private String username = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_homepage);

		ImageButton postStory = (ImageButton) findViewById(R.id.postStory);
		postStory.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				showPostStoryPage();
			}
		});

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentLayout,new HomeFragment()).commit();
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
			@Override
			public boolean onNavigationItemSelected(@NonNull MenuItem item) {
				Fragment selectedFragment = null;
				switch(item.getItemId()){
					case R.id.feedPage:
						selectedFragment = new HomeFragment();
						break;
					case R.id.allStories:
						selectedFragment = new StoriesFragment();
						break;
					case R.id.myStories:
						selectedFragment = new MyStoriesFragment();
						break;
					case R.id.savedList:
						selectedFragment = new SavedFragment();
						break;
				}
				getSupportFragmentManager().beginTransaction().replace(R.id.fragmentLayout,selectedFragment).commit();
				return true;
			}
		});

        mAuth = FirebaseAuth.getInstance();
		db = FirebaseFirestore.getInstance();

        loadHomePage();
    }

    public void showPostStoryPage(){
		Intent postStoryViewIntent = new Intent(this, AddStory.class);
		postStoryViewIntent.putExtra("username", username);
		startActivity(postStoryViewIntent);
	}


	public void showProfile(View v) {
        PopupMenu profileMenu = new PopupMenu(this,v);
        profileMenu.setOnMenuItemClickListener(this);
        profileMenu.inflate(R.menu.profile_menu);
        profileMenu.show();

    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch(item.getItemId()){
            case R.id.signOutOption:
                mAuth.signOut();
                // redirect user to login activity
                Intent goToLoginIntent = new Intent(HomepageActivity.this, Login.class);
                startActivity(goToLoginIntent);
                Toast.makeText(this, "Signed out successfully!", Toast.LENGTH_SHORT).show();
            default: return false;
        }
    }

    private void loadHomePage(){

        FirebaseUser user = mAuth.getCurrentUser();
		db.collection(user.getUid())
				.get()
				.addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
					@Override
					public void onComplete(@NonNull Task<QuerySnapshot> task) {
						if (task.isSuccessful()) {
							for (QueryDocumentSnapshot document : task.getResult()) {
								if (document.getId().equals("users")) {
									Log.i(TAG, document.getId() + " => " + document.getData());
									username = document.getData().get("user_name").toString();
									String userGreeting = "Hey " + username + "!";
									TextView greeting = findViewById(R.id.userGreeting);
									greeting.setText(userGreeting);
								}
							}
						} else {
							Log.w(TAG, "Error getting documents from Firestore.", task.getException());
						}
					}
				});

    }

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		if (getCurrentFocus() != null) {
			InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
		}
		return super.dispatchTouchEvent(ev);


	}
}