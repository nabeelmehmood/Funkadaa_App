package com.example.funkadaa.funkadaa;

import android.content.Intent;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.funkadaa.classes.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.TimeUnit;

public class SignUpActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private String email;
    private String password;
    private String name;
    private String phone;
    private String id;
    private DatabaseReference db;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    @Override
    protected void onStart() {
        super.onStart();
        mAuth = FirebaseAuth.getInstance();

    }

    private void updateUI(Object o) {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = FirebaseDatabase.getInstance().getReference();
        setContentView(R.layout.activity_sign_up);



    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mAuth=null;
        email=null;
        password=null;
        name=null;
        phone=null;
        db=null;
    }

    public void onClickRegister(View v){
        email = ((EditText)findViewById(R.id.signup_email)).getText().toString();
        password = ((EditText)findViewById(R.id.signup_password)).getText().toString();
        name = ((EditText)findViewById(R.id.signup_name)).getText().toString();
        phone = ((EditText)findViewById(R.id.signup_phone)).getText().toString();

        if (!email.isEmpty() && !password.isEmpty() && !name.isEmpty() && !phone.isEmpty()){

            Intent i = new Intent(this, VerifyPhoneActivity.class);
            User u = new User(name,email,phone);
            i.putExtra("password", password);
            i.putExtra("user",u);
            startActivity(i);
        }

    }




}
