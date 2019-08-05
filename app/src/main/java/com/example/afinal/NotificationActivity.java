package com.example.afinal;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.EventLog;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.support.v7.widget.SearchView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public class NotificationActivity extends AppCompatActivity {
    RecyclerView mRecycleView;
    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference mdatabaseRef;
    String categoryType;
    FirebaseRecyclerAdapter<Model, ViewHolder> firebaseRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);


//RecycleView
        mRecycleView = findViewById(R.id.recycleview);
        mRecycleView.setHasFixedSize(true);
        //set layout as linear Layout
        mRecycleView.setLayoutManager(new LinearLayoutManager(this));

        categoryType = getIntent().getExtras().getString("categoryType");
        Toast.makeText(this, "Category : " + categoryType, Toast.LENGTH_SHORT).show();

        //send query to firebase database
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mdatabaseRef = mFirebaseDatabase.getReference("uploads");

        getEventData();
        getSupportActionBar().setTitle(categoryType);

    }

    //searh events
    private void firebaseSearch(String searchText) {
        Query firebaseSearchQuery = mdatabaseRef.child(categoryType).orderByChild("title").startAt(searchText).endAt(searchText + "\uf8ff");
        FirebaseApp.initializeApp(this);

        FirebaseRecyclerOptions<Model> options =
                new FirebaseRecyclerOptions.Builder<Model>()
                        .setQuery(firebaseSearchQuery, Model.class)
                        .build();

        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Model, ViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull final Model model) {

                holder.mTextView.setText(model.getTitle());
                Picasso.get().load(model.getImgUrl()).into(holder.mImageTv);

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getApplicationContext(),EventsDetails.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("title",model.getTitle());
                        intent.putExtra("image",model.getImgUrl());
                        intent.putExtra("desc",model.getEventDesc());
                        intent.putExtra("date",model.getEventDateTime());
                        intent.putExtra("location",model.getEventLocation());
                        startActivity(intent);
                    }
                });

            }

            @NonNull
            @Override
            public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row, viewGroup, false);
//
                return new ViewHolder(view);
            }
        };


        //set adapter to recycle view
        mRecycleView.setAdapter(firebaseRecyclerAdapter);


    }
    //load data on recycle view


    @Override
    protected void onStart() {
        super.onStart();

        try {
            firebaseRecyclerAdapter.startListening();
        }catch (Exception e){

        }

    }

    public void getEventData(){

        final Query query = FirebaseDatabase.getInstance().getReference("uploads").child(categoryType);
        FirebaseApp.initializeApp(this);

        FirebaseRecyclerOptions<Model> options =
                new FirebaseRecyclerOptions.Builder<Model>()
                        .setQuery(query, Model.class)
                        .build();

        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Model, ViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull final Model model) {

                holder.mTextView.setText(model.getTitle());
                Picasso.get().load(model.getImgUrl()).into(holder.mImageTv);

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getApplicationContext(),EventsDetails.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("title",model.getTitle());
                        intent.putExtra("image",model.getImgUrl());
                        intent.putExtra("desc",model.getEventDesc());
                        intent.putExtra("date",model.getEventDateTime());
                        intent.putExtra("location",model.getEventLocation());
                        startActivity(intent);
                    }
                });

            }

            @NonNull
            @Override
            public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row, viewGroup, false);
//
                return new ViewHolder(view);
            }
        };


        //set adapter to recycle view
        mRecycleView.setAdapter(firebaseRecyclerAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //inflate the menu; this add items to the action bar if it present
        getMenuInflater().inflate(R.menu.commonmenu, menu);
        MenuItem item = menu.findItem(R.id.app_bar_search);

        SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                firebaseSearch(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //filter as you type
                firebaseSearch(newText);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //handle other action bar item click here
        if (id == R.id.app_bar_search) {
            //TODO
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
