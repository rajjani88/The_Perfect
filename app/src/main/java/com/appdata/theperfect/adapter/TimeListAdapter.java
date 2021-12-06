package com.appdata.theperfect.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.appdata.theperfect.R;
import com.appdata.theperfect.databinding.RowSelectDateBinding;
import com.appdata.theperfect.model.ModelAppointmentTimeData;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class TimeListAdapter extends RecyclerView.Adapter<TimeListAdapter.ViewHolder> {

    private final Context context;
    private final OnItemClick onItemClick;
    private ArrayList<ModelAppointmentTimeData> arrayList;
    private int row_index=-1;

    public TimeListAdapter(Context context, ArrayList<ModelAppointmentTimeData> arrayList, OnItemClick onItemClick) {
        this.context = context;
        this.onItemClick = onItemClick;
        this.arrayList = arrayList;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        RowSelectDateBinding binding = DataBindingUtil.bind(LayoutInflater.from(context)
                .inflate(R.layout.row_select_date, parent, false));
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.binding.viewBlank.setVisibility(position==0? View.GONE:View.VISIBLE);
        holder.itemView.setOnClickListener(view -> onItemClick.onItemClick(position));
        holder.binding.tvTime.setText(arrayList.get(position).getTimeSlot());

        if(arrayList.get(position).isSelected())
            row_index=position;

        holder.binding.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                row_index=position;
                for (int i = 0; i <arrayList.size() ; i++) {
                    arrayList.get(i).setSelected(i == position);
                }
                notifyDataSetChanged();
            }
        });

        if(row_index==position) {
            holder.binding.cardView.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.color_pink));
        }else {
            holder.binding.cardView.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.white));
        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public interface OnItemClick {
        void onItemClick(int position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        RowSelectDateBinding binding;

        ViewHolder(RowSelectDateBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public void refreshData(ArrayList<ModelAppointmentTimeData> arrayList){
        this.arrayList=arrayList;
        notifyDataSetChanged();
    }

}