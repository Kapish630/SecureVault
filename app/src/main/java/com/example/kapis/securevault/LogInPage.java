package com.example.kapis.securevault;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.example.kapis.securevault.UserContract.TABLE_NAME;

public class LogInPage extends AppCompatActivity {

    @BindView(R.id.login_Email)
    EditText emailInput;

    @BindView(R.id.login_Password)
    EditText passwordInput;

    // DATABASE
    UserAccountDBHelper mDbHelper;

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

        // UserAccoutDBHelper instance
        mDbHelper = new UserAccountDBHelper(this);

        }



    @OnClick(R.id.login_ForgotPassword)
    public void forgotScreen() {
        // Bring you to the Forgot Password page
        Intent intent = new Intent(this, ForgotPassword.class);
        startActivity(intent);
    }

    @OnClick(R.id.login_NewAccount)
    public void registerScreen() {
        // Brings you to the Registration Page
        Intent intent = new Intent(this, Register.class);
        startActivity(intent);
    }

    @OnClick(R.id.login_SignIn)
    public void fingerprintScreen(View view) {
        // Brings you to the Fingerprint Screen
        if (!loginSuccess()) {
            emailInput.getText().clear();
            passwordInput.getText().clear();

            emailInput.setTextColor(getResources().getColor(colorBlack));
            passwordInput.setTextColor(getResources().getColor(colorBlack));

            emailInput.setBackgroundColor(getResources().getColor(colorWhite));
            passwordInput.setBackgroundColor(getResources().getColor(colorWhite));

            loginSuccess();
        } else {
            emailInput.setBackgroundColor(getResources().getColor(colorBrightGreen));
            passwordInput.setBackgroundColor(getResources().getColor(colorBrightGreen));
            Toast.makeText(this, "LOGIN SUCCESSFUL", Toast.LENGTH_SHORT).show();

            SharedPreferences sharedPreferences = getSharedPreferences("MyData", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("currentUser", emailInput.getText().toString());
            editor.apply();

            // Brings you to the Registration Page
            Intent intent = new Intent(this, SecondAuthentication.class);
            startActivity(intent);

        }
    }

    // Checks login success
    private boolean loginSuccess() {

        boolean valid = true;

        if(!isPasswordValid())
            valid = false;


        if(!valid) {
            passwordInput.setBackgroundColor(getResources().getColor(colorRed));
            passwordInput.setTextColor(getResources().getColor(colorWhite));
            emailInput.setBackgroundColor(getResources().getColor(colorRed));
            emailInput.setTextColor(getResources().getColor(colorWhite));
            Toast.makeText(this, "LOGIN UNSUCCESSFUL, TRY AGAIN", Toast.LENGTH_SHORT).show();
        }
        return valid;
    }

    // Queries database to see if the email entered in the edit text
    // exists with a password entry
    private boolean isPasswordValid()
    {
        boolean valid = true;

        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String query = "SELECT Email, Password from " + TABLE_NAME;
        Cursor cursor = db.rawQuery(query,null);
        String email, correctPass;
        correctPass = "NOT FOUND";

        if(cursor.moveToFirst())
        {
            do{
                email = cursor.getString(0);

                if(email.equals(emailInput.getText().toString()))
                {
                    correctPass = cursor.getString(1);
                    break;
                }

            }while(cursor.moveToNext());
        }
        cursor.close();

        if(!passwordInput.getText().toString().equals(correctPass))
        {
            valid = false;
        }


        return valid;
    }

}
