package com.example.kapis.securevault;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;


public class RecyclerViewAdapter_Folder extends RecyclerView.Adapter<RecyclerViewAdapter_Folder.ViewHolder>{

    private ArrayList<String> mFolderNames;
    private ArrayList<Uri> mFolderUris;
    private Context mContext;

    public RecyclerViewAdapter_Folder(Context context, ArrayList<String> folderNames, ArrayList<Uri> folderUris ) {
        mFolderNames = folderNames;
        mFolderUris = folderUris;
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_folder_list, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        holder.folderNames.setText(mFolderNames.get(position));

        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(mContext, mFolderNames.get(position), Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(mContext, ImageGalleryActivity.class);
                intent.putExtra("folder_name", mFolderNames.get(position));
//                intent.putExtra("folder_uri", mFolderUris.get(position));
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(mFolderNames.isEmpty()) { return 0; }
        else{ return mFolderNames.size(); }
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView folderNames;
        RelativeLayout parentLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            folderNames = itemView.findViewById(R.id.folder_layout);
            parentLayout = itemView.findViewById(R.id.folder_parent_layout);
        }
    }
}



