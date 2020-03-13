package com.example.myfirstapplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Message;
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
                /*
                todoItems.remove(position);
                aa.notifyDataSetChanged();
                mDbHelper.createNote(text.getText().toString(),"");
                fillData();

                text.setText("");

                 */
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
                builder.setMessage("\nAuteur : Noah Châtelain\nApprenti développeur");

                builder.setNegativeButton("Fermer \uD83D\uDE04", null);
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
                todoItems.clear();
                aa.notifyDataSetChanged();
                fillData();
            }
        });
        builder.setNegativeButton("Non", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }



}