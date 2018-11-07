package com.example.kapis.securevault;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.io.File;



public class RecyclerViewAdapter_Folder extends RecyclerView.Adapter<RecyclerViewAdapter_Folder.ViewHolder>{

    private ArrayList<String> mFolderNames;
    private Context mContext;

    public RecyclerViewAdapter_Folder(Context context, ArrayList<String> folderNames ) {
        mFolderNames = folderNames;
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_folder_list, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        holder.folderNames.setText(mFolderNames.get(position));

        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(mContext, mFolderNames.get(position), Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(mContext, ImageGalleryActivity.class);
                intent.putExtra("folder_name", mFolderNames.get(position));
                mContext.startActivity(intent);
            }
        });


        holder.parentLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setMessage("Are you sure you want to delete " + mFolderNames.get(position) + "?").setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                File dirToBeDeleted = new File(mContext.getFilesDir() + "/" + mFolderNames.get(position));
                                deleteFilesandDirectory(dirToBeDeleted);
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
                return true;
            }
        });

    }

    private void deleteFilesandDirectory(File toBeDeleted)
    {
        if(toBeDeleted.isDirectory())
        {
            for(File child : toBeDeleted.listFiles())
                deleteFilesandDirectory(child);
        }
        toBeDeleted.delete();
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



