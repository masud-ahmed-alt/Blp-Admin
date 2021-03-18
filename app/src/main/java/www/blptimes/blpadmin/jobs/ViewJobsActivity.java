package www.blptimes.blpadmin.jobs;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import www.blptimes.blpadmin.R;

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

public class ViewJobsActivity extends AppCompatActivity {

    private RecyclerView viewJobsRecycler;
    private ProgressBar progressBar;
    private ArrayList<JobsData> list;
    private JobsAdapter adapter;

    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_jobs);

        viewJobsRecycler = findViewById(R.id.jobsViewRecycler);
        progressBar = findViewById(R.id.progressBarViewNews);

        reference = FirebaseDatabase.getInstance().getReference().child("jobs");


        viewJobsRecycler.setLayoutManager(new LinearLayoutManager(this));
        viewJobsRecycler.setHasFixedSize(true);
        getNews();
    }

    private void getNews() {
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list = new ArrayList<>();
                for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                    JobsData data = snapshot.getValue(JobsData.class);
                    list.add(0,data);
                }

                adapter = new JobsAdapter(ViewJobsActivity.this,list);
                adapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);
                viewJobsRecycler.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(ViewJobsActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}