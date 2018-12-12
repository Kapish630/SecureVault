package com.example.kapis.securevault;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class activity_Choice extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_choice);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.choice_Notes)
    public void openNotes(){
        startActivity(new Intent(activity_Choice.this, activity_NotesMain.class));
    }

    @OnClick(R.id.choice_Gallery)
    public void openGallery(){
        startActivity(new Intent(activity_Choice.this, activity_FoldersMain.class));
    }

}
