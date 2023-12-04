package com.example.prospector.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

import com.example.prospector.interfaces.FileRemover;

public class DialogFromContextMenuDeleteFile extends DialogFragment {
    int pos;
    private FileRemover fileRemover;

    public DialogFromContextMenuDeleteFile(int pos) {
        this.pos = pos;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        fileRemover = (FileRemover) context;
    }

    @Override
    public Dialog onCreateDialog(Bundle saveInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Удалить файл?");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                fileRemover.remove(pos);
            }
        });
        builder.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        return builder.create();
    }
}
