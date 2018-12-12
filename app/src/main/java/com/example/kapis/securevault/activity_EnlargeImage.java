package com.example.kapis.securevault;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kapis.securevault.Images.GlideApp;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;

public class activity_EnlargeImage extends AppCompatActivity {

    @BindView(R.id.imageData_imageView)
    ImageView imageView;

    @BindView(R.id.imageData_imageSrc)
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enlarge_image);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        intent.getExtras();
        String photoPath = intent.getStringExtra("photoPath");

        textView.setText(photoPath);

        File file = new File(photoPath);

        GlideApp
                .with(this)
                .load(file)
                .into(imageView);
    }
}
