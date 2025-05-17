package com.example.fakeneptun.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.fakeneptun.R;
import com.example.fakeneptun.model.Message;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;

public class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.MessageViewHolder> {

    private List<Message> messageList;

    public MessagesAdapter(List<Message> messageList) {
        this.messageList = messageList;
    }

    public static class MessageViewHolder extends RecyclerView.ViewHolder {
        public TextView tvSender, tvMessageText, tvTimestamp;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            tvSender = itemView.findViewById(R.id.tvSender);
            tvMessageText = itemView.findViewById(R.id.tvMessageText);
            tvTimestamp = itemView.findViewById(R.id.tvTimestamp);
        }
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.message_item, parent, false);
        return new MessageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        Message message = messageList.get(position);
        holder.tvSender.setText(message.getSenderName() != null ? message.getSenderName() : "Ismeretlen");
        holder.tvMessageText.setText(message.getText());
        String timeFormatted = DateFormat.getDateTimeInstance().format(new Date(message.getTimestamp()));
        holder.tvTimestamp.setText(timeFormatted);
    }

    @Override
    public int getItemCount() {
        return (messageList != null) ? messageList.size() : 0;
    }
}