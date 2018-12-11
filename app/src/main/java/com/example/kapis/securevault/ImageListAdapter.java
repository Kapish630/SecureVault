package com.example.kapis.securevault;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;


public class ImageListAdapter extends ArrayAdapter {
    private Context context;
    private LayoutInflater inflater;
    private String [] imageList;

    public ImageListAdapter(Context context, String[] imageList) {
        super(context, R.layout.image_listview, imageList);
        this.context = context;
        this.imageList = imageList;//string of images paths will be used when called
        inflater = LayoutInflater.from(context);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        if(null == convertView){
            convertView = inflater.inflate(R.layout.image_listview, parent, false);
        }
        GlideApp
                .with(context)
                .load(imageList[position])
                .into((ImageView)convertView);
        return convertView;
    }

}
