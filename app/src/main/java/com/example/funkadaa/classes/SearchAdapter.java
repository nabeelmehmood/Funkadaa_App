package com.example.funkadaa.classes;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.example.funkadaa.funkadaa.R;

import java.util.ArrayList;
import java.util.Date;

public class SearchAdapter extends BaseAdapter {
    private Context mContext;


    SingleHomeFeedItem s ;

    // references to our images
    private Integer[] mThumbIds = {
            R.drawable.starrynight, R.drawable.zain12,
            R.drawable.zain1, R.drawable.zain11,
            R.drawable.zain2, R.drawable.zain13,
  /*          R.drawable.zain3, R.drawable.zain14,
            R.drawable.zain4, R.drawable.zain15,
            R.drawable.zain5, R.drawable.zain16,
            R.drawable.zain6, R.drawable.zain17,
            R.drawable.zain7, R.drawable.zain18,
            R.drawable.zain8, R.drawable.zain19,
            R.drawable.zain9, R.drawable.zain10,
  */  };

    public SearchAdapter(Context c) {
        mContext = c;

    }

    public int getCount() {
        return mThumbIds.length;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams((int)mContext.getResources().getDimension(R.dimen.width), (int)mContext.getResources().getDimension(R.dimen.height)));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(5, 5, 5, 5);
        } else {
            imageView = (ImageView) convertView;
        }


        imageView.setImageResource(mThumbIds[position]);
        return imageView;
    }


}
