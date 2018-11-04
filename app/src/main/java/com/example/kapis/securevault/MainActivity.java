package com.example.kapis.securevault;
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
import android.widget.TextView;
import android.widget.Toast;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements newfolderdialog.NewFolderListener {

    // Check if its the users first run
    SharedPreferences prefs = null;

    // Stores the FolderName where the Dialog for new folder is created
    String newFolderName;

    // Code to take picture request
    static final int REQUEST_TAKE_PHOTO = 1;

    // Stores the path of the picture in a String
    String mCurrentPhotoPath;

    // recvclerView in the activity_main.xml
    @BindView(R.id.main_FolderRecView)
    RecyclerView recyclerView;

    RecyclerView.LayoutManager layoutManager;
    ArrayList<String> folderList;
    ArrayList<Uri> folderUri;
    RecyclerViewAdapter_Folder adapter;


    @BindView(R.id.main_Header)
    TextView header;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        prefs = getSharedPreferences("MyData",MODE_PRIVATE);

        folderList = new ArrayList<String>();
        folderUri = new ArrayList<Uri>();

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new RecyclerViewAdapter_Folder(this, folderList, folderUri);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);

    }


    @Override
    protected void onResume() {
        super.onResume();
        if(prefs.getBoolean("firstRun",true)) {
            addNewFolder();
            prefs.edit().putBoolean("firstRun",false).apply();
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
    public void getNewFolderName(String foldername) {
           folderList.add(foldername);
           adapter.notifyDataSetChanged();
    }



    //Opens the Android camera and allows the user to take a picture.
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                Toast.makeText(this,"Error: Photo not saved. Try again.",Toast.LENGTH_SHORT).show();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    // Creates and returns an image
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

}
