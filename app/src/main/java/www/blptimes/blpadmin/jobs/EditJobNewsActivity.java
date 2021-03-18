package www.blptimes.blpadmin.jobs;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
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
import android.widget.ProgressBar;
import android.widget.TextView;
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
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EditJobNewsActivity extends AppCompatActivity {

    private ImageView editJobImage;
    private EditText jobTitle,noPost, description, howApply,advLink, applyLink;
    private Button editJobBtn;

    private String title, post, desc, howtoapply, advlinks, applylinks,image;

    private final int REQ = 1;
    private Bitmap bitmap=null;
    private String downloadUrl, uniqueKey;

    private ProgressDialog pd;

    private StorageReference storageReference;
    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_job_news);


        title = getIntent().getStringExtra("title");
        post = getIntent().getStringExtra("post");
        desc = getIntent().getStringExtra("desc");
        howtoapply = getIntent().getStringExtra("howtoapply");
        advlinks = getIntent().getStringExtra("advlinks");
        applylinks = getIntent().getStringExtra("applylinks");
        image = getIntent().getStringExtra("image");
        uniqueKey = getIntent().getStringExtra("uniqueKey");

        pd = new ProgressDialog(this);

        reference = FirebaseDatabase.getInstance().getReference().child("jobs");
        storageReference = FirebaseStorage.getInstance().getReference();

        editJobImage = findViewById(R.id.editJobImage);
        jobTitle = findViewById(R.id.jobsEditTitle);
        noPost = findViewById(R.id.noOfEditPosts);
        description = findViewById(R.id.jobsEditDescriptions);
        howApply = findViewById(R.id.howToEditApply);
        advLink = findViewById(R.id.advertisementEditLink);
        applyLink = findViewById(R.id.applyEditLink);
        editJobBtn = findViewById(R.id.editJobsNewsEditBtn);

        try {
            Picasso.get().load(image).into(editJobImage);
        } catch (Exception e) {
            e.printStackTrace();
        }

        jobTitle.setText(title);
        noPost.setText(post);
        description.setText(desc);
        howApply.setText(howtoapply);
        advLink.setText(advlinks);
        applyLink.setText(applylinks);

//
        editJobImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGalley();
            }
        });

        editJobBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                title = jobTitle.getText().toString();
                post = noPost.getText().toString();
                desc = description.getText().toString();
                howtoapply = howApply.getText().toString();
                advlinks = advLink.getText().toString();
                applylinks = applyLink.getText().toString();
                checkValidation();
            }
        });



    }
//
    private void checkValidation() {

        if (title.isEmpty()){
            jobTitle.setError("Empty");
            jobTitle.requestFocus();
        }else if (post.isEmpty()){
            noPost.setError("Empty");
            noPost.requestFocus();
        }else if (desc.isEmpty()){
            description.setError("Empty");
            description.requestFocus();
        }else if (howtoapply.isEmpty()){
            howApply.setError("Empty");
            howApply.requestFocus();
        }else if (advlinks.isEmpty()){
            advLink.setError("Empty");
            advLink.requestFocus();
        }else if (applylinks.isEmpty()){
            applyLink.setError("Empty");
            applyLink.requestFocus();
        }else if (bitmap==null){
            updateData(image);
        }else {
            uploadImage();
        }

    }

    private void uploadImage() {
        pd.setMessage("Uploading..");
        pd.setCanceledOnTouchOutside(false);
        pd.show();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,50,baos);
        byte[] finalimg = baos.toByteArray();
        final StorageReference filePath;
        filePath = storageReference.child("jobs").child(finalimg+"jpg");
        final UploadTask uploadTask= filePath.putBytes(finalimg);
        uploadTask.addOnCompleteListener(EditJobNewsActivity.this, new OnCompleteListener<UploadTask.TaskSnapshot>() {
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
                                    updateData(downloadUrl);
                                }
                            });
                        }
                    });
                }else {
                    pd.dismiss();
                    Toast.makeText(EditJobNewsActivity.this, "Something went wrong. Please try again.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void updateData(String s) {
        pd.setMessage("Uploading");
        pd.show();
        pd.setCanceledOnTouchOutside(false);

        HashMap hp = new HashMap();
        hp.put("jobTitle",title);
        hp.put("noPosts",post);
        hp.put("jobDescriptions",desc);
        hp.put("howApply",howtoapply);
        hp.put("advLinks",advlinks);
        hp.put("applyLinks",applylinks);
        hp.put("image",s);


        reference.child(uniqueKey).updateChildren(hp).addOnSuccessListener(new OnSuccessListener() {
            @Override
            public void onSuccess(Object o) {
                Toast.makeText(EditJobNewsActivity.this, "Job updated", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(EditJobNewsActivity.this,MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                pd.dismiss();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                Toast.makeText(EditJobNewsActivity.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
            }
        });
    }




    private void openGalley(){
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
            editJobImage.setImageBitmap(bitmap);
        }
    }
}