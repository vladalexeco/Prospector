package com.example.prospector.screenEditActivities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.preference.PreferenceManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.prospector.CommentScreenActivity;
import com.example.prospector.PointInfoToChange;
import com.example.prospector.R;
import com.example.prospector.TopLevelActivity;
import com.example.prospector.dialogs.QuitDialog;
import com.example.prospector.dialogs.SimpleDialog;

public class LatitudeScreenEditActivity extends AppCompatActivity implements QuitDialog.QuitDialogListener {

    public final static String order = "Order";
    public final static String search = "Search";
    public final static String mad = "Mad";
    public final static String index = "Index";
    public final static String comment = "Comment";
    public final static String latitude = "Latitude";
    public final static String longitude = "Longitude";

    EditText degreeEdit, minuteEdit, secondEdit;

    TextView degreeSign, minuteSign, secondSign;

    String orderScreen, searchScreen, madScreen, indexScreen, commentScreen, lonScreen;

    String latForChecking;

    String degreeSymbol = "\u00BA";
    String minuteSymbol = "'";
    String secondSymbol = "\"";

    String flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            Intent intent = new Intent(LatitudeScreenEditActivity.this, TopLevelActivity.class);
            startActivity(intent);
        }

        // Работа с темой
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean valueOfThemePreference = sharedPreferences.getBoolean("change_theme", false);

        if (valueOfThemePreference) {
            setTheme(R.style.Theme_AppCompat_Light);
        } else {
            setTheme(R.style.Theme_AppCompat);
        }
        //

        setContentView(R.layout.activity_latitude_screen);

        degreeEdit = (EditText) findViewById(R.id.degree);
        minuteEdit = (EditText) findViewById(R.id.minute);
        secondEdit = (EditText) findViewById(R.id.second);

        degreeSign = (TextView) findViewById(R.id.degreeSign);
        minuteSign = (TextView) findViewById(R.id.minuteSign);
        secondSign = (TextView) findViewById(R.id.secondSign);

        degreeSign.setText(degreeSymbol);
        minuteSign.setText(minuteSymbol);
        secondSign.setText(secondSymbol);

        TextView screenHeader = (TextView) findViewById(R.id.screenHeaderForLat);
        screenHeader.setText("Северная широта");

        Bundle arguments = getIntent().getExtras();

        if (arguments != null) {
            latForChecking = arguments.getString(LatitudeScreenEditActivity.latitude);
            if (!latForChecking.equals("no data")) {
                String[] firstSplit = latForChecking.split(degreeSymbol);
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
    protected void onStart() {
        super.onStart();
        Bundle arguments = getIntent().getExtras();
        if (arguments != null) {
            orderScreen = arguments.getString(LatitudeScreenEditActivity.order);
            searchScreen = arguments.getString(LatitudeScreenEditActivity.search);
            madScreen = arguments.getString(LatitudeScreenEditActivity.mad);
            indexScreen = arguments.getString(LatitudeScreenEditActivity.index);
            commentScreen = arguments.getString(LatitudeScreenEditActivity.comment);
            lonScreen = arguments.getString(LatitudeScreenEditActivity.longitude);
        }
    }

    public void onClickOkBtn(View view) {
        String currentDegree = degreeEdit.getText().toString();
        String currentMinute = minuteEdit.getText().toString();
        String currentSecond = secondEdit.getText().toString();

        String currentValCoord = currentDegree + degreeSymbol + currentMinute + minuteSymbol + currentSecond + secondSymbol;

        if (latForChecking.equals(currentValCoord)) {
            Intent intent = new Intent(LatitudeScreenEditActivity.this, PointInfoToChange.class);
            intent.putExtra(PointInfoToChange.ord, orderScreen);
            intent.putExtra(PointInfoToChange.searchEdit, searchScreen);
            intent.putExtra(PointInfoToChange.madEdit, madScreen);
            intent.putExtra(PointInfoToChange.indexEdit, indexScreen);
            intent.putExtra(PointInfoToChange.commentEdit, commentScreen);
            intent.putExtra(PointInfoToChange.latEdit, currentValCoord);
            intent.putExtra(PointInfoToChange.lonEdit, lonScreen);
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

    public void onClickCancelBtn(View view) {
        Intent intent = new Intent(LatitudeScreenEditActivity.this, PointInfoToChange.class);
        intent.putExtra(PointInfoToChange.ord, orderScreen);
        intent.putExtra(PointInfoToChange.searchEdit, searchScreen);
        intent.putExtra(PointInfoToChange.madEdit, madScreen);
        intent.putExtra(PointInfoToChange.indexEdit, indexScreen);
        intent.putExtra(PointInfoToChange.commentEdit, commentScreen);
        intent.putExtra(PointInfoToChange.latEdit, latForChecking);
        intent.putExtra(PointInfoToChange.lonEdit, lonScreen);
        startActivity(intent);
    }

    public void onQuitDialogPositiveClick(DialogFragment dialog) {
        Intent intent = new Intent(LatitudeScreenEditActivity.this, PointInfoToChange.class);
        if (flag.equals("no data")) {
            intent.putExtra(PointInfoToChange.ord, orderScreen);
            intent.putExtra(PointInfoToChange.searchEdit, searchScreen);
            intent.putExtra(PointInfoToChange.madEdit, madScreen);
            intent.putExtra(PointInfoToChange.indexEdit, indexScreen);
            intent.putExtra(PointInfoToChange.commentEdit, commentScreen);
            intent.putExtra(PointInfoToChange.latEdit, "no data");
            intent.putExtra(PointInfoToChange.lonEdit, lonScreen);
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
            intent.putExtra(PointInfoToChange.latEdit, currentValCoord);
            intent.putExtra(PointInfoToChange.lonEdit, lonScreen);
            startActivity(intent);
        }
    }

    public void onQuitDialogNegativeClick(DialogFragment dialog) {

    }
}