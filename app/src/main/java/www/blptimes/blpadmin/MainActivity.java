package www.blptimes.blpadmin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import www.blptimes.blpadmin.deleteNews.DeleteNewsActivity;
import www.blptimes.blpadmin.jobs.UploadJobsNewsActivity;
import www.blptimes.blpadmin.jobs.ViewJobsActivity;
import www.blptimes.blpadmin.login.LoginActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private CardView addNews, deleteNews, addJobNews, viewJobNews;
    private Button logout;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = this.getSharedPreferences("login",MODE_PRIVATE);
        editor = sharedPreferences.edit();

        if (sharedPreferences.getString("isLogin","false").equals("false")){
            openLogin();
        }
        addNews = findViewById(R.id.addNews);
        deleteNews = findViewById(R.id.deleteNews);
        addJobNews = findViewById(R.id.addJobNews);
        viewJobNews = findViewById(R.id.viewJobNews);
        logout = findViewById(R.id.btnLogout);


        addNews.setOnClickListener(this::onClick);
        deleteNews.setOnClickListener(this::onClick);
        addJobNews.setOnClickListener(this::onClick);
        viewJobNews.setOnClickListener(this::onClick);
        logout.setOnClickListener(this::onClick);
    }

    private void openLogin() {
        startActivity(new Intent(this, LoginActivity.class));
        finish();
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

            case R.id.btnLogout:
                editor.putString("isLogin","false");
                editor.commit();
                openLogin();
                break;
        }

    }
}