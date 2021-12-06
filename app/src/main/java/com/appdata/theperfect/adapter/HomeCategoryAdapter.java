package com.appdata.theperfect.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.appdata.theperfect.R;
import com.appdata.theperfect.databinding.RowCategoryHomeBinding;
import com.appdata.theperfect.databinding.RowHomeBinding;
import com.appdata.theperfect.model.ModelCategoryData;
import com.bumptech.glide.Glide;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class HomeCategoryAdapter extends RecyclerView.Adapter<HomeCategoryAdapter.ViewHolder> {

    private final Context context;
    private final OnItemClick onItemClick;
    private ArrayList<ModelCategoryData> arrayList;

    public HomeCategoryAdapter(Context context, ArrayList<ModelCategoryData> arrayList, OnItemClick onItemClick) {
        this.context = context;
        this.onItemClick = onItemClick;
        this.arrayList = arrayList;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        RowCategoryHomeBinding binding = DataBindingUtil.bind(LayoutInflater.from(context)
                .inflate(R.layout.row_category_home, parent, false));
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        /*ModelArticleInnerData model = arrayList.get(position);
        holder.binding.tvTitle.setText(model.getTitle());
        aideGuide02ListAdapter = new AideGuide02ListAdapter(context, model.getItem(),
                pos -> onItemClick.onListItemClick(position, model.getItem().get(pos)));

        holder.binding.recyclerView.setAdapter(aideGuide02ListAdapter);*/

//        holder.binding.viewBlank.setVisibility(position==0? View.VISIBLE:View.GONE);






        holder.itemView.setOnClickListener(view -> onItemClick.onItemClick(position));

        Glide.with(context).load(arrayList.get(position).getIconImage()).into(holder.binding.ivImage);
        holder.binding.tvName.setText(arrayList.get(position).getCategoryName());


       /* if(position==0) {
            holder.binding.ivImage.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.demo_a8));
            holder.binding.tvName.setText("Real Estate");
        }else if(position==1) {
            holder.binding.ivImage.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.demo_a7));
            holder.binding.tvName.setText("Car & Truck");
        }else if(position==2) {
            holder.binding.ivImage.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.demo_a6));
            holder.binding.tvName.setText("R.V");
        }else if(position==3) {
            holder.binding.ivImage.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.demo_a3));
            holder.binding.tvName.setText("Motorcycles");
        }else if(position==4) {
            holder.binding.ivImage.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.demo_a5));
            holder.binding.tvName.setText("Quads");
        }else if(position==5) {
            holder.binding.ivImage.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.demo_a2));
            holder.binding.tvName.setText("Dirt Bikes");
        }else if(position==6) {
            holder.binding.ivImage.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.demo_a1));
            holder.binding.tvName.setText("Boats");
        }else if(position==7) {
            holder.binding.ivImage.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_jewellery));
            holder.binding.tvName.setText("Jewellery");
        }else if(position==8) {
            holder.binding.ivImage.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_cell_phone));
            holder.binding.tvName.setText("Cell Phone");
        }*/
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
//        return 9;
    }

    public interface OnItemClick {
        void onItemClick(int position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        RowCategoryHomeBinding binding;

        ViewHolder(RowCategoryHomeBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public void setData(ArrayList<ModelCategoryData> arrayList){
        this.arrayList=arrayList;
        notifyDataSetChanged();
    }

}