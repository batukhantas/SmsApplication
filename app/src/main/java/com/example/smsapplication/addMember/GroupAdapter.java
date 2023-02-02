package com.example.smsapplication.addMember;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smsapplication.GroupModel;
import com.example.smsapplication.OnClickItem;
import com.example.smsapplication.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.GroupViewHolder>{
    List<GroupModel> groupModelList;
    OnClickItem onClickItem;

    public GroupAdapter(List<GroupModel> groupModelList, OnClickItem onClickItem) {
        this.groupModelList = groupModelList;
        this.onClickItem = onClickItem;
    }


    @NonNull
    @Override
    public GroupAdapter.GroupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        GroupViewHolder groupViewHolder = new GroupViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_addmember_groups, parent, false), onClickItem);
        return groupViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull GroupAdapter.GroupViewHolder holder, int position) {
        GroupModel groupModel= groupModelList.get(position);
        holder.setData(groupModel);

    }

    @Override
    public int getItemCount() {

        return groupModelList.size();
    }

    public class GroupViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView groupImage;
        TextView groupName, groupDescp;
        OnClickItem onClickItem;


        public GroupViewHolder(View itemView, OnClickItem onClickItem) {
            super(itemView);
            groupImage = itemView.findViewById(R.id.item_addMember_groupImage);
            groupName = itemView.findViewById(R.id.item_addMember_groupName);
            groupDescp = itemView.findViewById(R.id.item_addMember_groupDescription);
            this.onClickItem = onClickItem;
            itemView.setOnClickListener(this);

        }
        public void setData(GroupModel groupModel) {
            groupName.setText(groupModel.getGroupName());
            groupDescp.setText(groupModel.getGroupDescp());
            if(groupModel.getGroupImage() != null){
                Picasso.get().load(groupModel.getGroupImage()).into(groupImage);

            }
        }

        @Override
        public void onClick(View view) {

            onClickItem.onClickItem(getAdapterPosition());
        }
    }
}
