package com.example.artifox.dbandroidchiefapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.session.AccessTokenPair;
import com.dropbox.client2.session.AppKeyPair;
import com.dropbox.client2.session.Session;

/**
 * Created by artifox on 11/23/2015.
 */
public class WelcomeActivity extends AppCompatActivity {

    private DropboxAPI<AndroidAuthSession> dropbox;

    private final static String DROPBOX_NAME = "dropbox_prefs";
    private final static String ACCESS_KEY = "s7nao9yleg42klm";
    private final static String ACCESS_SECRET = "baxkngl7o81sado";

    private Button mLoginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welco_layout);

        AndroidAuthSession session;
        AppKeyPair pair = new AppKeyPair(ACCESS_KEY, ACCESS_SECRET);
        SharedPreferences prefs = getSharedPreferences(DROPBOX_NAME, 0);
        String key = prefs.getString(ACCESS_KEY, null);
        String secret = prefs.getString(ACCESS_SECRET, null);

        if (key != null && secret != null) {
            AccessTokenPair token = new AccessTokenPair(key, secret);
            session = new AndroidAuthSession(pair, Session.AccessType.AUTO, token);
        } else {
            session = new AndroidAuthSession(pair, Session.AccessType.AUTO);
        }
        dropbox = new DropboxAPI<AndroidAuthSession>(session);

        //mLoginButton = (Button) findViewById(R.id.loginButton);
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dropbox.getSession().startAuthentication(WelcomeActivity.this);
            }
        });


    }
}
