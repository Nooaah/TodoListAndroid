package com.example.myfirstapplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Message;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button addButton = findViewById(R.id.button_id);
        addButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                addItem();
            }
        });

        final EditText text = (EditText)findViewById(R.id.inputText);


        text.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN)
                    if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN || keyCode == KeyEvent.KEYCODE_ENTER || keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
                        addItem();
                        text.requestFocus();
                    }
                return false;
            }
        }) ;




    }


    public void addItem() {
        CheckBox checkbox = new CheckBox(getApplicationContext());
        checkbox.setTextColor(Color.BLACK);

        EditText text = (EditText)findViewById(R.id.inputText);
        String value = text.getText().toString().substring(0, 1).toUpperCase() + text.getText().toString().substring(1).toLowerCase();

        checkbox.setText(value);
        text.setText("");
        text.requestFocus();

        final LinearLayout linearLayout = (LinearLayout)findViewById(R.id.IdLinearLayout);
        linearLayout.addView(checkbox);
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
                builder.setTitle("À propos");
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
                final LinearLayout linearLayout = (LinearLayout)findViewById(R.id.IdLinearLayout);
                ((LinearLayout) linearLayout).removeAllViews();
            }
        });
        builder.setNegativeButton("Non", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }



}