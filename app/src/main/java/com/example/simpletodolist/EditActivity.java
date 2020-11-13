package com.example.simpletodolist;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Button;
import android.view.View;
import android.content.Intent;

public class EditActivity extends AppCompatActivity {

    EditText editText;
    Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        editText = findViewById(R.id.editText);

        saveButton = findViewById(R.id.saveButton);

        getSupportActionBar().setTitle("Edit item");

        editText.setText(getIntent().getStringExtra(MainActivity.KEY_ITEM_TEXT));

        //when user is done editing, click save button
        saveButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){

                //create intent which will contain results
                Intent intent = new Intent();

                //pass data (edit results)
                intent.putExtra(MainActivity.KEY_ITEM_TEXT, editText.getText().toString());

                intent.putExtra(MainActivity.KEY_ITEM_POSITION, getIntent().getExtras().getInt(MainActivity.KEY_ITEM_POSITION));

                //set result of intent
                setResult(RESULT_OK, intent);

                //finish activity, close screen, return
                finish();


            }
        });

    }
}