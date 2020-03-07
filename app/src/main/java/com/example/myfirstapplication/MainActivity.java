package com.example.myfirstapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;


public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final LinearLayout linearLayout = (LinearLayout)findViewById(R.id.IdLinearLayout);

        final Button addButton = findViewById(R.id.button_id);
        addButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                CheckBox checkbox = new CheckBox(getApplicationContext());
                checkbox.setTextColor(Color.BLACK);

                EditText text = (EditText)findViewById(R.id.inputText);
                String value = text.getText().toString().substring(0, 1).toUpperCase() + text.getText().toString().substring(1).toLowerCase();

                checkbox.setText(value);
                text.setText("");

                linearLayout.addView(checkbox);

            }
        });


    }

}