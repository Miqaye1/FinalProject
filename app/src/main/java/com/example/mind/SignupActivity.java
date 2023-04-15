package com.example.mind;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.mind.databinding.ActivitySignupBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.UUID;

public class SignupActivity extends AppCompatActivity {

    ActivitySignupBinding binding;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference usersRef = db.collection("users");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        binding = ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = binding.signupEmail.getText().toString();
                String password = binding.signupPassword.getText().toString();
                String name = binding.signupName.getText().toString();
                String surname = binding.signupSurname.getText().toString();

                if(email.equals("") || password.equals("") || name.equals("") || surname.equals("")) {
                    CustomToast.showErrorToast(SignupActivity.this, "An error occurred!", "All fields are mandatory", 1000);
                }
                else if (password.length() < 6) {
                    CustomToast.showErrorToast(SignupActivity.this, "An error occurred!", "Password should be at least 6 characters", 1000);
                }
                else if (name.length() < 2 || surname.length() < 2) {
                    CustomToast.showErrorToast(SignupActivity.this, "An error occurred!", "Name and surname should be at least 2 characters", 1000);
                }
                else {
                    Query emailQuery = usersRef.whereEqualTo("email", email);
                    emailQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    CustomToast.showErrorToast(SignupActivity.this, "An error occurred!", "You have already registered with this email", 1000);
                                    return;
                                }
                                DatabaseHelper helperClass = new DatabaseHelper(name, surname, email, password);
                                String userId = UUID.randomUUID().toString();
                                DocumentReference userDocRef = usersRef.document(userId);
                                userDocRef.set(helperClass);
                                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                startActivity(intent);
                            } else {
                                CustomToast.showErrorToast(SignupActivity.this, "An error occurred!", "Please check your internet connection", 1000);
                            }
                        }
                    });
                }
            }
        });

        binding.gotoLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}