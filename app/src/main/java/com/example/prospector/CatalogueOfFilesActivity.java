package com.example.prospector;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.preference.PreferenceManager;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.prospector.dialogs.DialogFromContextMenuDeleteFile;
import com.example.prospector.dialogs.DialogFromContextMenuRenameFile;
import com.example.prospector.interfaces.FileRemover;
import com.example.prospector.interfaces.FileRenamer;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class CatalogueOfFilesActivity extends AppCompatActivity implements FileRemover, DialogFromContextMenuRenameFile.DialogListener, FileRenamer {

    final static String listKey = "List of files";

    String newNameForRename = null;

    ArrayList<String> listOfFiles;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            Intent intent = new Intent(CatalogueOfFilesActivity.this, TopLevelActivity.class);
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

        setContentView(R.layout.activity_catalogue_of_files);

        Bundle arguments = getIntent().getExtras();

        if (arguments != null) {
            String[] arr = arguments.getStringArray(listKey);
            listOfFiles = new ArrayList<String>(Arrays.asList(arr));
            Collections.reverse(listOfFiles);
        }

        ListView listView = (ListView) findViewById(R.id.listViewFiles);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listOfFiles);
        listView.setAdapter(adapter);
        registerForContextMenu(listView);

        AdapterView.OnItemClickListener itemClickListener =   new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String file = listOfFiles.get(position);
                Intent intent = new Intent(CatalogueOfFilesActivity.this, JournalActivity.class);
                intent.putExtra(JournalActivity.file, "Open file");
                intent.putExtra(JournalActivity.fileName, file);
                startActivity(intent);
            }
        };

        listView.setOnItemClickListener(itemClickListener);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.context_menu_for_catalogue_of_files, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        String pos = Integer.toString(info.position);
        switch (item.getItemId()) {
            case R.id.edit:
                DialogFromContextMenuRenameFile dialogFromContextMenuRenameFile = new DialogFromContextMenuRenameFile(listOfFiles.get(info.position), info.position);
                dialogFromContextMenuRenameFile.show(getSupportFragmentManager(), "rename");
                return true;
            case R.id.delete:
                DialogFromContextMenuDeleteFile dialogFromContextMenu = new DialogFromContextMenuDeleteFile(info.position);
                dialogFromContextMenu.show(getSupportFragmentManager(), "delete");
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    @Override
    public void remove(int pos) {
        String file = listOfFiles.get(pos);
        listOfFiles.remove(pos);
        adapter.notifyDataSetChanged();
        CatalogueOfFilesActivity.this.deleteFile(file);
    }

    public  void onDialogPositiveClick(DialogFragment dialog) {
        Dialog dialogView = dialog.getDialog();
        EditText manualInput = (EditText) dialogView.findViewById(R.id.rename);
        String text = manualInput.getText().toString();
        newNameForRename = text;
    }

    public void onDialogNegativeClick(DialogFragment dialog) {

    }

    public void rename(int pos) {
        if (!listOfFiles.get(pos).equals(newNameForRename)) {
            File fileDir = CatalogueOfFilesActivity.this.getFilesDir();
            if (fileDir.exists()) {
                File from = new File(fileDir, listOfFiles.get(pos));
                File to = new File(fileDir, newNameForRename);
                if (from.exists()) {
                    from.renameTo(to);
                    listOfFiles.set(pos, newNameForRename);
                    adapter.notifyDataSetChanged();
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, TopLevelActivity.class);
        startActivity(intent);
    }
}