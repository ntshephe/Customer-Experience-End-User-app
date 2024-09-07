package com.example.cxmuserapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cxmuserapp.ChatActivity;
import com.example.cxmuserapp.R;
import com.example.cxmuserapp.UsersHelperClass;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private Context context;
    private List<UsersHelperClass> userModelList;

    public UserAdapter(Context context) {
        this.context = context;
        userModelList = new ArrayList<>();
    }

    public void add(UsersHelperClass userModel){
        userModelList.add(userModel);
        notifyDataSetChanged();
    }
    public void clear(){
        userModelList.clear();
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public UserAdapter.UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_chatlist, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserAdapter.UserViewHolder holder, int position) {

        UsersHelperClass userModel = userModelList.get(position);
        holder.onlineName.setText(userModel.getName());
        holder.onlineEmail.setText(userModel.getEmail());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(context, ChatActivity.class);
                    intent.putExtra("id", userModel.getUserId());
                    context.startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                    // Handle the exception, e.g., show a toast message
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return userModelList.size();
    }

    public class UserViewHolder extends RecyclerView.ViewHolder {

        TextView onlineName, onlineEmail;
        CircleImageView onlineProfile;
        public UserViewHolder(@NonNull View itemView) {
            super(itemView);

            onlineName = itemView.findViewById(R.id.nameonline);
            onlineEmail = itemView.findViewById(R.id.emailOnline);
            onlineProfile = itemView.findViewById(R.id.profileOnline);
        }
    }
}
