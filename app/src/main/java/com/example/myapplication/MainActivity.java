package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MultiAutoCompleteTextView;

import com.example.myapplication.objects.User;
import com.example.myapplication.screens.Chat;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private EditText phone,name,password;
    private AutoCompleteTextView gender;
    private SharedPreferences sharedPreferences;
    private Button login;
    private void init() {
        initAutoComplete();
        sharedPreferences=getSharedPreferences("userData",MODE_PRIVATE);
        if(sharedPreferences.getString("phone","").length()>0){
            goToNextActivity();
        }
        phone=findViewById(R.id.phone_number);
        name=findViewById(R.id.name);
        password=findViewById(R.id.password);
        login =findViewById(R.id.login_button);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.
                        collection("users").
                        document(phone.getText().toString()).
                        set(new User(name.getText().toString(),
                                password.getText().toString(),
                                phone.getText().toString(),
                                gender.getText().toString())).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        sharedPreferences.edit().putString("phone",phone.getText().toString()).apply();
                        goToNextActivity();

                    }
                });
            }
        });


    }

    private void goToNextActivity() {
        Intent intent=new Intent(MainActivity.this, Chat.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }



    private void initAutoComplete() {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, GENDERS);
         gender = findViewById(R.id.gender);
        gender.setAdapter(adapter);
    }
    private static final String[] GENDERS = new String[] {
            "Male", "Female", "Unknown",
    };
}