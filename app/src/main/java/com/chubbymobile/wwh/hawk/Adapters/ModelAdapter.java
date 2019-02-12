package com.chubbymobile.wwh.hawk.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.androidkun.adapter.BaseAdapter;
import com.androidkun.adapter.ViewHolder;
import com.chubbymobile.wwh.hawk.Bean.Vehicle;
import com.chubbymobile.wwh.hawk.R;

import java.util.ArrayList;
import java.util.List;

public class ModelAdapter extends BaseAdapter {

    private List<Vehicle> copyList;

    private OnItemClickListener itemClickListener;
    public interface OnItemClickListener{
        void onItemClick(String str, int position);
    }
    public void setItemClickListener(OnItemClickListener itemClickListener){
        this.itemClickListener = itemClickListener;
    }

    public ModelAdapter(Context context, List<Vehicle> data) {
        super(context, R.layout.item_mode, data);
        copyList = new ArrayList<Vehicle>(data);
    }

    @Override
    public void convert(ViewHolder holder, Object o) {
        holder.setImageResource(R.id.tv_image, ((Vehicle)o).getId());
        holder.setText(R.id.tv_name, ((Vehicle)o).getModel());
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        convert(holder, datas.get(position));
        holder.itemView.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(itemClickListener!=null)
                    itemClickListener.onItemClick(((Vehicle)datas.get(position)).getModel(), position);
            }
        });
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return ViewHolder.get(context,parent,layoutId);
    }

    public void filter(String queryText, List<Vehicle> data)
    {
        data.clear();
        if(queryText.isEmpty())
        {
            data.addAll(copyList);
        }
        else
        {
            for(Vehicle vh: copyList)
            {
                if(vh.getModel().toLowerCase().contains(queryText.toLowerCase()))
                {
                    data.add(vh);
                }
            }
        }
        notifyDataSetChanged();
    }
}