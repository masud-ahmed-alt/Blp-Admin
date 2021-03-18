package www.blptimes.blpadmin.deleteNews;

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
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import www.blptimes.blpadmin.R;
import www.blptimes.blpadmin.news.NewsData;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewAdapter> {

    private Context context;
    private ArrayList<NewsData> list;

    public NewsAdapter(Context context, ArrayList<NewsData> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public NewsViewAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.news_feed_item_layout, parent, false);

        return new NewsViewAdapter(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewAdapter holder, int position) {

        NewsData currentItem = list.get(position);
        holder.deleteNewsHeadline.setText(currentItem.getHeadLine());
        holder.deleteNewsSubHeadline.setText(currentItem.getSubHeadline());

        try {
            if (currentItem.getImage() != null)
                Picasso.get().load(currentItem.getImage()).into(holder.deleteNewsImage);
        } catch (Exception e) {
            e.printStackTrace();
        }

        holder.deleteNewsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("news");
                reference.child(currentItem.getKey()).removeValue()
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(context, "News Deleted", Toast.LENGTH_SHORT).show();
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

        holder.editNewsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, EditNewsActivity.class);

                intent.putExtra("headline", currentItem.getHeadLine());
                intent.putExtra("subHeadline", currentItem.getSubHeadline());
                intent.putExtra("mainNews", currentItem.getNews());
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

    public class NewsViewAdapter extends RecyclerView.ViewHolder {

        private TextView deleteNewsHeadline, deleteNewsSubHeadline;
        private Button deleteNewsBtn, editNewsBtn;
        private ImageView deleteNewsImage;

        public NewsViewAdapter(@NonNull View itemView) {
            super(itemView);

            deleteNewsHeadline = itemView.findViewById(R.id.deleteNewsHeadline);
            deleteNewsSubHeadline = itemView.findViewById(R.id.deleteNewsSubHeadline);
            deleteNewsBtn = itemView.findViewById(R.id.deleteNewsBtn);
            deleteNewsImage = itemView.findViewById(R.id.deleteNewsImage);
            editNewsBtn = itemView.findViewById(R.id.editNewsBtn);
        }
    }
}
