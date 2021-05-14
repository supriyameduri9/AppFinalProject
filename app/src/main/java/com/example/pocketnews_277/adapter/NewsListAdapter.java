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

public class NewsListAdapter extends RecyclerView.Adapter<NewsListAdapter.NewsViewHolder> {

    private Context context;
    private List<ArticleModel> newsList;

    public NewsListAdapter(Context context, List<ArticleModel> newsList){

        this.context = context;
        this.newsList = newsList;

    }

    public void setNewsList(List<ArticleModel> newsList) {
        this.newsList = newsList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public NewsListAdapter.NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.news_recycler_row, parent,false);
        return new NewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsListAdapter.NewsViewHolder holder, int position) {
        holder.title.setText(this.newsList.get(position).getTitle());
        holder.author.setText(this.newsList.get(position).getAuthor());
        holder.publishedAt.setText(this.newsList.get(position).getPrettyPublishedAt());
        Glide.with(context)
                .load(this.newsList.get(position).getUrlToImage())
                .apply(RequestOptions.centerCropTransform())
                .into(holder.newsImage);


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
				ArticleModel newsItem = newsList.get(position);
				Intent detailViewIntent = new Intent(context, NewsDetailView.class);
				detailViewIntent.putExtra("url", newsItem.getUrl());
				detailViewIntent.putExtra("title", newsItem.getTitle());
				detailViewIntent.putExtra("img", newsItem.getUrlToImage());
				detailViewIntent.putExtra("date", newsItem.getPublishedDate());
				detailViewIntent.putExtra("spanTime", newsItem.getPrettyPublishedAt());
				detailViewIntent.putExtra("author", newsItem.getAuthor());

				context.startActivity(detailViewIntent);

            }
        });
    }

    @Override
    public int getItemCount() {
        if(this.newsList != null){
            return this.newsList.size();
        }
        return 0;
    }

    public class NewsViewHolder extends RecyclerView.ViewHolder{

        TextView title;
        TextView author;
        TextView publishedAt;
        ImageView newsImage;


        public NewsViewHolder(View newsItem){
            super(newsItem);
            newsImage = (ImageView) newsItem.findViewById(R.id.newsImg);
            title = (TextView) newsItem.findViewById(R.id.newsTitle);
            author = (TextView) newsItem.findViewById(R.id.author);
            publishedAt = (TextView) newsItem.findViewById(R.id.detailViewDate);

        }

    }
}
