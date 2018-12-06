package com.example.kapis.securevault;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ImageGalleryActivity extends AppCompatActivity {

    String mCurrentPhotoPath;
    @BindView(R.id.gallery_folderName)
    TextView folderPathLabel;

    @BindView(R.id.images_GridView)
    GridView gridView;

    //ArrayList<String> f = new ArrayList<String>();
    File[] listFile; //array of files
    public static final int REQUEST_PERMISSION = 200;
    public static final int REQUEST_IMAGE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_gallery);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        folderPathLabel.setText(intent.getStringExtra("folder_name"));
        gridView = (GridView) findViewById(R.id.images_GridView);
        getFromFile();
        //Not sure if this is needed
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_PERMISSION);
        }
    }

    @OnClick(R.id.galleryNewItem)
    public void AddNewPhoto(){
        openCameraIntent();
    }

    //not sure if this is needed
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_PERMISSION && grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Thanks for granting Permission", Toast.LENGTH_SHORT).show();
            }
        }
    }

    //Not sure if this is storing pictures in another file
    private void openCameraIntent() {
        Intent pictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (pictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            }
            catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this,"Error: Photo not saved. Try again.",Toast.LENGTH_SHORT).show();
                return;
            }
            Uri photoUri = FileProvider.getUriForFile(this, getPackageName() +".provider", photoFile);
            pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
            startActivityForResult(pictureIntent, REQUEST_IMAGE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        if (requestCode == REQUEST_IMAGE) {
            if (resultCode == RESULT_OK) {
                getFromFile();//method to show images with adapter
            }
            else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "You cancelled the operation", Toast.LENGTH_SHORT).show();
            }
        }
    }
    //method gets files from the folder and is used in the adapter
    public void getFromFile()
    {
        File file= new File(getFilesDir(),folderPathLabel.getText().toString());
        String [] fi;
        if (file.isDirectory())
        {
            listFile = file.listFiles();
            fi = new String[listFile.length];
            for (int i = 0; i < listFile.length; i++)
            {
                fi[i] = listFile[i].getAbsolutePath();
                //f.add(listFile[i].getAbsolutePath());
                //Toast.makeText(this, listFile[i].getAbsolutePath().toString(), Toast.LENGTH_SHORT).show();
            }
            gridView.setAdapter(
                    new ImageListAdapter(ImageGalleryActivity.this, fi )
            );

        }
    }

    // Creates and returns an image
    private File createImageFile()throws IOException{
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        //path to store images
        File str2 = getDatabasePath(getFilesDir().toString()+ "/" + folderPathLabel.getText());
        File image = File.createTempFile(
                imageFileName,  // prefix
                ".jpg",   // suffix
                str2      // directory
        );
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }
}
