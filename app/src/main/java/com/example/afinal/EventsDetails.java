package com.example.afinal;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.Random;

public class EventsDetails extends AppCompatActivity {
TextView mTitleTv ;
ImageView mImageView;

Button button;
    TextView date, des, location;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events_details);
      //  mdatabaseRef = FirebaseDatabase.getInstance().getReference();
mTitleTv=findViewById(R.id.event_title);
mImageView=findViewById(R.id.event_image);
        date = findViewById(R.id.event_date);
        des = findViewById(R.id.event_details);
        location = findViewById(R.id.event_location);
        button=findViewById(R.id.likebutton);

//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Random RAND = new Random();
//                int position = RAND.nextInt(colors.length);
//                int position2 = (index++);
//                String nextValue = colors[position];
//                String textValue = values[position2];
//                tv.setText(textValue);
//                rl.setBackgroundColor(Color.parseColor(nextValue));
//                n.setBackgroundColor(Color.argb(25,0,0,1));
//            }
    //    });

        button.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // Do your work here
                        button.setBackgroundColor(Color.WHITE);
                        return true;
                    case MotionEvent.ACTION_UP:

                        button.setBackgroundColor(Color.RED);
                        return true;
                    default:
                        return false;
                }
            }
        });
//view events from intent
        String title=getIntent().getExtras().getString("title");
        String image = getIntent().getExtras().getString("image");
        String date = getIntent().getExtras().getString("date");
        String desc = getIntent().getExtras().getString("desc");
        String location = getIntent().getExtras().getString("location");



        Picasso.get().load(image).into(mImageView);

        getSupportActionBar().setTitle(title);

        //set data to view
        mTitleTv.setText(title);
        des.setText(desc);

    }
}
