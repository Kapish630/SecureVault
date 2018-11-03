package com.example.kapis.securevault;

import android.app.KeyguardManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.Manifest;
import android.os.Build;
import android.os.Bundle;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyPermanentlyInvalidatedException;
import android.security.keystore.KeyProperties;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SecondAuthentication extends AppCompatActivity {

    // Declare a string variable for the key we’re going to use in our fingerprint authentication
    private static final String KEY_NAME = "yourKey";
    Cipher cipher;
    KeyStore keyStore;
    KeyGenerator keyGenerator;
    FingerprintManager.CryptoObject cryptoObject;
    FingerprintManager fingerprintManager;
    KeyguardManager keyguardManager;

    @BindView(R.id.fingerprint_Message) TextView message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_authentication);
        ButterKnife.bind(this);

        // If you’ve set your app’s minSdkVersion to anything lower than 23, then you’ll need to verify that the device is running Marshmallow
        // or higher before executing any fingerprint-related code
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //Get an instance of KeyguardManager and FingerprintManager//
            keyguardManager =
                    (KeyguardManager) getSystemService(KEYGUARD_SERVICE);
            fingerprintManager =
                    (FingerprintManager) getSystemService(FINGERPRINT_SERVICE);

            //Check whether the device has a fingerprint sensor//
            if (!fingerprintManager.isHardwareDetected()) {
                // If a fingerprint sensor isn’t available, then inform the user that they’ll be unable to use your app’s fingerprint functionality//
                message.setTextColor(getResources().getColor(R.color.colorBlack));
                message.setBackgroundColor(getResources().getColor(R.color.colorRed));
                message.setText(R.string.err_No_Hardware);
            }
            //Check whether the user has granted your app the USE_FINGERPRINT permission//
            else if (ActivityCompat.checkSelfPermission(this, Manifest.permission.USE_FINGERPRINT)
                    != PackageManager.PERMISSION_GRANTED) {
                // If your app doesn't have this permission, then display the following text//
                message.setTextColor(getResources().getColor(R.color.colorBlack));
                message.setBackgroundColor(getResources().getColor(R.color.colorRed));
                message.setText(R.string.err_No_Permission);
            }

            //Check that the lockscreen is secured//
            else if (!keyguardManager.isKeyguardSecure()) {
                // If the user hasn’t secured their lockscreen with a PIN password or pattern, then display the following text//
                message.setTextColor(getResources().getColor(R.color.colorBlack));
                message.setBackgroundColor(getResources().getColor(R.color.colorRed));
                message.setText(R.string.err_No_LockonPhone);
            }

            //Check that the user has registered at least one fingerprint//
            else if (!fingerprintManager.hasEnrolledFingerprints()) {
                // If the user hasn’t configured any fingerprints, then display the following message//
                message.setTextColor(getResources().getColor(R.color.colorBlack));
                message.setBackgroundColor(getResources().getColor(R.color.colorRed));
                message.setText(R.string.err_No_Fingerprints);
            }

             else {
                try { generateKey();
                } catch (FingerprintException e) {
                    e.printStackTrace();
                }

                if (initCipher()) {
                    //If the cipher is initialized successfully, then create a CryptoObject instance//
                    cryptoObject = new FingerprintManager.CryptoObject(cipher);

                    // Here, I’m referencing the FingerprintHandler class that we’ll create in the next section. This class will be responsible
                    // for starting the authentication process (via the startAuth method) and processing the authentication process events//
                    FingerprintHandler helper = new FingerprintHandler(this);
                    helper.startAuth(fingerprintManager, cryptoObject);

                }
            }
        }
    }

//Create the generateKey method that we’ll use to gain access to the Android keystore and generate the encryption key//

    private void generateKey() throws FingerprintException {
        try {
            // Obtain a reference to the Keystore using the standard Android keystore container identifier (“AndroidKeystore”)//
            keyStore = KeyStore.getInstance("AndroidKeyStore");

            //Generate the key//
            keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore");

            //Initialize an empty KeyStore//
            keyStore.load(null);

            //Initialize the KeyGenerator//
            keyGenerator.init(new

                    //Specify the operation(s) this key can be used for//
                    KeyGenParameterSpec.Builder(KEY_NAME,
                    KeyProperties.PURPOSE_ENCRYPT |
                            KeyProperties.PURPOSE_DECRYPT)
                    .setBlockModes(KeyProperties.BLOCK_MODE_CBC)

                    //Configure this key so that the user has to confirm their identity with a fingerprint each time they want to use it//
                    .setUserAuthenticationRequired(true)
                    .setEncryptionPaddings(
                            KeyProperties.ENCRYPTION_PADDING_PKCS7)
                    .build());

            //Generate the key//
            keyGenerator.generateKey();

        } catch (KeyStoreException
                | NoSuchAlgorithmException
                | NoSuchProviderException
                | InvalidAlgorithmParameterException
                | CertificateException
                | IOException exc) {
            exc.printStackTrace();
            throw new FingerprintException(exc);
        }
    }

    //Create a new method that we’ll use to initialize our cipher//
    public boolean initCipher() {
        try {
            //Obtain a cipher instance and configure it with the properties required for fingerprint authentication//
            cipher = Cipher.getInstance(
                    KeyProperties.KEY_ALGORITHM_AES + "/"
                            + KeyProperties.BLOCK_MODE_CBC + "/"
                            + KeyProperties.ENCRYPTION_PADDING_PKCS7);
        } catch (NoSuchAlgorithmException |
                NoSuchPaddingException e) {
            throw new RuntimeException("Failed to get a Cipher", e);
        }

        try {
            keyStore.load(null);
            SecretKey key = (SecretKey) keyStore.getKey(KEY_NAME,
                    null);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            //Return true if the cipher has been initialized successfully//
            return true;
        } catch (KeyPermanentlyInvalidatedException e) {

            //Return false if cipher initialization failed//
            return false;
        } catch (KeyStoreException | CertificateException
                | UnrecoverableKeyException | IOException
                | NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException("Failed to initialize the Cipher", e);
        }
    }

    private class FingerprintException extends Exception {
        public FingerprintException(Exception e) {
            super(e);
        }
    }


}