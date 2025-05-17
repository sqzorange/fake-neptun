package com.example.fakeneptun.activities;

import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.fakeneptun.R;
import com.example.fakeneptun.adapter.MessagesAdapter;
import com.example.fakeneptun.model.Message;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MessagesActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MessagesAdapter adapter;
    private List<Message> messagesList;
    private EditText etMessage;
    private Button btnSend;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        setContentView(R.layout.activity_messages);
        setupInsets();

        recyclerView = findViewById(R.id.recyclerViewMessages);
        etMessage = findViewById(R.id.etMessage);
        btnSend = findViewById(R.id.btnSend);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        messagesList = new ArrayList<>();
        adapter = new MessagesAdapter(messagesList);
        recyclerView.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();

        db.collection("messages")
                .orderBy("timestamp")
                .addSnapshotListener((querySnapshot, error) -> {
                    if (error != null) {
                        Toast.makeText(MessagesActivity.this, "Hiba az üzenetek betöltésekor: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                        return;
                    }
                    messagesList.clear();
                    for (DocumentSnapshot doc : querySnapshot.getDocuments()) {
                        Message msg = doc.toObject(Message.class);
                        if (msg != null) {
                            msg.setId(doc.getId());
                            messagesList.add(msg);
                        }
                    }
                    adapter.notifyDataSetChanged();
                    if (!messagesList.isEmpty())
                        recyclerView.smoothScrollToPosition(messagesList.size() - 1);
                });

        btnSend.setOnClickListener(v -> {
            String text = etMessage.getText().toString().trim();
            if (text.isEmpty()) return;

            String senderId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
            db.collection("users").document(senderId).get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            String firstName = documentSnapshot.getString("firstName");
                            String familyName = documentSnapshot.getString("familyName");
                            String senderName;
                            if (firstName != null && familyName != null) {
                                senderName = firstName + " " + familyName;
                            } else {
                                senderName = "Ismeretlen";
                            }
                            Message newMessage = new Message(senderId, senderName, text, System.currentTimeMillis());
                            db.collection("messages")
                                    .add(newMessage)
                                    .addOnSuccessListener(documentReference -> etMessage.setText(""))
                                    .addOnFailureListener(e ->
                                            Toast.makeText(MessagesActivity.this, "Hiba az üzenet küldésekor: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                                    );
                        } else {
                            Toast.makeText(MessagesActivity.this, "Felhasználói adatok nem találhatók.", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(e ->
                            Toast.makeText(MessagesActivity.this, "Hiba a felhasználói adatok lekérésekor: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                    );
        });
    }

    private void setupInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.rootView), (v, insets) -> {
            int imeBottom = insets.getInsets(WindowInsetsCompat.Type.ime()).bottom;
            ViewGroup inputContainer = findViewById(R.id.inputContainer);
            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) inputContainer.getLayoutParams();
            params.bottomMargin = imeBottom;
            inputContainer.setLayoutParams(params);
            return insets;
        });
    }
}