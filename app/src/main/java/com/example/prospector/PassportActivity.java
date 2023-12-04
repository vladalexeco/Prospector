package com.example.prospector;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import com.example.prospector.dialogs.SimpleDialog;

public class PassportActivity extends AppCompatActivity {

    EditText editName, editObject, editNumberOfObject, editDate, editTemperature, editPressure,
    editHumidity, editWind;

    CheckBox checkBox;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            Intent intent = new Intent(PassportActivity.this, TopLevelActivity.class);
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

        setContentView(R.layout.activity_passport);

        editName = (EditText) findViewById(R.id.editName);
        editObject = (EditText) findViewById(R.id.editObject);
        editNumberOfObject = (EditText) findViewById(R.id.editNumberOfObject);
        editDate = (EditText) findViewById(R.id.editDate);
        editTemperature = (EditText) findViewById(R.id.editTemperature);
        editPressure = (EditText) findViewById(R.id.editPressure);
        editHumidity = (EditText) findViewById(R.id.editHumidity);
        editWind = (EditText) findViewById(R.id.editWind);

        checkBox = (CheckBox) findViewById(R.id.checkBoxClimate);

        if (!checkBox.isChecked()) {
            climateSettings(false);
        }

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    climateSettings(true);
                } else {
                    climateSettings(false);
                }
            }
        });
    }

    private void climateSettings(boolean b) {
        editTemperature.setEnabled(b);
        editPressure.setEnabled(b);
        editHumidity.setEnabled(b);
        editWind.setEnabled(b);
    }

    private boolean checkEmpty(EditText editText) {
        return editText.getText().toString().equals("");
    }

    public void onClickOkBtn(View view) {
        if (checkBox.isChecked()) {
            if (checkEmpty(editName) || checkEmpty(editObject) || checkEmpty(editNumberOfObject) ||
                    checkEmpty(editDate) || checkEmpty(editTemperature) || checkEmpty(editPressure) ||
                    checkEmpty(editHumidity) || checkEmpty(editWind)) {
                SimpleDialog simpleDialog = new SimpleDialog("Заполните все поля");
                simpleDialog.show(getSupportFragmentManager(), "simple");
            } else {
                Intent intent = new Intent(PassportActivity.this, JournalActivity.class);
                intent.putExtra(JournalActivity.file, "New file");
                intent.putExtra(JournalActivity.climateSettings, true);
                intent.putExtra(JournalActivity.name, editName.getText().toString());
                intent.putExtra(JournalActivity.object, editObject.getText().toString());
                intent.putExtra(JournalActivity.numberOfObject, editNumberOfObject.getText().toString());
                intent.putExtra(JournalActivity.date, editDate.getText().toString());
                intent.putExtra(JournalActivity.temperature,editTemperature.getText().toString());
                intent.putExtra(JournalActivity.pressure, editPressure.getText().toString());
                intent.putExtra(JournalActivity.humidity, editHumidity.getText().toString());
                intent.putExtra(JournalActivity.wind, editWind.getText().toString());
                startActivity(intent);
            }
        } else {
            if (checkEmpty(editName) || checkEmpty(editObject) || checkEmpty(editNumberOfObject) ||
        checkEmpty(editDate)) {
                SimpleDialog simpleDialog = new SimpleDialog("Заполните все поля");
                simpleDialog.show(getSupportFragmentManager(), "simple");
            } else {
                Intent intent = new Intent(PassportActivity.this, JournalActivity.class);
                intent.putExtra(JournalActivity.file, "New file");
                intent.putExtra(JournalActivity.climateSettings, false);
                intent.putExtra(JournalActivity.name, editName.getText().toString());
                intent.putExtra(JournalActivity.object, editObject.getText().toString());
                intent.putExtra(JournalActivity.numberOfObject, editNumberOfObject.getText().toString());
                intent.putExtra(JournalActivity.date, editDate.getText().toString());
                startActivity(intent);
            }
        }
    }

    public void onClickCancelBtn(View view) {
        Intent intent = new Intent(PassportActivity.this, TopLevelActivity.class);
        startActivity(intent);
    }
}