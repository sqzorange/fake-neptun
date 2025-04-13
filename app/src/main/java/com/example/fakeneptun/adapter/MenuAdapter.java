package com.example.fakeneptun.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.fakeneptun.R;
import com.example.fakeneptun.model.MenuItem;

import java.util.List;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.ViewHolder> {
    private Context context;
    private List<MenuItem> items;

    public MenuAdapter(Context context, List<MenuItem> items) {
        this.context = context;
        this.items = items;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView icon;
        public TextView title;

        public ViewHolder(View view) {
            super(view);
            icon = view.findViewById(R.id.menuIcon);
            title = view.findViewById(R.id.menuTitle);
        }
    }

    @Override
    public MenuAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.activity_menu_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MenuAdapter.ViewHolder holder, int position) {
        MenuItem item = items.get(position);
        holder.icon.setImageResource(item.iconResId);
        holder.title.setText(item.title);

        holder.itemView.setOnClickListener(v -> {
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
