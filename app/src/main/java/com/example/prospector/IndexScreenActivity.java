package com.example.prospector;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.preference.PreferenceManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.prospector.dialogs.DialogIndexInfo;
import com.example.prospector.dialogs.QuitDialog;
import com.example.prospector.dialogs.SimpleDialog;

public class IndexScreenActivity extends AppCompatActivity implements QuitDialog.QuitDialogListener {

    final static String order = "Order";
    final static String latitude = "Latitude";
    final static String longitude = "Longitude";

    TextView orderTextView;
    TextView indexTextView;
    TextView latTextView;
    TextView lonTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            Intent intent = new Intent(IndexScreenActivity.this, TopLevelActivity.class);
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

        orderTextView = (TextView) findViewById(R.id.ord_item_screen_appoint);
        indexTextView = (TextView) findViewById(R.id.index_item_screen_appoint);
        latTextView = (TextView) findViewById(R.id.valueLat);
        lonTextView = (TextView) findViewById(R.id.valueLon);

        TextView indexScreenHeader = (TextView) findViewById(R.id.screenHeader);
        indexScreenHeader.setText("Индекс грунта");

        Bundle arguments = getIntent().getExtras();

        if (arguments != null) {
            String order = arguments.getString(IndexScreenActivity.order);
            String latitude = arguments.getString(IndexScreenActivity.latitude);
            String longitude = arguments.getString(IndexScreenActivity.longitude);

            orderTextView.setText(order);
            latTextView.setText(latitude);
            lonTextView.setText(longitude);
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

    public void onClickEnterBtn(View view) {
        // Переводит на следующий экран (Комментарии)
        if (indexTextView.getText().toString().equals("")) {
            SimpleDialog simpleDialog = new SimpleDialog("Введите индекс грунта");
            simpleDialog.show(getSupportFragmentManager(), "simple");
        } else {
            Intent intent = new Intent(IndexScreenActivity.this, CommentScreenActivity.class);
            intent.putExtra(CommentScreenActivity.order, orderTextView.getText().toString());
            intent.putExtra(CommentScreenActivity.index, indexTextView.getText().toString());
            intent.putExtra(CommentScreenActivity.latitude, latTextView.getText().toString());
            intent.putExtra(CommentScreenActivity.longitude, lonTextView.getText().toString());
            startActivity(intent);
        }
    }

    public void onClickCancelBtn(View view) {
        // Срабатывает при нажатии на клавишу "Отмена". Открывается диалог с подтверждением отмены.
        QuitDialog dialog = new QuitDialog("Отменить точку");
        dialog.show(getSupportFragmentManager(), "quit");
    }

    public void onQuitDialogPositiveClick(DialogFragment dialog) {
        // Срабатывает при нажатии на клавишу "Ок" диалога  отмены. Удаляет радактируемую точку и
        // Переносит в JournalActivity
        Intent intent = new Intent(IndexScreenActivity.this, JournalActivity.class);
        intent.putExtra(JournalActivity.file, "");
        intent.putExtra(JournalActivity.cancel, true);
        startActivity(intent);
    }

    public void onQuitDialogNegativeClick(DialogFragment dialog) {

    }

    @Override
    public void onBackPressed() {
        QuitDialog quitDialog = new QuitDialog("Отменить точку?");
        quitDialog.show(getSupportFragmentManager(), "quit");
    }

}