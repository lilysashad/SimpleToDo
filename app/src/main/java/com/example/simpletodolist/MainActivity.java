package com.example.simpletodolist;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import java.util.ArrayList;
import java.util.List;
import androidx.annotation.Nullable;
import android.widget.Button;
import android.widget.EditText;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.view.View;
import android.widget.Toast;
import java.io.File;
import org.apache.commons.io.FileUtils;
import java.nio.charset.Charset;
import android.util.Log;
import java.io.IOException;
import android.content.Intent;

public class MainActivity extends AppCompatActivity {

    public static final String KEY_ITEM_TEXT = "item_text";
    public static final String KEY_ITEM_POSITION = "item_position";
    public static final int EDIT_TEXT_CODE = 20;

    List<String> items;
    Button addButton;
    EditText editText;
    RecyclerView rvItems;
    Adapter itemsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addButton = findViewById(R.id.addButton);
        editText = findViewById(R.id.listItem);
        rvItems = findViewById(R.id.rvItems);

        loadItems();

        Adapter.OnLongClickListener onLongClickListener = new Adapter.OnLongClickListener() {

            @Override
            public void onItemLongClicked(int position) {

                //delete item from model
                items.remove(position);

                //notify adapter
                itemsAdapter.notifyItemRemoved(position);
                Toast.makeText(getApplicationContext(), "Item was removed", Toast.LENGTH_SHORT).show();

                saveItems();

            }

        };

        Adapter.OnClickListener onClickListener = new Adapter.OnClickListener() {

            @Override
            public void onItemClicked(int position){

                Log.d("MainActivity", "Single click at position " + position);

                //create new edit activity
                Intent i = new Intent(MainActivity.this, EditActivity.class);

                //pass data being edited
                i.putExtra(KEY_ITEM_TEXT, items.get(position));

                i.putExtra(KEY_ITEM_POSITION, position);

                //display edited activity
                startActivityForResult(i, EDIT_TEXT_CODE);

        }

    };

        itemsAdapter = new Adapter(items, onLongClickListener, onClickListener);
        rvItems.setAdapter(itemsAdapter);
        rvItems.setLayoutManager(new LinearLayoutManager(this));

        addButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){

                String todoItem = editText.getText().toString();

                //add item to model
                items.add(todoItem);

                //notify adapter that item is inserted
                itemsAdapter.notifyItemInserted(items.size()-1);

                editText.setText("");
                Toast.makeText(getApplicationContext(),"Item was added", Toast.LENGTH_SHORT).show();

                saveItems();
            }
        });
    }

    //handle result of edit activity
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){

        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK && requestCode == EDIT_TEXT_CODE){

            //retrieve updated text value
            String itemText = data.getStringExtra(KEY_ITEM_TEXT);

            //extract original position of edited item from position key
            int position = data.getExtras().getInt(KEY_ITEM_POSITION);

            //update model at right position with new item text
            items.set(position, itemText);

            //notify adapter
            itemsAdapter.notifyItemChanged(position);

            //persist changes
            saveItems();
            Toast.makeText(getApplicationContext(), "Item updated successfully", Toast.LENGTH_SHORT).show();

        }

        else{

            Log.w("MainActivity","Unknown call to onActivityResult");

        }

    }

    private File getDataFile(){

        return new File(getFilesDir(), "data.txt");

    }

    //this function loads items by reading each line of data.txt
    private void loadItems() {

        try {

            items = new ArrayList<>(FileUtils.readLines(getDataFile(), Charset.defaultCharset()));

        }

        catch(IOException e){

            Log.e("MainActivity", "Error reading items", e);
            items = new ArrayList<>();
        }

    }

    //this function saves items by writing into data.txt
    private void saveItems(){

        try{

            FileUtils.writeLines(getDataFile(), items);

        }

        catch(IOException e){

            Log.e("MainActivity","Error writing items", e);
        }
    }
}