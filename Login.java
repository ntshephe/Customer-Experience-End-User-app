package com.example.cxmuserapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity {
    EditText log_UserName, loginPass;
    TextView loginBtn, to_sign, forgot_pass_redirect;

    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        log_UserName = findViewById(R.id.login_email);
        loginPass = findViewById(R.id.login_pass);
        loginBtn = findViewById(R.id.login_button);
        to_sign = findViewById(R.id.signUpRedirect);
        forgot_pass_redirect = findViewById(R.id.forgot_pass);

        forgot_pass_redirect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this, ForgotPassword.class));
                finish();
            }
        });

        to_sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this, Register.class));
                finish();
            }
        });

        mAuth = FirebaseAuth.getInstance();

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String loginUsername = log_UserName.getText().toString();
                String loginPassword = loginPass.getText().toString();

                if (loginUsername.isEmpty() || loginPassword.isEmpty()) {
                    Toast.makeText(Login.this, "Invalid login details. Please try again!", Toast.LENGTH_SHORT).show();
                    return;
                }

                mAuth.signInWithEmailAndPassword(loginUsername, loginPassword)
                        .addOnCompleteListener(Login.this, task -> {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                FirebaseUser user = mAuth.getCurrentUser();
                                assert user != null;
                                String userId = user.getUid();
                                DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(userId);
                                userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        String userType = dataSnapshot.child("userType").getValue(String.class);
                                        if (userType != null && userType.equals("user")) {
                                            String nameFromDB = user.getDisplayName();
                                            String emailFromDB = user.getEmail();
                                            Uri imageFromDB = user.getPhotoUrl();
                                            String mobileFromDB = user.getPhoneNumber();

                                            Intent intent = new Intent(Login.this, MainActivity.class);
                                            intent.putExtra("mobile", mobileFromDB);
                                            intent.putExtra("name", nameFromDB);
                                            intent.putExtra("email", emailFromDB);
                                            intent.putExtra("imageUrl",imageFromDB);
                                            startActivity(intent);
                                            finish();
                                        } else {
                                            // User is not an admin, deny access and display a message
                                            Toast.makeText(Login.this, "You are registered as a service provider. Please use the service provide app.", Toast.LENGTH_SHORT).show();
                                            mAuth.signOut(); // Log out the user
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                        // Handle database error
                                    }
                                });
                            } else {
                                // If sign in fails, display a message to the user.
                                Toast.makeText(Login.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        to_sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this, Register.class));
                finish();
            }
        });
    }
}
