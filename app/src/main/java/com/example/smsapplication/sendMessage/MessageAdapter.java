package com.example.smsapplication.sendMessage;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smsapplication.MessageModel;
import com.example.smsapplication.OnClickItem;
import com.example.smsapplication.R;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder>{

    List<MessageModel> messageModelList;
    OnClickItem onClickItem;

    public MessageAdapter(List<MessageModel> messageModelList, OnClickItem onClickItem) {
        this.messageModelList = messageModelList;
        this.onClickItem = onClickItem;
    }


    @NonNull
    @Override
    public MessageAdapter.MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        MessageViewHolder messageViewHolder = new MessageViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sendmessage_message, parent, false), onClickItem);
        return messageViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.MessageViewHolder holder, int position) {
        MessageModel messageModel= messageModelList.get(position);
        holder.setData(messageModel);

    }

    @Override
    public int getItemCount() {
        return messageModelList.size();
    }

    public class MessageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView messageName, messageDescp;

        OnClickItem onClickItem;

        public MessageViewHolder( View itemView, OnClickItem onClickItem) {
            super(itemView);
            messageName = itemView.findViewById(R.id.item_sendMessage_messageTitle);
            messageDescp = itemView.findViewById(R.id.item_sendMessage_message);

            this.onClickItem= onClickItem;
            itemView.setOnClickListener(this);
        }
        public void setData(MessageModel messageModel){
            messageName.setText(messageModel.getMessageName());
            messageDescp.setText(messageModel.getMessageDescp());

        }


        @Override
        public void onClick(View view) {
            onClickItem.onClickItem(getAdapterPosition());

        }
    }
}
