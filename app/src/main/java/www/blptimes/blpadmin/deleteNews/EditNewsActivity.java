package www.blptimes.blpadmin.deleteNews;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import www.blptimes.blpadmin.MainActivity;
import www.blptimes.blpadmin.R;
import www.blptimes.blpadmin.jobs.EditJobNewsActivity;

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
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;

public class EditNewsActivity extends AppCompatActivity {

    private ImageView editNewsImageView;
    private EditText editNewsHeadline, editNewsSubHeadline, editMainNews;
    private Button uploadEditNewsBtn;



    private String headline,subHeadline, mainNews,image;

    private final int REQ = 1;
    private Bitmap bitmap=null;
    private String downloadUrl, uniqueKey;

    private ProgressDialog pd;

    private StorageReference storageReference;
    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_news);

        editNewsImageView = findViewById(R.id.editNewsImage);
        editNewsHeadline = findViewById(R.id.editNewsHeadline);
        editNewsSubHeadline = findViewById(R.id.editNewsSubHeadline);
        editMainNews = findViewById(R.id.editMainNews);
        uploadEditNewsBtn = findViewById(R.id.editNewsBtn);

        reference = FirebaseDatabase.getInstance().getReference().child("news");
        storageReference = FirebaseStorage.getInstance().getReference();

        pd = new ProgressDialog(this);

        headline = getIntent().getStringExtra("headline");
        subHeadline = getIntent().getStringExtra("subHeadline");
        mainNews = getIntent().getStringExtra("mainNews");
        image = getIntent().getStringExtra("image");
        uniqueKey = getIntent().getStringExtra("uniqueKey");



        try {
            Picasso.get().load(image).into(editNewsImageView);
        } catch (Exception e) {
            e.printStackTrace();
        }

        editNewsHeadline.setText(headline);
        editNewsSubHeadline.setText(subHeadline);
        editMainNews.setText(mainNews);


        editNewsImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGalley();
            }
        });

        uploadEditNewsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                headline = editNewsHeadline.getText().toString();
                subHeadline = editNewsSubHeadline.getText().toString();
                mainNews = editMainNews.getText().toString();
                checkValidation();
            }
        });
    }

    private void checkValidation() {

        if (headline.isEmpty()){
            editNewsHeadline.setError("Empty");
            editNewsHeadline.requestFocus();
        }else if (subHeadline.isEmpty()){
            editNewsSubHeadline.setError("Empty");
            editNewsSubHeadline.requestFocus();
        }else if (mainNews.isEmpty()){
            editMainNews.setError("Empty");
            editMainNews.requestFocus();
        }else if (bitmap==null){
            updateData(image);
        }else {
            uploadImage();
        }

    }

    private void uploadImage() {
        pd.setMessage("Uploading");
        pd.setCanceledOnTouchOutside(false);
        pd.show();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,50,baos);
        byte[] finalimg = baos.toByteArray();
        final StorageReference filePath;
        filePath = storageReference.child("news").child(finalimg+"jpg");
        final UploadTask uploadTask= filePath.putBytes(finalimg);
        uploadTask.addOnCompleteListener(EditNewsActivity.this, new OnCompleteListener<UploadTask.TaskSnapshot>() {
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
                                    pd.dismiss();
                                }
                            });
                        }
                    });
                }else {
                    pd.dismiss();
                    Toast.makeText(EditNewsActivity.this, "Something went wrong. Please try again.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void updateData(String s) {

        pd.setMessage("Uploading...");
        pd.setCanceledOnTouchOutside(false);
        pd.show();

        HashMap hp = new HashMap();
        hp.put("headLine",headline);
        hp.put("subHeadline",subHeadline);
        hp.put("news",mainNews);
        hp.put("image",s);


        reference.child(uniqueKey).updateChildren(hp).addOnSuccessListener(new OnSuccessListener() {
            @Override
            public void onSuccess(Object o) {
                Toast.makeText(EditNewsActivity.this, "News updated", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(EditNewsActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                pd.dismiss();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                Toast.makeText(EditNewsActivity.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
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
            editNewsImageView.setImageBitmap(bitmap);
        }
    }

}