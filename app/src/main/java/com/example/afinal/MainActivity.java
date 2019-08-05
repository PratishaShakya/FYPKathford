package com.example.afinal;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.google.firebase.messaging.FirebaseMessaging;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    ViewFlipper viewFlipper;
    private DrawerLayout drawer;
    GridView gridView;
    MainAdapter adapter;

    String[] grid = {"Educational","Art","Food","Sport","Musical","Sale"};
    int[] gridimages = {R.drawable.edu, R.drawable.art, R.drawable.food,R.drawable.sports,R.drawable.concert,R.drawable.sale};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//GridView
                gridView = findViewById(R.id.grid_view);

        MainAdapter adapter = new MainAdapter(MainActivity.this,grid,gridimages);
        gridView.setAdapter(adapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(getApplicationContext(), "You clicked "+grid[position],Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(),NotificationActivity.class);
                intent.putExtra("categoryType",grid[position]);
                startActivity(intent);

            }
        });

        FirebaseMessaging.getInstance().subscribeToTopic("eventsUser");

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView=findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawer,toolbar, R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        int images[] = {R.drawable.arbit, R.drawable.locus, R.drawable.eventss};

        viewFlipper = findViewById(R.id.view_flipper);

        for (int image : images) {
            flipperImages(image);
        }

    }
    public void flipperImages(int image)
    {
        ImageView imageView = new ImageView(this);
        imageView.setBackgroundResource(image);

        viewFlipper.addView(imageView);
       viewFlipper.setFlipInterval(3000); // 4sec
        viewFlipper.setAutoStart(true);

        //animation
        viewFlipper.setInAnimation(this,android.R.anim.slide_in_left);
       viewFlipper.setInAnimation(this,android.R.anim.slide_out_right);

    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }
        else {
            super.onBackPressed();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
         return false;
            }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.mnuNotification) {
            Toast.makeText(this, "Notification menu is clicked", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(MainActivity.this, NotificationActivity.class));
        } else if (id == R.id.app_bar_search) {
            Toast.makeText(this, "Search menu is clicked", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(MainActivity.this, SignIn.class));
        }
        return super.onOptionsItemSelected(item);

    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.nav_profile:
                // getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new ProfileFragment()).commit();
                startActivity(new Intent(MainActivity.this, ProfileActivity.class));
                break;
            case R.id.nav_notification:
                // getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new HomeFragment()).commit();
                startActivity(new Intent(MainActivity.this, NotificationActivity.class));
                break;
            case R.id.nav_addevent:
                startActivity(new Intent(MainActivity.this,AddEventActivity.class));
                break;
            case R.id.nav_reviews:
                startActivity(new Intent(MainActivity.this,ReviewActivity.class));
                break;
            case R.id.nav_settings:
                startActivity(new Intent(MainActivity.this,SettingActivity.class));

        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    }
