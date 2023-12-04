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

import com.example.prospector.dialogs.DialogCommentList;
import com.example.prospector.dialogs.DialogFragmentManualInputComment;
import com.example.prospector.dialogs.QuitDialog;
import com.example.prospector.dialogs.SimpleDialog;
import com.example.prospector.interfaces.CommentChanger;

public class CommentScreenActivity extends AppCompatActivity implements
        DialogFragmentManualInputComment.DialogListener, QuitDialog.QuitDialogListener, CommentChanger {

    final static String order = "Order";
    final static String index = "Index";
    final static String latitude = "Latitude";
    final static String longitude = "Longitude";

    TextView orderTextView;
    TextView indexTextView;
    TextView commentTextView;
    TextView latTextView;
    TextView lonTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            Intent intent = new Intent(CommentScreenActivity.this, TopLevelActivity.class);
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

        orderTextView = findViewById(R.id.ord_item_screen_appoint);
        indexTextView = findViewById(R.id.index_item_screen_appoint);
        commentTextView = findViewById(R.id.comment_item_screen_appoint);
        latTextView = findViewById(R.id.valueLat);
        lonTextView = findViewById(R.id.valueLon);

        final TextView commentScreenHeader = (TextView) findViewById(R.id.screenHeader);
        commentScreenHeader.setText("Комметарии");

        Bundle arguments = getIntent().getExtras();

        if (arguments != null) {
            String order = arguments.getString(CommentScreenActivity.order);
            String index = arguments.getString(CommentScreenActivity.index);
            String latitude = arguments.getString(CommentScreenActivity.latitude);
            String longitude = arguments.getString(CommentScreenActivity.longitude);

            orderTextView.setText(order);
            indexTextView.setText(index);
            latTextView.setText(latitude);
            lonTextView.setText(longitude);
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

    public void onClickCancelBtn(View view) {
        // Срабатывает при нажатии на клавишу "Отмена". Открывается диалог с подтверждением отмены.
        QuitDialog quitDialog = new QuitDialog("Отменить точку");
        quitDialog.show(getSupportFragmentManager(), "quit");
    }

    public void onQuitDialogPositiveClick(DialogFragment dialog) {
        // Срабатывает при нажатии на клавишу "Ок" диалога  отмены. Удаляет радактируемую точку и
        // Переносит в JournalActivity
        Intent intent = new Intent(CommentScreenActivity.this, JournalActivity.class);
        intent.putExtra(JournalActivity.file, "");
        intent.putExtra(JournalActivity.cancel, true);
        startActivity(intent);
    }

    public void onQuitDialogNegativeClick(DialogFragment dialog) {

    }

    public void onClickEnterBtn(View view) {
        // Переводит на следующий экран (Поиск)
        if (commentTextView.getText().toString().equals("")) {
            SimpleDialog dialog = new SimpleDialog("Введите комментарий");
            dialog.show(getSupportFragmentManager(), "simple");
        } else {
            Intent intent = new Intent(CommentScreenActivity.this, SearchScreenActivity.class);
            intent.putExtra(SearchScreenActivity.order, orderTextView.getText().toString());
            intent.putExtra(SearchScreenActivity.index, indexTextView.getText().toString());
            intent.putExtra(SearchScreenActivity.comment, commentTextView.getText().toString());
            intent.putExtra(SearchScreenActivity.latitude, latTextView.getText().toString());
            intent.putExtra(SearchScreenActivity.longitude, lonTextView.getText().toString());
            startActivity(intent);
        }
    }

    @Override
    public void change(String comment) {
        // Метод вызываемый при выбора комментария из диалогового списка при долгом нажатии
        // на клавишу комментария
        commentTextView.setText(comment);
    }

}