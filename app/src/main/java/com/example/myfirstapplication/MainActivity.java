package com.example.myfirstapplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    private NotesDbAdapter mDbHelper;
    ListView list ;
    List<String> todoItems;
    EditText text;
    ArrayAdapter aa;
    int darkMode = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Création de la BDD
        mDbHelper = new NotesDbAdapter(this);
        mDbHelper.open();
        fillData();

        list = findViewById(R.id.listview);
        ListView view = (ListView) findViewById(R.id.listview);
        registerForContextMenu(view);

        final Button addButton = findViewById(R.id.button);
        addButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                addItem();
            }
        });

        text = findViewById(R.id.editText);

        todoItems = new ArrayList<String>();

        aa = new ArrayAdapter(this, android.R.layout.simple_list_item_1, todoItems);

        text.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN)
                    if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN || keyCode == KeyEvent.KEYCODE_ENTER || keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
                        addItem();
                        text.requestFocus();
                    }
                return false;
            }
        });
        list.setAdapter(aa);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mDbHelper.deleteNote(id);
                fillData();
            }
        });

        fillData();
    }







    public void addItem() {
        /*CheckBox checkbox = new CheckBox(getApplicationContext());
        checkbox.setTextColor(Color.BLACK);

        EditText text = (EditText)findViewById(R.id.inputText);
        String value = text.getText().toString().substring(0, 1).toUpperCase() + text.getText().toString().substring(1).toLowerCase();


        text.setText("");
        text.requestFocus();*/

        /*
        todoItems.add(0, text.getText().toString().substring(0, 1).toUpperCase() + text.getText().toString().substring(1).toLowerCase());
        aa.notifyDataSetChanged();
*/

        String msg = text.getText().toString();
        mDbHelper.createNote(msg,"");
        fillData();
        text.setText("");


    }

    private void fillData() {
        final ListView listview = (ListView) findViewById(R.id.listview);

        Cursor c = mDbHelper.fetchAllNotes();
        startManagingCursor(c);

        String[] from = new String[] { NotesDbAdapter.KEY_TITLE };
        int[] to = new int[] { R.id.text1 };

        SimpleCursorAdapter notes =
                new SimpleCursorAdapter(this, R.layout.notes_row, c, from, to);
        listview.setAdapter(notes);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.game_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete:
                deleteAll();
                return true;
            case R.id.about:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("À propos \uD83D\uDC68\u200D\uD83D\uDCBB");
                builder.setMessage("\nAuteur : Noah Châtelain\nApprenti Développeur Full Stack");

                builder.setNegativeButton("Fermer", null);
                builder.setPositiveButton("Voir le Git \uD83D\uDE04", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Uri webWaze = Uri.parse("https://github.com/Nooaah/TodoListAndroid");
                        Intent webWazeIntent = new Intent(Intent.ACTION_VIEW, webWaze);
                        startActivity(webWazeIntent);

                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public void deleteAll() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Effacer tout");
        builder.setMessage("Voulez vous vraiment tout effacer ?");
        builder.setPositiveButton("Oui", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                System.out.println("tsess##########");
                for (int i=0;i<todoItems.size();i++) {
                    todoItems.remove(i);
                }
                todoItems.clear();
                aa.notifyDataSetChanged();
                fillData();
            }
        });
        builder.setNegativeButton("Non", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)
                item.getMenuInfo();
        Cursor SelectedTaskCursor = (Cursor) list.getItemAtPosition(info.position);
        final String SelectedTask =
                SelectedTaskCursor.getString(SelectedTaskCursor.getColumnIndex("title"));
        switch (item.getItemId()) {

            case R.id.google:
                Toast.makeText(this, "Rechercher avec Google", Toast.LENGTH_SHORT).show();
                Uri webGoogle = Uri.parse("https://www.google.fr/search?q=" + SelectedTask);
                Intent webIntentGoogle = new Intent(Intent.ACTION_VIEW, webGoogle);
                startActivity(webIntentGoogle);
                return true;
            case R.id.maps:
                Toast.makeText(this, "Rechercher avec Google Maps", Toast.LENGTH_SHORT).show();
                Uri location = Uri.parse("geo:0,0?q=" + SelectedTask);
                Intent webmaps = new Intent(Intent.ACTION_VIEW, location);
                startActivity(webmaps);
                return true;
            case R.id.waze:
                Toast.makeText(this, "Rechercher avec Waze", Toast.LENGTH_SHORT).show();
                Uri webWaze = Uri.parse("https://waze.com/ul?q=" + SelectedTask);
                Intent webWazeIntent = new Intent(Intent.ACTION_VIEW, webWaze);
                startActivity(webWazeIntent);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }


}