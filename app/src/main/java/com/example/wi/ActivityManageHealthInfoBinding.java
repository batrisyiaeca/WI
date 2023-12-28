package com.example.wi;

import android.view.LayoutInflater;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

// Your class definition
public class ActivityManageHealthInfoBinding {
    public RecyclerView recyclerViewHealthInfo;
    public View btnAddHealthInfo;
    public RecyclerView recycler;
    private byte root;

    public ActivityManageHealthInfoBinding(View rootView) {
        recyclerViewHealthInfo = rootView.findViewById(R.id.recyclerViewHealthInfo);
        btnAddHealthInfo = rootView.findViewById(R.id.btnAddHealthInfo);
    }

    public static ActivityManageHealthInfoBinding inflate(LayoutInflater layoutInflater) {
        return null;
    }

    public byte getRoot() {
        return root;
    }

    public void setRoot(byte root) {
        this.root = root;
    }
}
