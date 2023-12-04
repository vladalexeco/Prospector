package com.example.prospector;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.LabeledIntent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class TopLevelActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Работа с темой
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean valueOfThemePreference = sharedPreferences.getBoolean("change_theme", false);

        if (valueOfThemePreference) {
            setTheme(R.style.Theme_AppCompat_Light);
        } else {
            setTheme(R.style.Theme_AppCompat);
        }
        //

        setContentView(R.layout.activity_top_level);
        ListView listView = (ListView) findViewById(R.id.top_level_list);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                R.layout.text_view_for_top_level_list,
                getResources().getStringArray(R.array.table_of_contents));
        listView.setAdapter(adapter);

        AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    Intent intent = new Intent(TopLevelActivity.this, PassportActivity.class);
                    startActivity(intent);
                } else if (position == 1) {
                    String[] fileList = TopLevelActivity.this.fileList();
                    if (fileList.length > 0) {
                        Intent intent = new Intent(TopLevelActivity.this, CatalogueOfFilesActivity.class);
                        intent.putExtra(CatalogueOfFilesActivity.listKey, fileList);
                        startActivity(intent);
                    } else {
                        Toast.makeText(TopLevelActivity.this, "Empty file directory", Toast.LENGTH_SHORT).show();
                    }
                } else if (position == 2) {
                    String fileName = getLastFileNameFromCache();

                    if (fileName.equals("")) {
                        Toast.makeText(TopLevelActivity.this, "Нет файла", Toast.LENGTH_SHORT).show();
                    } else {
                        Intent intent = new Intent(TopLevelActivity.this, JournalActivity.class);
                        intent.putExtra(JournalActivity.file , "Open file");
                        intent.putExtra(JournalActivity.fileName, fileName);
                        startActivity(intent);
                    }
                }

            }
        };
        listView.setOnItemClickListener(itemClickListener);
    }

    private String getLastFileNameFromCache() {
        FileInputStream fin = null;
        File file = new File(getCacheDir(), JournalActivity.lastOpenedFile);
        if (!file.exists()) {
            return "";
        }

        try {
            fin = new FileInputStream(file);
            byte[] bytes = new byte[fin.available()];
            fin.read(bytes);
            String text = new String(bytes);
            return text;
        } catch (IOException ex) {
            ex.getStackTrace();
            return "";
        }
    }

    @Override
    public void onBackPressed() {
        this.finishAffinity();
    }
}