package com.example.workflow.chat;

import static com.example.workflow.utils.ConstantUtils.KEY_CHAT;
import static com.example.workflow.utils.ConstantUtils.KEY_RECEIVER;
import static com.example.workflow.utils.ConstantUtils.KEY_RECEIVER_USER_ID;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.workflow.activities.ChatActivity;
import com.example.workflow.R;
import com.example.workflow.models.UserModel;
import com.google.firebase.database.FirebaseDatabase;
import java.util.HashMap;
import java.util.List;

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.Myholder>{
    FirebaseDatabase firebaseDatabase;
    Context context;
    String receiverUserId;
    String listUserName;
    String iconUrl;
    String lastMessage;
    List<UserModel> receiverUsersList;
    HashMap<String, String> lastMessageMap;


    public ChatListAdapter(Context _context, List<UserModel> _receiverUsersList) {
        this.context = _context;
        this.receiverUsersList = _receiverUsersList;
        lastMessageMap = new HashMap<>();
    }

    @NonNull
    @Override
    public ChatListAdapter.Myholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_chatlist, parent, false);
        return new Myholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatListAdapter.Myholder holder, int position) {
        listUserName = receiverUsersList.get(position).getUserName();
        receiverUserId = receiverUsersList.get(position).getUserId();
//        iconUrl = receiverUsersList.get(position).getImage();
        lastMessage = lastMessageMap.get(receiverUserId);
        holder.txtViewName.setText(listUserName);

        if(lastMessage == null || lastMessage.equals("default")) {
            holder.txtViewLastMessage.setVisibility(View.GONE);
        } else {
            holder.txtViewLastMessage.setVisibility(View.VISIBLE);
            holder.txtViewLastMessage.setText(lastMessage);
        }

//        try {
//            Glide.with(context).load(iconUrl).into(holder.imgViewIcon);
//        } catch(Exception ex) {
//            ex.printStackTrace();
//        }

        holder.itemView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ChatActivity.class);
                intent.putExtra(KEY_RECEIVER_USER_ID, receiverUserId);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return receiverUsersList.size();
    }

    private String getLastMessage(String userId) {
        lastMessage = firebaseDatabase.getReference(KEY_CHAT).orderByChild(KEY_RECEIVER).equalTo(userId).limitToLast(1).toString();

        if(lastMessage.length() != 0) {
            return lastMessage;
        } else {
            return null;
        }
    }

    protected void setLastMessageMap(String userId, String lastMessage) {
        lastMessageMap.put(userId, lastMessage);
    }

    class Myholder extends RecyclerView.ViewHolder {
        ImageView imgViewIcon;
        TextView txtViewName;
        TextView txtViewLastMessage;

        public Myholder(@NonNull View itemView) {
            super(itemView);
            imgViewIcon = itemView.findViewById(R.id.iconChatList);
            txtViewName = itemView.findViewById(R.id.txtViewNameChatList);
            txtViewLastMessage = itemView.findViewById(R.id.txtViewLastMessage);
        }
    }
}
