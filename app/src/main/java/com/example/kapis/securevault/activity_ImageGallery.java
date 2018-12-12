package com.example.kapis.securevault;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kapis.securevault.Images.ImageListAdapter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class activity_ImageGallery extends AppCompatActivity {

    String mCurrentPhotoPath;
    File currentF;
    @BindView(R.id.gallery_folderName)
    TextView folderPathLabel;

    @BindView(R.id.images_GridView)
    GridView gridView;

    //ArrayList<String> f = new ArrayList<String>();
    File[] listFile; //array of files
    public static final int REQUEST_PERMISSION = 200;
    public static final int REQUEST_IMAGE = 100;
    public static final int RESULT_LOAD_IMG = 300;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_image_gallery);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        folderPathLabel.setText(intent.getStringExtra("folder_name"));
        gridView = findViewById(R.id.images_GridView);
        getFromFile();
        //Not sure if this is needed
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_PERMISSION);
        }


        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String photoPath = listFile[position].getAbsolutePath();
                Intent intent = new Intent(activity_ImageGallery.this, activity_EnlargeImage.class);
                intent.putExtra("photoPath", photoPath);
                startActivity(intent);
            }
        });

        //allows users to click on an image from the grid, show a dialog box, and gives the option to delete the image selected
        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

                CharSequence[] items = {"Delete"};//add more options in the future
                AlertDialog.Builder dialog = new AlertDialog.Builder(activity_ImageGallery.this);

                dialog.setTitle("Choose an action");
                dialog.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        if (item == 0) {
                            // delete
                            for (int i = 0; i < listFile.length; i++)
                                if(position == i)//image position is the index number of the array of files in the directory
                                    listFile[i].delete();

                            Toast.makeText(getApplicationContext(), "Image Deleted", Toast.LENGTH_SHORT).show();
                            getFromFile();// display gridView

                        }
                    }
                });
                dialog.show();
                return true;
            }
        } );
    }

    @OnClick(R.id.galleryNewItem)
    public void listImportOptions(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Choose 'Gallery' to import picture or 'Camera' to take new picture").setCancelable(false)
                .setPositiveButton("Camera", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) { openCameraIntent();
                    }
                })
                .setNegativeButton("Gallery", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
openGalleryIntent();                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
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

    private void openGalleryIntent(){
            Intent galleryIntent = new Intent(Intent.ACTION_PICK);
            galleryIntent.setType("image/*");
            File importFile = null;
            try {
                importFile = createImageFile();
            } catch (IOException e)
            {
                e.printStackTrace();
                Toast.makeText(this,"Error: Photo not saved. Try again.",Toast.LENGTH_SHORT).show();
                return;
            }
            Uri photoUri = FileProvider.getUriForFile(this, getPackageName() +".provider", importFile);
            startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
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
        if( requestCode == RESULT_LOAD_IMG)
        {
            if (resultCode == RESULT_OK)
            {
                Matrix matrix = new Matrix();
                matrix.postRotate(90);

                BitmapFactory.Options options = new BitmapFactory.Options();
                Bitmap bitmap = BitmapFactory.decodeFile(currentF.getAbsoluteFile().toString(),options);
                bitmap = Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight(),matrix,true);

                FileOutputStream out;
                try{
                    out = new FileOutputStream(currentF);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 85, out);
                    out.flush();
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                getFromFile();//method to show images with adapter
            }
            else if (resultCode == RESULT_CANCELED)
            {
                currentF.delete();//delete current image
                getFromFile();//method to show images with adapter
                Toast.makeText(this, "You cancelled the operation:", Toast.LENGTH_SHORT).show();
            }
        }
        else if (requestCode == REQUEST_IMAGE) {
            if (resultCode == RESULT_OK) {
                Matrix matrix = new Matrix();
                matrix.postRotate(90);

                BitmapFactory.Options options = new BitmapFactory.Options();
                Bitmap bitmap = BitmapFactory.decodeFile(currentF.getAbsoluteFile().toString(),options);
                bitmap = Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight(),matrix,true);

                FileOutputStream out;
                try{
                    out = new FileOutputStream(currentF);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 85, out);
                    out.flush();
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                getFromFile();//method to show images with adapter
            }
            else if (resultCode == RESULT_CANCELED) {
                currentF.delete();//delete current image
                getFromFile();//method to show images with adapter
                Toast.makeText(this, "You cancelled the operation:", Toast.LENGTH_SHORT).show();
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
                fi[i] = listFile[i].getAbsolutePath();//array of file path locations
                //f.add(listFile[i].getAbsolutePath());
                //Toast.makeText(this, listFile[i].getAbsolutePath().toString(), Toast.LENGTH_SHORT).show();
            }
            gridView.setAdapter(
                    new ImageListAdapter(activity_ImageGallery.this, fi )//use array of file paths as argument in constructor
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

        currentF = image;
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }
}
