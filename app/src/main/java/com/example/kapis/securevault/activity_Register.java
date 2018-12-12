package com.example.kapis.securevault;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class activity_Register extends AppCompatActivity {

    @BindView(R.id.registerEmail)
    EditText emailInput;

    @BindView(R.id.registerPassword1)
    EditText passwordInput;

    @BindView(R.id.registerPassword2)
    EditText password2Input;

    public FirebaseAuth mAuth;
    private ProgressDialog progressDialog;

    // COLORS USED
    final int colorRed = R.color.colorRed;
    final int colorBrightGreen = R.color.colorBrightGreen;
    final int colorWhite = R.color.colorWhite;
    final int colorBlack = R.color.colorBlack;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        ButterKnife.bind(this);
    }

    // Method that checks to see if the input is valid
    // It will also turn the incorrect fields red
    private boolean isValid()
    {
        boolean valid = true;

        if(TextUtils.isEmpty(emailInput.getText())
                || !Patterns.EMAIL_ADDRESS.matcher(emailInput.getText()).matches())
        {
            valid = false;
            emailInput.setBackgroundColor(getResources().getColor(colorRed));
            emailInput.setTextColor(getResources().getColor(colorWhite));
        }

        if(passwordInput.getText().toString().trim().isEmpty() ||
                passwordInput.getText().length() < 8 ||
                passwordInput.getText().toString().equals("NOT FOUND") ||
                password2Input.getText().toString().trim().isEmpty() ||
                !passwordInput.getText().toString().trim().equals(password2Input.getText().toString().trim()))
        {
            valid = false;
            passwordInput.setBackgroundColor(getResources().getColor(colorRed));
            passwordInput.setTextColor(getResources().getColor(colorWhite));
            password2Input.setBackgroundColor(getResources().getColor(colorRed));
            password2Input.setTextColor(getResources().getColor(colorWhite));
        }

        return valid;
    }


    // This is the activity_Register button
    // It calls the isValid Method to see if input is correct
    // Any field that is valid will turn back to original colors if it was red (incorrect) before.
    @OnClick(R.id.registerButton)
    public void submit_btn_func(View view) {
        if (!isValid()) {

            emailInput.getText().toString().trim();
            emailInput.setBackgroundColor(getResources().getColor(colorWhite));
            emailInput.setTextColor(getResources().getColor(colorBlack));

            passwordInput.getText().clear();
            passwordInput.setBackgroundColor(getResources().getColor(colorWhite));
            passwordInput.setTextColor(getResources().getColor(colorBlack));

            password2Input.getText().clear();
            password2Input.setBackgroundColor(getResources().getColor(colorWhite));
            password2Input.setTextColor(getResources().getColor(colorBlack));

            isValid();
            }

        // If all of the fields are valid then it accepts the email and pass
        // Creates a new user with those values
        else {

            progressDialog.setMessage("Registering User...Please Wait");
            progressDialog.show();
            String EMAIL = emailInput.getText().toString().trim();
            String PASS = passwordInput.getText().toString().trim();

            SharedPreferences sharedPreferences = getSharedPreferences("MyData", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("registered?",1);
            editor.putString("localEmail",EMAIL);
            editor.apply();

            mAuth.createUserWithEmailAndPassword(EMAIL, PASS)
                    .addOnCompleteListener(activity_Register.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){

                                emailInput.setBackgroundColor(getResources().getColor(colorBrightGreen));
                                emailInput.setTextColor(getResources().getColor(colorBlack));

                                passwordInput.setBackgroundColor(getResources().getColor(colorBrightGreen));
                                passwordInput.setTextColor(getResources().getColor(colorBlack));

                                password2Input.setBackgroundColor(getResources().getColor(colorBrightGreen));
                                password2Input.setTextColor(getResources().getColor(colorBlack));
                                Toast.makeText(activity_Register.this,"REGISTRATION SUCCESSFUL", Toast.LENGTH_SHORT).show();
                                // Passes new user information to the Main Activity screen for login validation
                                Intent newUser = new Intent(activity_Register.this, activity_LoginPage.class);
                                startActivity(newUser);
                            }
                            else
                            {
                                progressDialog.dismiss();
                                Toast.makeText(activity_Register.this,"ERROR: " + task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                                if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                                    final AlertDialog.Builder builder = new AlertDialog.Builder(activity_Register.this);
                                    builder.setMessage("Another account already has this email associated with it. " +
                                            "Please use different email or go to Login Screen to reset password.").setCancelable(false)
                                            .setPositiveButton("Login Screen", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    FirebaseAuth auth = FirebaseAuth.getInstance();
                                                    auth.signOut();
                                                    startActivity(new Intent(activity_Register.this, activity_LoginPage.class));
                                                }
                                            })
                                            .setNegativeButton("Try Again with new email", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.dismiss();
                                                }
                                            });
                                    AlertDialog alert = builder.create();
                                    alert.show();

                                }
                            }
                        }
                    });

        }
    }


}
