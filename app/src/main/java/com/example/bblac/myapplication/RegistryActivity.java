package com.example.bblac.myapplication;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class RegistryActivity extends AppCompatActivity {
    private Button button;
    private String userName;
    private String passWord;
    private EditText UserField;
    private EditText PassField;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_registy);
        button = (Button) findViewById(R.id.saveButton);
        UserField = (EditText) findViewById(R.id.nameEditText);
        PassField = (EditText) findViewById(R.id.PassEditText);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
}
