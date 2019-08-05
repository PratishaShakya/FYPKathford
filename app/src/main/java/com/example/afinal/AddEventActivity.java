package com.example.afinal;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddEventActivity extends AppCompatActivity {
    private static final int CHOOSE_IMAGE = 1;

    private Button chooseImage, btnUploadImage;
    private ImageView imagePreview;
    private EditText imgDescription;
    private ProgressBar progressBar;
    private Uri imgUri;
    String categoryType, title, desc, location, date;
    private EditText eventLocation, txtTitle;
    Button eventDate;
   // private MapView locationMap;
    Spinner spinner;
    private StorageReference mstorageRef;
    private DatabaseReference mdatabaseRef;
    String[] category = {"Educational","Art","Food","Sport","Musical","Sale"};
    Calendar mcurrent = Calendar.getInstance();
    ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        chooseImage = findViewById(R.id.chooseimg);
        btnUploadImage = findViewById(R.id.uploadbtn);
        imgDescription = findViewById(R.id.imgDescription);
        imagePreview = findViewById(R.id.imgPreview);
//        progressBar = findViewById(R.id.progress_bar);
        eventDate = findViewById(R.id.eventtime);
        txtTitle = findViewById(R.id.txtTitle);
        eventLocation = findViewById(R.id.eventlocation);
      //  locationMap = findViewById(R.id.mapView);
        spinner = findViewById(R.id.spinner);
        mProgressDialog=new ProgressDialog(AddEventActivity.this);
        mstorageRef = FirebaseStorage.getInstance().getReference("uploads");
        mdatabaseRef = FirebaseDatabase.getInstance().getReference("uploads");
//        progressBar.setVisibility(View.GONE);

        chooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFileChoose();
            }
        });

        btnUploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getData();

            }
        });

        eventDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDate();
            }
        });

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_spinner_item, category);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinner.setAdapter(adapter);


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                categoryType = spinner.getSelectedItem().toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void showFileChoose() {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), CHOOSE_IMAGE);


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CHOOSE_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imgUri = data.getData();
//            Picasso.with(this).load(imgUri).into(imagePreview);

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imgUri);
                imagePreview.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String getFileExtension(Uri uri) {

        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));

    }


    public void getData() {
        title = txtTitle.getText().toString().trim();
        date = eventDate.getText().toString().trim();
        desc = imgDescription.getText().toString().trim();
        location = eventLocation.getText().toString().trim();



        if (title.isEmpty()) {
            txtTitle.setError("Enter title");
            return;
        }

        if (desc.isEmpty()) {
            imgDescription.setError("Enter Descriptio");
            return;
        }

        if (date.equals("Date & Time")) {
            Toast.makeText(this, "Select Date", Toast.LENGTH_SHORT).show();
            return;
        }

        if (location.isEmpty()) {
            eventLocation.setError("Enter Address");
            return;
        }

        uploadFile(location, title, categoryType, desc, date, 21.887292, 85.4342342);

    }

    public void uploadFile(final String location, final String title, final String categoryType, final String desc, final String date, final Double lat, final Double lng) {


        if(imgUri!=null){

            mProgressDialog.setTitle("Please Wait....");
            mProgressDialog.show();
            StorageReference storageReference= mstorageRef.child(System.currentTimeMillis() +"." +getFileExtension(imgUri));

            storageReference.putFile(imgUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                            while (!urlTask.isSuccessful());
                            Uri downloadUrl = urlTask.getResult();

                            mProgressDialog.dismiss();
                           Model upload = new Model(downloadUrl.toString(), title, lat, lng, location, date, desc);

                            mdatabaseRef.child(categoryType).push().setValue(upload);
                            Toast.makeText(AddEventActivity.this, "Event  Upload successful", Toast.LENGTH_LONG).show();
                          //  sendNotifications();

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            mProgressDialog.dismiss();
                            Toast.makeText(AddEventActivity.this,e.getMessage(), Toast.LENGTH_LONG).show();

                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            mProgressDialog.setTitle("Image is uploaded");
                        }
                    });
        }
        else {
            Toast.makeText(AddEventActivity.this, "Please Select Image", Toast.LENGTH_LONG).show();

        }

    }

    public void getDate() {
        final Calendar c = Calendar.getInstance();

        int mDay1 = c.get(Calendar.DAY_OF_MONTH);

        c.set(Calendar.DAY_OF_MONTH, mDay1);

        int mYear = mcurrent.get(Calendar.YEAR);
        int mMonth = mcurrent.get(Calendar.MONTH);
        int mDay = mcurrent.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog mDatePicker;
        mDatePicker = new DatePickerDialog(AddEventActivity.this, R.style.MyMaterialTheme, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                selectedmonth = selectedmonth + 1;

                if (selectedday < 10 && selectedmonth < 10) {

                    eventDate.setText(selectedyear + "-" + "0" + selectedmonth + "-" + "0" + selectedday);


                } else if (selectedday > 9 && selectedmonth < 10) {
                    eventDate.setText(selectedyear + "-" + "0" + selectedmonth + "-" + selectedday);

                } else if (selectedday < 9 && selectedmonth > 9) {
                    eventDate.setText(selectedyear + "-" + selectedmonth + "-" + "0" + selectedday);

                } else {
                    eventDate.setText(selectedyear + "-" + selectedmonth + "-" + selectedday);

                }

            }
        }, mYear, mMonth, mDay);
        mDatePicker.getDatePicker().setMinDate(c.getTimeInMillis());

        mDatePicker.show();
    }

  /*  public void sendNotifications(){

        ApiInterface notificationInterface = RetrofitClient.getFormData().create(ApiInterface.class);
        JsonObject jsonObject = new JsonObject();

        jsonObject.addProperty("to","/topics/eventsUser");

        JsonObject jsonObject1 = new JsonObject();
        jsonObject1.addProperty("title","Evento");
        jsonObject1.addProperty("body",categoryType+" Events");
        jsonObject1.addProperty("content_available",true);
        jsonObject1.addProperty("priority","high");
        jsonObject1.addProperty("sound","default");
        jsonObject.add("notification",jsonObject1);

        System.out.println("Json : "+jsonObject);

        Call<JsonObject> call = notificationInterface.sendNotification(jsonObject);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                if (response.isSuccessful()){
                    Toast.makeText(AddEventActivity.this, "Send notification sucessfully", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });

    }
    */
}