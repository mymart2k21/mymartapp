package com.joshi.mymart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.joshi.mymart.models.UserModel;

public class RegistrationActivity extends AppCompatActivity {

    Button signUp;
    TextView name,email,password;
    TextView signin;
    FirebaseAuth auth;
    FirebaseDatabase database;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        auth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
        progressBar=findViewById(R.id.progressbar);
        progressBar.setVisibility(View.GONE);
        signUp=findViewById(R.id.reg_btn);
        name=findViewById(R.id.name);
        email=findViewById(R.id.email);
        password=findViewById(R.id.password);
        signin=findViewById(R.id.login_btn);

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              createUser();
              progressBar.setVisibility(View.VISIBLE);
            }
        });

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegistrationActivity.this,LoginActivity.class));
            }
        });

    }

    private void createUser() {

            String userName=name.getText().toString();
            String userEmail=email.getText().toString();
            String userPassword=password.getText().toString();

            if(TextUtils.isEmpty(userName)){
                Toast.makeText(this, "Please Enter Name", Toast.LENGTH_SHORT).show();
                return;
            }
            if(TextUtils.isEmpty(userEmail)){
                Toast.makeText(this, "Please Enter Email", Toast.LENGTH_SHORT).show();
                return;
            }
            if(TextUtils.isEmpty(userPassword)){
                Toast.makeText(this, "Please Enter Password", Toast.LENGTH_SHORT).show();
                return;
            }
            if(userPassword.length() < 6){
                Toast.makeText(this, "Password length must be greater than 6 characters", Toast.LENGTH_SHORT).show();
            }

            //Craete User

            auth.createUserWithEmailAndPassword(userEmail,userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){

                        UserModel userModel = new UserModel(userName,userEmail,userPassword);
                        String id= task.getResult().getUser().getUid();
                        database.getReference().child("Users").child(id).setValue(userModel);
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(RegistrationActivity.this, "Registered Successfully", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(RegistrationActivity.this, "Error"+task.getException(), Toast.LENGTH_LONG).show();
                    }
                }
            });
        }

    }
