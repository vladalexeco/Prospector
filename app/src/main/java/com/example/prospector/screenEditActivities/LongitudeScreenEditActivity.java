package com.example.prospector.screenEditActivities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.prospector.CommentScreenActivity;
import com.example.prospector.PointInfoToChange;
import com.example.prospector.R;
import com.example.prospector.TopLevelActivity;
import com.example.prospector.dialogs.QuitDialog;
import com.example.prospector.dialogs.SimpleDialog;

public class LongitudeScreenEditActivity extends LatitudeScreenEditActivity {

    String lonForChecking;
    String latScreen;

    public final static String order = "Order";
    public final static String search = "Search";
    public final static String mad = "Mad";
    public final static String index = "Index";
    public final static String comment = "Comment";
    public final static String latitude = "Latitude";
    public final static String longitude = "Longitude";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            Intent intent = new Intent(LongitudeScreenEditActivity.this, TopLevelActivity.class);
            startActivity(intent);
        }

        TextView screenHeader = (TextView) findViewById(R.id.screenHeaderForLat);
        screenHeader.setText("Восточная долгота");

        Bundle arguments = getIntent().getExtras();

        if (arguments != null) {
            lonForChecking = arguments.getString(LongitudeScreenEditActivity.longitude);
            if (!lonForChecking.equals("no data")) {
                String[] firstSplit = lonForChecking.split(degreeSymbol);
                String degreeVal = firstSplit[0];
                String[] secondSplit = firstSplit[1].split(minuteSymbol);
                String minuteVal = secondSplit[0];
                String secondVal = secondSplit[1].substring(0, secondSplit[1].length() - 1);

                degreeEdit.setText(degreeVal);
                minuteEdit.setText(minuteVal);
                secondEdit.setText(secondVal);
            } else {
                degreeEdit.setText("");
                minuteEdit.setText("");
                secondEdit.setText("");
            }
        }
    }

    @Override
    public void onClickOkBtn(View view) {
        super.onClickOkBtn(view);
        String currentDegree = degreeEdit.getText().toString();
        String currentMinute = minuteEdit.getText().toString();
        String currentSecond = secondEdit.getText().toString();

        String currentValCoord = currentDegree + degreeSymbol + currentMinute + minuteSymbol + currentSecond + secondSymbol;

        if (lonForChecking.equals(currentValCoord)) {
            Intent intent = new Intent(LongitudeScreenEditActivity.this, PointInfoToChange.class);
            intent.putExtra(PointInfoToChange.ord, orderScreen);
            intent.putExtra(PointInfoToChange.searchEdit, searchScreen);
            intent.putExtra(PointInfoToChange.madEdit, madScreen);
            intent.putExtra(PointInfoToChange.indexEdit, indexScreen);
            intent.putExtra(PointInfoToChange.commentEdit, commentScreen);
            intent.putExtra(PointInfoToChange.latEdit, latScreen);
            intent.putExtra(PointInfoToChange.lonEdit, currentValCoord);
            startActivity(intent);
        } else if (currentDegree.equals("") && currentMinute.equals("") && currentSecond.equals("")) {
            flag = "no data";
            QuitDialog quitDialog = new QuitDialog("Скорректировать значение?");
            quitDialog.show(getSupportFragmentManager(), "quit");
        } else if (currentDegree.equals("") || currentMinute.equals("") || currentSecond.equals("")) {
            SimpleDialog dialog = new SimpleDialog("Заполните все поля или оставьте их пустыми");
            dialog.show(getSupportFragmentManager(), "simple");
        } else {
            flag = "current data";
            QuitDialog quitDialog = new QuitDialog("Скорректировать значение?");
            quitDialog.show(getSupportFragmentManager(), "quit");
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        Bundle arguments = getIntent().getExtras();
        if (arguments != null) {
            latScreen = arguments.getString(LongitudeScreenEditActivity.latitude);
        }
    }

    public void onClickCancelBtn(View view) {
        Intent intent = new Intent(LongitudeScreenEditActivity.this, PointInfoToChange.class);
        intent.putExtra(PointInfoToChange.ord, orderScreen);
        intent.putExtra(PointInfoToChange.searchEdit, searchScreen);
        intent.putExtra(PointInfoToChange.madEdit, madScreen);
        intent.putExtra(PointInfoToChange.indexEdit, indexScreen);
        intent.putExtra(PointInfoToChange.commentEdit, commentScreen);
        intent.putExtra(PointInfoToChange.latEdit, latScreen);
        intent.putExtra(PointInfoToChange.lonEdit, lonForChecking);
        startActivity(intent);
    }

    public void onQuitDialogPositiveClick(DialogFragment dialog) {
        Intent intent = new Intent(LongitudeScreenEditActivity.this, PointInfoToChange.class);
        if (flag.equals("no data")) {
            intent.putExtra(PointInfoToChange.ord, orderScreen);
            intent.putExtra(PointInfoToChange.searchEdit, searchScreen);
            intent.putExtra(PointInfoToChange.madEdit, madScreen);
            intent.putExtra(PointInfoToChange.indexEdit, indexScreen);
            intent.putExtra(PointInfoToChange.commentEdit, commentScreen);
            intent.putExtra(PointInfoToChange.latEdit, latScreen);
            intent.putExtra(PointInfoToChange.lonEdit, "no data");
            startActivity(intent);
        } else if (flag.equals("current data")) {
            String currentDegree = degreeEdit.getText().toString();
            String currentMinute = minuteEdit.getText().toString();
            String currentSecond = secondEdit.getText().toString();

            String currentValCoord = currentDegree + degreeSymbol + currentMinute + minuteSymbol + currentSecond + secondSymbol;

            intent.putExtra(PointInfoToChange.ord, orderScreen);
            intent.putExtra(PointInfoToChange.searchEdit, searchScreen);
            intent.putExtra(PointInfoToChange.madEdit, madScreen);
            intent.putExtra(PointInfoToChange.indexEdit, indexScreen);
            intent.putExtra(PointInfoToChange.commentEdit, commentScreen);
            intent.putExtra(PointInfoToChange.latEdit, latScreen);
            intent.putExtra(PointInfoToChange.lonEdit, currentValCoord);
            startActivity(intent);
        }
    }

    public void onQuitDialogNegativeClick(DialogFragment dialog) {

    }
}