package com.example.kapis.securevault;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class RecyclerViewAdaptor_Folder extends RecyclerView.Adapter<RecyclerViewAdaptor_Folder.myViewHolder> {

    private ArrayList<String> list;

    public RecyclerViewAdaptor_Folder(ArrayList<String> list){
        this.list = list;
    }



    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {


        TextView textView = (TextView) LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_folder_list,viewGroup,false);
        myViewHolder myViewHolder = new myViewHolder(textView);


        return myViewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder viewHolder, int position) {

        viewHolder.folderName.setText(list.get(position));

    }

    @Override
    public int getItemCount() {
        if(list.isEmpty()) { return 0; }
        else{ return list.size(); }
    }

    public static class myViewHolder extends RecyclerView.ViewHolder {

        TextView folderName;
        public myViewHolder(@NonNull TextView itemView) {
            super(itemView);
            folderName = itemView;
        }
    }

}
