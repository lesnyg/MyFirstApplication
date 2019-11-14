package com.example.myfirstapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ServiceAdapter extends RecyclerView.Adapter<ServiceAdapter.ServiceHolder> {
    interface ServiceClickListener {
        void setServiceClick(String serviceDate);
    }

    private ServiceClickListener mListener;

    private List<String> mItems = new ArrayList<>();

    public ServiceAdapter() {
    }

    public ServiceAdapter(ServiceClickListener listener) {
        mListener = listener;
    }

    public void setItems(List<String> items) {
        this.mItems = items;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ServiceHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_servicelist, parent, false);
        final ServiceHolder viewHolder = new ServiceHolder(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.setServiceClick(mItems.get(viewHolder.getAdapterPosition()));
                }
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ServiceHolder holder, int position) {
        String date = mItems.get(position);

        holder.tv_servicedate.setText(date);
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public static class ServiceHolder extends RecyclerView.ViewHolder {
        TextView tv_servicedate;

        public ServiceHolder(@NonNull View itemView) {
            super(itemView);
            tv_servicedate = itemView.findViewById(R.id.tv_servicedate);
        }
    }
}
