package www.blptimes.blpadmin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import www.blptimes.blpadmin.news.NewsData;

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

public class UploadNewsActivity extends AppCompatActivity {


    private CardView addImage;
    private ImageView newsImageView;
    private EditText newsHeadline, newsSubHeadline, MainNews;
    private Button uploadNewsBtn;


    private final int REQ = 1;
    private Bitmap bitmap;

    private DatabaseReference reference, dbRef;
    private StorageReference storageReference;

    private String downloadUrl = "";

    private ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_news);



        addImage = findViewById(R.id.addNewsImage);
        newsImageView = findViewById(R.id.newsImageView);
        newsHeadline = findViewById(R.id.newsHeadline);
        newsSubHeadline = findViewById(R.id.newsSubHeadline);
        MainNews = findViewById(R.id.MainNews);
        uploadNewsBtn = findViewById(R.id.uploadNewsBtn);

        reference = FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference();

        pd = new ProgressDialog(this);



        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        uploadNewsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (newsHeadline.getText().toString().isEmpty()){
                    newsHeadline.setError("Empty");
                    newsHeadline.requestFocus();
                }else if (newsSubHeadline.getText().toString().isEmpty()){
                    newsSubHeadline.setError("Empty");
                    newsSubHeadline.requestFocus();
                }else if (MainNews.getText().toString().isEmpty()){
                    MainNews.setError("Empty");
                    MainNews.requestFocus();
                }else if (bitmap == null){
//                    uploadData();
                    Toast.makeText(UploadNewsActivity.this, "Please Select News Image", Toast.LENGTH_SHORT).show();
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
        filePath = storageReference.child("news").child(finalimg+"jpg");
        final UploadTask uploadTask= filePath.putBytes(finalimg);

        uploadTask.addOnCompleteListener(UploadNewsActivity.this, new OnCompleteListener<UploadTask.TaskSnapshot>() {
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
                    Toast.makeText(UploadNewsActivity.this, "Something went wrong. Please try again.", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void uploadData() {
        dbRef = reference.child("news");
        final String uniqueKey = dbRef.push().getKey();

        String headline = newsHeadline.getText().toString();
        String subHeadline = newsSubHeadline.getText().toString();
        String news = MainNews.getText().toString();

        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MM-yy");
        String date = currentDate.format(calForDate.getTime());

        Calendar calForTime = Calendar.getInstance();
        SimpleDateFormat currentTime = new SimpleDateFormat("hh:mm a");
        String time = currentTime.format(calForTime.getTime());

        NewsData newsData = new NewsData(headline,subHeadline,news,downloadUrl,date,time,uniqueKey);

        dbRef.child(uniqueKey).setValue(newsData).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                pd.dismiss();
                Toast.makeText(UploadNewsActivity.this, "News Uploaded.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(UploadNewsActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                Toast.makeText(UploadNewsActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
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
           newsImageView.setImageBitmap(bitmap);
        }
    }
}