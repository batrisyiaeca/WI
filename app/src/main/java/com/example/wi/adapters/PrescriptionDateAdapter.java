package com.example.wi.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wi.AddPrescriptionDateActivity;
import com.example.wi.R;

import java.util.List;

public class PrescriptionDateAdapter extends RecyclerView.Adapter<PrescriptionDateAdapter.PrescriptionDateViewHolder> {

    private List<AddPrescriptionDateActivity.PrescriptionDate> prescriptionDateList;

    public PrescriptionDateAdapter(List<AddPrescriptionDateActivity.PrescriptionDate> prescriptionDateList) {
        this.prescriptionDateList = prescriptionDateList;
    }
    @NonNull
    @Override
    public PrescriptionDateViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_prescription_date, parent, false);
        return new PrescriptionDateViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PrescriptionDateViewHolder holder, int position) {
        AddPrescriptionDateActivity.PrescriptionDate prescriptionDate = prescriptionDateList.get(position);
        holder.bind(prescriptionDate);
    }

    @Override
    public int getItemCount() {
        return prescriptionDateList.size();
    }

    public class PrescriptionDateViewHolder extends RecyclerView.ViewHolder {

        private final TextView titleTextView;
        private final TextView descriptionTextView;
        private final TextView dateTextView;
        private final TextView condition1TextView;
        private final TextView condition2TextView;

        public PrescriptionDateViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.textTitle);
            descriptionTextView = itemView.findViewById(R.id.textDescription);
            dateTextView = itemView.findViewById(R.id.textDate);
            condition1TextView = itemView.findViewById(R.id.textCondition1);
            condition2TextView = itemView.findViewById(R.id.textCondition2);
        }

        public void bind(AddPrescriptionDateActivity.PrescriptionDate prescriptionDate) {
            titleTextView.setText("Title: " + prescriptionDate.getTitle());
            descriptionTextView.setText("Description: " + prescriptionDate.getDescription());
            dateTextView.setText("Date: " + prescriptionDate.getDate());
            condition1TextView.setText("Condition 1: " + prescriptionDate.getCondition1());
            condition2TextView.setText("Condition 2: " + prescriptionDate.getCondition2());
        }
    }
}
