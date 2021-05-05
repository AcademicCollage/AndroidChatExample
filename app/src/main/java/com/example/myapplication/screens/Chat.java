package com.example.myapplication.screens;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.airbnb.lottie.LottieAnimationView;
import com.example.myapplication.R;
import com.example.myapplication.RecyclerChat;
import com.example.myapplication.Utils;
import com.example.myapplication.objects.ChatConversation;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class Chat extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private RecyclerView recyclerView;
    private LottieAnimationView lottieAnimationView;
    private ArrayList<ChatConversation> chats;
    private SharedPreferences sharedPreferences;
    private EditText textOfUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        lottieAnimationView=findViewById(R.id.animationView);
        getChatsFromServer();
        sendMessage();
        sharedPreferences=getSharedPreferences("userData",MODE_PRIVATE);


    }

    private void sendMessage() {
        ImageButton imageButton=findViewById(R.id.send_message);
        EditText editText=findViewById(R.id.message_to_send);
        imageButton.setOnClickListener(new View.OnClickListener() {



            @Override
            public void onClick(View v) {
                db.
                        collection("chats")
                        .document(""+new Date().getTime()).
                        set(new ChatConversation(Utils.user.getName(),new Date().toGMTString(),editText.getText().toString(),""));

            }
        });
    }

    private void getChatsFromServer() {

        db.collection("chats").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if(error==null){
                    chats= (ArrayList<ChatConversation>) value.toObjects(ChatConversation.class);
                    if(chats.size()>0){
                        lottieAnimationView.setVisibility(View.GONE);
                    }else{
                        lottieAnimationView.setVisibility(View.VISIBLE);
                    }

                    setRecyclerView();
                }
            }
        });
        db.collection("chats").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                chats= (ArrayList<ChatConversation>) queryDocumentSnapshots.toObjects(ChatConversation.class);
                if(chats.size()>0){
                    lottieAnimationView.setVisibility(View.GONE);
                }

                setRecyclerView();
            }
        });
    }

    private void setRecyclerView() {
        recyclerView = findViewById(R.id.recycler_chat);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        RecyclerChat mAdapter = new RecyclerChat(chats);
        recyclerView.setAdapter(mAdapter);
    }
}