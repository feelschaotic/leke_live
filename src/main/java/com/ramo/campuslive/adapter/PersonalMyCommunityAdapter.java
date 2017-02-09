package com.ramo.campuslive.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.juss.mediaplay.entity.MyCommunity;
import com.ramo.campuslive.R;

import java.util.List;

public class PersonalMyCommunityAdapter extends RecyclerView.Adapter<PersonalMyCommunityAdapter.CommunityHolder> {
    private List<MyCommunity> communityList;

    public PersonalMyCommunityAdapter(List<MyCommunity> communityList) {
        this.communityList = communityList;
    }

    @Override
    public CommunityHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_my_community_item, parent, false);
        return new CommunityHolder(v);
    }

    @Override
    public void onBindViewHolder(final CommunityHolder holder, final int position) {
        MyCommunity comm = communityList.get(position);

//            Community community = (Community) comm
//            if (community.getCover() == null)
        holder.cover.setImageResource(comm.getSocityLogo());
//            else
//                holder.cover.setImageBitmap(ImageManageUtil.Byte2Bitmap(community.getCover()));

//            if (community.getUser().getHead() == null)
        holder.head.setImageResource(R.mipmap.ic_launcher);
//            else
//                holder.head.setImageBitmap(ImageManageUtil.Byte2Bitmap(community.getUser().getHead()));

        holder.desc.setText(comm.getSocityDesc());
        holder.name.setText(comm.getSocityName());
        holder.likeNum.setText(comm.getMemberNum() + "");
    /*    } else if (comm instanceof LiveRoom) {

            LiveRoom liveRoom = (LiveRoom) comm;
            if (liveRoom.getCover() == null)
                holder.cover.setImageResource(R.drawable.ic_load_error);
            else
                holder.cover.setImageBitmap(ImageManageUtil.Byte2Bitmap(liveRoom.getCover()));

            if (liveRoom.getUser().getHead() == null)
                holder.head.setImageResource(R.mipmap.ic_launcher);
            else
                holder.head.setImageBitmap(ImageManageUtil.Byte2Bitmap(liveRoom.getUser().getHead()));

            holder.desc.setText(String.valueOf(liveRoom.getLive_title()));
            holder.name.setText(liveRoom.getUser().getName());
            holder.likeNum.setText(String.valueOf(liveRoom.getLikeNum()));
        }*/

        holder.trash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                communityList.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, communityList.size());
            }
        });
    }

    @Override
    public int getItemCount() {
        return communityList.size();
    }

    class CommunityHolder extends RecyclerView.ViewHolder {
        ImageView head;
        TextView desc;
        TextView name;
        TextView likeNum;
        ImageView cover;
        ImageView trash;

        public CommunityHolder(View itemView) {
            super(itemView);
            head = (ImageView) itemView.findViewById(R.id.community_head);
            desc = (TextView) itemView.findViewById(R.id.community_desc);
            name = (TextView) itemView.findViewById(R.id.community_userName);
            likeNum = (TextView) itemView.findViewById(R.id.community_likeNum);
            cover = (ImageView) itemView.findViewById(R.id.community_cover);
            trash = (ImageView) itemView.findViewById(R.id.trash);
        }

    }

}


