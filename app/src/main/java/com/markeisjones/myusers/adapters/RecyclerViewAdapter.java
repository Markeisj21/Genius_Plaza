package com.markeisjones.myusers.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.markeisjones.myusers.R;
import com.markeisjones.myusers.models.UserModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {
    private Context mContext;
    private ArrayList<UserModel> mUserList;

    public RecyclerViewAdapter(Context context, ArrayList<UserModel> userList){
        mContext = context;
        mUserList = userList;

    }


    @Override
    public MyViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.cardview_item,parent,false  );
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder( MyViewHolder holder, int position) {
        UserModel currentItem = mUserList.get(position);

        String imageurl = currentItem.getImageurl();
        String id = currentItem.getId();
        String firstName = currentItem.getFirstname();
        String lastName= currentItem.getLastname();

        Picasso.with(mContext).load(imageurl).fit().centerInside().into(holder.mImageView);
        //Picasso.get().load(avatar).into(holder.mAvatar);
        holder.mId.setText(id);
        holder.mFirstName.setText(firstName);
        holder.mLastName.setText(lastName);

    }

    @Override
    public int getItemCount() {
        return mUserList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public ImageView mImageView;
        public TextView mId;
        public TextView mFirstName;
        public TextView mLastName;


        public MyViewHolder( View itemView) {
            super(itemView);
            mImageView= itemView.findViewById(R.id.main_image);
            mId = itemView.findViewById(R.id.main_id);
            mFirstName = itemView.findViewById(R.id.main_first);
            mLastName = itemView.findViewById(R.id.main_last);
        }
    }

}
