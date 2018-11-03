package com.example.kapis.securevault;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {


    String[] mClasses;
    List<String> lstClasses;
    TextView mTv;
    LinearLayout mLinearLayout;
    Button mBtn;
    ListView mLv;
    MyAdapter adapter;
    String str="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);


        mClasses=getResources().getStringArray(R.array.fsc_bcs_classes);


        lstClasses=new ArrayList<String>(Arrays.asList(mClasses));

        mTv=(TextView)findViewById(R.id.main_Header);
        mLinearLayout=(LinearLayout)findViewById(R.id.main_Linear);

        mBtn = new Button(this);
        mLv=new ListView(this);

        adapter=new MyAdapter(this,-1,mClasses);

        mLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ChristmasParty();
            }
        });

        mLv.setAdapter(adapter);
        mLinearLayout.addView(mLv);





    }


    // If the user clicks the Lock Button in the top right corner
    @OnClick(R.id.main_LockBtn)
    public void lockApp(){

        // Create a dialog box where the user can choose whether they want to sign out or not
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
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
                        return;
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();

    }


    public void ChristmasParty() {
        Intent intent = new Intent(this, ChristmasParty.class);
        startActivity(intent);
    }

}
