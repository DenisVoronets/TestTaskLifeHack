package com.example.testtasklifehackstudio;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import java.util.List;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {
    private List<CompanyVisitCard> companyVisitCardList;
    private Context mContext;

    RecyclerViewAdapter(List<CompanyVisitCard> catList, Context context) {
        this.companyVisitCardList = catList;
        this.mContext = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_view_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
        final CompanyVisitCard companyVisitCard = companyVisitCardList.get(position);
        Animation animBounce = AnimationUtils.loadAnimation(mContext, R.anim.line_move);
        holder.mNameTextView.setText(companyVisitCard.getCompanyName());
        holder.imageViewVisitCard.startAnimation(animBounce);
        Picasso.get().load(companyVisitCard.getCompanyImage()).into(holder.imageViewVisitCard);

        holder.mNameTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext,CardDescription.class);
                intent.putExtra("id",companyVisitCard.getId());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return companyVisitCardList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView mNameTextView;
        private ImageView imageViewVisitCard;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            AssetManager assetManager = mContext.getAssets();
            mNameTextView = itemView.findViewById(R.id.textViewLarge);
            mNameTextView.setTypeface(Typeface.createFromAsset(assetManager, "fonts/textstyle.ttf"));
            imageViewVisitCard = itemView.findViewById(R.id.imageView_visit_card);
        }
    }
}
