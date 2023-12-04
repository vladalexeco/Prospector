package com.example.prospector.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;

import com.example.prospector.CatalogueOfFilesActivity;
import com.example.prospector.R;
import com.example.prospector.interfaces.FileRenamer;

public class DialogFromContextMenuRenameFile extends DialogFragment {
    String name;
    int pos;
    DialogListener mListener;
    private FileRenamer fileRenamer;

    public DialogFromContextMenuRenameFile(String name, int position) {
        this.name = name;
        this.pos = position;
    }

    public interface DialogListener {
        public void onDialogPositiveClick(DialogFragment dialog);
        public void onDialogNegativeClick(DialogFragment dialog);
    }

    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
        try {
            mListener = (DialogListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + "must implement DialogListener");
        }

        fileRenamer = (FileRenamer) activity;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.dialog_manual_input_rename_file, null));
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mListener.onDialogPositiveClick(DialogFromContextMenuRenameFile.this);
                fileRenamer.rename(pos);
            }
        });
        builder.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mListener.onDialogNegativeClick(DialogFromContextMenuRenameFile.this);
            }
        });
        return builder.create();
    }

    @Override
    public void onStart() {
        super.onStart();
        EditText editText = (EditText) getDialog().findViewById(R.id.rename);
        editText.setText(name);
    }

}
