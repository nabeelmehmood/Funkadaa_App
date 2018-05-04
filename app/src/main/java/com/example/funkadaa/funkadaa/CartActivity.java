package com.example.funkadaa.funkadaa;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.funkadaa.classes.CartAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CartActivity extends AppCompatActivity {
    String curruser;
    ArrayList<String> items = new ArrayList<>();
    RecyclerView rv;
    CartAdapter ad;
    Button checkout;
    Context c = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        checkout = findViewById(R.id.checkoutbutton);
        checkout.setOnClickListener(checkoutListener);
        curruser = getIntent().getStringExtra("user");
        rv = findViewById(R.id.cartrecycler);
        rv.setLayoutManager(new LinearLayoutManager(this));
        ad = new CartAdapter(this,items);
        rv.setAdapter(ad);
        DatabaseReference mref = FirebaseDatabase.getInstance().getReference().child("users").child(curruser).child("cart");
        mref.addListenerForSingleValueEvent(cartListener);
    }

    View.OnClickListener checkoutListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Toast.makeText(c,"Checkout pressed", Toast.LENGTH_SHORT).show();
        }
    };

    ValueEventListener cartListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            if (dataSnapshot.exists()) {
                for (DataSnapshot messageSnapshot: dataSnapshot.getChildren()) {
                    items.add(messageSnapshot.getKey());
                }
            }
            ad.notifyDataSetChanged();
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };
}
