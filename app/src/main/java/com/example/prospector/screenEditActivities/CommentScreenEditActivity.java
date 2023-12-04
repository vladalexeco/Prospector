package com.example.prospector.screenEditActivities;

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

import com.example.prospector.CommentScreenActivity;
import com.example.prospector.PointInfoToChange;
import com.example.prospector.R;
import com.example.prospector.TopLevelActivity;
import com.example.prospector.dialogs.DialogCommentList;
import com.example.prospector.dialogs.DialogFragmentManualInputComment;
import com.example.prospector.dialogs.QuitDialog;
import com.example.prospector.interfaces.CommentChanger;

public class CommentScreenEditActivity extends AppCompatActivity implements DialogFragmentManualInputComment.DialogListener,
        CommentChanger, QuitDialog.QuitDialogListener {

    public final static String order = "Order";
    public final static String search = "Search";
    public final static String mad = "Mad";
    public final static String index = "Index";
    public final static String comment = "Comment";
    public final static String latitude = "Latitude";
    public final static String longitude = "Longitude";

    TextView orderTextView, searchTextView, madTextView, indexTextView, commentTextView, latTextView, lonTextView;

    String commentForChecking;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            Intent intent = new Intent(CommentScreenEditActivity.this, TopLevelActivity.class);
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

        setContentView(R.layout.activity_comment_screen);

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
            String index = arguments.getString(MadScreenEditActivity.index);
            commentForChecking = arguments.getString(MadScreenEditActivity.comment);
            String lat = arguments.getString(MadScreenEditActivity.latitude);
            String lon = arguments.getString(MadScreenEditActivity.longitude);

            orderTextView.setText(order);
            searchTextView.setText(search);
            madTextView.setText(mad);
            indexTextView.setText(index);
            commentTextView.setText(commentForChecking);
            latTextView.setText(lat);
            lonTextView.setText(lon);
        }

        // Клавиша поляна
        Button btnGlade = (Button) findViewById(R.id.glade);
        btnGlade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickCommentBtn(v);
            }
        });
        btnGlade.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                onLongClickCommentBtn(R.array.glade);
                return true;
            }
        });

        // Клавиша ж/д путь
        Button btnRails = (Button) findViewById(R.id.rails);
        btnRails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickCommentBtn(v);
            }
        });
        btnRails.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                onLongClickCommentBtn(R.array.rails);
                return true;
            }
        });

        // Клавиша дорога
        Button btnRoad = (Button) findViewById(R.id.road);
        btnRoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickCommentBtn(v);
            }
        });
        btnRoad.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                onLongClickCommentBtn(R.array.road);
                return true;
            }
        });

        // Клавиша лес
        Button btnForest = (Button) findViewById(R.id.forest);
        btnForest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickCommentBtn(v);
            }
        });
        btnForest.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                onLongClickCommentBtn(R.array.forest);
                return true;
            }
        });

        // Клавиша насыпь
        Button btnMound = (Button) findViewById(R.id.mound);
        btnMound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickCommentBtn(v);
            }
        });
        btnMound.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                onLongClickCommentBtn(R.array.mound);
                return true;
            }
        });

        // Клавиша болото
        Button btnSwamp = (Button) findViewById(R.id.swamp);
        btnSwamp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickCommentBtn(v);
            }
        });
        btnSwamp.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                onLongClickCommentBtn(R.array.swamp);
                return true;
            }
        });

        // Клавиша пустырь
        Button btnWaste = (Button) findViewById(R.id.waste);
        btnWaste.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickCommentBtn(v);
            }
        });
        btnWaste.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                onLongClickCommentBtn(R.array.waste);
                return true;
            }
        });

        // Клавиша у здания
        Button btnBuild = (Button) findViewById(R.id.build);
        btnBuild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickCommentBtn(v);
            }
        });
        btnBuild.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                onLongClickCommentBtn(R.array.build);
                return true;
            }
        });

        // Клаивиша канава
        Button btnTrench = (Button) findViewById(R.id.trench);
        btnTrench.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickCommentBtn(v);
            }
        });
        btnTrench.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                onLongClickCommentBtn(R.array.trench);
                return true;
            }
        });

        // Клавиша у реки
        Button btnRiver = (Button) findViewById(R.id.river);
        btnRiver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickCommentBtn(v);
            }
        });
        btnRiver.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                onLongClickCommentBtn(R.array.river);
                return true;
            }
        });

        // Клавиша заросли
        Button btnJungle = (Button) findViewById(R.id.jungle);
        btnJungle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickCommentBtn(v);
            }
        });
        btnJungle.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                onLongClickCommentBtn(R.array.jungle);
                return true;
            }
        });

        // Клавиша ИССО
        Button btnIsso = (Button) findViewById(R.id.isso);
        btnIsso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickCommentBtn(v);
            }
        });
        btnIsso.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                onLongClickCommentBtn(R.array.isso);
                return true;
            }
        });
    }

    private void onClickCommentBtn(View v) {
        // При быстром нажатии на клавишу комментария
        Button btn = (Button) v;
        String text = btn.getText().toString();
        commentTextView.setText(text);
    }

    private void onLongClickCommentBtn(int idArr) {
        // При долгом нажатии на клавишу комментария
        String[] arr = getResources().getStringArray(idArr);
        DialogCommentList dialogCommentList = new DialogCommentList(arr);
        dialogCommentList.show(getSupportFragmentManager(), "comments");
    }

    public void onClickManualInputBtn(View view) {
        // При нажатии на клавишу "Ручной ввод". При этом открывается диалоговое окно
        // с редактируемым текстовым полем
        DialogFragment dialog = new DialogFragmentManualInputComment();
        dialog.show(getSupportFragmentManager(), "custom");
    }

    public void onDialogPositiveClick(DialogFragment dialog) {
        // При нажатии на клавишу "Ок" диалогового окна "Ручной ввод".
        // Набранное значение заносится в ячейку "Комментарии"
        Dialog dialogView = dialog.getDialog();
        EditText manualInput = (EditText) dialogView.findViewById(R.id.editComment);
        String text = manualInput.getText().toString().trim();
        commentTextView.setText(text);
    }

    public void onDialogNegativeClick(DialogFragment dialog) {

    }

    @Override
    public void change(String comment) {
        // Метод вызываемый при выбора комментария из диалогового списка при долгом нажатии
        // на клавишу комментария
        commentTextView.setText(comment);
    }

    public void onClickCancelBtn(View view) {
        Intent intent = new Intent(CommentScreenEditActivity.this, PointInfoToChange.class);
        intent.putExtra(PointInfoToChange.ord, orderTextView.getText().toString());
        intent.putExtra(PointInfoToChange.searchEdit, searchTextView.getText().toString());
        intent.putExtra(PointInfoToChange.madEdit, madTextView.getText().toString());
        intent.putExtra(PointInfoToChange.indexEdit, indexTextView.getText().toString());
        intent.putExtra(PointInfoToChange.commentEdit, commentForChecking);
        intent.putExtra(PointInfoToChange.latEdit, latTextView.getText().toString());
        intent.putExtra(PointInfoToChange.lonEdit, lonTextView.getText().toString());
        startActivity(intent);
    }

    public void onClickEnterBtn(View view) {
        if (commentForChecking.equals(commentTextView.getText().toString())) {
            Intent intent = new Intent(CommentScreenEditActivity.this, PointInfoToChange.class);
            intent.putExtra(PointInfoToChange.ord, orderTextView.getText().toString());
            intent.putExtra(PointInfoToChange.searchEdit, searchTextView.getText().toString());
            intent.putExtra(PointInfoToChange.madEdit, madTextView.getText().toString());
            intent.putExtra(PointInfoToChange.indexEdit, indexTextView.getText().toString());
            intent.putExtra(PointInfoToChange.commentEdit, commentTextView.getText().toString());
            intent.putExtra(PointInfoToChange.latEdit, latTextView.getText().toString());
            intent.putExtra(PointInfoToChange.lonEdit, lonTextView.getText().toString());
            startActivity(intent);
        } else {
            QuitDialog dialog = new QuitDialog("Редактировать комментарий?");
            dialog.show(getSupportFragmentManager(), "quit");
        }
    }

    public void onQuitDialogPositiveClick(DialogFragment dialogFragment) {
        Intent intent = new Intent(CommentScreenEditActivity.this, PointInfoToChange.class);
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