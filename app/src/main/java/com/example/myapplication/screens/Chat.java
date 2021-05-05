package com.example.myapplication.screens;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import com.airbnb.lottie.LottieAnimationView;
import com.example.myapplication.R;
import com.example.myapplication.RecyclerChat;
import com.example.myapplication.objects.ChatConversation;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class Chat extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private RecyclerView recyclerView;
    private LottieAnimationView lottieAnimationView;
    private ArrayList<ChatConversation> chats;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        lottieAnimationView=findViewById(R.id.animationView);
        getChatsFromServer();

    }

    private void getChatsFromServer() {

        db.collection("chats").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if(error==null){
                    chats= (ArrayList<ChatConversation>) value.toObjects(ChatConversation.class);
                    if(chats.size()>0){
                        lottieAnimationView.setVisibility(View.GONE);
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