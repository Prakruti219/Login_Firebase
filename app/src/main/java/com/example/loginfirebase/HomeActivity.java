package com.example.loginfirebase;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;

public class HomeActivity extends AppCompatActivity {
    TextView UserName, PhoneNoText, EmailText, GenderText, AddressText;
    ImageView Image;
    Button BtnChooseImg;
    private static final int PICK_IMAGE_REQUEST = 1;

    DatabaseReference databasereference;
    FirebaseDatabase firebasedatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);

            UserName = findViewById(R.id.UserNameTextID);
            PhoneNoText = findViewById(R.id.PhoneNoTextID);
            EmailText = findViewById(R.id.EmailTextID);
            GenderText = findViewById(R.id.GenderTextID);
            AddressText = findViewById(R.id.AddressTextID);
            Image = findViewById(R.id.ImageID);
            BtnChooseImg = findViewById(R.id.BtnChooseImageID);

            BtnChooseImg.setOnClickListener(view -> openGallery());


            databasereference = FirebaseDatabase.getInstance().getReference().child("UserDetails");


            SharedPreferences sharedPreferences = getSharedPreferences("UserData", Context.MODE_PRIVATE);
            String username = sharedPreferences.getString("username", "Guest");

            databasereference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String usernametxt = snapshot.child(username).child("UserName").getValue().toString();
                    UserName.setText(usernametxt);

                    String phonenotxt = snapshot.child(username).child("PhoneNo").getValue().toString();
                    PhoneNoText.setText(phonenotxt);


                    String emailtxt = snapshot.child(username).child("Email").getValue().toString();
                    EmailText.setText(emailtxt);

                    String gendertxt = snapshot.child(username).child("Gender").getValue().toString();
                    GenderText.setText(gendertxt);

                    String addresstxt = snapshot.child(username).child("Address").getValue().toString();
                    AddressText.setText(addresstxt);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });













            return insets;
        });
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            try {
                Bitmap bitmap;
                if (Build.VERSION.SDK_INT >= 29) {
                    ImageDecoder.Source source = ImageDecoder.createSource(getContentResolver(), imageUri);
                    bitmap = ImageDecoder.decodeBitmap(source);
                } else {
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                }
                Image.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}