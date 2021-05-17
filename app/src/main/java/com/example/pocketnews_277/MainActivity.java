package com.example.pocketnews_277;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.WindowManager;

import com.example.pocketnews_277.R;
import com.example.pocketnews_277.notifications.CustomFirebaseMessagingService;
import com.example.pocketnews_277.viewmodel.Login;

public class MainActivity extends AppCompatActivity {
    Thread timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
			NotificationChannel notificationChannel = new NotificationChannel(CustomFirebaseMessagingService.CHANNEL_ID, CustomFirebaseMessagingService.CHANNEL_NAME,
					NotificationManager.IMPORTANCE_DEFAULT);
			notificationChannel.setDescription("Any description can be given!");
			NotificationManager manager = getSystemService(NotificationManager.class);
			manager.createNotificationChannel(notificationChannel);
		}

        timer = new Thread() {
            @Override
            public void run() {
                try {
                    synchronized (this) {
                        wait(3330);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    Intent intent = new Intent(getApplicationContext(), Login.class);
                    startActivity(intent);
                    finish();

                }
            }
        };
        timer.start();
    }
}