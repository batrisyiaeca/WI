package com.example.wi.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wi.R;
import com.example.wi.models.HealthInfo;

import java.util.List;

public class HealthInfoAdapter extends RecyclerView.Adapter<HealthInfoAdapter.HealthViewHolder> {

    private List<HealthInfo> healthInfoList;
    private Context context;
    private OnItemClickListener itemClickListener;

    public HealthInfoAdapter(Context context, List<HealthInfo> healthInfoList) {
        this.context = context;
        this.healthInfoList = healthInfoList;
    }

    public HealthInfoAdapter(List<HealthInfo> healthInfoList) {
    }

    @NonNull
    @Override
    public HealthViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_health_info, parent, false);
        return new HealthViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HealthViewHolder holder, int position) {
        HealthInfo healthInfo = healthInfoList.get(position);
        holder.bind(healthInfo);
    }

    @Override
    public int getItemCount() {
        return healthInfoList.size();
    }

    public void setOnItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(int position, View view);
    }

    public class HealthViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView weightTextView;
        private final TextView heightTextView;
        private final TextView bloodPressureTextView;
        private final TextView heartRateTextView;
        private final TextView dateTextView;

        public HealthViewHolder(@NonNull View itemView) {
            super(itemView);
            weightTextView = itemView.findViewById(R.id.textWeight);
            heightTextView = itemView.findViewById(R.id.textHeight);
            bloodPressureTextView = itemView.findViewById(R.id.textBloodPressure);
            heartRateTextView = itemView.findViewById(R.id.textHeartRate);
            dateTextView = itemView.findViewById(R.id.textDate);

            itemView.setOnClickListener(this);
        }

        @SuppressLint("SetTextI18n")
        public void bind(HealthInfo healthInfo) {
            weightTextView.setText("Weight: " + healthInfo.getWeight());
            heightTextView.setText("Height: " + healthInfo.getHeight());
            bloodPressureTextView.setText("Blood Pressure: " + healthInfo.getBloodPressure());
            heartRateTextView.setText("Heart Rate: " + healthInfo.getHeartRate());
            dateTextView.setText("Date: " + healthInfo.getDate());
        }

        @Override
        public void onClick(View v) {
            if (itemClickListener != null) {
                itemClickListener.onItemClick(getAdapterPosition(), v);
            }
        }
    }
}

