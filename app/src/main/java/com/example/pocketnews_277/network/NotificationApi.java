package com.example.pocketnews_277.network;


import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface NotificationApi {

	@FormUrlEncoded
	@POST("send")
	Call<ResponseBody> sendNotification(
			@Field("topic") String topic,
			@Field("title") String title,
			@Field("body") String body,
			@Field("articleId") String articleId
	);

}
