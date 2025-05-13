package com.example.fakeneptun.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.fakeneptun.activities.ClassesActivity;
import com.example.fakeneptun.R;
import com.example.fakeneptun.activities.ExamsActivity;
import com.example.fakeneptun.activities.ExamsManagementActivity;
import com.example.fakeneptun.activities.GradesActivity;
import com.example.fakeneptun.activities.GradesEntryActivity;
import com.example.fakeneptun.activities.MessagesActivity;
import com.example.fakeneptun.activities.ScheduleActivity;
import com.example.fakeneptun.activities.StudentsActivity;
import com.example.fakeneptun.activities.LessonsActivity;
import com.example.fakeneptun.model.MenuItem;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.ViewHolder> {
    private Context context;
    private List<MenuItem> items;

    private static final Map<String, Class<?>> activityMap = new HashMap<>();

    static {
        activityMap.put("Tantárgyak", LessonsActivity.class);
        activityMap.put("Hallgatók", StudentsActivity.class);
        activityMap.put("Jegyek rögzítése", GradesEntryActivity.class);
        activityMap.put("Vizsgák kezelése", ExamsManagementActivity.class);
        activityMap.put("Üzenetek", MessagesActivity.class);
        activityMap.put("Óráim", ScheduleActivity.class);
        activityMap.put("Jegyek", GradesActivity.class);
        activityMap.put("Tárgyfelvét", ClassesActivity.class);
        activityMap.put("Vizsgák", ExamsActivity.class);
    }

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
            String menuItemTitle = item.title.trim();

            Class<?> activityClass = activityMap.get(menuItemTitle);
            if (activityClass != null) {
                context.startActivity(new Intent(context, activityClass));
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
