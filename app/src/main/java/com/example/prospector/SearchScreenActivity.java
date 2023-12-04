package com.example.prospector;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.preference.PreferenceManager;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.prospector.dialogs.DialogFragmentManualInputMadSearch;
import com.example.prospector.dialogs.QuitDialog;
import com.example.prospector.dialogs.SimpleDialog;

public class SearchScreenActivity extends AppCompatActivity implements DialogFragmentManualInputMadSearch.DialogListener, QuitDialog.QuitDialogListener {

    final static String order = "Order";
    final static String index = "Index";
    final static String comment = "Comment";
    final static String latitude = "Latitude";
    final static String longitude = "Longitude";

    TextView searchTextView;
    TextView orderTextView;
    TextView indexTextView;
    TextView commentTextView;
    TextView latTextView;
    TextView lonTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            Intent intent = new Intent(SearchScreenActivity.this, TopLevelActivity.class);
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

        setContentView(R.layout.activity_search_mad_screen);

        orderTextView = findViewById(R.id.ord_item_screen_appoint);
        searchTextView = (TextView) findViewById(R.id.search_item_screen_appoint);
        indexTextView = (TextView) findViewById(R.id.index_item_screen_appoint);
        commentTextView = (TextView) findViewById(R.id.comment_item_screen_appoint);
        latTextView = (TextView) findViewById(R.id.valueLat);
        lonTextView = (TextView) findViewById(R.id.valueLon);

        TextView searchScreenHeader = findViewById(R.id.screenHeader);
        searchScreenHeader.setText("Окно поиска");

        Bundle arguments = getIntent().getExtras();
        if (arguments != null) {
            String order = arguments.getString(SearchScreenActivity.order);
            String index = arguments.getString(SearchScreenActivity.index);
            String comment = arguments.getString(SearchScreenActivity.comment);
            String latitude = arguments.getString(SearchScreenActivity.latitude);
            String longitude = arguments.getString(SearchScreenActivity.longitude);

            orderTextView.setText(order);
            indexTextView.setText(index);
            commentTextView.setText(comment);
            latTextView.setText(latitude);
            lonTextView.setText(longitude);
        }
    }

    public void onClickNumBtn(View view) {
        Button btn = (Button) view;
        String valueBtn = btn.getText().toString();
        searchTextView.setText(valueBtn);
    }

    public void onClickManualInputBtn(View view) {
        DialogFragment dialog = new DialogFragmentManualInputMadSearch();
        dialog.show(getSupportFragmentManager(), "custom");
    }

    public void onDialogPositiveClick(DialogFragment dialog) {
        Dialog dialogView = dialog.getDialog();
        EditText manualInput = (EditText) dialogView.findViewById(R.id.editTextNumberDecimal);
        String text = manualInput.getText().toString();
        searchTextView.setText(text);
    }

    public void onDialogNegativeClick(DialogFragment dialog) {

    }

    public void onQuitDialogPositiveClick(DialogFragment dialog) {
        Intent intent = new Intent(SearchScreenActivity.this, JournalActivity.class);
        intent.putExtra(JournalActivity.file, "");
        intent.putExtra(JournalActivity.cancel, true);
        startActivity(intent);
    }

    public void onQuitDialogNegativeClick(DialogFragment dialog) {

    }

    public void onClickCancelBtn(View view) {
        QuitDialog quitDialog = new QuitDialog("Отменить точку?");
        quitDialog.show(getSupportFragmentManager(), "quit");
    }

    public void onClickEnterBtn(View view) {
        if (searchTextView.getText().toString().equals("")) {
            SimpleDialog simpleDialog = new SimpleDialog("Введите значение ПОИСКА");
            simpleDialog.show(getSupportFragmentManager(), "simple");
        } else {
            Intent intent = new Intent(SearchScreenActivity.this, MadScreenActivity.class);
            intent.putExtra(MadScreenActivity.order, orderTextView.getText().toString());
            intent.putExtra(MadScreenActivity.search, searchTextView.getText().toString());
            intent.putExtra(MadScreenActivity.index, indexTextView.getText().toString());
            intent.putExtra(MadScreenActivity.comment, commentTextView.getText().toString());
            intent.putExtra(MadScreenActivity.latitude, latTextView.getText().toString());
            intent.putExtra(MadScreenActivity.longitude, lonTextView.getText().toString());
            startActivity(intent);
        }
    }


}