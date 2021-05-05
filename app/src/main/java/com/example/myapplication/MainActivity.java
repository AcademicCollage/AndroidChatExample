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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private EditText phone, name, password;
    private AutoCompleteTextView gender;
    private SharedPreferences sharedPreferences;
    private Button login;

    private void init() {


        sharedPreferences = getSharedPreferences("userData", MODE_PRIVATE);

        if (sharedPreferences.getString("phone", "").length() > 0) {
            db.collection("user").document(sharedPreferences.getString("phone", "")).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    Utils.user = documentSnapshot.toObject(User.class);
                    goToNextActivity();
                }
            });
        } else {
            setContentView(R.layout.activity_main);
            initAutoComplete();
            phone = findViewById(R.id.phone_number);
            name = findViewById(R.id.name);
            password = findViewById(R.id.password);
            login = findViewById(R.id.login_button);
            login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    User user = new User(name.getText().toString(),
                            password.getText().toString(),
                            phone.getText().toString(),
                            gender.getText().toString());
                    setContentView(R.layout.splash_screen);
                    db.
                            collection("users").
                            document(phone.getText().toString()).
                            set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            sharedPreferences.edit().putString("phone", phone.getText().toString()).apply();
                            Utils.user = user;
                            goToNextActivity();

                        }
                    });
                }
            });
        }


    }

    private void goToNextActivity() {
        Intent intent = new Intent(MainActivity.this, Chat.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);
        init();
    }


    private void initAutoComplete() {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, GENDERS);
        gender = findViewById(R.id.gender);
        gender.setAdapter(adapter);
    }

    private static final String[] GENDERS = new String[]{
            "Male", "Female", "Unknown",
    };
}