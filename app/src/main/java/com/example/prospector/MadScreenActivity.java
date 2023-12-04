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

import org.w3c.dom.Text;

public class MadScreenActivity extends AppCompatActivity implements DialogFragmentManualInputMadSearch.DialogListener, QuitDialog.QuitDialogListener {

    final static String order = "Order";
    final static String search = "Search";
    final static String index = "Index";
    final static String comment = "Comment";
    final static String latitude = "Latitude";
    final static String longitude = "Longitude";

    TextView orderTextView;
    TextView searchTextView;
    TextView madTextView;
    TextView indexTextView;
    TextView commentTextView;
    TextView latTextView;
    TextView lonTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            Intent intent = new Intent(MadScreenActivity.this, TopLevelActivity.class);
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

        Button btnEnter = (Button) findViewById(R.id.btnEnter);
        btnEnter.setText("Готово");

        orderTextView = (TextView) findViewById(R.id.ord_item_screen_appoint);
        searchTextView = (TextView) findViewById(R.id.search_item_screen_appoint);
        madTextView = (TextView) findViewById(R.id.mad_item_screen_appoint);
        indexTextView = (TextView) findViewById(R.id.index_item_screen_appoint);
        commentTextView = (TextView) findViewById(R.id.comment_item_screen_appoint);
        latTextView = (TextView) findViewById(R.id.valueLat);
        lonTextView = (TextView) findViewById(R.id.valueLon);

        TextView madScreenHeader = (TextView) findViewById(R.id.screenHeader);
        madScreenHeader.setText("Окно МАД");

        Bundle arguments = getIntent().getExtras();
        if (arguments != null) {
            String order = arguments.getString(MadScreenActivity.order);
            String search = arguments.getString(MadScreenActivity.search);
            String index = arguments.getString(MadScreenActivity.index);
            String comment = arguments.getString(MadScreenActivity.comment);
            String latitude = arguments.getString(MadScreenActivity.latitude);
            String longitude = arguments.getString(MadScreenActivity.longitude);

            orderTextView.setText(order);
            searchTextView.setText(search);
            indexTextView.setText(index);
            commentTextView.setText(comment);
            latTextView.setText(latitude);
            lonTextView.setText(longitude);
        }
    }

    public void onClickNumBtn(View view) {
        Button btn = (Button) view;
        String valueBtn = btn.getText().toString();
        madTextView.setText(valueBtn);
    }

    public void onClickManualInputBtn(View view) {
        DialogFragment dialog = new DialogFragmentManualInputMadSearch();
        dialog.show(getSupportFragmentManager(), "custom");
    }

    public void onDialogPositiveClick(DialogFragment dialog) {
        Dialog dialogView = dialog.getDialog();
        EditText manualInput = (EditText) dialogView.findViewById(R.id.editTextNumberDecimal);
        String text = manualInput.getText().toString();
        madTextView.setText(text);
    }

    public void onDialogNegativeClick(DialogFragment dialog) {

    }

    public void onClickCancelBtn(View view) {
        QuitDialog quitDialog = new QuitDialog("Отменить точку?");
        quitDialog.show(getSupportFragmentManager(), "quit");
    }

    public void onQuitDialogPositiveClick(DialogFragment dialog) {
        Intent intent = new Intent(MadScreenActivity.this, JournalActivity.class);
        intent.putExtra(JournalActivity.file, "");
        intent.putExtra(JournalActivity.cancel, true);
        startActivity(intent);
    }

    public void onQuitDialogNegativeClick(DialogFragment dialog) {

    }

    public void onClickEnterBtn(View view) {
        if (madTextView.getText().toString().equals("")) {
            SimpleDialog simpleDialog = new SimpleDialog("Введите значение МАД");
            simpleDialog.show(getSupportFragmentManager(), "simple");
        } else {
            Intent intent = new Intent(MadScreenActivity.this, JournalActivity.class);
            intent.putExtra(JournalActivity.cancel, false);
            intent.putExtra(JournalActivity.file, "");
            intent.putExtra(JournalActivity.order, orderTextView.getText().toString());
            intent.putExtra(JournalActivity.search, searchTextView.getText().toString());
            intent.putExtra(JournalActivity.mad, madTextView.getText().toString());
            intent.putExtra(JournalActivity.index, indexTextView.getText().toString());
            intent.putExtra(JournalActivity.comment, commentTextView.getText().toString());
            intent.putExtra(JournalActivity.latitude, latTextView.getText().toString());
            intent.putExtra(JournalActivity.longitude, lonTextView.getText().toString());

            startActivity(intent);
        }
    }
}