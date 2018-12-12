package com.example.kapis.securevault;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kapis.securevault.Folders.FoldersAdapter;
import com.example.kapis.securevault.Folders.dialog_Folder;
import com.google.firebase.auth.FirebaseAuth;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class activity_FoldersMain extends AppCompatActivity implements dialog_Folder.NewFolderListener {

    // Check if its the users first run
    SharedPreferences prefs = null;


    // recvclerView in the activity_folders_mainers_main.xml
    @BindView(R.id.main_FolderRecView)
    RecyclerView recyclerView;

    RecyclerView.LayoutManager layoutManager;
    ArrayList<String> folderList;
    FoldersAdapter adapter;


    @BindView(R.id.main_Header)
    TextView header;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_folders_main);
        ButterKnife.bind(this);

        folderList = new ArrayList<String>(Arrays.asList(fileList()));
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new FoldersAdapter(this, folderList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
    }


    public boolean createFolder(String typedInFolderName){
        String newDirectoryName = getFilesDir() + "/" + typedInFolderName;
        File newDirectory = new File(newDirectoryName);
        if(!newDirectory.exists())
            if(!newDirectory.mkdir()){
                Toast.makeText(this, "For some reason, " + typedInFolderName + " can't be created. Try again", Toast.LENGTH_LONG).show();
                adapter.notifyDataSetChanged();
                return false;
            }
            else {
                Toast.makeText(this, "New Folder has been added.", Toast.LENGTH_SHORT).show();
                adapter.notifyDataSetChanged();
                return true;
        }
        else {
            Toast.makeText(this, "Folder with that name already exits. Please try again with an original name.", Toast.LENGTH_LONG).show();
            adapter.notifyDataSetChanged();
            return false;
        }
    }



    // If the user clicks the Lock Button in the top right corner
    @OnClick(R.id.main_LockBtn)
    public void lockApp(){
        // Create a dialog box where the user can choose whether they want to sign out or not
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to lock the app?").setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseAuth auth = FirebaseAuth.getInstance();
                        auth.signOut();
                        startActivity(new Intent(activity_FoldersMain.this, activity_LoginPage.class));
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
    }

    // If the user clicks the Lock Button in the top right corner
    @OnClick(R.id.main_Logo)
    public void goHome(){
        startActivity(new Intent(activity_FoldersMain.this, activity_Choice.class));
    }

    //In here we will allow the user to add a new folder
    @OnClick(R.id.mainNewFolder)
    public void addNewFolder() {
        openNewFolderDialog();
    }

    public void openNewFolderDialog(){
        dialog_Folder dialog_Folder = new dialog_Folder();
        dialog_Folder.show(getSupportFragmentManager(),"newFolderDialog");
    }

    @Override
    public void getNewFolderName(String typedInFolderName) {
           boolean created = createFolder(typedInFolderName);
           if(created) {
               folderList.add(typedInFolderName);
               adapter.notifyDataSetChanged();
           }
    }


}
