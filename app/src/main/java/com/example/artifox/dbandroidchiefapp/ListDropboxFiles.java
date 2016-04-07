package com.example.artifox.dbandroidchiefapp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.DropboxAPI.Entry;
import com.dropbox.client2.exception.DropboxException;

import java.util.ArrayList;

/**
 * Created by artifox on 3/28/2015.
 */
public class ListDropboxFiles extends AsyncTask<Void, Void, ArrayList<String>>{

    private DropboxAPI<?> dropbox;
    private String path;
    private Handler handler;


    public ListDropboxFiles(DropboxAPI<?> dropbox, String path, Handler handler) {
        this.dropbox = dropbox;
        this.path = path;
        this.handler = handler;
    }


    @Override
    protected void onPostExecute(ArrayList<String> result) {
        Message msgObj = handler.obtainMessage();
        Bundle b = new Bundle();
        b.putStringArrayList("data", result);
        msgObj.setData(b);
        handler.sendMessage(msgObj);
    }

    @Override
    protected ArrayList<String> doInBackground(Void... params) {
        ArrayList<String> files = new ArrayList<String>();
        try {
            Entry directory = dropbox.metadata(path, 1000, null, true, null);
            for (Entry entry : directory.contents) {
                files.add(entry.fileName());
            }
        } catch (DropboxException e) {
            System.out.println("Error in doInBackap");
            e.printStackTrace();
        }

        return files;
    }
}
