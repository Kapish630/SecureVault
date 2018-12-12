package com.example.kapis.securevault;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class activity_SplashScreen extends AppCompatActivity {

    public static int splash_time = 1500;
    public static int first;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        SharedPreferences sharedPreferences = getSharedPreferences("MyData", Context.MODE_PRIVATE);

        first = sharedPreferences.getInt("registered?",0);

        // Switch from Splash Screen after 1.5 seconds
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(first == 0)
                {
                    Intent nextIntent = new Intent(activity_SplashScreen.this, activity_Register.class);
                    startActivity(nextIntent);
                }
                else {
                    Intent nextIntent = new Intent(activity_SplashScreen.this, activity_LoginPage.class);
                    startActivity(nextIntent);

                }
                finish();
            }
        }, splash_time);


    }

}
