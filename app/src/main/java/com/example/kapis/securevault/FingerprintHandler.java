package com.example.kapis.securevault;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.Manifest;
import android.os.CancellationSignal;
import android.os.Handler;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FingerprintHandler extends FingerprintManager.AuthenticationCallback {

    // You should use the CancellationSignal method whenever your app can no longer process user input, for example when your app goes
    // into the background. If you don’t use this method, then other apps will be unable to access the touch sensor, including the lockscreen!//

    private CancellationSignal cancellationSignal;
    private Context context;
    ImageView image;

    public FingerprintHandler(Context mContext) {
        this.context = mContext;
        image = ((Activity)context).findViewById(R.id.fingerprint_Image);
    }

    //Implement the startAuth method, which is responsible for starting the fingerprint authentication process//

    public void startAuth(FingerprintManager manager, FingerprintManager.CryptoObject cryptoObject) {

        cancellationSignal = new CancellationSignal();
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        manager.authenticate(cryptoObject, cancellationSignal, 0, this, null);
    }


    @Override
    //onAuthenticationError is called when a fatal error has occurred. It provides the error code and error message as its parameters//
    public void onAuthenticationError(int errMsgId, CharSequence errString) {
        //I’m going to display the results of fingerprint authentication as a series of toasts.
        //Here, I’m creating the message that’ll be displayed if an error occurs//
        Toast.makeText(context, "Authentication Error:\n" + errString, Toast.LENGTH_LONG).show();
    }

    @Override
    //onAuthenticationFailed is called when the fingerprint doesn’t match with any of the fingerprints registered on the device//
    public void onAuthenticationFailed() {
        Toast.makeText(context, "Authentication Failed", Toast.LENGTH_LONG).show();
    }


    @Override
    //onAuthenticationHelp is called when a non-fatal error has occurred. This method provides additional information about the error,
    //so to provide the user with as much feedback as possible I’m incorporating this information into my toast//
    public void onAuthenticationHelp(int helpMsgId, CharSequence helpString) {
        Toast.makeText(context, "Authentication Error:\n" + helpString, Toast.LENGTH_LONG).show();
    }


    @Override
    //onAuthenticationSucceeded is called when a fingerprint has been successfully matched to one of the fingerprints stored on the user’s device//
    public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {
        Toast.makeText(context, "Access Granted!", Toast.LENGTH_LONG).show();
        image.setImageResource(R.drawable.ic_clear_check);
        signInScreen();
    }


    public void signInScreen() {
        // brings you back to the SignInPage
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent homeIntent = new Intent(context, MainActivity.class);
                context.startActivity(homeIntent);
            }
        }, 400);
    }
}