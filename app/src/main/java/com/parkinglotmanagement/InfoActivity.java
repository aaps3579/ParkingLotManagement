package com.parkinglotmanagement;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

public class InfoActivity extends AppCompatActivity {

    EditText etName,etEmail;
    Button btSave;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        Intent i=getIntent();
        if(!i.hasExtra("Flag"))
        {
            sharedPreferences = getSharedPreferences("MyRef", MODE_PRIVATE);
            editor = sharedPreferences.edit();
            if (sharedPreferences.contains("Email")) {
                startActivity(new Intent(this, Home.class));
                finish();
            }
        }
        etEmail=(EditText)findViewById(R.id.etEmail);
        etName=(EditText)findViewById(R.id.etName);
        btSave=(Button)findViewById(R.id.btSave);
        btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                View view = getCurrentFocus();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                String Name=etName.getText().toString().trim(),Email=etEmail.getText().toString().trim();
                if(Name.isEmpty()||Email.isEmpty())
                {
                    Snackbar.make(v,"Please Fill Details",Snackbar.LENGTH_SHORT).show();
                }
                else if(!android.util.Patterns.EMAIL_ADDRESS.matcher(Email).matches())
                {
                    Snackbar.make(v,"Check Your Email",Snackbar.LENGTH_SHORT).show();
                }
                else
                {
                    //Save Detials To SharedPref
                    sharedPreferences=getSharedPreferences("MyRef",MODE_PRIVATE);
                    editor = sharedPreferences.edit();
                    if(sharedPreferences.contains("Name"))
                    {
                        editor.clear();
                    }
                    editor.putString("Name",Name);
                    editor.putString("Email",Email);
                    editor.commit();
                    startActivity(new Intent(InfoActivity.this,Home.class));
                    finish();
                }

            }
        });
    }
}
