package com.example.cxmuserapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.cxmuserapp.adapters.MessageAdapter;
import com.example.cxmuserapp.helpersClasses.MessageModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.UUID;

public class ChatActivity extends AppCompatActivity {

    String receiverId;
    DatabaseReference databaseReferenceSender, databaseReferenceReceiver;
    String senderRoom, receiverRoom;
    MessageAdapter messageAdapter;
    RecyclerView recyclerChat;
    Button btnSendMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        recyclerChat = findViewById(R.id.recycler_chat);
        btnSendMsg = findViewById(R.id.button_gchat_send);
        ImageView mainPageRedirect = findViewById(R.id.mainPageRedirect);

        mainPageRedirect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        receiverId = getIntent().getStringExtra("id");
        senderRoom = FirebaseAuth.getInstance().getUid() + receiverId;
        receiverRoom = receiverId + FirebaseAuth.getInstance().getUid(); // Fix receiver room initialization

        messageAdapter = new MessageAdapter(this);
        recyclerChat.setAdapter(messageAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true); // Display items from the end of the list
        recyclerChat.setLayoutManager(layoutManager);

        databaseReferenceSender = FirebaseDatabase.getInstance().getReference("chats").child(senderRoom);
        databaseReferenceReceiver = FirebaseDatabase.getInstance().getReference("chats").child(receiverRoom);

        // Combine both sender and receiver messages into a single list
        final ArrayList<MessageModel> messages = new ArrayList<>();

        databaseReferenceSender.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messages.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    MessageModel messageModel = dataSnapshot.getValue(MessageModel.class);
                    messages.add(messageModel);
                }
                // Call method to display messages
                displayMessages(messages);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle database error
            }
        });

        databaseReferenceReceiver.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    MessageModel messageModel = dataSnapshot.getValue(MessageModel.class);
                    messages.add(messageModel);
                }
                // Call method to display messages
                displayMessages(messages);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle database error
            }
        });

        btnSendMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText messageTxt = findViewById(R.id.edit_gchat_message);
                String message = messageTxt.getText().toString();

                if (message.trim().length() > 0) {
                    sendMessage(message);
                    messageTxt.setText("");
                }
            }
        });
    }

    private void sendMessage(String message) {
        String messageId = UUID.randomUUID().toString();
        long timestamp = System.currentTimeMillis(); // Add timestamp to the message
        MessageModel messageModel = new MessageModel(messageId, FirebaseAuth.getInstance().getUid(), message, timestamp);

        // Add the message only to the sender's database reference
        databaseReferenceSender.child(messageId).setValue(messageModel);
    }
    private void displayMessages(ArrayList<MessageModel> messages) {
        // Sort the messages based on their timestamps
        Collections.sort(messages, new Comparator<MessageModel>() {
            @Override
            public int compare(MessageModel o1, MessageModel o2) {
                return Long.compare(o1.getTimestamp(), o2.getTimestamp());
            }
        });

        // Display the sorted messages in the RecyclerView
        messageAdapter.setMessages(messages);
        // Scroll to the bottom of the list
        recyclerChat.smoothScrollToPosition(messages.size() - 1);
    }
}
