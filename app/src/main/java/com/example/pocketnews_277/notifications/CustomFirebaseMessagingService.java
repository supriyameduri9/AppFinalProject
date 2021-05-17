package com.example.pocketnews_277.notifications;



import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.pocketnews_277.R;
import com.example.pocketnews_277.viewmodel.NotificationHandlerActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

public class CustomFirebaseMessagingService extends FirebaseMessagingService {
	public static final String CHANNEL_ID = "pocket_news";
	public static final String CHANNEL_NAME = "Pocket News";

	@Override
	public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
		super.onMessageReceived(remoteMessage);
		Log.i("CustomFirebaseMessagingService", "Received notification");
		if (remoteMessage.getNotification() != null){
			String title = remoteMessage.getNotification().getTitle();
			String body = remoteMessage.getNotification().getBody();
			String articleId = "";
			if(remoteMessage.getData().size() > 0) {
				Map<String, String> data = remoteMessage.getData();
				articleId = data.get("articleId");
			}
			Log.i("CustomFirebaseMessagingService", "title: " + title + " body: " + body + " articleId: " + articleId);

			sendNotification(getApplicationContext(), title, body, articleId);
		}
	}

	public static void sendNotification(Context context, String title, String message, String articleId){
		NotificationManager manager = context.getSystemService(NotificationManager.class);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
			NotificationChannel notificationChannel = new NotificationChannel(CustomFirebaseMessagingService.CHANNEL_ID, CustomFirebaseMessagingService.CHANNEL_NAME,
					NotificationManager.IMPORTANCE_DEFAULT);
			notificationChannel.setDescription("Any description can be given!");
			manager.createNotificationChannel(notificationChannel);
		}

		Intent intent = new Intent(context, NotificationHandlerActivity.class);
		intent.putExtra("articleId", articleId);
		PendingIntent pendingIntent = PendingIntent.getActivity(context, 100, intent, PendingIntent.FLAG_CANCEL_CURRENT);

		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, CustomFirebaseMessagingService.CHANNEL_ID)
				.setSmallIcon(R.drawable.notification_icon)
				.setContentTitle(title)
				.setContentText(message)
				.setContentIntent(pendingIntent)
				.setAutoCancel(true)
				.setPriority(NotificationCompat.PRIORITY_HIGH);
		NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
		notificationManagerCompat.notify(1, mBuilder.build());
	}
}
