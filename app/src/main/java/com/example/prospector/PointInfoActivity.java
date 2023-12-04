package com.example.prospector;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import org.w3c.dom.Text;

public class PointInfoActivity extends AppCompatActivity {

    static final String serialOfPoint = "Serial";

    TextView numOfPoint, searchOfPoint, madOfPoint, groundOfPoint, commentOfPoint,
    latOfPoint, lonOfPoint, dateOfPoint, timeOfPoint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            Intent intent = new Intent(PointInfoActivity.this, TopLevelActivity.class);
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

        setContentView(R.layout.activity_point_info);

        numOfPoint = (TextView) findViewById(R.id.numOfPoint);
        searchOfPoint = (TextView) findViewById(R.id.searchOfPoint);
        madOfPoint = (TextView) findViewById(R.id.madOfPoint);
        groundOfPoint = (TextView) findViewById(R.id.groundOfPoint);
        commentOfPoint = (TextView) findViewById(R.id.commentOfPoint);
        latOfPoint = (TextView) findViewById(R.id.latOfPoint);
        lonOfPoint = (TextView) findViewById(R.id.lonOfPoint);
        dateOfPoint = (TextView) findViewById(R.id.textViewDate);
        timeOfPoint = (TextView) findViewById(R.id.textViewTime);

        Bundle arguments = getIntent().getExtras();

        if (arguments != null) {
            int serial = arguments.getInt(serialOfPoint);
            Point point = JournalActivity.points.get(serial);
            numOfPoint.setText(point.getOrd());
            searchOfPoint.setText(point.getSearch());
            madOfPoint.setText(point.getMad());
            groundOfPoint.setText(point.getIndex());
            commentOfPoint.setText(point.getComment());
            latOfPoint.setText(point.getLatitude());
            lonOfPoint.setText(point.getLongitude());
            String date = point.getDate();
            String time = point.getTime();
            dateOfPoint.setText(date);
            timeOfPoint.setText(time);
        }
    }
}