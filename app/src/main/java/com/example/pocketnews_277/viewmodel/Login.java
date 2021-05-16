package com.example.pocketnews_277.viewmodel;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.pocketnews_277.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {
    final String TAG = "Login";

    private FirebaseAuth mAuth;
    private Button gotToSignUpButton;
    private Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        TextInputLayout emailLoginInput = findViewById(R.id.emailLogin);
        TextInputLayout passwordLoginInput = findViewById(R.id.password);
        loginButton = (Button) findViewById(R.id.signOutButton);

        gotToSignUpButton = (Button) findViewById(R.id.goToSignupPage);
        gotToSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToSignUpIntent = new Intent(Login.this, SignUp.class);
                startActivity(goToSignUpIntent);

            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
				String email = emailLoginInput.getEditText().getText().toString();
				String password = passwordLoginInput.getEditText().getText().toString();

				emailLoginInput.setError(null);
				passwordLoginInput.setError(null);

				if(email.isEmpty()){
					emailLoginInput.setError("Please enter email");
					emailLoginInput.requestFocus();
					return;
				} else if(password.isEmpty()){
					passwordLoginInput.setError("Please enter password");
					passwordLoginInput.requestFocus();
					return;
				}

				login(email, password);

            }
        });
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
	protected void onStart() {
		super.onStart();
		if (mAuth.getCurrentUser() != null) {
			sendToHomePage();
		}
	}

	private void sendToHomePage(){
		Intent goToHomepage = new Intent(Login.this,HomepageActivity.class);
		startActivity(goToHomepage);
	}

	private void login(String email, String password){

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            // send user to the homepage, once login is successful
							sendToHomePage();

                            // updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(Login.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            // updateUI(null);
                        }

                        // ...
                    }
                });

    }
}