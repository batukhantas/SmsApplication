package com.example.smsapplication.createGroup;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smsapplication.GroupModel;
import com.example.smsapplication.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.GroupViewHolder> {
    List<GroupModel> groupModelList;

    public GroupAdapter(List<GroupModel> groupModelList) {
        this.groupModelList = groupModelList;
    }


    @NonNull
    @Override
    public GroupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        GroupViewHolder groupViewHolder = new GroupViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_create_group, parent, false));
        return groupViewHolder;
    }



    @Override
    public void onBindViewHolder(@NonNull GroupViewHolder holder, int position) {
        GroupModel groupModel= groupModelList.get(position);
        holder.setData(groupModel);

    }

    @Override
    public int getItemCount() {
        return groupModelList.size();
    }

    public class GroupViewHolder extends RecyclerView.ViewHolder {
        ImageView group_Image;
        TextView group_Name, group_Descp;

        public GroupViewHolder(View itemView) {
            super(itemView);
            group_Image = itemView.findViewById(R.id.item_groupIV);
            group_Name = itemView.findViewById(R.id.item_groupName);
            group_Descp = itemView.findViewById(R.id.item_groupDesc);
        }

        public void setData(GroupModel groupModel) {
            group_Name.setText(groupModel.getGroupName());
            group_Descp.setText(groupModel.getGroupDescp());

            if(groupModel.getGroupImage() != null){
                Picasso.get().load(groupModel.getGroupImage()).into(group_Image);
            }
        }
    }
}