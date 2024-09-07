package com.example.cxmuserapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;

import com.example.cxmuserapp.adapters.UserAdapter;
import com.example.cxmuserapp.adapters.UserRatingAdapter;
import com.example.cxmuserapp.helpersClasses.FeedbackHelperClass;

import com.example.cxmuserapp.helpersClasses.RatingsHelperClass;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class MainActivity extends AppCompatActivity {
    MeowBottomNavigation bottomNavigation;
    RelativeLayout home_layout, profiles_layout, main_layout;
    RecyclerView recyclerMainChat;
    LinearLayout chats_layout;
    DatabaseReference databaseReference;
    UserAdapter userAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users");

        bottomNavigation = findViewById(R.id.bottomNavigation);

        chats_layout = findViewById(R.id.chats_layout);
        home_layout = findViewById(R.id.home_layout);
        main_layout = findViewById(R.id.main_layout);
        profiles_layout = findViewById(R.id.profiles_layout);
        recyclerMainChat = findViewById(R.id.recyclerMainChat);


        bottomNavigation.add(new MeowBottomNavigation.Model(1, R.drawable.chat));
        bottomNavigation.add(new MeowBottomNavigation.Model(2, R.drawable.baseline_home_24));
        bottomNavigation.add(new MeowBottomNavigation.Model(3, R.drawable.baseline_person_2_24));
        meowNavigation();


        userAdapter = new UserAdapter(this);
        recyclerMainChat.setAdapter(userAdapter);
        recyclerMainChat.setLayoutManager(new LinearLayoutManager(this));

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userAdapter.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String uid = dataSnapshot.getKey();
                    if (!uid.equals(FirebaseAuth.getInstance().getUid())) {
                        UsersHelperClass userModel = dataSnapshot.getValue(UsersHelperClass.class); // Removed redundant child(uid)
                        userAdapter.add(userModel);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error
            }
        });

        FloatingActionButton btnFab = findViewById(R.id.fab);
        btnFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupMenu(v); // Call method to show popup menu
            }
        });

        RecyclerView ratingRecycler = findViewById(R.id.ratingsRecyclerView);

        List<RatingsHelperClass> rateList = new ArrayList<>();
        ratingRecycler.setHasFixedSize(true);
        ratingRecycler.setLayoutManager(new LinearLayoutManager(this));

        DatabaseReference rateRef = FirebaseDatabase.getInstance().getReference("ratings");

        rateRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                rateList.clear(); // Clear the list before adding new items
                for (DataSnapshot ratingSnapshot : snapshot.getChildren()) {
                    String userId = ratingSnapshot.getKey(); // Use getKey() to get user ID
                    double myrating = ratingSnapshot.child("rating").getValue(Double.class);
                    String ratingComment = ratingSnapshot.child("ratingComment").getValue(String.class);

                    userRef.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot userSnapshot) {
                            if (userSnapshot.exists()) {
                                String userName = userSnapshot.child("name").getValue(String.class);
                                String imageUrl = userSnapshot.child("imageUrl").getValue(String.class);

                                RatingsHelperClass myRateList = new RatingsHelperClass(userId, userName, (float) myrating, ratingComment, imageUrl);
                                rateList.add(myRateList);
                                // Move adapter initialization outside the loop
                                UserRatingAdapter userRatingAdapter = new UserRatingAdapter(rateList);
                                ratingRecycler.setAdapter(userRatingAdapter);
                                userRatingAdapter.notifyDataSetChanged();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            // Handle error
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error
            }
        });
    }

    private void meowNavigation() {
        bottomNavigation.setOnClickMenuListener(new Function1<MeowBottomNavigation.Model, Unit>() {
            @Override
            public Unit invoke(MeowBottomNavigation.Model model) {
                switch (model.getId()) {
                    case 1:
                        chats_layout.setVisibility(View.VISIBLE);
                        home_layout.setVisibility(View.GONE);
                        profiles_layout.setVisibility(View.GONE);
                        main_layout.setBackgroundColor(Color.parseColor("#FFFFFF"));
                        break;
                    case 2:
                        chats_layout.setVisibility(View.GONE);
                        home_layout.setVisibility(View.VISIBLE);
                        profiles_layout.setVisibility(View.GONE);
                        main_layout.setBackgroundColor(Color.parseColor("#FFFFFF"));
                        break;
                    case 3:
                        chats_layout.setVisibility(View.GONE);
                        home_layout.setVisibility(View.GONE);
                        profiles_layout.setVisibility(View.VISIBLE);
                        main_layout.setBackgroundColor(Color.parseColor("#FFFFFF"));
                        break;
                }
                return null;
            }
        });
    }

    // Method to show the popup menu
    private void showPopupMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(MainActivity.this, view);
        popupMenu.getMenuInflater().inflate(R.menu.fab_menu, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.menu_rate_us) {
                    showRatingBottomSheetDialog();
                    return true;
                } else if (item.getItemId() == R.id.menu_log_complaint) {
                    showComplaintBottomSheet();
                    return true;
                }
                return false;
            }
        });
        popupMenu.show();
    }

    private void showComplaintBottomSheet() {
        View bottomFeedbackSheetView = LayoutInflater.from(this).inflate(R.layout.activity_give_feedback_bottom_sheet, null);

        CircleImageView userPic = bottomFeedbackSheetView.findViewById(R.id.feedbackUserProfilePic);
        TextInputEditText feedbackTxt = bottomFeedbackSheetView.findViewById(R.id.feebackInputText);
        TextView btnSubmitFeedback = bottomFeedbackSheetView.findViewById(R.id.btn_submit_feedback);
        TextView cancelFeedbackBtn = bottomFeedbackSheetView.findViewById(R.id.btn_cancel_feedback);

        BottomSheetDialog feedbackbottomSheetDialog = new BottomSheetDialog(this);
        feedbackbottomSheetDialog.setContentView(bottomFeedbackSheetView);

        DatabaseReference reference;
        FirebaseDatabase database;

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

            String userId = currentUser.getUid();

            DatabaseReference currentUserRef = FirebaseDatabase.getInstance().getReference("users").child(userId);
            currentUserRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String profilePicUrl = snapshot.child("imageUrl").getValue(String.class);
                    String currentUserName = snapshot.child("name").getValue(String.class);
                    if (profilePicUrl != null && !profilePicUrl.isEmpty()) {
                        Picasso.get().load(profilePicUrl).into(userPic);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Handle database error
                }
            });


        cancelFeedbackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                feedbackbottomSheetDialog.dismiss();
            }
        });

        btnSubmitFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the current user ID and name
                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                if (currentUser != null) {
                    String userId = currentUser.getUid();
                    String userName = currentUser.getDisplayName();

                    // Get the feedback text
                    String feedbackText = feedbackTxt.getText().toString().trim();

                    // Get the current date
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                    String currentDateAndTime = sdf.format(new Date());

                    // Create a FeedbackHelperClass object
                    FeedbackHelperClass feedbackData = new FeedbackHelperClass(userId, userName, feedbackText, currentDateAndTime);

                    // Get reference to Firebase Database
                    DatabaseReference feedbackRef = FirebaseDatabase.getInstance().getReference("complains");

                    // Generate a new unique key for the feedback entry
                    String feedbackId = feedbackRef.push().getKey();

                    // Write the feedback data to the database
                    feedbackRef.child(feedbackId).setValue(feedbackData)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(MainActivity.this, "Complaint submitted successfully", Toast.LENGTH_SHORT).show();
                                        feedbackbottomSheetDialog.dismiss();
                                    } else {
                                        Toast.makeText(MainActivity.this, "Failed to submit complaint", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                } else {
                    Toast.makeText(MainActivity.this, "User not logged in", Toast.LENGTH_SHORT).show();
                }

                feedbackbottomSheetDialog.dismiss();
            }
        });

        feedbackbottomSheetDialog.show();
    }
    // Method to show the bottom dialog layout for "rate us"
    private void showRatingBottomSheetDialog() {
        // Inflate the bottom sheet dialog layout
        View bottomSheetView = LayoutInflater.from(this).inflate(R.layout.activity_rate_us_bottom_sheet, null);

        // Find views in the bottom sheet dialog layout
        RatingBar ratingBar = bottomSheetView.findViewById(R.id.rating_bar);
        TextView btnSubmitRating = bottomSheetView.findViewById(R.id.btn_submit_rating);
        TextView cancelRatingBtn = bottomSheetView.findViewById(R.id.btn_cancel_rating);
        TextInputEditText rating_comment =bottomSheetView.findViewById(R.id.textRatingComment);



        // Create a BottomSheetDialog with the inflated view
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(bottomSheetView);


        cancelRatingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
            }
        });

        // Set onclick listener for the submit rating button
        btnSubmitRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                if (currentUser != null) {
                    String userId = currentUser.getUid();
                    String userName = currentUser.getDisplayName();
                    float ratingValue = ratingBar.getRating();
                    String ratingComment = rating_comment.getText().toString();

                    DatabaseReference ratingsRef = FirebaseDatabase.getInstance().getReference("ratings");

                    // Create or update the rating directly
                    RatingsHelperClass ratingData = new RatingsHelperClass(userId, userName, ratingValue,ratingComment,null);
                    ratingsRef.child(userId).setValue(ratingData)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(MainActivity.this, "Rating submitted", Toast.LENGTH_SHORT).show();
                                        bottomSheetDialog.dismiss();
                                    } else {
                                        Toast.makeText(MainActivity.this, "Failed to submit rating", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                } else {
                    // User is not logged in
                    Toast.makeText(MainActivity.this, "User not logged in", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Show the bottom sheet dialog
        bottomSheetDialog.show();
    }
}

