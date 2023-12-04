package com.example.prospector.dialogs;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

public class DialogForEditTheme extends DialogFragment {

    String question;

    public DialogForEditTheme(String question) {
        this.question = question;
    }

    public interface DialogForEditThemeListener {
        public void onQuitDialogPositiveClick(DialogFragment dialog);
        public void onQuitDialogNegativeClick(DialogFragment dialog);
    }

    DialogForEditTheme.DialogForEditThemeListener mListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (DialogForEditThemeListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException("Must implement DialogForEditThemeListener");
        }
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(question);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mListener.onQuitDialogPositiveClick(DialogForEditTheme.this);
            }
        });

        builder.setNegativeButton("Нет", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mListener.onQuitDialogNegativeClick(DialogForEditTheme.this);
            }
        });

        return builder.create();
    }
}