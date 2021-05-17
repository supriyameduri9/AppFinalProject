package com.example.pocketnews_277.viewmodel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.pocketnews_277.R;
import com.example.pocketnews_277.model.ArticleModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.time.Instant;
import java.time.temporal.ChronoField;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AddStory extends AppCompatActivity {

	private final String TAG = "AddStory";

	private ImageView inputImage;
	private TextInputEditText inputTitle;
	private Spinner inputCategory;
	private EditText inputContent;
	private AppCompatButton submitButton, cancelButton;
	private String username;

	private Uri imageUri;
	private FirebaseAuth mAuth;
	private FirebaseStorage storage;
	private StorageReference storageRef;
	private FirebaseFirestore db;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_add_story);

		Intent intent = getIntent();
		username = intent.getStringExtra("username");

		mAuth = FirebaseAuth.getInstance();
		storage = FirebaseStorage.getInstance();
		storageRef = storage.getReference();

		inputTitle = (TextInputEditText) findViewById(R.id.inputTitle);
		inputContent = (EditText) findViewById(R.id.inputContent);
		inputCategory = (Spinner) findViewById(R.id.inputCategory);
		ArrayAdapter<String> myAdapater = new ArrayAdapter<String>(AddStory.this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.Category));
		myAdapater.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		inputCategory.setAdapter(myAdapater);

		inputImage = (ImageView) findViewById(R.id.inputImage);
		inputImage.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				choosePicture();
			}
		});

		submitButton = (AppCompatButton) findViewById(R.id.submitButton);
		submitButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				saveStory();
			}
		});

		cancelButton = (AppCompatButton) findViewById(R.id.cancelButton);
		cancelButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});

	}

	private void addPostToDb(String imageUrl) {
		String currentTime = Instant.now().with(ChronoField.NANO_OF_SECOND, 0).toString();
		ArticleModel articleModel = new ArticleModel(username,
				inputTitle.getEditableText().toString(), currentTime, imageUrl,
				inputContent.getEditableText().toString(),
				inputCategory.getSelectedItem().toString());

		Log.i(TAG, "Saving article to Firestore db:" + articleModel);

		// Add article to firestore db.
		FirebaseUser user = mAuth.getCurrentUser();

		db = FirebaseFirestore.getInstance();
		DocumentReference storiesRef = db.collection("stories").document(user.getUid());
		storiesRef.update("articles", FieldValue.arrayUnion(articleModel)).addOnSuccessListener(
				new OnSuccessListener<Void>() {
					@Override
					public void onSuccess(Void aVoid) {
						Toast.makeText(AddStory.this, "Posted story successfully",
								Toast.LENGTH_SHORT).show();
						finish();
					}
				}
		).addOnFailureListener(
				new OnFailureListener() {
					@Override
					public void onFailure(@NonNull Exception e) {
						Log.w(TAG, "Error adding document", e);
						Toast.makeText(AddStory.this, "Unable to post story. Please try again!",
								Toast.LENGTH_LONG).show();
					}
				}
		);

	}

	private void saveStory(){

		inputTitle.setError(null);
		inputContent.setError(null);

		if(inputTitle.getEditableText().toString().isEmpty()){
			inputTitle.setError("Please enter title");
			inputTitle.requestFocus();
			return;
		} else if(inputContent.getEditableText().toString().isEmpty()){
			inputContent.setError("Please enter content");
			inputContent.requestFocus();
			return;
		}


		final ProgressDialog pd = new ProgressDialog(this);
		pd.setTitle("Uploading Image..");
		pd.show();

		final String randomKey = UUID.randomUUID().toString();
		StorageReference imageRef = storageRef.child("images/" + randomKey);

		imageRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
			@Override
			public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
				imageRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
					@Override
					public void onComplete(@NonNull Task<Uri> task) {
						addPostToDb(task.getResult().toString());
						pd.dismiss();

					}
				});
			}
		}).addOnFailureListener(new OnFailureListener() {
			@Override
			public void onFailure(@NonNull Exception e) {
				pd.dismiss();

			}
		}).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
			@Override
			public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
				double progressPercent = (100.00 * snapshot.getBytesTransferred()/snapshot.getTotalByteCount());
				pd.setMessage("Progress: " + (int) progressPercent + "%");
			}
		});
	}




	private void choosePicture(){
		Intent intent = new Intent();
		intent.setType("image/*");
		intent.setAction(Intent.ACTION_GET_CONTENT);
		startActivityForResult(intent, 1);
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		if (getCurrentFocus() != null) {
			InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
		}
		return super.dispatchTouchEvent(ev);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null){
			imageUri = data.getData();
			inputImage.setImageURI(imageUri);
		}
	}
}