package com.example.bblac.myapplication;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {
    SQLiteOpenHelper openHelper;
    SQLiteDatabase db;
    private Context context;
    private EditText Username;
    private Button Login;
    private Button Register;
    private EditText Password;
    private String userName;
    private String passWord;
    Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Login = (Button) findViewById(R.id.button3);
        Register = (Button) findViewById(R.id.button4);
        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, RegistryActivity.class);
                startActivity(intent);
            }
        });

        Username = (EditText) findViewById(R.id.editText1);
        Password = (EditText) findViewById(R.id.editText2);

    }
}
