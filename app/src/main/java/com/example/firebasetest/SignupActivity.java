package com.example.firebasetest;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class SignupActivity extends AppCompatActivity {
    Button btnRegister;
    EditText etEmail, etPassword, etCPassword;

    FirebaseAuth mAuth;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_activity_signup);

        mAuth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(this);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etCPassword = findViewById(R.id.etConfirmPassword);
        btnRegister = findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailID = etEmail.getText().toString();
                String paswd = etPassword.getText().toString();
                String cpaswd = etCPassword.getText().toString();
                if (emailID.isEmpty()) {
                    etEmail.setError("Provide your Email first!");
                    etEmail.requestFocus();
                } else if (paswd.isEmpty()) {
                    etPassword.setError("Enter your password");
                    etPassword.requestFocus();
                } else if (cpaswd.isEmpty()) {
                    etPassword.setError("Enter Confirm password");
                    etPassword.requestFocus();
                }else if (!cpaswd.equals(paswd)) {
                    etCPassword.setError("Password and Confirm Password must be same.");
                    etCPassword.requestFocus();
                } else if (emailID.isEmpty() && paswd.isEmpty() && cpaswd.isEmpty()) {
                    Toast.makeText(SignupActivity.this, "Fields Empty!", Toast.LENGTH_SHORT).show();
                } else if (!(emailID.isEmpty() && paswd.isEmpty() && cpaswd.isEmpty())) {
                    progressDialog.show();
                    progressDialog.setMessage("SignUp is in progress");
                    progressDialog.setCanceledOnTouchOutside(false);
                    mAuth.createUserWithEmailAndPassword(emailID, paswd).addOnCompleteListener(SignupActivity.this, new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {

                            if (!task.isSuccessful()) {
                                Toast.makeText(SignupActivity.this.getApplicationContext(),
                                        "SignUp unsuccessful: " + task.getException().getMessage(),
                                        Toast.LENGTH_SHORT).show();
                            } else {
                                progressDialog.dismiss();
                                startActivity(new Intent(SignupActivity.this, DetailsActivity.class));
                                finish();
                            }
                        }
                    });
                } else {
                    Toast.makeText(SignupActivity.this, "Error", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
