package com.example.funkadaa.funkadaa;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.funkadaa.classes.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class InitializeUserActivity extends AppCompatActivity {

    User u;
    boolean online = false;
    Button button;
    Context c;

    ValueEventListener userListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            // Get Post object and use the values to update the UI
            u = dataSnapshot.getValue(User.class);
            u.setId(dataSnapshot.getKey());
            Toast.makeText(c,"User data successfully retrieved",Toast.LENGTH_SHORT).show();
            button.setEnabled(true);


            // ...
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            // Getting Post failed, log a message
            Log.w("USER", "loadPost:onCancelled", databaseError.toException());
            // ...
        }
    };

    public void onClick(View v){

        Intent i = new Intent(c,MainActivity.class);
     //   i.putExtra("user",u);
        Toast.makeText(c,"Redirecting",Toast.LENGTH_SHORT).show();
        startActivity(i);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        c = this;
        setContentView(R.layout.activity_initialize_user);
        isInternetOn(this);
        String userid = getIntent().getStringExtra("user");
        u = new User();
        DatabaseReference mref = FirebaseDatabase.getInstance().getReference().child("users").child(userid);
        Toast.makeText(this,"Getting user data", Toast.LENGTH_SHORT).show();
        button = (Button)findViewById(R.id.inituser);
        button.setEnabled(false);
        mref.addValueEventListener(userListener);
    }



    public void isInternetOn(Context context)
    {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo)
        {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    online = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    online = true;
        }
    }
}
