package com.example.funkadaa.funkadaa;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.google.android.gms.ads.MobileAds;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class FirstScreenActivity extends AppCompatActivity {
    /**
  **/
    private FirebaseAuth mAuth;

    TextToSpeech ts;
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        mAuth = FirebaseAuth.getInstance();
        MobileAds.initialize(this, "ca-app-pub-2509527130298191~8086187127");
        FirebaseUser currentUser = mAuth.getCurrentUser();
        //mAuth.signOut();
        if(currentUser != null)
          updateUI(currentUser);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        ts=new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {

            }
        });
        setContentView(R.layout.activity_first_screen);

    }
public void SpeakLogin(View view)
{
    String text;
    text="Please Login";
    ts.speak(text,TextToSpeech.QUEUE_FLUSH,null);
}
    public void SpeakRegister(View view)
    {
        String text;
        text="Register Kero Yaar";
        ts.speak(text,TextToSpeech.QUEUE_FLUSH,null);
    }
    protected void updateUI(FirebaseUser u){
        if (u != null){
            Intent i = new Intent(this, MainActivity.class);
            i.putExtra("user", u);
            startActivity(i);
        }
    }

    public void onClickRegister(View v){
        SpeakRegister(v);
        Intent i = new Intent(this, SignUpActivity.class);
        startActivity(i);
    }

    public void onClickLogin(View v){
        SpeakLogin(v);
        Intent i = new Intent(this, LoginActivity.class);
        startActivity(i);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mAuth=null;
    }
}
