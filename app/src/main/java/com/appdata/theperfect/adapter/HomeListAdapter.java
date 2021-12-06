package com.appdata.theperfect.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.appdata.theperfect.R;
import com.appdata.theperfect.databinding.RowHomeBinding;
import com.appdata.theperfect.model.ModelSelllerListData;
import com.bumptech.glide.Glide;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class HomeListAdapter extends RecyclerView.Adapter<HomeListAdapter.ViewHolder> {

    private final Context context;
    private final OnItemClick onItemClick;
    private ArrayList<ModelSelllerListData> arrayList;

    public HomeListAdapter(Context context, ArrayList<ModelSelllerListData> arrayList, OnItemClick onItemClick) {
        this.context = context;
        this.onItemClick = onItemClick;
        this.arrayList = arrayList;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        RowHomeBinding binding = DataBindingUtil.bind(LayoutInflater.from(context)
                .inflate(R.layout.row_home, parent, false));
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        /*ModelArticleInnerData model = arrayList.get(position);
        holder.binding.tvTitle.setText(model.getTitle());
        aideGuide02ListAdapter = new AideGuide02ListAdapter(context, model.getItem(),
                pos -> onItemClick.onListItemClick(position, model.getItem().get(pos)));

        holder.binding.recyclerView.setAdapter(aideGuide02ListAdapter);*/

        holder.binding.viewBlank.setVisibility(position == 0 ? View.VISIBLE : View.GONE);
        holder.itemView.setOnClickListener(view -> onItemClick.onItemClick(position));
        Glide.with(context).load(arrayList.get(position).getProfileimage()).into(holder.binding.ivUser);
        holder.binding.tvName.setText(arrayList.get(position).getName());
        holder.binding.tvTown.setText(arrayList.get(position).getTown());
        if (arrayList.get(position).getRatingStar() != null) {
            holder.binding.ratingBar.setVisibility(View.VISIBLE);
            holder.binding.ratingBar.setRating(Float.parseFloat(arrayList.get(position).getRatingStar()));
        }else
            holder.binding.ratingBar.setVisibility(View.GONE);
       /* if(position==0) {
            holder.binding.ivUser.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.demo_girl1));
          //  holder.binding.tvName.setText(R.string.txt_hygiene);
        }else if(position==1) {
            holder.binding.ivUser.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.demo_man1));
           // holder.binding.tvName.setText(R.string.txt_planning);
        }else if(position==2) {
            holder.binding.ivUser.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.demo_girl2));
           // holder.binding.tvName.setText(R.string.txt_commandes);
        }else if(position==3) {
            holder.binding.ivUser.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.demo_man2));
          //  holder.binding.tvName.setText(R.string.txt_inventories);
        }else if(position==4) {
            holder.binding.ivUser.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.demo_man3));
          //  holder.binding.tvName.setText(R.string.txt_exit);
        }else if(position==5) {
            holder.binding.ivUser.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.demo_girl1));
           // holder.binding.tvName.setText(R.string.txt_exit);
        }else if(position==6) {
            holder.binding.ivUser.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.demo_man1));
          //  holder.binding.tvName.setText(R.string.txt_exit);
        }else if(position==7) {
            holder.binding.ivUser.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.demo_girl2));
          //  holder.binding.tvName.setText(R.string.txt_exit);
        }else if(position==8) {
            holder.binding.ivUser.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.demo_man2));
         //   holder.binding.tvName.setText(R.string.txt_exit);
        }else if(position==9) {
            holder.binding.ivUser.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.demo_man3));
          //  holder.binding.tvName.setText(R.string.txt_exit);
        }else if(position==10) {
            holder.binding.ivUser.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.demo_girl1));
         //   holder.binding.tvName.setText(R.string.txt_exit);
        }else if(position==11) {
            holder.binding.ivUser.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.demo_man1));
          //  holder.binding.tvName.setText(R.string.txt_exit);
        }*/
    }

    @Override
    public int getItemCount() {
//        return 12;
        return arrayList.size();
    }

    public interface OnItemClick {
        void onItemClick(int position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        RowHomeBinding binding;

        ViewHolder(RowHomeBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public void refreshData(ArrayList<ModelSelllerListData> arrayList) {
        this.arrayList = arrayList;
        notifyDataSetChanged();
    }

}