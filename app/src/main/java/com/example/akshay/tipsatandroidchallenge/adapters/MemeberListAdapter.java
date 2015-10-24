package com.example.akshay.tipsatandroidchallenge.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.akshay.tipsatandroidchallenge.R;
import com.example.akshay.tipsatandroidchallenge.database.MembersModel;
import com.example.akshay.tipsatandroidchallenge.interfaces.OnItemClickListener;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by akshay on 24/10/15.
 */
public class MemeberListAdapter extends RecyclerView.Adapter<MemeberListAdapter.MemberHolder> {

    List<MembersModel> list;
    Context context;
    OnItemClickListener onItemClickListener;


    public MemeberListAdapter(List<MembersModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public MemberHolder onCreateViewHolder(ViewGroup viewGroup, int pos) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_members, viewGroup, false);
        MemberHolder memberHolder = new MemberHolder(view);
        return memberHolder;
    }

    @Override
    public void onBindViewHolder(MemberHolder memberHolder, int pos) {
        MembersModel membersModel = list.get(pos);
        String status = membersModel.getStatus();
        if (!TextUtils.isEmpty(status)) {
            memberHolder.textView.setText("" + status);
        } else {
            memberHolder.textView.setText("" + context.getString(R.string.error_nodata));
        }
        String imageUrl = membersModel.getImage();
        if (!TextUtils.isEmpty(imageUrl)) {
            Picasso.with(context).load(imageUrl).placeholder(R.mipmap.img_placeholder).resize(
                    (int) context.getResources().getDimension(R.dimen.imig_size),
                    (int) context.getResources().getDimension(R.dimen.imig_size)).
                    into(memberHolder.imageView);
        } else {
            memberHolder.imageView.setImageResource(R.mipmap.img_placeholder);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public class MemberHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView textView;
        ImageView imageView;

        public MemberHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.statusText);
            imageView = (ImageView) itemView.findViewById(R.id.imgStatus);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClickListener(itemView, getAdapterPosition());
            }
        }
    }
}
