package com.example.bblac.myapplication;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.bblac.myapplication.DatabaseAdapter;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.Set;

public class RegistryActivity extends AppCompatActivity {
    private Button button;
    private EditText UserField;
    private EditText PassField;
    private DatabaseAdapter dbHelper;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_registy);
        initControls();
        SharedPreferences settings = getSharedPreferences(MainActivity.MY_PREFS, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putLong("uid" , 0);
        editor.commit();
        dbHelper = new DatabaseAdapter(this);
        dbHelper.open();
        initControls();
    }


    private boolean isValidUser(String pass) {
        if (pass != null && pass.length() > 6) {
            return true;
        }
        return false;
    }


    private void initControls()
    {
        UserField = (EditText) findViewById(R.id.nameEditText);
        PassField = (EditText) findViewById(R.id.PassEditText);
        button = (Button) findViewById(R.id.saveButton);

        button.setOnClickListener(new Button.OnClickListener() {
            public void onClick (View v){
                final String pass = PassField.getText().toString();
                if (!isValidPassword(pass)) {
                    PassField.setError("Invalid Password");
                }
                if (isValidPassword(pass)) {
                    RegisterMe(v);
                }
            }
        });
    }

    private boolean isValidPassword(String pass){
        if(pass != null && pass.length() > 4) {
            return true;
        }
        return false;
    }

    public void RegisterMe(View v) {
        //Get user details.
        String Username = UserField.getText().toString();
        String Pssworld = PassField.getText().toString();

        //Check if all fields have been completed.
        if(Username.equals("") || Pssworld.equals("")) {
            Toast.makeText(getApplicationContext() , "Please ensure all fields have been completed." , Toast.LENGTH_SHORT ).show();
            return;
        }

        //Encrypt password with MD5.
        Pssworld = md5(Pssworld);

        //Check database for existing users.
        Cursor cursor = dbHelper.fetchUser(Username, Pssworld);
        if( cursor == null ){
            Toast.makeText(getApplicationContext() , "qury Error" , Toast.LENGTH_SHORT).show();
        } else {
            startManagingCursor(cursor);
            //Check for duplicate usernames
            if(cursor.getCount() > 0){
                Toast.makeText(getApplicationContext(), "The username is already registered",
                        Toast.LENGTH_SHORT).show();
                startManagingCursor(cursor);
                cursor.close();
                return;
            }
            stopManagingCursor(cursor);
            cursor.close();
            cursor = dbHelper.fetchUser(Username, Pssworld);
            if (cursor == null) {
                Toast.makeText(getApplicationContext(), "Database query error",
                        Toast.LENGTH_SHORT).show();
                return;
            } else {
                startManagingCursor(cursor);

                if (cursor.getCount() > 0) {
                    Toast.makeText(getApplicationContext(), "The username is already registered",
                            Toast.LENGTH_SHORT).show();
                    stopManagingCursor(cursor);
                    cursor.close();
                    return;
                }
                stopManagingCursor(cursor);
                cursor.close();
            }
            //Create the new username.
            long id = dbHelper.createUser(Username, Pssworld);
            if(id > 0){
                Toast.makeText(getApplicationContext(), "Your username was created",
                        Toast.LENGTH_SHORT).show();
                //saveLoggedInUId(id , username , password);
                Intent i = new Intent( v.getContext() , MainActivity.class);
                startActivity(i);
                finish();
            } else {
                Toast.makeText(getApplicationContext(), "Failt to create new username",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

        public void saveLoggedInUId(long i , String username , String password){
            SharedPreferences settings = getSharedPreferences(MainActivity.MY_PREFS, 0);
            SharedPreferences.Editor editor = settings.edit();
            editor.putLong("uid", i);
            editor.putString("username", username);
            editor.putString("password", password);
            editor.commit();
    }

    private String md5(String s) {
        try {
            MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();
            StringBuffer hexString = new StringBuffer();
            for (int i=0; i<messageDigest.length; i++)
                hexString.append(Integer.toHexString(0xFF & messageDigest[i]));
                return hexString.toString();
             } catch (NoSuchAlgorithmException e) {
                return s;
        }
    }
}

