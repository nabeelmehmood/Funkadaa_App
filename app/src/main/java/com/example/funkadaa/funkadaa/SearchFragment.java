package com.example.funkadaa.funkadaa;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
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

import java.util.Date;

public class SearchFragment extends Fragment {

    GridView gridview;
    SearchAdapter ad;
    Context c;
    Fragment F;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
      //  container.removeAllViews();

        c = getContext();
        ad = new SearchAdapter(c);

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

        gridview = (GridView) getView().findViewById(R.id.gridview);

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
                ChangeFrag();
            }
        });

    }


}
