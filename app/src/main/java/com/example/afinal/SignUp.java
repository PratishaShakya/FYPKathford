package com.example.afinal;

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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

public class SignUp extends AppCompatActivity {
    public EditText useremail, password, username, confirmpwd;
    private EditText phonenumber;
    private Button createAccount;
    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        setupUIViews();
firebaseAuth=FirebaseAuth.getInstance();
createAccount.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        if (validate()) {
            String user_email =useremail.getText().toString().trim();
            String user_password = password.getText().toString().trim();
            firebaseAuth.createUserWithEmailAndPassword(user_email,user_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        Toast.makeText(SignUp.this,"Account Created Successfully",Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(SignUp.this,SignIn.class));
                    }
                    else {
                        Toast.makeText(SignUp.this,"Account Creation Failed",Toast.LENGTH_SHORT).show();
                    }
                    if (task.getException() instanceof FirebaseAuthUserCollisionException)
                    {
                        Toast.makeText(SignUp.this, "User with this email already exist.", Toast.LENGTH_SHORT).show();
                    }
                }
            });


        }
    }

});

    }
    private void setupUIViews() {
        useremail = (EditText) findViewById(R.id.sign_up_email);
        username = (EditText) findViewById(R.id.sign_up_username);
        phonenumber = (EditText) findViewById(R.id.sign_up_phone);
        password = (EditText) findViewById(R.id.sign_up_pwd);
        confirmpwd = (EditText) findViewById(R.id.sign_up_confirm_pwd);
        createAccount= findViewById(R.id.sign_up_button);
    }
    private Boolean validate(){
        Boolean res=false;
        String email = useremail.getText().toString();
        String name = username.getText().toString();
        String phone = phonenumber.getText().toString();
        String pwd = password.getText().toString();
        String cpwd = confirmpwd.getText().toString();



        String valid = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
        if(email.matches(valid)){
            res=true;
        }

        if (email.isEmpty() || name.isEmpty() || phone.isEmpty() && pwd.isEmpty() || cpwd.isEmpty()) {
            Toast.makeText(SignUp.this, " fields should not be blank ", Toast.LENGTH_SHORT).show();
        } else {
            res=true;
        }
        return res;
    }

}
