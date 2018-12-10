package com.example.kapis.securevault;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LogInPage extends AppCompatActivity {

    FirebaseAuth mAuth;

    @BindView(R.id.login_Email)
    EditText emailInput;

    @BindView(R.id.login_Password)
    EditText passwordInput;

    // COLORS USED
    final int colorRed = R.color.colorRed;
    final int colorBrightGreen = R.color.colorBrightGreen;
    final int colorWhite = R.color.colorWhite;
    final int colorBlack = R.color.colorBlack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in_page);

        ButterKnife.bind(this);

        mAuth = FirebaseAuth.getInstance();

        }



    @OnClick(R.id.login_ForgotPassword)
    public void forgotScreen() {
        // Bring you to the Forgot Password page
        Intent intent = new Intent(this, ForgotPassword.class);
        startActivity(intent);
    }


    @OnClick(R.id.login_SignIn)
    public void fingerprintScreen(View view) {
        // Brings you to the Fingerprint Screen

        SharedPreferences sharedPreferences = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        String localEmail = sharedPreferences.getString("localEmail", "NOT FOUND");

        String email = emailInput.getText().toString().trim();
        String pass = passwordInput.getText().toString().trim();

        if(email.isEmpty()
                || pass.isEmpty() )
        {
            emailInput.setTextColor(getResources().getColor(colorWhite));
            passwordInput.setTextColor(getResources().getColor(colorWhite));
            emailInput.setBackgroundColor(getResources().getColor(colorRed));
            passwordInput.setBackgroundColor(getResources().getColor(colorRed));
            Toast.makeText(LogInPage.this,"Email/Password cant be empty",Toast.LENGTH_SHORT).show();
            return;
        }

        if(!email.equals(localEmail))
        {
            emailInput.setTextColor(getResources().getColor(colorWhite));
            passwordInput.setTextColor(getResources().getColor(colorWhite));
            emailInput.setBackgroundColor(getResources().getColor(colorRed));
            passwordInput.setBackgroundColor(getResources().getColor(colorRed));
            Toast.makeText(LogInPage.this,"The email you entered is not the email you registered with.",Toast.LENGTH_LONG).show();
            return;
        }

        else {

            final ProgressBar progressBar = findViewById(R.id.login_ProgressBar);
            progressBar.setVisibility(View.VISIBLE);
            progressBar.setIndeterminate(true);

            mAuth.signInWithEmailAndPassword(email, pass)
                    .addOnCompleteListener(LogInPage.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if ( task.isSuccessful()) {
                                emailInput.setBackgroundColor(getResources().getColor(colorBrightGreen));
                                emailInput.setTextColor(getResources().getColor(colorBlack));
                                passwordInput.setBackgroundColor(getResources().getColor(colorBrightGreen));
                                passwordInput.setTextColor(getResources().getColor(colorBlack));
                                Toast.makeText(LogInPage.this, "LOGIN SUCCESSFUL", Toast.LENGTH_SHORT).show();

                                // Brings you to the 2nd Auth Page
                                Intent intent = new Intent(LogInPage.this, SecondAuthentication.class);
                                startActivity(intent);
                            } else {
                                emailInput.setTextColor(getResources().getColor(colorWhite));
                                passwordInput.setTextColor(getResources().getColor(colorWhite));

                                emailInput.setBackgroundColor(getResources().getColor(colorRed));
                                passwordInput.setBackgroundColor(getResources().getColor(colorRed));
                                Toast.makeText(LogInPage.this, "ERROR: LOGIN UNSUCCESSFUL", Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                            }
                        }
                    });
            }
                       }
                       }
