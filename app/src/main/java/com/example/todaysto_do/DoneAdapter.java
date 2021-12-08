package com.example.todaysto_do;

import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class DoneAdapter extends RecyclerView.Adapter<DoneAdapter.CustomViewHolder> {

    private ArrayList<MainData> arrayList;

    public DoneAdapter(ArrayList<MainData> arrayList) {
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public DoneAdapter.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_done, parent, false);
        CustomViewHolder holder = new CustomViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull DoneAdapter.CustomViewHolder holder, int position) {
        holder.iv_profile.setImageResource(arrayList.get(position).getIcon());
        holder.tv_content.setText(arrayList.get(position).getContent());
        holder.tv_content.setPaintFlags(holder.tv_content.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        holder.itemView.setTag(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                remove(holder.getAdapterPosition());
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return (arrayList != null ? arrayList.size() : 0);
    }

    public void remove(int position){
        try{
            arrayList.remove(position);
            notifyItemRemoved(position);
        } catch (IndexOutOfBoundsException e){
            e.printStackTrace();
        }
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        protected ImageView iv_profile;
        protected TextView tv_content;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            this.iv_profile = (ImageView) itemView.findViewById(R.id.iv_profile);
            this.tv_content = (TextView) itemView.findViewById(R.id.tv_content);
        }
    }
}
