package com.example.loginfirebase;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    TextView UserNameError, PhonoNoError, PasswordError, EmailError, AddressError, GenderError, ClickText;
    EditText UserName, PhoneNo, Password, Email, Address;
    android.widget.RadioGroup RadioGroup;
    RadioButton RadioBtn1, RadioBtn2;
    Button SubmitBtn;
    DatabaseReference database;
    FirebaseDatabase firebase;
    String radiogroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);

            UserNameError = findViewById(R.id.UserNameEmpty);
            PhonoNoError = findViewById(R.id.PhoneNoEmpty);
            PasswordError = findViewById(R.id.PasswordEmpty);
            EmailError = findViewById(R.id.EmailEmpty);
            AddressError = findViewById(R.id.AddressEmpty);
            GenderError = findViewById(R.id.GenderEmpty);
            UserName = findViewById(R.id.UserNameID);
            PhoneNo = findViewById(R.id.PhoneNoID);
            Password = findViewById(R.id.PasswordID);
            Email = findViewById(R.id.EmailID);
            Address = findViewById(R.id.AddressID);
            RadioGroup = findViewById(R.id.RadioGroupID);
            RadioBtn1 = findViewById(R.id.RadioBtn1ID);
            RadioBtn2 = findViewById(R.id.RadioBtn2ID);
            SubmitBtn = findViewById(R.id.SubmitBtnID);
            ClickText = findViewById(R.id.ClickTextID);

            RadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(android.widget.RadioGroup group, int checkedId) {
                    if(checkedId == R.id.RadioBtn1ID){
                        RadioBtn2.setChecked(false);
                        radiogroup = RadioBtn1.getText().toString();
                    } else if (checkedId == R.id.RadioBtn2ID){
                        RadioBtn1.setChecked(false);
                        radiogroup = RadioBtn2.getText().toString();
                    }
                }
            });

            Email.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    String email =  s.toString().trim();

                    if(Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                        SubmitBtn.setEnabled(true);
                        EmailError.setVisibility(View.GONE);

                    } else {
                        SubmitBtn.setEnabled(false);
                        EmailError.setVisibility(View.VISIBLE);
                        EmailError.setText("Incorrect email format");
                    }
                }
            });

            SubmitBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(UserName.getText().toString().trim().isEmpty())
                    {
                        UserNameError.setText("Username cannot be empty");
                        UserNameError.setVisibility(View.VISIBLE);
                    }

                    else if(PhoneNo.getText().toString().trim().isEmpty()) {
                        PhonoNoError.setText("Phoneno cannot be empty");
                        PhonoNoError.setVisibility(View.VISIBLE);
                    }

                    else if(Password.getText().toString().trim().isEmpty()) {
                        PasswordError.setText("Password cannot be empty");
                        PasswordError.setVisibility(View.VISIBLE);
                    }

                    else if(Email.getText().toString().trim().isEmpty())
                    {
                        EmailError.setText("Email cannot be empty");
                        EmailError.setVisibility(View.VISIBLE);
                    }

                    else if (Address.getText().toString().trim().isEmpty()) {
                        AddressError.setText("Address cannot be empty");
                        AddressError.setVisibility(View.VISIBLE);

                    } else if(radiogroup.trim().isEmpty()){
                        GenderError.setText("Gender cannot be empty");
                        GenderError.setVisibility(View.VISIBLE);
                    }

                    else
                    {
                        database = FirebaseDatabase.getInstance().getReference().child("UserDetails");
                        String username = UserName.getText().toString();
                        String phoneno = PhoneNo.getText().toString();
                        String password = Password.getText().toString();
                        String email = Email.getText().toString();
                        String address = Address.getText().toString();

                        database.child(username).child("UserName").setValue(username);
                        database.child(username).child("PhoneNo").setValue(phoneno);
                        database.child(username).child("Password").setValue(password);
                        database.child(username).child("Email").setValue(email);
                        database.child(username).child("Address").setValue(address);
                        database.child(username).child("Gender").setValue(radiogroup).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task)
                            {
                                if (task.isSuccessful()){

                                    Toast.makeText(MainActivity.this, "All Data Added Successfully", Toast.LENGTH_SHORT).show();

                                    Intent i = new Intent(MainActivity.this,LoginActivity.class);
                                    startActivity(i);

                                }
                                else
                                {
                                    Toast.makeText(MainActivity.this, "Some Error", Toast.LENGTH_SHORT).show();

                                }
                            }
                        });
                    }
                }
            });


            ClickText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(MainActivity.this,LoginActivity.class);
                    startActivity(intent);
                }
            });















            return insets;
        });
    }
}