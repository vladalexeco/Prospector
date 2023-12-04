package com.example.prospector.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

import com.example.prospector.interfaces.CommentChanger;

public class DialogCommentList extends DialogFragment {
    String[] comments;
    private CommentChanger commentChanger;

    public DialogCommentList(String[] comments) {
        this.comments = comments;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        commentChanger = (CommentChanger) context;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Комментарии");
        builder.setItems(comments, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                commentChanger.change(comments[which]);
            }
        });

        return builder.create();
    }

}
