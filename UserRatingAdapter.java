package com.example.cxmuserapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cxmuserapp.R;
import com.example.cxmuserapp.helpersClasses.RatingsHelperClass;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.List;

public class UserRatingAdapter extends RecyclerView.Adapter<UserRatingAdapter.ViewHolder> {

    List<RatingsHelperClass> rateList;
    Context context;

    public UserRatingAdapter(List<RatingsHelperClass> rateList) {
        this.rateList = rateList;
        this.context = context;
    }


    @NonNull
    @Override
    public UserRatingAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rate_items,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserRatingAdapter.ViewHolder holder, int position) {

        RatingsHelperClass rate = rateList.get(position);

        holder.rateUsername.setText(rate.getUserName());
        holder.rateNum.setRating(rate.getRating());
        holder.rateComment.setText(rate.getRatingComment());
        Picasso.get().load(rate.getImageUrl()).placeholder(R.drawable.defaultprofile).into(holder.rateImage);

    }

    @Override
    public int getItemCount() {
        return rateList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView rateUsername,rateComment;
        RatingBar rateNum;
        RoundedImageView rateImage;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            rateUsername = itemView.findViewById(R.id.rt_username);
            rateComment = itemView.findViewById(R.id.rt_commentText);
            rateImage = itemView.findViewById(R.id.rt_image);
            rateNum = itemView.findViewById(R.id.rt_ratingBar);
        }
    }
}
