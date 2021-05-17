package com.example.pocketnews_277.repository;

import android.util.Log;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;

import com.example.pocketnews_277.db.ArticleDatabase;
import com.example.pocketnews_277.model.ArticleModel;
import com.google.android.gms.common.logging.Logger;
import com.google.android.gms.tasks.Continuation;
import com.google.firebase.database.annotations.NotNull;

import java.util.List;

public class NewsRepository {

    @NotNull
    private final ArticleDatabase db;

    @Nullable
    public final Long upsert(@NotNull ArticleModel articleModel) {
        return this.db.getArticleDao().upsert(articleModel);
    }

    @NotNull
    public final LiveData<List<ArticleModel>> getSavedNews() {
        return this.db.getArticleDao().getAllArticles();
    }

    public final void deleteArticle(@NotNull ArticleModel articleModel) {
        this.db.getArticleDao().deleteArticle(articleModel);
    }

    @NotNull
    public final ArticleDatabase getDb() {
        return this.db;
    }

    public NewsRepository(ArticleDatabase db) {
        this.db = db;
    }
}
