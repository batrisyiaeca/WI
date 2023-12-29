package com.example.wi;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdminFAQAdapter extends FirebaseRecyclerAdapter<FAQModel, AdminFAQAdapter.MyHolder> {

    public AdminFAQAdapter(@NonNull FirebaseRecyclerOptions<FAQModel> options) {
        super(options);
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_view_faq, parent, false);
        CardView cardView = view.findViewById(R.id.cardView);
        cardView.setClickable(false);
        cardView.setFocusable(false);
        return new MyHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull MyHolder holder, final int position, @NonNull FAQModel model) {
        holder.showQue.setText(model.getQuestion());
        holder.showAns.setText(model.getAnswer());

        holder.editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DialogPlus dialogPlus =DialogPlus.newDialog(holder.showQue.getContext())
                        .setContentHolder(new ViewHolder(R.layout.admin_update_faq))
                        .setExpanded(true,1200)
                        .create();

                //dialogPlus.show();

                View view = dialogPlus.getHolderView();

                EditText que = view.findViewById(R.id.txtQuestion);
                EditText ans = view.findViewById(R.id.txtAnswer);

                Button updateBtn = view.findViewById(R.id.updateBtn);

                que.setText(model.getQuestion());
                ans.setText(model.getAnswer());

                dialogPlus.show();

                updateBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Map<String, Object> map = new HashMap<>();
                        map.put("question", que.getText().toString());
                        map.put("answer", ans.getText().toString());

                        FirebaseDatabase.getInstance().getReference().child("FAQ")
                                .child(getRef(position).getKey()).updateChildren(map)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(holder.showQue.getContext(), "FAQ updated successfully", Toast.LENGTH_SHORT).show();
                                        dialogPlus.dismiss();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(holder.showQue.getContext(), "Update Error", Toast.LENGTH_SHORT).show();
                                        dialogPlus.dismiss();
                                    }
                                });
                    }
                });
            }
        });

        holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(holder.showQue.getContext());
                builder.setTitle("Are you sure you want to delete?");
                builder.setMessage("Data deletion cannot be undone");

                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseDatabase.getInstance().getReference().child("FAQ")
                                .child(getRef(position).getKey()).removeValue();
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(holder.showQue.getContext(), "Cancelled", Toast.LENGTH_SHORT).show();
                    }
                });

                builder.show();
            }
        });
    }

    class MyHolder extends RecyclerView.ViewHolder {
        TextView showQue, showAns;
        ImageButton editBtn, deleteBtn;

        public MyHolder(@NonNull View itemView) {
            super(itemView);

            showQue = itemView.findViewById(R.id.showQuestion);
            showAns = itemView.findViewById(R.id.showAnswer);
            editBtn = itemView.findViewById(R.id.editFAQBtn);
            deleteBtn = itemView.findViewById(R.id.deleteFAQBtn);

            itemView.setOnClickListener(view -> {
                Intent AdminAddFaqActivity = new Intent(view.getContext(), AdminFaqs.class);
                int position = getBindingAdapterPosition();

                AdminAddFaqActivity.putExtra("question", getItem(position).getQuestion());
                AdminAddFaqActivity.putExtra("answer", getItem(position).getAnswer());

                view.getContext().startActivity(AdminAddFaqActivity);
                AdminAddFaqActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            });
        }
    }
}
