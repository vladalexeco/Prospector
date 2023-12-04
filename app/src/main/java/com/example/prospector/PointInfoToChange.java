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

import com.example.prospector.dialogs.QuitDialog;
import com.example.prospector.screenEditActivities.LatitudeScreenEditActivity;
import com.example.prospector.screenEditActivities.CommentScreenEditActivity;
import com.example.prospector.screenEditActivities.IndexScreenEditActivity;
import com.example.prospector.screenEditActivities.LongitudeScreenEditActivity;
import com.example.prospector.screenEditActivities.MadScreenEditActivity;
import com.example.prospector.screenEditActivities.SearchScreenEditActivity;

import java.util.jar.JarOutputStream;

public class PointInfoToChange extends AppCompatActivity implements QuitDialog.QuitDialogListener {

    public final static String pointPosition = "pointPosition"; // для extras из JournalActivity
    public final static String fromJournal = "fromJournal";

    public final static String ord = "Ord";
    public final static String searchEdit = "SearchEdit"; // для extras из *ScreenEditActivity
    public final static String madEdit = "MadEdit";
    public final static String indexEdit = "IndexEdit";
    public final static String commentEdit = "CommentEdit";
    public final static String latEdit = "LatEdit";
    public final static String lonEdit = "LonEdit";

    int position;
    Boolean from_journal; // эта переменная равна true только когда предыдущая активность - JournalActivity
                            // В остальных случаях она равна false
    static Point point;

    TextView numOfPoint, searchOfPoint, madOfPoint, groundOfPoint, commentOfPoint,
            latOfPoint, lonOfPoint;

    Button editSearchBtn, editMadBtn, editGroundBtn, editCommentBtn, editLatBtn, editLonBtn;

    String editOrd, editSearch, editMad, editIndex, editComment, editLat, editLon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            Intent intent = new Intent(PointInfoToChange.this, TopLevelActivity.class);
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

        setContentView(R.layout.activity_point_info_to_change);

        numOfPoint = (TextView) findViewById(R.id.numOfPoint);
        searchOfPoint = (TextView) findViewById(R.id.searchOfPoint);
        madOfPoint = (TextView) findViewById(R.id.madOfPoint);
        groundOfPoint = (TextView) findViewById(R.id.groundOfPoint);
        commentOfPoint = (TextView) findViewById(R.id.commentOfPoint);
        latOfPoint = (TextView) findViewById(R.id.latOfPoint);
        lonOfPoint = (TextView) findViewById(R.id.lonOfPoint);

        editSearchBtn = (Button) findViewById(R.id.editSearch);
        editMadBtn = (Button) findViewById(R.id.editMad);
        editGroundBtn = (Button) findViewById(R.id.editGround);
        editCommentBtn = (Button) findViewById(R.id.editComm);
        editLatBtn = (Button) findViewById(R.id.editLat);
        editLonBtn = (Button) findViewById(R.id.editLon);

        TextView dateView = (TextView) findViewById(R.id.textViewDateDate);
        TextView timeView = (TextView) findViewById(R.id.textViewTimeTime);

        Bundle arguments = getIntent().getExtras();

        if (arguments != null) {
            position = arguments.getInt(pointPosition);
            from_journal = arguments.getBoolean(PointInfoToChange.fromJournal);
        }

