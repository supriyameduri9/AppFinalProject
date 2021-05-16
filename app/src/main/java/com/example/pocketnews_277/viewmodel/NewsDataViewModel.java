package com.example.pocketnews_277.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.pocketnews_277.model.ArticleModel;
import com.example.pocketnews_277.model.NewsDataModel;
import com.example.pocketnews_277.network.APIService;
import com.example.pocketnews_277.network.RetrofitInstance;
import com.example.pocketnews_277.repository.NewsRepository;
import com.google.firebase.database.annotations.NotNull;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewsDataViewModel extends ViewModel {

    private static String API_KEY = "7b7fc2b99c0b4a0cb22c1fd2201203c9";
    private static String TODAY = (DateTimeFormatter.ofPattern("yyyy/MM/dd")).format(LocalDate.now());

    private MutableLiveData<NewsDataModel> newsData;
    private MutableLiveData<NewsDataModel> trendingNewsData;

    @NotNull
    private final NewsRepository newsRepository;

    public NewsDataViewModel(NewsRepository newsRepository) {
        this.newsRepository = newsRepository;
        newsData = new MutableLiveData<>();
        trendingNewsData = new MutableLiveData<>();
    }

    public MutableLiveData<NewsDataModel> getNewsDataObserver() {
        return newsData;
    }

    public MutableLiveData<NewsDataModel> getTrendingNewsDataObserver() {
        return trendingNewsData;
    }

    public void makeApiCall() {
        makeApiCall("");
    }

    public void makeApiCall(String keyword) {
        APIService apiService = RetrofitInstance.getRetroClient().create(APIService.class);

        Call<NewsDataModel> call =
                (keyword.isEmpty()) ?
                        apiService.newsList(API_KEY, TODAY) :
                        apiService.searchNewsList(API_KEY, TODAY, keyword);

        call.enqueue(new Callback<NewsDataModel>() {
            @Override
            public void onResponse(Call<NewsDataModel> call, Response<NewsDataModel> response) {
                newsData.postValue(response.body());
            }

            @Override
            public void onFailure(Call<NewsDataModel> call, Throwable t) {
                newsData.postValue(null);
            }
        });
    }

    public void makeTrendingApiCall(String category) {
        APIService apiService = RetrofitInstance.getRetroClient().create(APIService.class);

        Call<NewsDataModel> call = apiService.trendingNewsList(API_KEY, category);
        call.enqueue(new Callback<NewsDataModel>() {
            @Override
            public void onResponse(Call<NewsDataModel> call, Response<NewsDataModel> response) {
                trendingNewsData.postValue(response.body());
            }

            @Override
            public void onFailure(Call<NewsDataModel> call, Throwable t) {
                trendingNewsData.postValue(null);
            }
        });
    }

   /* public final Long upsert(@NotNull ArticleModel articleModel){
         return this.db.getArticleDao().upsert(articleModel);
    }

    public final LiveData<List<ArticleModel>> getSavedNews(){
        return this.db.getArticleDao().getAllArticles();
    }*/

    public Long saveArticle(ArticleModel articleModel){
      return  newsRepository.upsert(articleModel);
    }

    public final LiveData<List<ArticleModel>> getSavedNews(){
        return newsRepository.getSavedNews();

    }

    public final void deleteArticle(ArticleModel articleModel) {
        newsRepository.deleteArticle(articleModel);
    }
}
