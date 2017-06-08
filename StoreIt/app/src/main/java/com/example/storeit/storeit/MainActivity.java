package com.example.storeit.storeit;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private SharedPreferences prefs = null;
    private DomainsDataSource dataSource;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dataSource = new DomainsDataSource(this);
        dataSource.open();

        if (dataSource.isUserFirstTime()){
            setContentView(R.layout.signup_activity);
        }
        else {
            setContentView(R.layout.activity_main);
        }
    }

    public void onLogin(View view){
        Intent intent = new Intent(this, Home.class);
        EditText userName = (EditText)findViewById(R.id.editText);
        EditText password = (EditText)findViewById(R.id.editText2);

        if(dataSource.isUserExist(userName.getText().toString(), password.getText().toString())){
            String message = userName.getText().toString();
            intent.putExtra("EXTRA_MESSAGE", message);
            startActivity(intent);
            MainActivity.this.finish();
        }
        else{
            //System.out.println("Invalid login:\n"+userName.getText()+"\t"+password.getText());
            Toast.makeText(getApplicationContext(), "Invalid Username or Password!",Toast.LENGTH_LONG).show();
        }
    }

    public void onSignUp(View view){
        EditText userName = (EditText)findViewById(R.id.editText);
        EditText passWord = (EditText)findViewById(R.id.editText2);

        if(userName!=null && userName.length()>0 && passWord!=null && passWord.length()>0){
            int result = dataSource.addUser(userName.getText().toString(), passWord.getText().toString());
            if(result < 0){
                Toast.makeText(getApplicationContext(), "User creation failed! Try again..!",Toast.LENGTH_LONG).show();
            }
            else {
                Toast.makeText(getApplicationContext(), "User creation sucessful!!!",Toast.LENGTH_LONG).show();
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                //SignUp.this.finish();
            }

        }
        else{
            Toast.makeText(getApplicationContext(), "Choose valid Username & Password",Toast.LENGTH_LONG).show();
        }
    }
}
