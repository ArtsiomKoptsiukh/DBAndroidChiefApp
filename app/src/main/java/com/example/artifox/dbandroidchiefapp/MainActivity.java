package com.example.artifox.dbandroidchiefapp;

import android.app.DialogFragment;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.session.AccessTokenPair;
import com.dropbox.client2.session.AppKeyPair;
import com.dropbox.client2.session.Session.AccessType;
import com.dropbox.client2.session.TokenPair;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements View.OnClickListener, StartDialog.SignInDialogListener {

    private DropboxAPI<AndroidAuthSession> dropbox;
    private final static String FILE_DIR = "/DropboxSample/";
    private final static String DROPBOX_NAME = "dropbox_prefs";
    private final static String ACCESS_KEY = "s7nao9yleg42klm";
    private final static String ACCESS_SECRET = "baxkngl7o81sado";

    private Button logIn;
    private Button uploadFile;
    private Button downloadFile;
    private Button listFiles;
    private LinearLayout container;
    private boolean isLoggedIn;

    private Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);


//код из примера
        //logIn = (Button) findViewById(R.id.dropbox_login);
        //logIn.setOnClickListener(this);
        uploadFile = (Button) findViewById(R.id.upload_file);
        uploadFile.setOnClickListener(this);
        downloadFile = (Button) findViewById(R.id.download_file);
        downloadFile.setOnClickListener(this);
        listFiles = (Button) findViewById(R.id.list_files);
        listFiles.setOnClickListener(this);
        container = (LinearLayout) findViewById(R.id.container_files);
        loggedIn(false);

        AndroidAuthSession session;
        AppKeyPair pair = new AppKeyPair(ACCESS_KEY, ACCESS_SECRET);
        SharedPreferences prefs = getSharedPreferences(DROPBOX_NAME, 0);
        String key = prefs.getString(ACCESS_KEY, null);
        String secret = prefs.getString(ACCESS_SECRET, null);

        if (key != null && secret != null) {
            AccessTokenPair token = new AccessTokenPair(key, secret);
            session = new AndroidAuthSession(pair, AccessType.AUTO, token);
        } else {
            session = new AndroidAuthSession(pair, AccessType.AUTO);
        }
        dropbox = new DropboxAPI<AndroidAuthSession>(session);
//конец кода из примера
        DialogFragment dialogFragment = new StartDialog();
        dialogFragment.show(getFragmentManager(),"welcomeDialog");
    }


    @Override
    protected void onResume() {
        super.onResume();
    //после логина, делаем кнопки доступными
        AndroidAuthSession session = dropbox.getSession();
        if (session.authenticationSuccessful()) {
            try {
                session.finishAuthentication();
                TokenPair tokens = session.getAccessTokenPair();
                SharedPreferences prefs = getSharedPreferences(DROPBOX_NAME, 0);
                Editor editor = prefs.edit();
                editor.putString(ACCESS_KEY, tokens.key);
                editor.putString(ACCESS_SECRET, tokens.secret);
                editor.commit();
                loggedIn(true);
            } catch (IllegalStateException e) {
                Toast.makeText(this, "Error during Dropbox authentication",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void loggedIn(boolean isLogged) {
        isLoggedIn = isLogged;
        uploadFile.setEnabled(isLogged);
        listFiles.setEnabled(isLogged);
       // logIn.setText(isLogged ? "Log out" : "Log in");
    }

    private final Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            ArrayList<String> result = msg.getData().getStringArrayList("data");
            for (String fileName : result) {
                Log.i("ListFiles", fileName);
                TextView tv = new TextView(MainActivity.this);
                tv.setText(fileName);
                container.addView(tv);
            }
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            /*case R.id.dropbox_login:
                if (isLoggedIn) {
                    dropbox.getSession().unlink();
                    loggedIn(false);
                } else {
                    dropbox.getSession().startAuthentication(MainActivity.this);
                }

                break;*/
            case R.id.list_files:
                ListDropboxFiles list = new ListDropboxFiles(dropbox, FILE_DIR,
                        handler);
                list.execute();
                break;
            case R.id.upload_file:
                UploadFileToDropbox upload = new UploadFileToDropbox(this, dropbox,
                        FILE_DIR);
                upload.execute();
                break;
            case R.id.download_file:
                DownloadFileFromDropbox download = new DownloadFileFromDropbox(this, dropbox,
                        FILE_DIR);
                download.execute();
                break;
            default:
                break;
        }

    }

    @Override
    public void onSignInPositiveClick(DialogFragment dialog) {
        dropbox.getSession().startAuthentication(MainActivity.this);
    }

    @Override
    public void onSignInNegativeClick(DialogFragment dialog) {

    }
}
