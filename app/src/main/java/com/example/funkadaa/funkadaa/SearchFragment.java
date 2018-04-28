package com.example.funkadaa.funkadaa;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.Toast;

import com.example.funkadaa.classes.*;
import com.example.funkadaa.classes.Post;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import static android.content.ContentValues.TAG;

public class SearchFragment extends Fragment {

    GridView gridview;
    SearchAdapter ad;
    Context c;
    Fragment F;
    DatabaseReference mref;

    ValueEventListener postsListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            // Get Post object and use the values to update the UI
            PostsList h = new PostsList();
            h = dataSnapshot.getValue(PostsList.class);
            HashMap<String, com.example.funkadaa.classes.Post> list = h.getH();
            ArrayList<GridViewItem> g = new ArrayList<>();

            for (HashMap.Entry<String,Post> e : list.entrySet()) {
                // use e.getKey(), e.getValue()

                GridViewItem s1 = new GridViewItem(e.getValue().getImageID(),e.getKey());
                g.add(s1);
            }

            ad = new SearchAdapter(c,g);
            gridview = (GridView) getView().findViewById(R.id.gridview);
            gridview.setAdapter(ad);
            LinearLayoutManager mLayoutManager = new LinearLayoutManager(c);

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            // Getting Post failed, log a message
            Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
            // ...
        }
    };

    @Override
    public void onStart() {
        super.onStart();
        c = getContext();
        mref= FirebaseDatabase.getInstance().getReference().child("posts");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
      //  container.removeAllViews();

//        ad = new SearchAdapter(c);
        mref.addValueEventListener(postsListener);
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



        //SingleHomeFeedItem(User u, String imageUrlItem, String imageUrlDp, String description, Date time)
        final Intent intent=new Intent(c,SearchItemClicked.class);

        mref.addValueEventListener(postsListener);

        //i.putExtra("SelectedSubject",MyCourses.get(rv.getChildAdapterPosition(child)));
      //  startActivityForResult(i,345);

//        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            public void onItemClick(AdapterView<?> parent, View v,
//                                    int position, long id) {
//                Toast.makeText(getContext(), "" + position,
//                        Toast.LENGTH_SHORT).show();
//
//               // startActivity(intent);
//
//                F = new SearchItem();
//                ChangeFrag();
//            }
//        });

    }


}
