package com.example.artifox.dbandroidchiefapp;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;

/**
 * Created by artifox on 11/23/2015.
 */
public class StartDialog extends DialogFragment {

    public interface SignInDialogListener {
        public void onSignInPositiveClick(DialogFragment dialog);

        public void onSignInNegativeClick(DialogFragment dialog);
    }

    SignInDialogListener mSignInDialogListener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        builder.setView(inflater.inflate(R.layout.welco_layout, null));
        builder.setPositiveButton("Sign In", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mSignInDialogListener.onSignInPositiveClick(StartDialog.this);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                System.exit(0);
            }
        });
        return builder.create();

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mSignInDialogListener = (SignInDialogListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() +
                    " must implement NoticeDialogListener");
        }
    }
}
