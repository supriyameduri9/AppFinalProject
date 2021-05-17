package com.example.pocketnews_277.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.pocketnews_277.model.ArticleModel;

import java.util.List;

@Dao
public interface ArticleDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long upsert(ArticleModel articleModel);

    @Query("Select * from articles")
    LiveData<List<ArticleModel>> getAllArticles();

    @Delete
    void deleteArticle(ArticleModel articleModel);

}
