package com.example.firebasetest;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EditActivity extends AppCompatActivity implements ValueEventListener{
    EditText etFName, etLName, etEmail, etPhone, etAddress, etGender;
    Button btnSave;
    RadioButton rbMale, rbFemale;
    String fName, lName, Email, Phone, Address, Gender;

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
        setContentView(R.layout.layout_activity_edit);

        etFName = findViewById(R.id.etFName);
        etLName = findViewById(R.id.etLName);
        etEmail = findViewById(R.id.etEmail);
        etPhone = findViewById(R.id.etPhone);
        etAddress = findViewById(R.id.etAddress);
        btnSave = findViewById(R.id.btnSave);
        rbMale = findViewById(R.id.rbMale);
        rbFemale = findViewById(R.id.rbFemale);
        rbMale.setChecked(true);
        Gender = "Male";
        Log.v("k","k");
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fName = etFName.getText().toString();
                lName = etLName.getText().toString();
                Email = etEmail.getText().toString();
                Phone = etPhone.getText().toString();
                Address = etAddress.getText().toString();
                if (fName.isEmpty()) {
                    etFName.setError("Provide your Email first!");
                    etFName.requestFocus();
                } else if (lName.isEmpty()) {
                    etLName.setError("Enter Password!");
                    etLName.requestFocus();
                } else if (Email.isEmpty()) {
                    etEmail.setError("Enter Password!");
                    etEmail.requestFocus();
                } else if (Address.isEmpty()) {
                    etAddress.setError("Enter Password!");
                    etAddress.requestFocus();
                } else if (Phone.isEmpty()) {
                    etPhone.setError("Enter Password!");
                    etPhone.requestFocus();
                } else if (!(fName.isEmpty() && lName.isEmpty() && Email.isEmpty() && Phone.isEmpty() && Address.isEmpty())) {
                    fNameReference.setValue(fName);
                    lNameReference.setValue(lName);
                    emailReference.setValue(Email);
                    addressReference.setValue(Address);
                    phoneReference.setValue(Phone);
                    genderReference.setValue(Gender);
                }
            }
        });
    }

    public void rbClicked(View view) {
        switch (view.getId()) {
            case R.id.rbMale:
                Gender = "Male";
                break;
            case R.id.rbFemale:
                Gender = "Female";
                break;
        }
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
                    etFName.setText(fName);
                    break;
                case "lName":
                    String lName = dataSnapshot.getValue(String.class);
                    etLName.setText(lName);
                    break;
                case "email":
                    String email = dataSnapshot.getValue(String.class);
                    etEmail.setText(email);
                    break;
                case "phone":
                    String phone = dataSnapshot.getValue(String.class);
                    etPhone.setText(phone);
                    break;
                case "address":
                    String address = dataSnapshot.getValue(String.class);
                    etAddress.setText(address);
                    break;
                case "gender":
                    String gender = dataSnapshot.getValue(String.class);
                    if (gender.equals("Male")){
                        rbMale.setChecked(true);
                    }if (gender.equals("Female")){
                        rbFemale.setChecked(true);
                }
                    break;
            }
        }
    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {

    }
}
