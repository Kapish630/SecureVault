package com.example.kapis.securevault;

import android.content.Intent;
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
        mClasses=getResources().getStringArray(R.array.fsc_bcs_classes);


        lstClasses=new ArrayList<String>(Arrays.asList(mClasses));

        mTv=(TextView)findViewById(R.id.foldersHeader);
        mLinearLayout=(LinearLayout)findViewById(R.id.linear);

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
    public void ChristmasParty() {
        Intent intent = new Intent(this, ChristmasParty.class);
        startActivity(intent);
    }

}
