package com.example.kapis.securevault;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnLongClick;

public class MainActivity extends AppCompatActivity implements newfolderdialog.NewFolderListener {

    // Check if its the users first run
    SharedPreferences prefs = null;


    // recvclerView in the activity_main.xml
    @BindView(R.id.main_FolderRecView)
    RecyclerView recyclerView;

    RecyclerView.LayoutManager layoutManager;
    ArrayList<String> folderList;
    RecyclerViewAdapter_Folder adapter;


    @BindView(R.id.main_Header)
    TextView header;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        prefs = getSharedPreferences("MyData",MODE_PRIVATE);

        checkFirstRun();

        folderList = new ArrayList<String>(Arrays.asList(fileList()));
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new RecyclerViewAdapter_Folder(this, folderList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
    }

    private void checkFirstRun(){
        if(prefs.getBoolean("firstRun",true)) {
            addNewFolder();
            prefs.edit().putBoolean("firstRun",false).apply();
        }
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


    /*

    @Override
    protected void onResume() {
        super.onResume();
        if(prefs.getBoolean("firstRun",true)) {
            addNewFolder();
            prefs.edit().putBoolean("firstRun",false).apply();
        }
    }

*/
    // If the user clicks the Lock Button in the top right corner
    @OnClick(R.id.main_LockBtn)
    public void lockApp(){

        // Create a dialog box where the user can choose whether they want to sign out or not
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to lock the app?").setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(MainActivity.this, LogInPage.class));
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

    //In here we will allow the user to add a new folder
    @OnClick(R.id.mainNewFolder)
    public void addNewFolder() {
        openNewFolderDialog();
    }

    public void openNewFolderDialog(){
        newfolderdialog newfolderdialog = new newfolderdialog();
        newfolderdialog.show(getSupportFragmentManager(),"newFolderDialog");
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
