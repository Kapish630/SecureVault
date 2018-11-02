package com.example.kapis.securevault;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MyAdapter extends ArrayAdapter<String> implements View.OnClickListener {

    private final Context mContext;
    private final String[] lstItems;
    public MyAdapter(@NonNull Context context, int resource, @NonNull String[] objects) {
        super(context, resource, objects);
        this.mContext=context;
        this.lstItems=objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater=(LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.itemlayout, parent, false);
        TextView tv1=(TextView) rowView.findViewById(R.id.text2);
        TextView tv2 = (TextView) rowView.findViewById(R.id.text3);
        tv1.setText(lstItems[position]);

        return rowView;
    }

    @Override
    public void onClick(View v) {

    }
}
