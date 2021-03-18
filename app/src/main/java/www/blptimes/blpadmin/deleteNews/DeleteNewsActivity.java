package www.blptimes.blpadmin.deleteNews;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import www.blptimes.blpadmin.R;
import www.blptimes.blpadmin.news.NewsData;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DeleteNewsActivity extends AppCompatActivity {

    private RecyclerView deleteNewsRecycler;
    private ProgressBar progressBar;
    private ArrayList<NewsData> list;
    private NewsAdapter adapter;

    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_news);

        deleteNewsRecycler = findViewById(R.id.deleteNewsRecycler);
        progressBar = findViewById(R.id.progressBar);

        reference = FirebaseDatabase.getInstance().getReference().child("news");


        deleteNewsRecycler.setLayoutManager(new LinearLayoutManager(this));
        deleteNewsRecycler.setHasFixedSize(true);
        getNews();
    }

    private void getNews() {
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list = new ArrayList<>();
                for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                    NewsData data = snapshot.getValue(NewsData.class);
                    list.add(0,data);
                }

                adapter = new NewsAdapter(DeleteNewsActivity.this,list);
                adapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);
                deleteNewsRecycler.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(DeleteNewsActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}