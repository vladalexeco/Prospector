package com.example.prospector;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.preference.EditTextPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreferenceCompat;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Toast;

import com.example.prospector.dialogs.DialogForEditTheme;
import com.example.prospector.dialogs.QuitDialog;
import com.example.prospector.dialogs.SimpleDialog;

public class SettingsFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);

        EditTextPreference relativeErrorPreference = findPreference("relative_error");
        SwitchPreferenceCompat changeThemePreference = findPreference("change_theme");
        EditTextPreference savePointsPreference = findPreference("save_points");


        if (relativeErrorPreference != null) {
            relativeErrorPreference.setSummaryProvider(new Preference.SummaryProvider<EditTextPreference>() {
                @Override
                public CharSequence provideSummary(EditTextPreference preference) {
                    String text = preference.getText();
                    if (TextUtils.isEmpty(text)) {
                        return "Значение по умолчанию 20 %";
                    }

                    return "Значение: " + text + " %";
                }
            });

            relativeErrorPreference.setOnBindEditTextListener(new EditTextPreference.OnBindEditTextListener() {
                @Override
                public void onBindEditText(@NonNull EditText editText) {
                    editText.setInputType(InputType.TYPE_CLASS_NUMBER);
                }
            });
        }

        if (changeThemePreference != null) {
            changeThemePreference.setSummaryProvider(new Preference.SummaryProvider<SwitchPreferenceCompat>() {
                @Override
                public CharSequence provideSummary(SwitchPreferenceCompat preference) {
                    boolean check = preference.isChecked();
                    if (check) {
                        return "Светлая тема";
                    } else {
                        return "Темная тема";
                    }
                }
            });
        }

        if (savePointsPreference != null) {
            savePointsPreference.setSummaryProvider(new Preference.SummaryProvider<EditTextPreference>() {
                @Override
                public CharSequence provideSummary(EditTextPreference preference) {
                    String text = preference.getText();
                    if (TextUtils.isEmpty(text)) {
                        return "Сохранение после 5 точек";
                    } else {
                        return "Сохранение после " + text + " точек";
                    }
                }
            });
        }

        savePointsPreference.setOnBindEditTextListener(new EditTextPreference.OnBindEditTextListener() {
            @Override
            public void onBindEditText(@NonNull EditText editText) {
                editText.setInputType(InputType.TYPE_CLASS_NUMBER);
            }
        });

        Preference.OnPreferenceChangeListener onPreferenceChangeListener= new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                SimpleDialog simpleDialog = new SimpleDialog("Тема будет изменена");
                simpleDialog.show(getFragmentManager(), "simple");
                return true;
            }
        };

        changeThemePreference.setOnPreferenceChangeListener(onPreferenceChangeListener);
    }



}