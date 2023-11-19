package com.example.workflow.activities;

import static com.example.workflow.utils.ConstantUtils.KEY_CHAT;
import static com.example.workflow.utils.ConstantUtils.KEY_CHAT_LIST;
import static com.example.workflow.utils.ConstantUtils.KEY_DILIHAT;
import static com.example.workflow.utils.ConstantUtils.KEY_IMAGE;
import static com.example.workflow.utils.ConstantUtils.KEY_MESSAGE;
import static com.example.workflow.utils.ConstantUtils.KEY_RECEIVER;
import static com.example.workflow.utils.ConstantUtils.KEY_RECEIVER_USER_ID;
import static com.example.workflow.utils.ConstantUtils.KEY_SENDER;
import static com.example.workflow.utils.ConstantUtils.KEY_TIME_STAMP;
import static com.example.workflow.utils.ConstantUtils.KEY_USER;
import static com.example.workflow.utils.ConstantUtils.KEY_USER_ID;
import static com.example.workflow.utils.ConstantUtils.KEY_USER_NAME;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.workflow.R;
import com.example.workflow.chat.ChatAdapter;
import com.example.workflow.chat.ChatModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
public class ChatActivity extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference dbref;
    Query userQuery;
    String userId;
    String receiverUserId;
    String iconUrl;
    String receiverUserName;
    String image;
    List<ChatModel> chatList;
    boolean notify = false;
    RecyclerView recyclerViewChat;
    ImageView imgViewProfile;
    TextView txtViewReceiverName;
    EditText editTxtMessage;
    ImageButton imgBtnMessageSend;
    ChatAdapter adapterChat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        imgViewProfile = findViewById(R.id.iconProfileChat);
        txtViewReceiverName = findViewById(R.id.txtViewReceiverNameChat);
        editTxtMessage = findViewById(R.id.editTxtMessage);
        imgBtnMessageSend = findViewById(R.id.imgBtnMessageSend);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        recyclerViewChat = findViewById(R.id.recyclerViewChat);
        recyclerViewChat.setHasFixedSize(true);
        recyclerViewChat.setLayoutManager(linearLayoutManager);
        receiverUserId = getIntent().getStringExtra(KEY_RECEIVER_USER_ID);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        dbref = firebaseDatabase.getReference(KEY_USER);
        getCurrentUser();

        //Hit button message send
        imgBtnMessageSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notify = true;
                String message = editTxtMessage.getText().toString().trim();

                if(TextUtils.isEmpty(message)) {
                    Toast.makeText(ChatActivity.this, "Please Write Message", Toast.LENGTH_LONG).show();
                } else {
                    sendMessage(message);
                }
                editTxtMessage.setText("");
            }
        });

        //Change receiver user status in real time
        userQuery = dbref.orderByChild(KEY_USER_ID).equalTo(receiverUserId);
        userQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    receiverUserName = String.valueOf(dataSnapshot.child(KEY_USER_NAME).getValue());
                    iconUrl = String.valueOf(dataSnapshot.child(KEY_IMAGE).getValue());
//                    if(processing.equals(userId)) {
//                        txtViewOnlineStatus.setText("Typing....");
//                    } else {
//                        if(onlineStatus.equals(key.KEY_ONLINE)) {
//                            txtViewOnlineStatus.setText(onlineStatus);
//                        } else {
//                            Calendar calendar = Calendar.getInstance();
//                            calendar.setTimeInMillis(Long.parseLong(onlineStatus));
//                            String timeData = DateFormat.format(key.KEY_DATE_FORMAT_CHAT, calendar).toString();
//                            txtViewOnlineStatus.setText("Last Seen:" + timeData);
//                        }
//                    }
                    txtViewReceiverName.setText(receiverUserName);
                    try {
                        Glide.with(ChatActivity.this).load(image).placeholder(R.drawable.account_icon).into(imgViewProfile);
                    } catch (Exception ex) {

                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        readMessages();
    }

    private void readMessages() {
        chatList = new ArrayList<>();
        dbref = FirebaseDatabase.getInstance().getReference().child(KEY_CHAT);
        dbref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(chatList.isEmpty() == false) {
                    chatList.clear();
                }

                for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    ChatModel modelChat = dataSnapshot.getValue(ChatModel.class);
                    if((modelChat.getSender().equals(userId) && modelChat.getReceiver().equals(receiverUserId)) ||
                            (modelChat.getReceiver().equals(userId) && modelChat.getSender().equals(receiverUserId))) {
                        chatList.add(modelChat);
                    }

                    adapterChat = new ChatAdapter(ChatActivity.this, chatList, iconUrl);
                    adapterChat.notifyDataSetChanged();
                    recyclerViewChat.setAdapter(adapterChat);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void sendMessage(String message) {
        dbref = FirebaseDatabase.getInstance().getReference();
        String timeStamp = String.valueOf(System.currentTimeMillis());
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put(KEY_SENDER, userId);
        hashMap.put(KEY_RECEIVER, receiverUserId);
        hashMap.put(KEY_MESSAGE, message);
        hashMap.put(KEY_TIME_STAMP, timeStamp);
        hashMap.put(KEY_DILIHAT, false);

        dbref.child(KEY_CHAT).push().setValue(hashMap);

        final DatabaseReference dbrefChatListSender = FirebaseDatabase.getInstance().getReference(KEY_CHAT_LIST).child(receiverUserId).child(userId);
        dbrefChatListSender.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists() == false) {
                    dbrefChatListSender.child(KEY_USER_ID).setValue(userId);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        final DatabaseReference dbrefChatListReceiver = FirebaseDatabase.getInstance().getReference(KEY_CHAT_LIST).child(userId).child(receiverUserId);
        dbrefChatListReceiver.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists() == false) {
                    dbrefChatListReceiver.child(KEY_USER_ID).setValue(receiverUserId);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        return super.onSupportNavigateUp();
    }

    private void getCurrentUser() {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if(user != null) {
            userId = user.getUid();
        }
    }

}
