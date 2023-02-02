package com.example.smsapplication.createMessage;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smsapplication.MessageModel;
import com.example.smsapplication.R;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {
    List<MessageModel> messageModelList;

    public MessageAdapter(List<MessageModel> messageModelList) {
        this.messageModelList = messageModelList;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        MessageViewHolder messageViewHolder = new MessageViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_create_messages, parent, false));
        return messageViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        MessageModel messageModel = messageModelList.get(position);
        holder.setData(messageModel);
    }

    @Override
    public int getItemCount() {
        return messageModelList.size();
    }

    public class MessageViewHolder extends RecyclerView.ViewHolder {
        TextView messageName, messageDescp;
        public MessageViewHolder(View itemView) {
            super(itemView);
            messageName = itemView.findViewById(R.id.item_messagesName);
            messageDescp = itemView.findViewById(R.id.item_messagesDesc);
        }

        public void setData(MessageModel messageModel){
            messageName.setText(messageModel.getMessageName());
            messageDescp.setText(messageModel.getMessageDescp());
        }
    }
}
