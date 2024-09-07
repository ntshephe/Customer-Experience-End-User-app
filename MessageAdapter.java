package com.example.cxmuserapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cxmuserapp.R;
import com.example.cxmuserapp.UsersHelperClass;
import com.example.cxmuserapp.helpersClasses.MessageModel;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MyMessageHolder> {
    private Context context;
    private List<MessageModel> messageModelList;
    private static final int VIEW_TYPE_SENDER = 0;
    private static final int VIEW_TYPE_RECEIVER = 1;

    public MessageAdapter(Context context) {
        this.context = context;
        messageModelList = new ArrayList<>();
    }

    public void add(MessageModel messageModel) {
        messageModelList.add(messageModel);
        notifyDataSetChanged();
    }

    public void setMessages(List<MessageModel> messages) {
        messageModelList = messages;
        notifyDataSetChanged();
    }

    public void clear() {
        messageModelList.clear();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MessageAdapter.MyMessageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == VIEW_TYPE_SENDER) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_sender_row, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_receiver_row, parent, false);
        }
        return new MyMessageHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.MyMessageHolder holder, int position) {
        MessageModel messageModel = messageModelList.get(position);
        holder.mgs.setText(messageModel.getMessage());
    }

    @Override
    public int getItemCount() {
        return messageModelList.size();
    }

    @Override
    public int getItemViewType(int position) {
        // Determine if the message is sent by the current user or received from others
        if (messageModelList.get(position).getSenderId().equals(FirebaseAuth.getInstance().getUid())) {
            return VIEW_TYPE_SENDER; // Sender message
        } else {
            return VIEW_TYPE_RECEIVER; // Receiver message
        }
    }

    public class MyMessageHolder extends RecyclerView.ViewHolder {
        TextView mgs;

        public MyMessageHolder(@NonNull View itemView) {
            super(itemView);
            mgs = itemView.findViewById(R.id.messageChat);
        }
    }

    public void sortMessagesByLatest() {
        Collections.reverse(messageModelList);
        notifyDataSetChanged();
    }
}