package com.example.funkadaa.funkadaa;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.funkadaa.classes.User;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {
    CallbackManager callbackManager;
    private static final String EMAIL = "email";
    int RC_SIGN_IN = 1134;
    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;

    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "foo@example.com:hello", "bar@example.com:world"
    };
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private FirebaseAuth mAuth;
    String email;
    String password;
    User u = new User();
    GoogleSignInClient mGoogleSignInClient;
    SignInButton signInButton;
    Context c;

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);

    }

    private void updateUIGoogle(GoogleSignInAccount account) {
        if (account != null){
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
        }
    }

    protected void updateUI(FirebaseUser u){
        if (u != null){
            Intent i = new Intent(this, MainActivity.class);
            i.putExtra("user", u);
            startActivity(i);
        }
    }

    OnClickListener googlesignin = new OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent signInIntent = mGoogleSignInClient.getSignInIntent();
            startActivityForResult(signInIntent, RC_SIGN_IN);
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        c = this;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.example.funkadaa.funkadaa",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        signInButton = findViewById(R.id.sign_in_button_google);
        signInButton.setOnClickListener(googlesignin);
        mAuth = FirebaseAuth.getInstance();
        callbackManager = CallbackManager.Factory.create();
        LoginButton loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions("email", "public_profile");
        loginButton.registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        handleFacebookAccessToken(loginResult.getAccessToken());
                        final AccessToken accessToken = loginResult.getAccessToken();

                    }

                    @Override
                    public void onCancel() {
                        // App code
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        // App code
                    }
                });

    }

    private void setUser(String id,  String name) {
        u.setId(id);

        u.setName(name);
        FirebaseDatabase.getInstance().getReference().child("users").child(id).child("name").setValue(u.getName());
        FirebaseDatabase.getInstance().getReference().child("users").child(id).child("id").setValue(u.getId());
        FirebaseDatabase.getInstance().getReference().child("users").child(id).child("dp").setValue("default.jpg");

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        email=null;
        password=null;
        mAuth=null;
    }


    private void handleFacebookAccessToken(AccessToken token) {
        Log.d("FACEBOOK", "handleFacebookAccessToken:" + token);
        final AccessToken accessToken = token;
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("FACEBOOK", "signInWithCredential:success");
                            final FirebaseUser user = mAuth.getCurrentUser();
                            GraphRequest request = GraphRequest.newGraphPathRequest(
                                    accessToken,
                                    "/"+accessToken.getUserId(),
                                    new GraphRequest.Callback() {
                                        @Override
                                        public void onCompleted(GraphResponse response) {
                                            // Insert your code here
                                            JSONObject j = response.getJSONObject();
                                            try {
                                                String id = accessToken.getUserId();
                                                String name = j.getString("name");
                                                setUser(mAuth.getUid(),name);
                                                updateUI(user);

                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }


                                    });


                            request.executeAsync();


                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("FACEBOOK", "signInWithCredential:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // ...
                    }
                });
    }

    public void onClickSignIn(View v){
        email = ((EditText)findViewById(R.id.signin_email)).getText().toString();
        password = ((EditText)findViewById(R.id.signin_password)).getText().toString();
        if (!email.isEmpty() && !password.isEmpty()){
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d("login", "signInWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                updateUI(user);
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w("login", "signInWithEmail:failure", task.getException());
                                Toast.makeText(LoginActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                                updateUI(null);
                            }

                            // ...
                        }
                    });

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w("GOOGLE", "Google sign in failed", e);
                // ...
            }
        }

    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d("GOOGLE", "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("GOOGLE", "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            String uid = mAuth.getUid();
                            String dp = mAuth.getCurrentUser().getPhotoUrl().toString();
                            String name = mAuth.getCurrentUser().getDisplayName();
                            String email = mAuth.getCurrentUser().getEmail();
                            String phone = mAuth.getCurrentUser().getPhoneNumber();
                            User user1 = new User();
                            user1.setName(name);
                            user1.setDp(dp);
                            user1.setPhone(phone);
                            user1.setEmail(email);
                            DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("users").child(uid);
                            ref.setValue(user1);
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("GOOGLE", "signInWithCredential:failure", task.getException());
                            Toast.makeText(c, "Authentication Failed.", Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // ...
                    }
                });
    }

}

