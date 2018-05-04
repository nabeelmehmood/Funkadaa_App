package com.example.funkadaa.funkadaa;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.Toast;

import com.example.funkadaa.classes.HomeAdapter;
import com.example.funkadaa.classes.SearchAdapter;
import com.example.funkadaa.classes.SearchItemClicked;
import com.example.funkadaa.classes.SingleHomeFeedItem;
import com.example.funkadaa.classes.User;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

public class SearchFragment extends Fragment {

    GridView gridview;
    SearchAdapter ad;
    Context c;
    Fragment F;
    DatabaseReference mRef;
    AdView mAdView;


    ValueEventListener postListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            // Get Post object and use the values to update the UI
            ArrayList<String> s = new ArrayList<>();
            ArrayList<String> p = new ArrayList<>();
            for (DataSnapshot messageSnapshot: dataSnapshot.getChildren()) {
                String imageID = (String) messageSnapshot.child("imageID").getValue();
                String postID = (String) messageSnapshot.getKey();
                s.add(imageID);
                p.add(postID);
            }
            ad.setImgIDs(s);
            ad.setPostIDs(p);
            ad.notifyDataSetChanged();

            // ...
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            // Getting Post failed, log a message
            Log.w("POSTS", "loadPost:onCancelled", databaseError.toException());
            // ...
        }
    };


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
      //  container.removeAllViews();

        c = getContext();
        ad = new SearchAdapter(c);
        mRef = FirebaseDatabase.getInstance().getReference().child("posts");
        return inflater.inflate(R.layout.fragment_search, container, false);
    }
    void  ChangeFrag() {
        FragmentManager manager= getFragmentManager();//getSupportFragmentManager();
        FragmentTransaction transaction=manager.beginTransaction();
        transaction.replace(R.id.fragment,F);
        transaction.commit();
        Bundle bundle = new Bundle();

    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        mAdView = getView().findViewById(R.id.adView2);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        gridview = (GridView) getView().findViewById(R.id.gridview);
        mRef.addListenerForSingleValueEvent(postListener);
        gridview.setAdapter(ad);

        //SingleHomeFeedItem(User u, String imageUrlItem, String imageUrlDp, String description, Date time)
        final Intent intent=new Intent(c,SearchItemClicked.class);


        //i.putExtra("SelectedSubject",MyCourses.get(rv.getChildAdapterPosition(child)));
      //  startActivityForResult(i,345);

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                Toast.makeText(getContext(), "" + position,
                        Toast.LENGTH_SHORT).show();

               // startActivity(intent);

                F = new SearchItem();
                Bundle b = new Bundle();
                String postid = ad.getPostIDs().get(position);
                b.putString("postid", postid);
                F.setArguments(b);
                ChangeFrag();
            }
        });

    }


}
