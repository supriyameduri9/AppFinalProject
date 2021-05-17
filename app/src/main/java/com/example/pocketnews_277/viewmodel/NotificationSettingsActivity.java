package com.example.pocketnews_277.viewmodel;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.SwitchCompat;

import com.example.pocketnews_277.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.HashMap;
import java.util.Map;

public class NotificationSettingsActivity extends AppCompatActivity {

	private final static String TAG = "NotificationSettingsActivity";
	private final static String ALL_NOTIFICATIONS = "ALL";
	private final static String POSITIVE_NOTIFICATIONS = "POSITIVE_ONLY";
	private final static String NO_NOTIFICATIONS = "NONE";

	private SwitchCompat allowNotifySwitch;
	private RadioButton allNewsButton;
	private RadioButton positiveNewsButton;
	private AppCompatButton saveChanges;
	private RadioGroup radioGroup;

	private FirebaseAuth mAuth;
	private FirebaseFirestore db;
	private String oldNotificationSetting = "";

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.push_notifications);

		mAuth = FirebaseAuth.getInstance();
		db = FirebaseFirestore.getInstance();

		allowNotifySwitch = (SwitchCompat) findViewById(R.id.allowNotifySwitch);
		radioGroup = (RadioGroup) findViewById(R.id.radio_group);
		radioGroup.setVisibility(View.GONE);
		allNewsButton = (RadioButton) findViewById(R.id.allNewsButton);
		positiveNewsButton = (RadioButton) findViewById(R.id.positiveNewsButton);

		saveChanges = (AppCompatButton) findViewById(R.id.saveNotifications);
		saveChanges.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				saveNotificationSettings();
			}
		});

		allowNotifySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					Log.i(TAG, "Notifications are turned on!");
					radioGroup.setVisibility(View.VISIBLE);
					allNewsButton.setChecked(true); // default setting

				} else {
					Log.i(TAG, "Notifications are turned off!");
					radioGroup.setVisibility(View.GONE);
				}
			}
		});

		loadFromFirestoreDb();
	}

	private String GetNewNotificationSettings(){
		if (!allowNotifySwitch.isChecked()){
			return NO_NOTIFICATIONS;
		}
		int checkedRadioButtonId = radioGroup.getCheckedRadioButtonId();
		if (checkedRadioButtonId == R.id.positiveNewsButton){
			return POSITIVE_NOTIFICATIONS;
		} else {
			return ALL_NOTIFICATIONS;
		}
	}

	private void updateUI(String oldNotificationSetting){
		if (oldNotificationSetting.equals(NO_NOTIFICATIONS)) {
			allowNotifySwitch.setChecked(false);
			radioGroup.setVisibility(View.GONE);
		}
		else if (oldNotificationSetting.equals(ALL_NOTIFICATIONS)) {
			allowNotifySwitch.setChecked(true);
			allNewsButton.setChecked(true);
			radioGroup.setVisibility(View.VISIBLE);
		}
		else if (oldNotificationSetting.equals(POSITIVE_NOTIFICATIONS)) {
			allowNotifySwitch.setChecked(true);
			positiveNewsButton.setChecked(true);
			radioGroup.setVisibility(View.VISIBLE);
		}
	}

	private void loadFromFirestoreDb(){
		FirebaseUser user = mAuth.getCurrentUser();
		db.collection(user.getUid())
				.get()
				.addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
					@Override
					public void onComplete(@NonNull Task<QuerySnapshot> task) {
						if (task.isSuccessful()) {
							for (QueryDocumentSnapshot document : task.getResult()) {
								if (document.getId().equals("notification_info")) {
									Log.i(TAG, document.getId() + " => " + document.getData());
									if (document.getData().get("notification_info") != null) {
										oldNotificationSetting = document.getData().get("notification_info").toString();
										updateUI(oldNotificationSetting);
									}
								}
							}
						} else {
							Log.w(TAG, "Error getting notification settings from Firestore.", task.getException());
						}
					}
				});
	}


	private void saveNotificationSettings(){
		String newNotificationSetting = GetNewNotificationSettings();
		Log.i(TAG, "Old notification settings: " + oldNotificationSetting + ". New notification settings: " + newNotificationSetting);
		if (newNotificationSetting.equals(oldNotificationSetting)) {
			Toast.makeText(NotificationSettingsActivity.this, "No changes are made!",
					Toast.LENGTH_SHORT).show();
			finish();
		}
		else {
			writeNotificationSettingToFirestore(newNotificationSetting, oldNotificationSetting);
		}
	}

	private void writeNotificationSettingToFirestore(String newNotificationSetting, String oldNotificaitonSettings){
		FirebaseUser user = mAuth.getCurrentUser();
		Map<String, Object> notificationInfo = new HashMap<>();
		notificationInfo.put("notification_info", newNotificationSetting);
		db.collection(user.getUid()).document("notification_info").set(notificationInfo).addOnSuccessListener(
				new OnSuccessListener<Void>() {
					@Override
					public void onSuccess(Void aVoid) {
						updateTopicSubscriptions(newNotificationSetting, oldNotificaitonSettings);
						Toast.makeText(NotificationSettingsActivity.this, "Changes are saved.",
								Toast.LENGTH_SHORT).show();
						finish();
					}
				}
		).addOnFailureListener(
				new OnFailureListener() {
					@Override
					public void onFailure(@NonNull Exception e) {
						Log.w(TAG, "Error saving notifications in firestore", e);
						Toast.makeText(NotificationSettingsActivity.this, "Unable to save!",
								Toast.LENGTH_SHORT).show();
					}
				}
		);
	}

	private void updateTopicSubscriptions(String newNotificationSetting, String oldNotificaitonSettings){
		if (oldNotificaitonSettings != NO_NOTIFICATIONS){
			FirebaseMessaging.getInstance().unsubscribeFromTopic(oldNotificaitonSettings);
		}
		FirebaseMessaging.getInstance().subscribeToTopic(newNotificationSetting);
	}
}
