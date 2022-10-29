package com.example.databases;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    TextView productID;
    EditText productName, productPrice;
    Button addButton, findButton, deleteButton;
    ListView productListView;

    ArrayList<String> productList;
    ArrayAdapter adapter;
    ArrayAdapter<String> special;
    MyDBHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        productList = new ArrayList<>();
        ArrayList<String> findList = new ArrayList<>();

        // info layout
        productID = findViewById(R.id.productID);
        productName = findViewById(R.id.productName);
        productPrice = findViewById(R.id.productPrice);

        //buttons
        addButton = findViewById(R.id.addButton);
        findButton = findViewById(R.id.findButton);
        deleteButton = findViewById(R.id.deleteButton);

        // listview
        productListView = findViewById(R.id.productListView);

        // db handler
        dbHandler = new MyDBHandler(this);

        // button listeners
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = productName.getText().toString();
                double price = Double.parseDouble(productPrice.getText().toString());
                Product product = new Product(name, price);
                dbHandler.addProduct(product);

                productName.setText("");
                productPrice.setText("");

                Toast.makeText(MainActivity.this, "Add product", Toast.LENGTH_SHORT).show();
                viewProducts();
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = productName.getText().toString();
                dbHandler.deleteProduct(name);
                productName.setText("");
                productPrice.setText("");
                Toast.makeText(MainActivity.this, "Delete product", Toast.LENGTH_SHORT).show();
                viewProducts();
            }
        });

        viewProducts();

        findButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                productList.clear();

                String named = productName.getText().toString();
                String price = productPrice.getText().toString();
                MyDBHandler db = new MyDBHandler(MainActivity.this);

                if(named.length()>0 && price.isEmpty()){
                    ArrayList<String> selectName = db.findList(named);
                    special = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, selectName);
                    productListView.setAdapter(special);
                    db.close();
                }
                if(price.length()>0 && named.isEmpty()){

                    ArrayList<String> selectPrice = db.findList(Double.parseDouble(price));
                    special = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, selectPrice);
                    productListView.setAdapter(special);
                    db.close();
                }
                if(price.length()>0 && named.length()>0){
                    ArrayList<String> selectBoth = db.findList(named, Double.parseDouble(price));
                    special = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, selectBoth);
                    productListView.setAdapter(special);
                    db.close();
                }

            }
        });
    }


    private void viewProducts() {
        productList.clear();
        Cursor cursor = dbHandler.getData();
        if (cursor.getCount() == 0) {
            Toast.makeText(MainActivity.this, "Nothing to show", Toast.LENGTH_SHORT).show();
        } else {
            while (cursor.moveToNext()) {
                productList.add(cursor.getString(1) + " (" + cursor.getString(2) + ")");
            }
        }

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, productList);
        productListView.setAdapter(adapter);

        cursor.close();
        dbHandler.close();
    }

}