package com.bcit.pomodoro_scheduler;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

/**
 * This is where the login logic will go.
 */
public class MainActivity extends AppCompatActivity {

    SignInButton signInButton;
    GoogleSignInClient mGoogleSignInClient;
    String GOOGLE_ACCOUNT = "googleAccount";
    int RC_SIGN_IN = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setUpGoogleSign();
        setUpSignInBtn();
        checkForExistingUser();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Goal returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.
            updateLoginUI(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("Error", "signInResult:failed code=" + e.getStatusCode());
            updateLoginUI(null);
        }
    }

    public void setUpSignInBtn() {
        // Set the dimensions of the sign-in button.
        signInButton = findViewById(R.id.btn_main_signIn);
        signInButton.setSize(SignInButton.SIZE_STANDARD);
        findViewById(R.id.btn_main_signIn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.btn_main_signIn:
                        signIn();
                        break;
                }
            }
        });
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    public void setUpGoogleSign() {
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.server_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    public void checkForExistingUser() {
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if (account != null) {
            updateLoginUI(account);
        }
    }

    public void updateLoginUI(GoogleSignInAccount account) {
        if (account == null) {
            Log.w("LogIn", "Didn't Work");
            return;
        }

        com.google.android.gms.common.SignInButton signInBtn = findViewById(R.id.btn_main_signIn);
        signInBtn.setVisibility(View.GONE);

        Button logOutBtn = findViewById(R.id.btn_main_signOut);
        logOutBtn.setVisibility(View.VISIBLE);
        logOutBtn.setOnClickListener(view -> logOut());

        Button launchBtn = findViewById(R.id.btn_main_launchBtn);
        launchBtn.setVisibility(View.VISIBLE);
        launchBtn.setOnClickListener(view -> {goToCalendarActivity(account.getEmail());});

        goToCalendarActivity(account.getEmail());
    }

    public void goToCalendarActivity(String userEmail) {
        Intent intent = new Intent(this, CalendarActivity.class);
        intent.putExtra(GOOGLE_ACCOUNT, userEmail);
        startActivity(intent);
    }

    public void logOut() {
        com.google.android.gms.common.SignInButton signInBtn = findViewById(R.id.btn_main_signIn);
        signInBtn.setVisibility(View.VISIBLE);

        Button logOutBtn = findViewById(R.id.btn_main_signOut);
        logOutBtn.setVisibility(View.GONE);

        Button launchBtn = findViewById(R.id.btn_main_launchBtn);
        launchBtn.setVisibility(View.GONE);

        mGoogleSignInClient.signOut();
    }
}