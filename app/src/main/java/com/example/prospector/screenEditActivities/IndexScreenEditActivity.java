package com.example.prospector.screenEditActivities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.preference.PreferenceManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.prospector.CommentScreenActivity;
import com.example.prospector.IndexScreenActivity;
import com.example.prospector.PointInfoToChange;
import com.example.prospector.R;
import com.example.prospector.TopLevelActivity;
import com.example.prospector.dialogs.DialogIndexInfo;
import com.example.prospector.dialogs.QuitDialog;
import com.example.prospector.dialogs.SimpleDialog;

import org.w3c.dom.Text;

public class IndexScreenEditActivity extends AppCompatActivity implements QuitDialog.QuitDialogListener  {

    public final static String order = "Order";
    public final static String search = "Search";
    public final static String mad = "Mad";
    public final static String index = "Index";
    public final static String comment = "Comment";
    public final static String latitude = "Latitude";
    public final static String longitude = "Longitude";

    TextView orderTextView, searchTextView, madTextView, indexTextView, commentTextView, latTextView, lonTextView;

    String indexForChecking; // переменная объявляется как поле класса, так как в дальнейшем будет использоваться
                            // для проверки изменения значения виджета TextView madTextView

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            Intent intent = new Intent(IndexScreenEditActivity.this, TopLevelActivity.class);
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

        setContentView(R.layout.activity_index_screen);

        TextView searchScreenHeader = findViewById(R.id.screenHeader);
        searchScreenHeader.setText("Окно Индекса");

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
            String order = arguments.getString(MadScreenEditActivity.order);
            String search = arguments.getString(MadScreenEditActivity.search);
            String mad = arguments.getString(MadScreenEditActivity.mad);
            indexForChecking = arguments.getString(MadScreenEditActivity.index);
            String comment = arguments.getString(MadScreenEditActivity.comment);
            String lat = arguments.getString(MadScreenEditActivity.latitude);
            String lon = arguments.getString(MadScreenEditActivity.longitude);

            orderTextView.setText(order);
            searchTextView.setText(search);
            madTextView.setText(mad);
            indexTextView.setText(indexForChecking);
            commentTextView.setText(comment);
            latTextView.setText(lat);
            lonTextView.setText(lon);
        }
    }

    public void onClickNumBtn(View view) {
        // Срабатывает при нажатии на цифровую клавишу
        // При повторном нажатии добавляет индекс через запятую
        Button btn = (Button) view;
        String text = btn.getText().toString();
        if (indexTextView.getText().toString().equals("")) {
            indexTextView.setText(text);
        } else {
            String temp = indexTextView.getText().toString();
            temp = temp + "," + text;
            indexTextView.setText(temp);
        }
    }

    public void onClickInfoBtn(View view) {
        // Выводит диалоговое окно информации о грунтах
        DialogFragment dialog = new DialogIndexInfo();
        dialog.show(getSupportFragmentManager(), "info");
    }

    public void onClickBackBtn(View view) {
        // Удаляет последний индекс
        String index = indexTextView.getText().toString();
        if (!index.equals("")) {
            if (index.length() == 1) {
                indexTextView.setText("");
            } else {
                String newIndex = index.substring(0, index.length() - 2);
                indexTextView.setText(newIndex);
            }
        }
    }

    public void onClickClearBtn(View view) {
        // Очищает все поле индекса
        indexTextView.setText("");
    }

    public void onClickCancelBtn(View view) {
        Intent intent = new Intent(IndexScreenEditActivity.this, PointInfoToChange.class);
        intent.putExtra(PointInfoToChange.ord, orderTextView.getText().toString());
        intent.putExtra(PointInfoToChange.searchEdit, searchTextView.getText().toString());
        intent.putExtra(PointInfoToChange.madEdit, madTextView.getText().toString());
        intent.putExtra(PointInfoToChange.indexEdit, indexForChecking);
        intent.putExtra(PointInfoToChange.commentEdit, commentTextView.getText().toString());
        intent.putExtra(PointInfoToChange.latEdit, latTextView.getText().toString());
        intent.putExtra(PointInfoToChange.lonEdit, lonTextView.getText().toString());
        startActivity(intent);
    }

    public void onClickEnterBtn(View view) {
        if (indexTextView.getText().toString().equals("")) {
            SimpleDialog dialog = new SimpleDialog("Пустое поле");
            dialog.show(getSupportFragmentManager(), "simple");
        } else {
            if (indexForChecking.equals(indexTextView.getText().toString())) {
                Intent intent = new Intent(IndexScreenEditActivity.this, PointInfoToChange.class);
                intent.putExtra(PointInfoToChange.ord, orderTextView.getText().toString());
                intent.putExtra(PointInfoToChange.searchEdit, searchTextView.getText().toString());
                intent.putExtra(PointInfoToChange.madEdit, madTextView.getText().toString());
                intent.putExtra(PointInfoToChange.indexEdit, indexTextView.getText().toString());
                intent.putExtra(PointInfoToChange.commentEdit, commentTextView.getText().toString());
                intent.putExtra(PointInfoToChange.latEdit, latTextView.getText().toString());
                intent.putExtra(PointInfoToChange.lonEdit, lonTextView.getText().toString());
                startActivity(intent);
            } else {
                QuitDialog dialog = new QuitDialog("Редактировать значение Индекса Грунта?");
                dialog.show(getSupportFragmentManager(), "quit");
            }
        }
    }

    public void onQuitDialogPositiveClick(DialogFragment dialogFragment) {
        Intent intent = new Intent(IndexScreenEditActivity.this, PointInfoToChange.class);
        intent.putExtra(PointInfoToChange.ord, orderTextView.getText().toString());
        intent.putExtra(PointInfoToChange.searchEdit, searchTextView.getText().toString());
        intent.putExtra(PointInfoToChange.madEdit, madTextView.getText().toString());
        intent.putExtra(PointInfoToChange.indexEdit, indexTextView.getText().toString());
        intent.putExtra(PointInfoToChange.commentEdit, commentTextView.getText().toString());
        intent.putExtra(PointInfoToChange.latEdit, latTextView.getText().toString());
        intent.putExtra(PointInfoToChange.lonEdit, lonTextView.getText().toString());
        startActivity(intent);
    }

    public void onQuitDialogNegativeClick(DialogFragment dialogFragment) {

    }
}