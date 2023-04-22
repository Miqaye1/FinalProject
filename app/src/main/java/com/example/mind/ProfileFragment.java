package com.example.mind;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */

import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import com.bumptech.glide.Glide;
import com.example.mind.databinding.FragmentProfileBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ProfileFragment extends Fragment {

    private static final int PICK_IMAGE_REQUEST = 1;
    private FragmentProfileBinding binding;

    private ActivityResultLauncher<Intent> pickImageLauncher;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
       /* FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String userId1 = mAuth.getCurrentUser().getUid();

        DocumentReference docRef = db.collection("users").document(userId1);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    String name = documentSnapshot.getString("name");
                    String surname = documentSnapshot.getString("surname");
                    binding.textView.setText(name+" ");
                    binding.textView.setText(surname);

                    // Use the name and surname variables here
                } else {
                    // Document does not exist
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Handle any errors
            }
        });*/

        binding.imageView5.setOnClickListener(v -> pickImageFromGallery());

        pickImageLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == getActivity().RESULT_OK) {
                Intent data = result.getData();
                if (data != null && data.getData() != null) {
                    Uri uri = data.getData();
                    uploadImageToFirestore(uri);
                }
            }
        });

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String userId = user.getUid();
            db.collection("users").document(userId).get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists() && document.contains("profileImage")) {
                        String imageUrl = document.getString("profileImage");
                        Glide.with(requireActivity()).load(imageUrl).into(binding.imageView5);
                    }
                } else {
                    // Handle errors here
                }
            });
        } else {
            // Show sign-in prompt here
        }

        return binding.getRoot();
    }

    private void pickImageFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickImageLauncher.launch(intent);
    }

    private void uploadImageToFirestore(Uri uri) {
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        StorageReference imageRef = storageRef.child("images/" + UUID.randomUUID().toString());
        UploadTask uploadTask = imageRef.putFile(uri);

        uploadTask.continueWithTask(task -> {
            if (!task.isSuccessful()) {
                throw task.getException();
            }

            return imageRef.getDownloadUrl();
        }).addOnCompleteListener(task -> {
            try {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                    saveImageToFirestore(downloadUri);
                } else {
                    // Handle errors here
                }
            } catch (Exception e) {
                // Handle exceptions here
            }
        });
    }

    private void saveImageToFirestore(Uri downloadUri) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        Map<String, Object> data = new HashMap<>();
        data.put("profileImage", downloadUri.toString());

        db.collection("users").document(userId).set(data, SetOptions.merge())
                .addOnSuccessListener(aVoid -> {
                    // Handle success here
                })
                .addOnFailureListener(e -> {
                    // Handle errors here
                });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}