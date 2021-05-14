package com.example.pocketnews_277.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.pocketnews_277.R;
import com.example.pocketnews_277.model.ArticleModel;
import com.example.pocketnews_277.viewmodel.NewsDetailView;

import java.util.List;

public class TrendingListAdapter extends RecyclerView.Adapter<TrendingListAdapter.TrendingNewsViewHolder> {
    Context context;
    private List<ArticleModel> trendingNewsList;

    public TrendingListAdapter(Context context, List<ArticleModel> trendingNewsList) {
        this.context = context;
        this.trendingNewsList = trendingNewsList;
    }


    public void setTrendingNewsList(List<ArticleModel> trendingNewsList) {
        this.trendingNewsList = trendingNewsList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TrendingNewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.trending_card_design, parent,false);
        return new TrendingNewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TrendingNewsViewHolder holder, int position) {
        holder.trendingTitle.setText(trendingNewsList.get(position).getTitle());
        holder.trendingAuthor.setText(trendingNewsList.get(position).getAuthor());
        Glide.with(context)
                .load(this.trendingNewsList.get(position).getUrlToImage())
                .apply(RequestOptions.centerCropTransform())
                .into(holder.trendingImage);

		holder.itemView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				ArticleModel newsItem = trendingNewsList.get(position);
				Intent detailViewIntent = new Intent(context, NewsDetailView.class);
				detailViewIntent.putExtra("url", newsItem.getUrl());
				detailViewIntent.putExtra("title", newsItem.getTitle());
				detailViewIntent.putExtra("img", newsItem.getUrlToImage());
				detailViewIntent.putExtra("date", newsItem.getPublishedDate());
				detailViewIntent.putExtra("spanTime", newsItem.getPrettyPublishedAt());
				detailViewIntent.putExtra("author", newsItem.getAuthor());
				detailViewIntent.putExtra("readTime", newsItem.getAvgReadingTime());

				context.startActivity(detailViewIntent);

			}
		});
    }

    @Override
    public int getItemCount() {
        return trendingNewsList.size() < 10 ? trendingNewsList.size(): 5;
    }


    public class TrendingNewsViewHolder extends RecyclerView.ViewHolder{
        ImageView trendingImage;
        TextView trendingTitle;
        TextView trendingAuthor;

        public TrendingNewsViewHolder(@NonNull View itemView) {
            super(itemView);
            trendingImage = (ImageView) (itemView.findViewById(R.id.trendingImage));
            trendingTitle = (TextView) (itemView.findViewById(R.id.trendingTitle));
            trendingAuthor = (TextView) (itemView.findViewById(R.id.trendingAuthor));
        }
    }

}
