package com.example.pocketnews_277.viewmodel;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.example.pocketnews_277.R;
import com.example.pocketnews_277.model.ArticleModel;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;

public class NewsDetailView extends AppCompatActivity implements AppBarLayout.OnOffsetChangedListener {

	private ImageView imageView,saveButton,shareButton;
	private TextView date;
	private TextView time;
	private TextView title;
	private TextView minRead;
	private AppBarLayout appBarLayout;
	private FrameLayout date_behavior;
	private Toolbar toolbar;
	private boolean isHideToolbarView = false;
	private RelativeLayout iconsAppBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_news_detail_view);

		toolbar = findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		getSupportActionBar().setTitle("");
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		final CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.collapsing_toolbar);
		collapsingToolbarLayout.setTitle("");
		appBarLayout = findViewById(R.id.appbar);
		appBarLayout.addOnOffsetChangedListener(this);
		date_behavior = findViewById(R.id.date_behavior);
		iconsAppBar = findViewById(R.id.detailViewAppBar);
		imageView = findViewById(R.id.backdrop);
		time = findViewById(R.id.spanTime);
		title = findViewById(R.id.newsDetailTitle);
		date = findViewById(R.id.detailViewDate);
		minRead = findViewById(R.id.minRead);
		saveButton = findViewById(R.id.saveButton);
		shareButton  = findViewById(R.id.shareButton);

		Intent intent = getIntent();
		ArticleModel newsItem = (ArticleModel) intent.getSerializableExtra("item");
		String apiUrl = newsItem.getUrl();
		String apiTitle = newsItem.getTitle();
		String apiImg = newsItem.getUrlToImage();
		String apiDate = newsItem.getPublishedDate();
		String apiSpanTime = newsItem.getPrettyPublishedAt();
		String apiAuthor = newsItem.getAuthor();
		String readTime = newsItem.getAvgReadingTime();

		shareButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				try{
					Log.i("NEWS_DETAIL_VIEW: URL for sharing ", apiUrl);
				Intent i = new Intent(Intent.ACTION_SEND);
				i.setType("text/plan");
				i.putExtra(Intent.EXTRA_SUBJECT,"Shared News Article from Pocket News");
				String body = "\n" + "Share from app" + "\n";
				i.putExtra(Intent.EXTRA_TEXT,apiUrl);
				startActivity(Intent.createChooser(i,"Share with" ));
			}
			catch(Exception e) {
				Toast.makeText(getApplicationContext(),"Cannot Share", Toast.LENGTH_SHORT).show();
			}Log.i("NEWS_DETAIL_VIEW: URL for saving ", apiUrl);
			}
		});

		saveButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.i("NEWS_DETAIL_VIEW", "Entered save function");
				// Save newsItem object
			}
		});


		Glide.with(this)
				.load(apiImg)
				.apply(RequestOptions.centerCropTransform())
				.transition(DrawableTransitionOptions.withCrossFade())
				.into(imageView);
		date.setText(apiDate);
		title.setText(apiTitle);
		time.setText(apiAuthor + ". " + apiSpanTime);
		minRead.setText(readTime);

		initWebView(apiUrl);


	}

	private void initWebView(String url){
		WebView webView = findViewById(R.id.webView);
		webView.getSettings().setLoadsImagesAutomatically(true);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.getSettings().setDomStorageEnabled(true);
		webView.getSettings().setSupportZoom(true);
		webView.getSettings().setBuiltInZoomControls(true);
		webView.getSettings().setDisplayZoomControls(true);
		webView.setWebViewClient(new WebViewClient());
		webView.loadUrl(url);

	}

	@Override
	public boolean onSupportNavigateUp() {
		onBackPressed();
		return true;
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		supportFinishAfterTransition();
	}

	@Override
	public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
		int maxScroll = appBarLayout.getTotalScrollRange();
		float percentage = (float) Math.abs(verticalOffset) / maxScroll;
		if (percentage == 1f && isHideToolbarView) {

			date_behavior.setVisibility(View.GONE);
			iconsAppBar.setVisibility(View.VISIBLE);
			isHideToolbarView = !isHideToolbarView;

		} else if (percentage < 1f && isHideToolbarView) {

			date_behavior.setVisibility(View.VISIBLE);
			iconsAppBar.setVisibility(View.GONE);
			isHideToolbarView = !isHideToolbarView;
		}
	}

}