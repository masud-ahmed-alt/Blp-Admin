package www.blptimes.blpadmin.jobs;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import www.blptimes.blpadmin.MainActivity;
import www.blptimes.blpadmin.R;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class UploadJobsNewsActivity extends AppCompatActivity {

    private CardView addJobsNewsImage;
    private ImageView jobsNewsImageView;
    private EditText jobTitle, noPost, jobDescription, howApply, advLink, applyLink;
    private Button addJobNewsBtn;


    private final int REQ = 1;
    private Bitmap bitmap;
    private DatabaseReference reference, dbRef;
    private StorageReference storageReference;
    private String downloadUrl = "";
    private ProgressDialog pd;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_jobs_news);


        addJobsNewsImage = findViewById(R.id.addJobsNewsImage);
        jobsNewsImageView = findViewById(R.id.jobsNewsImageView);

        jobTitle = findViewById(R.id.jobsTitle);
        noPost = findViewById(R.id.noOfPosts);
        jobDescription = findViewById(R.id.jobsDescriptions);
        howApply = findViewById(R.id.howToApply);
        advLink = findViewById(R.id.advertisementLink);
        applyLink = findViewById(R.id.applyLink);
        addJobNewsBtn = findViewById(R.id.uploadJobsNewsBtn);




        reference = FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference();

        pd = new ProgressDialog(this);



        addJobsNewsImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        addJobNewsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (jobTitle.getText().toString().isEmpty()){
                    jobTitle.setError("Empty");
                    jobTitle.requestFocus();
                }else if (noPost.getText().toString().isEmpty()){
                    noPost.setError("Empty");
                    noPost.requestFocus();
                }else if (jobDescription.getText().toString().isEmpty()){
                    jobDescription.setError("Empty");
                    jobDescription.requestFocus();
                }else if (howApply.getText().toString().isEmpty()) {
                    howApply.setError("Empty");
                    howApply.requestFocus();
                }else if (advLink.getText().toString().isEmpty()) {
                    advLink.setError("Empty");
                    advLink.requestFocus();
                }else if (applyLink.getText().toString().isEmpty()) {
                    applyLink.setError("Empty");
                    applyLink.requestFocus();
                }else if (bitmap == null){
                    Toast.makeText(UploadJobsNewsActivity.this, "Please Select Jobs News Image", Toast.LENGTH_SHORT).show();
                }else {
                    uploadImage();
                }
            }
        });
    }

    private void uploadImage() {

        pd.setMessage("Uploading...");
        pd.show();
        pd.setCanceledOnTouchOutside(false);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,50,baos);
        byte[] finalimg = baos.toByteArray();
        final StorageReference filePath;
        filePath = storageReference.child("jobs").child(finalimg+"jpg");
        final UploadTask uploadTask= filePath.putBytes(finalimg);

        uploadTask.addOnCompleteListener(UploadJobsNewsActivity.this, new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()){
                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    downloadUrl = String.valueOf(uri);
                                    uploadData();
                                }
                            });
                        }
                    });
                }else {
                    pd.dismiss();
                    Toast.makeText(UploadJobsNewsActivity.this, "Something went wrong. Please try again.", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void uploadData() {
        dbRef = reference.child("jobs");
        final String uniqueKey = dbRef.push().getKey();

        String jobsTitle = jobTitle.getText().toString();
        String noPosts = noPost.getText().toString();
        String description = jobDescription.getText().toString();
        String apply = howApply.getText().toString();
        String advLinks = advLink.getText().toString();
        String applyLinks = applyLink.getText().toString();

        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MM-yy");
        String date = currentDate.format(calForDate.getTime());

        Calendar calForTime = Calendar.getInstance();
        SimpleDateFormat currentTime = new SimpleDateFormat("hh:mm a");
        String time = currentTime.format(calForTime.getTime());

        JobsData jobsData = new JobsData(jobsTitle,noPosts,description,apply,advLinks,applyLinks,downloadUrl,date,time,uniqueKey);

        dbRef.child(uniqueKey).setValue(jobsData).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                pd.dismiss();
                Toast.makeText(UploadJobsNewsActivity.this, "Job Uploaded.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(UploadJobsNewsActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                Toast.makeText(UploadJobsNewsActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void openGallery() {
        Intent pickImage = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickImage, REQ);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==REQ && resultCode==RESULT_OK){
            Uri uri = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),uri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            jobsNewsImageView.setImageBitmap(bitmap);
        }
    }
}