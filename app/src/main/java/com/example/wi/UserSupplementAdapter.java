package com.example.wi;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserSupplementAdapter extends RecyclerView.Adapter<UserSupplementAdapter.MyHolder>{

    Context context;
    List<SupplementModel> supplementModels;

    public UserSupplementAdapter(Context context, List<SupplementModel> supplementModelList) {
        this.context = context;
        this.supplementModels = supplementModelList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.show_supplement, parent , false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {

        String name = supplementModels.get(position).getSupName();
        String imageSup = supplementModels.get(position).getSupImage();

        holder.showName.setText(name);
        Glide.with(context).load(imageSup).into(holder.ImageView);

    }

    @Override
    public int getItemCount() {
        return supplementModels.size();
    }

    class MyHolder extends RecyclerView.ViewHolder{

        TextView showName;
        CircleImageView ImageView;

        public MyHolder(@NonNull View itemView) {
            super(itemView);

            showName = itemView.findViewById(R.id.txtShowSupplementName);
            ImageView = itemView.findViewById(R.id.sup_ImageView);

            itemView.setOnClickListener(view -> {
                Intent UserSupplementDetailActivity = new Intent(context,UserSupplementDetailActivity.class);
                int position = getAdapterPosition();

                UserSupplementDetailActivity.putExtra("SupId",supplementModels.get(position).getSupId());
                UserSupplementDetailActivity.putExtra("SupImage",supplementModels.get(position).getSupImage());

                UserSupplementDetailActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(UserSupplementDetailActivity);

            });

        }
    }

}


