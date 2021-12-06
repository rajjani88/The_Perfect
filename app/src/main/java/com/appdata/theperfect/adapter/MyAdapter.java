package com.appdata.theperfect.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.appdata.theperfect.R;
import com.appdata.theperfect.model.ModelCategoryData;
import com.appdata.theperfect.model.StateVO;

import java.util.ArrayList;
import java.util.List;

public class MyAdapter extends ArrayAdapter<ModelCategoryData> {
    private Context mContext;
    private MyAdapter myAdapter;
    private OnItemClick onItemClick;
    private boolean isFromView = false;
    private ArrayList<ModelCategoryData> arrayList;

    public MyAdapter(Context context, int resource, ArrayList<ModelCategoryData> arrayList, OnItemClick onItemClick) {
        super(context, resource, arrayList);
        this.mContext = context;
        this.arrayList = arrayList;
        this.onItemClick = onItemClick;
        this.myAdapter = this;
    }

    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    public View getCustomView(final int position, View convertView,
                              ViewGroup parent) {

        final ViewHolder holder;
        if (convertView == null) {
            LayoutInflater layoutInflator = LayoutInflater.from(mContext);
            convertView = layoutInflator.inflate(R.layout.spinner_item, null);
            holder = new ViewHolder();
            holder.mTextView = (TextView) convertView
                    .findViewById(R.id.tvName);
            holder.mCheckBox = (CheckBox) convertView
                    .findViewById(R.id.checkbox);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.mTextView.setText(arrayList.get(position).getCategoryName());

        // To check weather checked event fire from getview() or user input
        isFromView = true;
        holder.mCheckBox.setChecked(arrayList.get(position).isSelected());
        isFromView = false;

        if ((position == 0)) {
            holder.mCheckBox.setVisibility(View.INVISIBLE);
        } else {
            holder.mCheckBox.setVisibility(View.VISIBLE);
        }
        holder.mCheckBox.setTag(position);
        holder.mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                int getPosition = (Integer) buttonView.getTag();

                if (!isFromView) {
                    arrayList.get(position).setSelected(isChecked);
                    onItemClick.onItemClick(getPosition);
                }
            }
        });
        return convertView;
    }

    public interface OnItemClick {
        void onItemClick(int position);
    }

    private class ViewHolder {
        private TextView mTextView;
        private CheckBox mCheckBox;
    }
}