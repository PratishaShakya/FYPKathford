package com.example.afinal;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class ProfileActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    public Button logout;
  public TextView myProfile , myEmail , username ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

   firebaseAuth = firebaseAuth.getInstance();
       myProfile = (TextView)findViewById(R.id.my_profile);
       myEmail = (TextView) findViewById(R.id.profile_email);
       username = (TextView) findViewById(R.id.profile_name);
        logout = (Button) findViewById(R.id.btn_log_out);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.signOut();
                finish();
                startActivity( new Intent(ProfileActivity.this , SignIn.class));
            }
        });
    }
}
