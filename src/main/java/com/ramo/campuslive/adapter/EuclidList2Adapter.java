package com.ramo.campuslive.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.juss.mediaplay.asynetask.LoadingImage;
import com.ramo.campuslive.EuclidActivity;
import com.ramo.campuslive.R;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Map;

/**
 * Created by Oleksii Shliama on 1/27/15.
 */
public class EuclidList2Adapter extends ArrayAdapter<Map<String, Object>> implements View.OnClickListener {

    public static final String KEY_AVATAR = "avatar";
    public static final String KEY_NAME = "name";
    public static final String KEY_DESCRIPTION_SHORT = "description_short";
    public static final String KEY_DESCRIPTION_FULL = "description_full";

    private final LayoutInflater mInflater;
    private List<Map<String, Object>> mData;
    private int layoutResourceId;
    private OnClickMainBtnListener onClickMainBtnListener;
    private ListView listView;

    private LoadingImage loadingImage;

    public EuclidList2Adapter(Context context, int layoutResourceId, List<Map<String, Object>> data,ListView listView) {
        super(context, layoutResourceId, data);
        mData = data;
        mInflater = LayoutInflater.from(context);
        this.listView=listView;
        loadingImage=new LoadingImage(listView);
        this.layoutResourceId = layoutResourceId;
    }

    public void setOnClickMainBtnListener(OnClickMainBtnListener onClickMainBtnListener) {
        this.onClickMainBtnListener = onClickMainBtnListener;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            convertView = mInflater.inflate(layoutResourceId, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.mViewOverlay = convertView.findViewById(com.yalantis.euclid.library.R.id.view_avatar_overlay);
            viewHolder.mListItemAvatar = (ImageView) convertView.findViewById(com.yalantis.euclid.library.R.id.image_view_avatar);
            viewHolder.mListItemName = (TextView) convertView.findViewById(com.yalantis.euclid.library.R.id.text_view_name);
            viewHolder.mListItemDescription = (TextView) convertView.findViewById(com.yalantis.euclid.library.R.id.text_view_description);
            viewHolder.main_addInTheme_iv = (ImageView) convertView.findViewById(com.yalantis.euclid.library.R.id.main_addInTheme_iv);


            viewHolder.main_like_iv = (ImageView) convertView.findViewById(com.yalantis.euclid.library.R.id.main_like_iv);
            viewHolder.main_delete_iv = (ImageView) convertView.findViewById(com.yalantis.euclid.library.R.id.main_delete_iv);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        String coverImgUrl = (String) mData.get(position).get(KEY_AVATAR);
        viewHolder.mListItemAvatar.setTag(coverImgUrl);
        //loadingImage.LoadImage2Asynctask(viewHolder.mListItemAvatar,coverImgUrl);

       /* Picasso.with(getContext()).load((Integer) mData.get(position).get(KEY_AVATAR))
                .resize(EuclidActivity.sScreenWidth, EuclidActivity.sProfileImageHeight).centerCrop()
                .placeholder(com.yalantis.euclid.library.R.color.blue)
                .into(viewHolder.mListItemAvatar);*/
        /*Picasso.with(getContext()).load(R.drawable.head)
                .resize(EuclidActivity.sScreenWidth, EuclidActivity.sProfileImageHeight).centerCrop()
                .placeholder(com.yalantis.euclid.library.R.color.blue)
                .into(viewHolder.mListItemAvatar);*/

        viewHolder.mListItemName.setText(mData.get(position).get(KEY_NAME).toString().toUpperCase());
        viewHolder.mListItemDescription.setText((String) mData.get(position).get(KEY_DESCRIPTION_SHORT));
        viewHolder.mViewOverlay.setBackground(EuclidActivity.sOverlayShape);

        if(viewHolder.main_addInTheme_iv!=null){
            viewHolder.main_addInTheme_iv.setTag(position);
            viewHolder.main_addInTheme_iv.setOnClickListener(this);
        }
        viewHolder.main_like_iv.setTag(position);
        viewHolder.main_delete_iv.setTag(position);
        viewHolder.main_like_iv.setOnClickListener(this);
        viewHolder.main_delete_iv.setOnClickListener(this);
        return convertView;
    }

    @Override
    public void onClick(View v) {
        if (onClickMainBtnListener != null) {
            onClickMainBtnListener.onClick(v, Integer.parseInt(v.getTag().toString()));
        }
    }

    static class ViewHolder {
        View mViewOverlay;
        ImageView mListItemAvatar;
        TextView mListItemName;
        TextView mListItemDescription;

        ImageView main_addInTheme_iv;
        ImageView main_like_iv;
        ImageView main_delete_iv;
    }
}
