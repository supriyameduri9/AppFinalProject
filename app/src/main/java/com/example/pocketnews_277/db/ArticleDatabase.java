package com.example.pocketnews_277.db;


import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import com.example.pocketnews_277.model.ArticleModel;

import com.example.pocketnews_277.model.ArticleModel;

@Database(version = 1,entities = {ArticleModel.class})
public abstract class ArticleDatabase extends RoomDatabase {
        public abstract ArticleDao getArticleDao();
        private static volatile ArticleDatabase instance;

        public static synchronized ArticleDatabase getInstance(Context context){
            if(instance == null) {
                instance = Room.databaseBuilder(context.getApplicationContext(),ArticleDatabase.class,"article_db.db").allowMainThreadQueries().
                        fallbackToDestructiveMigration().build();
            }
            return instance;
        }

}