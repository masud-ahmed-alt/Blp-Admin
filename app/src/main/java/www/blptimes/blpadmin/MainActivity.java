package www.blptimes.blpadmin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import www.blptimes.blpadmin.deleteNews.DeleteNewsActivity;
import www.blptimes.blpadmin.jobs.UploadJobsNewsActivity;
import www.blptimes.blpadmin.jobs.ViewJobsActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private CardView addNews, deleteNews, addJobNews, viewJobNews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addNews = findViewById(R.id.addNews);
        deleteNews = findViewById(R.id.deleteNews);
        addJobNews = findViewById(R.id.addJobNews);
        viewJobNews = findViewById(R.id.viewJobNews);


        addNews.setOnClickListener(this::onClick);
        deleteNews.setOnClickListener(this::onClick);
        addJobNews.setOnClickListener(this::onClick);
        viewJobNews.setOnClickListener(this::onClick);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {

        Intent intent;
        switch (v.getId()){
            case R.id.addNews:
            intent = new Intent(MainActivity.this,UploadNewsActivity.class);
            startActivity(intent);
            break;

            case R.id.deleteNews:
                intent = new Intent(MainActivity.this, DeleteNewsActivity.class);
                startActivity(intent);
                break;

                case R.id.addJobNews:
                intent = new Intent(MainActivity.this, UploadJobsNewsActivity.class);
                startActivity(intent);
                break;

            case R.id.viewJobNews:
                intent = new Intent(MainActivity.this, ViewJobsActivity.class);
                startActivity(intent);
                break;
        }

    }
}