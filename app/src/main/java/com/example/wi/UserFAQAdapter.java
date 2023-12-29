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

import com.example.wi.AdminFaqs;
import com.example.wi.FAQModel;
import com.example.wi.R;
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

public class UserFAQAdapter extends FirebaseRecyclerAdapter<FAQModel, UserFAQAdapter.MyHolder> {

    public UserFAQAdapter(@NonNull FirebaseRecyclerOptions<FAQModel> options) {
        super(options);
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_view_faq, parent, false);
        CardView cardView = view.findViewById(R.id.cardView);
        cardView.setClickable(false);
        cardView.setFocusable(false);
        return new MyHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull MyHolder holder, final int position, @NonNull FAQModel model) {
        holder.showQue.setText(model.getQuestion());
        holder.showAns.setText(model.getAnswer());
    }

    class MyHolder extends RecyclerView.ViewHolder {
        TextView showQue, showAns;

        public MyHolder(@NonNull View itemView) {
            super(itemView);

            showQue = itemView.findViewById(R.id.showQuestion);
            showAns = itemView.findViewById(R.id.showAnswer);

            // Check if the item is clickable before setting OnClickListener
            if (!itemView.isClickable()) {
                return;
            }

            itemView.setOnClickListener(view -> {
                // Your existing code for starting a new activity
                Intent UserFaqActivity = new Intent(view.getContext(), AdminFaqs.class);
                int position = getBindingAdapterPosition();

                UserFaqActivity.putExtra("question", getItem(position).getQuestion());
                UserFaqActivity.putExtra("answer", getItem(position).getAnswer());

                view.getContext().startActivity(UserFaqActivity);
                UserFaqActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            });
        }
    }
}
