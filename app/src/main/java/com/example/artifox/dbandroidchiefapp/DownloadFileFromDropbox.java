package com.example.artifox.dbandroidchiefapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.widget.ImageView;
import android.widget.Toast;

import com.dropbox.client2.DropboxAPI;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by artifox on 3/30/2015.
 */
public class DownloadFileFromDropbox extends AsyncTask<Void, Long, Boolean> {

    private DropboxAPI<?> dropbox;
    private String path;
    private Context context;

    public DownloadFileFromDropbox(Context context, DropboxAPI<?> dropbox,
                                   String path){
        this.context = context.getApplicationContext();
        this.dropbox = dropbox;
        this.path = path;
    }


    @Override
    protected Boolean doInBackground(Void... params) {

        FileOutputStream outputStream = null;
        try {
            File file = new File("/sdcard/file.txt");
            outputStream = new FileOutputStream(file);
            DropboxAPI.DropboxFileInfo info = dropbox.getFile("/DropboxSample/textfile.txt", null, outputStream, null);
            return true;
        }catch (Exception e) {
            System.out.println("Something went wrong: " + e);
        }finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                }
            }
        }
        return false;
    }

    @Override
    protected void onPostExecute(Boolean result) {
        if (result) {
            Toast.makeText(context, "File Download Sucesfully!",
                    Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(context, "Failed to download file", Toast.LENGTH_LONG)
                    .show();
        }
    }
}
