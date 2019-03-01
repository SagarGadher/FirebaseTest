package com.example.firebasetest;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class DetailsActivity extends AppCompatActivity {
    EditText etFName, etLName, etEmail, etPhone, etAddress, etGender;
    Button btnSave;
    RadioButton rbMale, rbFemale;
    String fName, lName, Email, Phone, Address, Gender;
    TextView tvChangePhoto;
    int CHANGE_IMAGE_REQUEST = 101;
    Uri mImageUri;
    ImageView ivUser;
    ProgressDialog progressDialog;
    boolean imageChange = false;

    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference rootReference = firebaseDatabase.getReference();
    DatabaseReference fNameReference = rootReference.child(mAuth.getUid()).child("fName");
    DatabaseReference lNameReference = rootReference.child(mAuth.getUid()).child("lName");
    DatabaseReference emailReference = rootReference.child(mAuth.getUid()).child("email");
    DatabaseReference addressReference = rootReference.child(mAuth.getUid()).child("address");
    DatabaseReference phoneReference = rootReference.child(mAuth.getUid()).child("phone");
    DatabaseReference genderReference = rootReference.child(mAuth.getUid()).child("gender");
    DatabaseReference imageReference = rootReference.child(mAuth.getUid()).child("image");

    FirebaseStorage rootStorage = FirebaseStorage.getInstance();
    StorageReference mStorageReference = rootStorage.getReference("image");

    Intent intent;
    StorageTask mStorageTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_activity_details);

        ivUser = findViewById(R.id.iVUser);
        tvChangePhoto = findViewById(R.id.tvChangePhoto);
        etFName = findViewById(R.id.etFName);
        etLName = findViewById(R.id.etLName);
        etEmail = findViewById(R.id.etEmail);
        etPhone = findViewById(R.id.etPhone);
        etAddress = findViewById(R.id.etAddress);
        btnSave = findViewById(R.id.btnSave);
        rbMale = findViewById(R.id.rbMale);
        rbFemale = findViewById(R.id.rbFemale);
        rbMale.setChecked(true);
        progressDialog = new ProgressDialog(this);
        Gender = "Male";
        tvChangePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePhoto();
            }
        });
        intent = new Intent(DetailsActivity.this, ProfileActivity.class);
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

                    if (mStorageTask != null && mStorageTask.isInProgress()) {
                        Toast.makeText(DetailsActivity.this, "Upload in Progress", Toast.LENGTH_SHORT).show();
                    }
                    if (!imageChange) {
                        Toast.makeText(DetailsActivity.this, "Pleas select any photo as profile", Toast.LENGTH_SHORT).show();
                    } else if (imageChange) {
                        uploadImage();
                        imageChange = false;
                    }
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

    public void changePhoto() {
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(i, CHANGE_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CHANGE_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            mImageUri = data.getData();

            Picasso.get().load(mImageUri).transform(new CropCircleTransformation()).fit().centerCrop().into(ivUser);
            imageChange = true;
        }
    }

    public String getFileExtension(Uri uri) {
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));

    }

    public void uploadImage() {
        if (mImageUri != null) {
            final StorageReference fileReference = mStorageReference.child(mAuth.getUid() + "." + getFileExtension(mImageUri));
            mStorageTask = fileReference.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            if (uri != null) {
                                imageReference.setValue(uri.toString());
                            }
                        }
                    });
                    Toast.makeText(DetailsActivity.this, "File Uploaded", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                    startActivity(intent);
                    finish();
                }
            }).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(DetailsActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    progressDialog.show();
                    progressDialog.setMessage("Upload is in progress");
                    progressDialog.setCanceledOnTouchOutside(false);
                }
            });
        }
    }
}