        if (from_journal) {
            point = JournalActivity.points.get(position);

            numOfPoint.setText(point.getOrd());
            searchOfPoint.setText(point.getSearch());
            madOfPoint.setText(point.getMad());
            groundOfPoint.setText(point.getIndex());
            commentOfPoint.setText(point.getComment());
            latOfPoint.setText(point.getLatitude());
            lonOfPoint.setText(point.getLongitude());

            dateView.setText(point.getDate());
            timeView.setText(point.getTime());
        }




    }

    public void onClickEditSearchBtn(View view) {
        Intent intent = new Intent(PointInfoToChange.this, SearchScreenEditActivity.class);
        intent.putExtra(SearchScreenEditActivity.order, numOfPoint.getText().toString());
        intent.putExtra(SearchScreenEditActivity.search, searchOfPoint.getText().toString());
        intent.putExtra(SearchScreenEditActivity.mad, madOfPoint.getText().toString());
        intent.putExtra(SearchScreenEditActivity.index, groundOfPoint.getText().toString());
        intent.putExtra(SearchScreenEditActivity.comment, commentOfPoint.getText().toString());
        intent.putExtra(SearchScreenEditActivity.latitude, latOfPoint.getText().toString());
        intent.putExtra(SearchScreenEditActivity.longitude, lonOfPoint.getText().toString());
        startActivity(intent);
    }

    public void onClickEditMadBtn(View view) {
        Intent intent = new Intent(PointInfoToChange.this, MadScreenEditActivity.class);
        intent.putExtra(MadScreenEditActivity.order, numOfPoint.getText().toString());
        intent.putExtra(MadScreenEditActivity.search, searchOfPoint.getText().toString());
        intent.putExtra(MadScreenEditActivity.mad, madOfPoint.getText().toString());
        intent.putExtra(MadScreenEditActivity.index, groundOfPoint.getText().toString());
        intent.putExtra(MadScreenEditActivity.comment, commentOfPoint.getText().toString());
        intent.putExtra(MadScreenEditActivity.latitude, latOfPoint.getText().toString());
        intent.putExtra(MadScreenEditActivity.longitude, lonOfPoint.getText().toString());
        startActivity(intent);
    }

    public void onClickEditGroundBtn(View view) {
        Intent intent = new Intent(PointInfoToChange.this, IndexScreenEditActivity.class);
        intent.putExtra(IndexScreenEditActivity.order, numOfPoint.getText().toString());
        intent.putExtra(IndexScreenEditActivity.search, searchOfPoint.getText().toString());
        intent.putExtra(IndexScreenEditActivity.mad, madOfPoint.getText().toString());
        intent.putExtra(IndexScreenEditActivity.index, groundOfPoint.getText().toString());
        intent.putExtra(IndexScreenEditActivity.comment, commentOfPoint.getText().toString());
        intent.putExtra(IndexScreenEditActivity.latitude, latOfPoint.getText().toString());
        intent.putExtra(IndexScreenEditActivity.longitude, lonOfPoint.getText().toString());
        startActivity(intent);
    }

    public void onClickEditCommentBtn(View view) {
        Intent intent = new Intent(PointInfoToChange.this, CommentScreenEditActivity.class);
        intent.putExtra(CommentScreenEditActivity.order, numOfPoint.getText().toString());
        intent.putExtra(CommentScreenEditActivity.search, searchOfPoint.getText().toString());
        intent.putExtra(CommentScreenEditActivity.mad, madOfPoint.getText().toString());
        intent.putExtra(CommentScreenEditActivity.index, groundOfPoint.getText().toString());
        intent.putExtra(CommentScreenEditActivity.comment, commentOfPoint.getText().toString());
        intent.putExtra(CommentScreenEditActivity.latitude, latOfPoint.getText().toString());
        intent.putExtra(CommentScreenEditActivity.longitude, lonOfPoint.getText().toString());
        startActivity(intent);
    }

    public void onClickEditLatBtn(View view) {
        Intent intent = new Intent(PointInfoToChange.this, LatitudeScreenEditActivity.class);
        intent.putExtra(LatitudeScreenEditActivity.order, numOfPoint.getText().toString());
        intent.putExtra(LatitudeScreenEditActivity.search, searchOfPoint.getText().toString());
        intent.putExtra(LatitudeScreenEditActivity.mad, madOfPoint.getText().toString());
        intent.putExtra(LatitudeScreenEditActivity.index, groundOfPoint.getText().toString());
        intent.putExtra(LatitudeScreenEditActivity.comment, commentOfPoint.getText().toString());
        intent.putExtra(LatitudeScreenEditActivity.latitude, latOfPoint.getText().toString());
        intent.putExtra(LatitudeScreenEditActivity.longitude, lonOfPoint.getText().toString());
        startActivity(intent);
    }

    public void onClickEditLonBtn(View view) {
        Intent intent = new Intent(PointInfoToChange.this, LongitudeScreenEditActivity.class);
        intent.putExtra(LongitudeScreenEditActivity.order, numOfPoint.getText().toString());
        intent.putExtra(LongitudeScreenEditActivity.search, searchOfPoint.getText().toString());
        intent.putExtra(LongitudeScreenEditActivity.mad, madOfPoint.getText().toString());
        intent.putExtra(LongitudeScreenEditActivity.index, groundOfPoint.getText().toString());
        intent.putExtra(LongitudeScreenEditActivity.comment, commentOfPoint.getText().toString());
        intent.putExtra(LongitudeScreenEditActivity.latitude, latOfPoint.getText().toString());
        intent.putExtra(LongitudeScreenEditActivity.longitude, lonOfPoint.getText().toString());
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Bundle arguments = getIntent().getExtras();

        if (arguments != null) {
            String ord = arguments.getString(PointInfoToChange.ord);
            String searchResp = arguments.getString(PointInfoToChange.searchEdit);
            String madResp = arguments.getString(PointInfoToChange.madEdit);
            String indexResp = arguments.getString(PointInfoToChange.indexEdit);
            String commentResp = arguments.getString(PointInfoToChange.commentEdit);
            String latResp = arguments.getString(PointInfoToChange.latEdit);
            String lonResp = arguments.getString(PointInfoToChange.lonEdit);

            if (ord != null) {
                numOfPoint.setText(ord);
                searchOfPoint.setText(searchResp);
                madOfPoint.setText(madResp);
                groundOfPoint.setText(indexResp);
                commentOfPoint.setText(commentResp);
                latOfPoint.setText(latResp);
                lonOfPoint.setText(lonResp);
            }

        }
    }

    public void onClickOkBtn(View view) {
        editOrd = numOfPoint.getText().toString();
        editSearch = searchOfPoint.getText().toString();
        editMad = madOfPoint.getText().toString();
        editIndex = groundOfPoint.getText().toString();
        editComment = commentOfPoint.getText().toString();
        editLat = latOfPoint.getText().toString();
        editLon = lonOfPoint.getText().toString();

        Point currentPoint = new Point(editOrd, editSearch, editMad, editIndex, editComment, editLat, editLon, "unknown", "unknown");
        if (point.equalsObj(currentPoint)) {
            Intent intent = new Intent(PointInfoToChange.this, JournalActivity.class);
            startActivity(intent);
        } else {
            QuitDialog quitDialog = new QuitDialog("Править данные?");
            quitDialog.show(getSupportFragmentManager(), "quit");
        }
    }

    public void onClickCancelBtn(View view) {
        Intent intent = new Intent(PointInfoToChange.this, JournalActivity.class);
        startActivity(intent);
    }

    public void onQuitDialogPositiveClick(DialogFragment dialogFragment) {
        Intent intent = new Intent(PointInfoToChange.this, JournalActivity.class);
        String[] arr = {editOrd, editSearch, editMad, editIndex, editComment, editLat, editLon};
        intent.putExtra(JournalActivity.file, "fromEditScreen");
        intent.putExtra(JournalActivity.editData, arr);
        startActivity(intent);
    }

    public void onQuitDialogNegativeClick(DialogFragment dialogFragment) {

    }
}