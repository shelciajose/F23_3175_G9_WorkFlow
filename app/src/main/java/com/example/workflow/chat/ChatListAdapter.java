package com.example.workflow.chat;

import static com.example.workflow.utils.ConstantUtils.KEY_CHAT;
import static com.example.workflow.utils.ConstantUtils.KEY_RECEIVER;
import static com.example.workflow.utils.ConstantUtils.KEY_RECEIVER_USER_ID;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
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

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.Myholder>{
    FirebaseDatabase firebaseDatabase;
    Context context;
    String listUserName;
    String iconUri;
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
        final String receiverUserId = receiverUsersList.get(position).getUserId();
        listUserName = receiverUsersList.get(position).getUserName();
        iconUri = receiverUsersList.get(position).getUserImage();
        lastMessage = lastMessageMap.get(receiverUserId);
        holder.txtViewName.setText(listUserName);

        if(lastMessage == null || lastMessage.equals("default")) {
            holder.txtViewLastMessage.setVisibility(View.GONE);
        } else {
            holder.txtViewLastMessage.setVisibility(View.VISIBLE);
            holder.txtViewLastMessage.setText(lastMessage);
        }

        if(iconUri != null) {
            if (iconUri.length() != 0) {
                holder.icon.setImageBitmap(convertb64ToImage(iconUri));
            } else {
                holder.icon.setImageResource(R.drawable.account_icon);
            }
        } else {
            holder.icon.setImageResource(R.drawable.account_icon);
        }

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
        CircleImageView icon;

        public Myholder(@NonNull View itemView) {
            super(itemView);
            imgViewIcon = itemView.findViewById(R.id.iconChatList);
            txtViewName = itemView.findViewById(R.id.txtViewNameChatList);
            txtViewLastMessage = itemView.findViewById(R.id.txtViewLastMessage);
            icon = itemView.findViewById(R.id.iconChatList);

        }
    }

    private Bitmap convertb64ToImage(String base64){
        final String pureBase64Encoded = base64.substring(base64.indexOf(",") + 1);
        final byte[] decodedBytes = Base64.decode(pureBase64Encoded, Base64.DEFAULT);
        Bitmap decodedBitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);

        return decodedBitmap;
    }
}
