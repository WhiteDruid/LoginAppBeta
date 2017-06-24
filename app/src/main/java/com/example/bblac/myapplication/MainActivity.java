package com.example.bblac.myapplication;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MainActivity extends AppCompatActivity  {
    private String userName;
    private String passWord;
    private final AppCompatActivity activity = MainActivity.this;
    private Context context;
    private EditText Username;
    private Button Login;
    private Button Register;
    private EditText Password;
    private DatabaseAdapter dbHelper;
    static final String MY_PREFS = "SharedPreferences";
    private Toolbar toolbar;
    MenuItem menu;
    DatabaseAdapter persons;
    DatabaseAdapter databaseAdapter;
    private AutoCompleteTextView carry;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences mySharedPreferences = getSharedPreferences(MY_PREFS, 0);
        SharedPreferences.Editor editor = mySharedPreferences.edit();
        editor.putLong("uid", 0);
        editor.commit();
        dbHelper = new DatabaseAdapter(this);
        dbHelper.open();
        initContorols();
        String thisUsername = Username.getText().toString();
        String[] array = {thisUsername};

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_resource , menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menu){
        switch (menu.getItemId()) {
            // User chose the "Favorite" action, mark the current item
            // as a favorite...
            case R.id.item1 :
                return true;

            case R.id.action_settings:
                // User chose the "Settings" item, show the app settings UI...
                return true;

            // If we got here, the user's action was not recognized.
            // Invoke the superclass to handle it.\
            default:
                return super.onOptionsItemSelected(menu);
        }
    }

    private ArrayAdapter<String> getEmailAddressAdapter(Context context) {
        Account[] accounts = AccountManager.get(context).getAccounts();
        String[] addresses = new String[accounts.length];
        for (int i = 0; i < accounts.length; i++) {
            addresses[i] = accounts[i].name;
        }
        return new ArrayAdapter<String>(context, android.R.layout.simple_dropdown_item_1line, addresses);
    }

    private void initContorols() {
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
        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LogMeIn(view);
            }
        });
    }

    private void LogMeIn(View v) {
        //Get the username and password
        String thisUsername = Username.getText().toString();
        String thisPassword = Password.getText().toString();

        thisPassword = md5(thisPassword);

        Cursor theUser = dbHelper.fetchUser(thisUsername, thisPassword);
        if (theUser != null) {
            startManagingCursor(theUser);
            if (theUser.getCount() > 0) {
                stopManagingCursor(theUser);
                theUser.close();
                Intent i = new Intent(v.getContext(), StartActivity.class);
                startActivity(i);
            }
        }else {
            Toast.makeText(this , "Pass or UserName is wrong" , Toast.LENGTH_SHORT).show();
            startManagingCursor(theUser);
            theUser.close();
        }
    }

    public void saveLoggedInUid(long id , String userName , String passWord){
        SharedPreferences sp = getSharedPreferences(MY_PREFS , 0);
        SharedPreferences.Editor editor = sp.edit();
        editor.putLong("uid" , id);
        editor.putString("username" , userName);
        editor.putString("password" , passWord);
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
        }

        catch (NoSuchAlgorithmException e) {
            return s;
        }
    }
}
