package com.example.prospector.screenEditActivities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.preference.PreferenceManager;

import com.example.prospector.CommentScreenActivity;
import com.example.prospector.Point;
import com.example.prospector.PointInfoToChange;
import com.example.prospector.R;
import com.example.prospector.TopLevelActivity;
import com.example.prospector.dialogs.DialogFragmentManualInputMadSearch;
import com.example.prospector.dialogs.QuitDialog;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class SearchScreenEditActivity extends AppCompatActivity implements DialogFragmentManualInputMadSearch.DialogListener,
        QuitDialog.QuitDialogListener {

    public final static String order = "Order";
    public final static String search = "Search";
    public final static String mad = "Mad";
    public final static String index = "Index";
    public final static String comment = "Comment";
    public final static String latitude = "Latitude";
    public final static String longitude = "Longitude";

    TextView orderTextView, searchTextView, madTextView, indexTextView, commentTextView, latTextView, lonTextView;

    String searchForChecking; // переменная объявляется как поле класса, так как в дальнейшем будет использоваться
                              // для проверки изменения значения виджета TextView searchTextView


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            Intent intent = new Intent(SearchScreenEditActivity.this, TopLevelActivity.class);
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

        TextView searchScreenHeader = findViewById(R.id.screenHeader);
        searchScreenHeader.setText("Окно поиска");

        Button btnOk = (Button) findViewById(R.id.btnEnter);
        btnOk.setText("Ok");

        orderTextView = (TextView) findViewById(R.id.ord_item_screen_appoint);
        searchTextView = (TextView) findViewById(R.id.search_item_screen_appoint);
        madTextView = (TextView) findViewById(R.id.mad_item_screen_appoint);
        indexTextView = (TextView) findViewById(R.id.index_item_screen_appoint);
        commentTextView = (TextView) findViewById(R.id.comment_item_screen_appoint);
        latTextView = (TextView) findViewById(R.id.valueLat);
        lonTextView = (TextView) findViewById(R.id.valueLon);


        Bundle arguments = getIntent().getExtras();

        if (arguments != null) {
            String order = arguments.getString(SearchScreenEditActivity.order);
            searchForChecking = arguments.getString(SearchScreenEditActivity.search);
            String mad = arguments.getString(SearchScreenEditActivity.mad);
            String index = arguments.getString(SearchScreenEditActivity.index);
            String comment = arguments.getString(SearchScreenEditActivity.comment);
            String latitude = arguments.getString(SearchScreenEditActivity.latitude);
            String longitude = arguments.getString(SearchScreenEditActivity.longitude);

            orderTextView.setText(order);
            searchTextView.setText(searchForChecking);
            madTextView.setText(mad);
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

    public void onClickCancelBtn(View view) {
        Intent intent = new Intent(SearchScreenEditActivity.this, PointInfoToChange.class);
        intent.putExtra(PointInfoToChange.ord, orderTextView.getText().toString());
        intent.putExtra(PointInfoToChange.searchEdit, searchForChecking);
        intent.putExtra(PointInfoToChange.madEdit, madTextView.getText().toString());
        intent.putExtra(PointInfoToChange.indexEdit, indexTextView.getText().toString());
        intent.putExtra(PointInfoToChange.commentEdit, commentTextView.getText().toString());
        intent.putExtra(PointInfoToChange.latEdit, latTextView.getText().toString());
        intent.putExtra(PointInfoToChange.lonEdit, lonTextView.getText().toString());
        startActivity(intent);
    }

    public void onClickEnterBtn(View view) {
        if (searchForChecking.equals(searchTextView.getText().toString())) {
            Intent intent = new Intent(SearchScreenEditActivity.this, PointInfoToChange.class);
            intent.putExtra(PointInfoToChange.ord, orderTextView.getText().toString());
            intent.putExtra(PointInfoToChange.searchEdit, searchTextView.getText().toString());
            intent.putExtra(PointInfoToChange.madEdit, madTextView.getText().toString());
            intent.putExtra(PointInfoToChange.indexEdit, indexTextView.getText().toString());
            intent.putExtra(PointInfoToChange.commentEdit, commentTextView.getText().toString());
            intent.putExtra(PointInfoToChange.latEdit, latTextView.getText().toString());
            intent.putExtra(PointInfoToChange.lonEdit, lonTextView.getText().toString());
            startActivity(intent);
        } else {
            QuitDialog dialog = new QuitDialog("Редактировать значение ПОИСКА?");
            dialog.show(getSupportFragmentManager(), "quit");
        }
    }

    public void onQuitDialogPositiveClick(DialogFragment dialog) {
        Intent intent = new Intent(SearchScreenEditActivity.this, PointInfoToChange.class);
        intent.putExtra(PointInfoToChange.ord, orderTextView.getText().toString());
        intent.putExtra(PointInfoToChange.searchEdit, searchTextView.getText().toString());
        intent.putExtra(PointInfoToChange.madEdit, madTextView.getText().toString());
        intent.putExtra(PointInfoToChange.indexEdit, indexTextView.getText().toString());
        intent.putExtra(PointInfoToChange.commentEdit, commentTextView.getText().toString());
        intent.putExtra(PointInfoToChange.latEdit, latTextView.getText().toString());
        intent.putExtra(PointInfoToChange.lonEdit, lonTextView.getText().toString());
        startActivity(intent);
    }

    public void onQuitDialogNegativeClick(DialogFragment dialog) {

    }


}