package www.blptimes.blpadmin.jobs;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import www.blptimes.blpadmin.R;


public class JobsAdapter extends RecyclerView.Adapter<JobsAdapter.JobsViewAdapter> {

    private Context context;
    private ArrayList<JobsData> list;

    public JobsAdapter(Context context, ArrayList<JobsData> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public JobsViewAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_jobs, parent, false);

        return new JobsViewAdapter(view);
    }

    @Override
    public void onBindViewHolder(@NonNull JobsViewAdapter holder, int position) {


        JobsData currentItem = list.get(position);

        holder.jobsTitle.setText(currentItem.getJobTitle());
        holder.jobsPosts.setText("Post :  "+currentItem.getNoPosts());

        try {
            Picasso.get().load(currentItem.getImage()).into(holder.jobsImage);
        } catch (Exception e) {
            e.printStackTrace();
        }

        holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("jobs");
                reference.child(currentItem.getKey()).removeValue()
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(context, "Job Deleted", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, "Something went wrong. Please try again.", Toast.LENGTH_SHORT).show();
                    }
                });
                notifyItemRemoved(position);
            }
        });
        holder.editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,EditJobNewsActivity.class);

                intent.putExtra("title", currentItem.getJobTitle());
                intent.putExtra("post", currentItem.getNoPosts());
                intent.putExtra("desc", currentItem.getJobDescriptions());
                intent.putExtra("howtoapply", currentItem.getHowApply());
                intent.putExtra("advlinks", currentItem.getAdvLinks());
                intent.putExtra("applylinks", currentItem.getApplyLinks());
                intent.putExtra("image", currentItem.getImage());
                intent.putExtra("uniqueKey", currentItem.getKey());
                context.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class JobsViewAdapter extends RecyclerView.ViewHolder {

        private ImageView jobsImage;
        private TextView jobsTitle, jobsPosts;
        private Button editBtn, deleteBtn;

        public JobsViewAdapter(@NonNull View itemView) {
            super(itemView);


            jobsImage = itemView.findViewById(R.id.jobsImageView);
            jobsTitle = itemView.findViewById(R.id.jobsViewTitle);
            jobsPosts = itemView.findViewById(R.id.jobsViewPosts);
            editBtn = itemView.findViewById(R.id.editBtn);
            deleteBtn = itemView.findViewById(R.id.deleteBtn);
        }
    }
}
