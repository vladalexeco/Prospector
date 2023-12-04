package com.example.prospector.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

public class QuitDialog extends DialogFragment {

    String question;
    int operation;

    public QuitDialog(String question) {
        this.question = question;
    }

    public interface QuitDialogListener {
        public void onQuitDialogPositiveClick(DialogFragment dialog);
        public void onQuitDialogNegativeClick(DialogFragment dialog);
    }

    QuitDialogListener mListener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (QuitDialogListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + "must implement QuitDialogListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(question);
        builder.setPositiveButton("Да", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mListener.onQuitDialogPositiveClick(QuitDialog.this);
            }
        });
        builder.setNegativeButton("Нет", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mListener.onQuitDialogNegativeClick(QuitDialog.this);
            }
        });
        return builder.create();
    }
}
