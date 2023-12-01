package com.example.workflow.chat;

import static com.example.workflow.utils.ConstantUtils.KEY_CHAT;
import static com.example.workflow.utils.ConstantUtils.KEY_CHAT_LIST;
import static com.example.workflow.utils.ConstantUtils.KEY_USER;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.workflow.R;
import com.example.workflow.models.UserModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.List;
import java.util.ArrayList;

public class ChatListFragment extends Fragment {
    String userId;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    DatabaseReference dbref;
    List<ChatModel> chatList;
    List<String> userOfChatList;
    List<UserModel> usersList;
    RecyclerView recyclerView;
    ChatListAdapter adapterChatList;

    public ChatListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat_list, container, false);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        userId = firebaseUser.getUid();
        chatList = new ArrayList<>();
        userOfChatList = new ArrayList<>();
        recyclerView = view.findViewById(R.id.chatlistrecycle);
        dbref = FirebaseDatabase.getInstance().getReference(KEY_CHAT_LIST).child(userId);
        dbref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                chatList.clear();
                if(snapshot != null) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        ChatListModel chatListModel = dataSnapshot.getValue(ChatListModel.class);
                        if (chatListModel.getUserId().equals(userId) == false) {
                            userOfChatList.add(chatListModel.getUserId());
                        }
                    }
                }
                loadChats();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        return view;
    }

    private void loadChats() {
        usersList = new ArrayList<>();
        dbref = FirebaseDatabase.getInstance().getReference(KEY_USER);
        dbref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                usersList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    UserModel user = dataSnapshot.getValue(UserModel.class);
                    if(user.getUserId() != null) {
                        if(user.getUserId().equals(userId) == false) {
                            usersList.add(user);
                        }
                    }

                    adapterChatList = new ChatListAdapter(getActivity(), usersList);
                    recyclerView.setAdapter(adapterChatList);

                    for (int i = 0; i < usersList.size(); i++) {
                        String receiverId = usersList.get(i).getUserId();
                        lastMessage(receiverId);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void lastMessage(final String receiverId) {
        dbref = FirebaseDatabase.getInstance().getReference(KEY_CHAT);
        dbref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String lastMessage = "default";
                for (DataSnapshot dataSnapshotLastMessage : dataSnapshot.getChildren()) {
                    ChatModel chat = dataSnapshotLastMessage.getValue(ChatModel.class);
                    if (chat == null) {
                        continue;
                    }
                    String sender = chat.getSender();
                    String receiver = chat.getReceiver();
                    if (sender == null || receiver == null) {
                        continue;
                    }
                    // checking for the type of message if
                    // message type is image then set
                    // last message as sent a photo
                    if ((receiver.equals(userId) && sender.equals(receiverId)) ||
                            (receiver.equals(receiverId) && sender.equals(userId))) {
                        lastMessage = chat.getMessage();
                    }
                }
                adapterChatList.setLastMessageMap(receiverId, lastMessage);
                adapterChatList.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    public interface RecyclerViewClickListener {
        public void listClick(View v, int position);
    }
}
