package com.example.mind;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;

import java.security.PrivateKey;



public class ProfileImage extends AppCompatActivity {

/*    private CircleImageView profileImageView;
    private Button closeButton, saveButton;

    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;

    private Uri imageUri;
    private String myUri = "";
    private StorageTask uploadTask;
    private StorageReference storageProfilePicsRef;


    @Override
    protected void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_image);


        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference.child("Users");
        storageProfilePicsRef = FirebaseStorage.getInstance().getReference().child("Profile Picture");

        profileImageView = findViewById(R.id.profile_image);

        closeButton = findViewById(R.id.btnClose);
        saveButton = findViewById(R.id.btnSave);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileImage.this, ProfileFragment.class));
            }
        });
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadProfileImage();
            }

            private void uploadProfileImage() {
            }
        });

    }*/
       @Override
       protected void onCreate (Bundle savedInstanceState) {
           super.onCreate(savedInstanceState);
           setContentView(R.layout.activity_profile_image);

       }

}
