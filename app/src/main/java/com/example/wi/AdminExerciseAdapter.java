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

public class AdminExerciseAdapter extends RecyclerView.Adapter<AdminExerciseAdapter.MyHolder>{

    Context context;
    List<ExerciseModel> exerciseModels;

    public AdminExerciseAdapter(Context context, List<ExerciseModel> exerciseModelList) {
        this.context = context;
        this.exerciseModels = exerciseModelList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.show_exercise, parent , false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        if (exerciseModels != null && position < exerciseModels.size()) {
            String name = exerciseModels.get(position).getExName();
            String imageEx = exerciseModels.get(position).getExImage();

            holder.showName.setText(name);
            Glide.with(context).load(imageEx).into(holder.ImageView);
        }
    }

    @Override
    public int getItemCount() {
        return exerciseModels.size();
    }

    class MyHolder extends RecyclerView.ViewHolder{

        TextView showName;
        CircleImageView ImageView;

        public MyHolder(@NonNull View itemView) {
            super(itemView);

            showName = itemView.findViewById(R.id.txtShowExercise);
            ImageView = itemView.findViewById(R.id.ex_ImageView);

            itemView.setOnClickListener(view -> {
                Intent AdminUpdateExercise = new Intent(context,AdminUpdateExercise.class);
                int position = getAdapterPosition();

                AdminUpdateExercise.putExtra("ExId",exerciseModels.get(position).getExId());
                AdminUpdateExercise.putExtra("ExImage",exerciseModels.get(position).getExImage());

                AdminUpdateExercise.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(AdminUpdateExercise);

            });

        }
    }

}


