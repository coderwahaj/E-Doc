package com.example.myapplication;

import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.Comparator;

public class ChatActivity extends AppCompatActivity {

    private EditText inputText;
    private Button sendButton;
    private RecyclerView messagesList;
    private MessagesAdapter adapter;
    private ArrayList<Message> messages = new ArrayList<>();
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        inputText = findViewById(R.id.input);
        sendButton = findViewById(R.id.button_send);
        messagesList = findViewById(R.id.list_of_messages);

        adapter = new MessagesAdapter(messages);
        messagesList.setLayoutManager(new LinearLayoutManager(this));
        messagesList.setAdapter(adapter);

        loadMessages();

        sendButton.setOnClickListener(view -> {
            String msgText = inputText.getText().toString();
            if (!msgText.isEmpty()) {
                sendMessageToFirestore(msgText);
                inputText.setText("");
            }
        });
    }

    private void loadMessages() {
        String currentUserId = mAuth.getCurrentUser().getUid();
        Log.d("ChatActivity", "Loading messages for user: " + currentUserId);

        // Query to get messages where the current user is either the sender or receiver
        db.collection("chatSessions")
                .document("session1")
                .collection("messages")
                .whereEqualTo("senderId", currentUserId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Message message = document.toObject(Message.class);
                            messages.add(message);
                        }
                        // Additional query to get messages where the current user is the receiver
                        db.collection("chatSessions")
                                .document("session1")
                                .collection("messages")
                                .whereEqualTo("receiverId", currentUserId)
                                .get()
                                .addOnCompleteListener(task2 -> {
                                    if (task2.isSuccessful()) {
                                        for (QueryDocumentSnapshot document : task2.getResult()) {
                                            Message message = document.toObject(Message.class);
                                            // Check to avoid adding the same message twice
                                            if (!messages.contains(message)) {
                                                messages.add(message);
                                            }
                                        }
                                        if (messages.isEmpty()) {
                                            Log.d("ChatActivity", "No messages found.");
                                        } else {
                                            messages.sort(Comparator.comparing(Message::getTimestamp())); // Sort messages by timestamp
                                        }
                                        adapter.notifyDataSetChanged();  // Notify the adapter to refresh the list
                                    } else {
                                        Log.e("ChatActivity", "Error getting documents: ", task2.getException());
                                    }
                                });
                    } else {
                        Log.e("ChatActivity", "Error getting documents: ", task.getException());
                    }
                });
    }

    private void sendMessageToFirestore(String messageText) {
        long timestamp = System.currentTimeMillis();
        String senderId = mAuth.getCurrentUser().getUid();
        String senderName = mAuth.getCurrentUser().getDisplayName();
        String senderEmail = mAuth.getCurrentUser().getEmail();

        Message msg = new Message(); // Update "receiverId" accordingly

        db.collection("chatSessions")
                .document("session1")
                .collection("messages")
                .add(msg)
                .addOnSuccessListener(documentReference -> {
                    messages.add(msg);
                    adapter.notifyDataSetChanged();
                    messagesList.scrollToPosition(messages.size() - 1);
                })
                .addOnFailureListener(e -> {
                    Log.e("ChatActivity", "Error writing message", e);
                });
    }

}