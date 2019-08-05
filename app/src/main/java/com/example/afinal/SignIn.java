package com.example.afinal;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.messaging.FirebaseMessaging;

public class SignIn extends AppCompatActivity {
    private EditText email, password;
    private Button login;
    private TextView create_Account;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        email = (EditText) findViewById(R.id.lgn_email);
        password = (EditText) findViewById(R.id.lgn_pwd);
        login = (Button) findViewById(R.id.lgn_button);
        create_Account = (TextView) findViewById(R.id.lgn_sign_up);
        firebaseAuth=FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();

        if(user != null){
            finish();
            startActivity(new Intent(SignIn.this, MainActivity.class));
        }

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validate(email.getText().toString().trim(), password.getText().toString().trim());



                //String emailText = email.getText().toString();
                //String passwordText = password.getText().toString();
//                   if (emailText.equals(("Admin")) && passwordText.equals("1234567")) {
//                        Intent intent = new Intent(SignInActivity.this, MainActivity.class);
//                        startActivity(intent);
//                    } else {
//                        Toast.makeText(SignInActivity.this, "Username Or Password Incorrect", Toast.LENGTH_SHORT).show();
//                    }
            }
        });



        create_Account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignIn.this, SignUp.class);
                startActivity(intent);
            }
        });
    }

    private void validate(String userEmail, String userPassword) {


        firebaseAuth.signInWithEmailAndPassword(userEmail, userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){

                    //Toast.makeText(MainActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                    checkEmailVerification();
                    FirebaseMessaging.getInstance().subscribeToTopic("eventsUser");

                }else{
                    Toast.makeText(SignIn.this, "Login Failed", Toast.LENGTH_SHORT).show();

                }
            }
        });


    }

    private void checkEmailVerification(){
        FirebaseUser firebaseUser = firebaseAuth.getInstance().getCurrentUser();
        Boolean emailflag = firebaseUser.isEmailVerified();

        startActivity(new Intent(SignIn.this, MainActivity.class));

    }



    }
