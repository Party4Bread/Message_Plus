package com.example.david0926.messageplus.Chat;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.david0926.messageplus.R;

import java.util.ArrayList;
import java.util.List;

public class RecycleAdapter_Chat extends RecyclerView.Adapter<RecycleHolder_Chat> {

    List<RecycleModel_Chat> items = new ArrayList<>();

    public List<RecycleModel_Chat> getItems() {
        return items;
    }

    public void add(RecycleModel_Chat data){
        items.add(data);
        notifyDataSetChanged();
    }

    public void delete(int position){
        items.remove(items.get(position));
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecycleHolder_Chat onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycle_item_chat, parent, false);
        return new RecycleHolder_Chat(v, R.drawable.ic_person);
    }

    @Override
    public void onBindViewHolder(@NonNull RecycleHolder_Chat holder, int position) {
        RecycleModel_Chat item = items.get(position);
        holder.name.setText(item.getNickname()+" ("+item.getName()+")");
        holder.msg.setText(item.getMsg());
        holder.time.setText(item.getTime());
        //holder.profile.setImageResource(items.get(position).getProfileId());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
