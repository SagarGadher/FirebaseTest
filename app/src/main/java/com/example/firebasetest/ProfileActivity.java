package com.example.firebasetest;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity implements ValueEventListener {
    TextView tvFName, tvLName, tvEmail, tvPhone, tvAddress, tvGender;
    ImageView ivUser;
    Button btnEdit;

    FirebaseAuth mAuth;

    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference rootReference = firebaseDatabase.getReference();
    DatabaseReference fNameReference = rootReference.child("fName");
    DatabaseReference lNameReference = rootReference.child("lName");
    DatabaseReference emailReference = rootReference.child("email");
    DatabaseReference addressReference = rootReference.child("address");
    DatabaseReference phoneReference = rootReference.child("phone");
    DatabaseReference genderReference = rootReference.child("gender");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_activity_profile);

        mAuth = FirebaseAuth.getInstance();

        ivUser = findViewById(R.id.iVUser);
        tvFName = findViewById(R.id.tvFName);
        tvLName = findViewById(R.id.tvLName);
        tvEmail = findViewById(R.id.tvEmail);
        tvPhone = findViewById(R.id.tvPhone);
        tvAddress = findViewById(R.id.etAddress);
        tvGender = findViewById(R.id.etGender);
        btnEdit = findViewById(R.id.btnEdit);
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileActivity.this, EditActivity.class));
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.appbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                signOut();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void signOut() {
        mAuth.signOut();
        startActivity(new Intent(ProfileActivity.this, LoginActivity.class));
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        fNameReference.addValueEventListener(this);
        lNameReference.addValueEventListener(this);
        emailReference.addValueEventListener(this);
        addressReference.addValueEventListener(this);
        phoneReference.addValueEventListener(this);
        genderReference.addValueEventListener(this);
    }

    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        if (dataSnapshot.getValue(String.class) != null) {
            String key = dataSnapshot.getKey();
            switch (key) {
                case "fName":
                    String fName = dataSnapshot.getValue(String.class);
                    tvFName.setText(fName);
                    break;
                case "lName":
                    String lName = dataSnapshot.getValue(String.class);
                    tvLName.setText(lName);
                    break;
                case "email":
                    String email = dataSnapshot.getValue(String.class);
                    tvEmail.setText(email);
                    break;
                case "phone":
                    String phone = dataSnapshot.getValue(String.class);
                    tvPhone.setText(phone);
                    break;
                case "address":
                    String address = dataSnapshot.getValue(String.class);
                    tvAddress.setText(address);
                    break;
                case "gender":
                    String gender = dataSnapshot.getValue(String.class);
                    tvGender.setText(gender);
                    break;
            }
        }
    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {

    }
}
