package com.ramo.campuslive.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.juss.mediaplay.asynetask.LoadingImage;
import com.ramo.campuslive.EuclidActivity;
import com.ramo.campuslive.server.ServerConstants;

import java.util.List;
import java.util.Map;

/**
 * Created by Oleksii Shliama on 1/27/15.
 */
public class EuclidListAdapter extends ArrayAdapter<Map<String, Object>> implements View.OnClickListener {

    public static final String KEY_AVATAR = "avatar";
    public static final String KEY_NAME = "name";
    public static final String KEY_DESCRIPTION_SHORT = "description_short";
    public static final String KEY_DESCRIPTION_FULL = "description_full";
    public static final String KEY_TYPE = "type";
    public static final String KEY_SCHOOL = "school";

    private final LayoutInflater mInflater;
    private List<Map<String, Object>> mData;
    private int layoutResourceId;
    private OnClickMainBtnListener onClickMainBtnListener;

    public static LoadingImage mImageLoader;
    private boolean mFirst = true;//是否第一次启动
    private ListView listView;
    private int mStart, mEnd;
    public static String urls[];

    public EuclidListAdapter(Context context, int layoutResourceId, List<Map<String, Object>> data, ListView listView) {
        super(context, layoutResourceId, data);
        mData = data;
        mInflater = LayoutInflater.from(context);
        this.layoutResourceId = layoutResourceId;
        this.listView = listView;
        newImageLoader(mData);
    }

    public void newImageLoader(List<Map<String, Object>> l) {
        mFirst = true;
        mImageLoader = new LoadingImage(listView);
        urls = new String[l.size()];
        for (int i = 0; i < l.size(); i++) {
            urls[i] = ServerConstants.ProUrl + l.get(i).get(KEY_AVATAR);
        }

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                //是否停止滚动状态
                if (scrollState == SCROLL_STATE_IDLE) {
                    //加载可见项
                    mImageLoader.loadPartImageView(mStart, mEnd);
                } else {
                    //停止任务
                    mImageLoader.cancelAllTask();
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                mStart = firstVisibleItem;
                mEnd = firstVisibleItem + visibleItemCount;
                //判断到第一次加载界面时，预先加载一屏幕图像
                if (mFirst && visibleItemCount > 0) {
                    mImageLoader.loadPartImageView(mStart, mEnd);
                    mFirst = false;
                }
            }
        });
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
            viewHolder.type = (TextView) convertView.findViewById(com.yalantis.euclid.library.R.id.text_view_type);
            viewHolder.school = (TextView) convertView.findViewById(com.yalantis.euclid.library.R.id.text_view_school);
            viewHolder.main_addInTheme_iv = (ImageView) convertView.findViewById(com.yalantis.euclid.library.R.id.main_addInTheme_iv);


            viewHolder.main_like_iv = (ImageView) convertView.findViewById(com.yalantis.euclid.library.R.id.main_like_iv);
            viewHolder.main_delete_iv = (ImageView) convertView.findViewById(com.yalantis.euclid.library.R.id.main_delete_iv);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

      /*  Picasso.with(getContext()).load((Integer)mData.get(position).get(KEY_AVATAR))
                .resize(EuclidActivity.sScreenWidth, EuclidActivity.sProfileImageHeight).centerCrop()
                .placeholder(com.yalantis.euclid.library.R.color.blue)
                .into(viewHolder.mListItemAvatar);*/
        // viewHolder.mListItemAvatar.setImageResource(R.drawable.head);
        viewHolder.mListItemAvatar.setTag(urls[position]);
        mImageLoader.loaderImageByThread(viewHolder.mListItemAvatar, urls[position]);

        Map<String, Object> obj = mData.get(position);
        viewHolder.mListItemName.setText(obj.get(KEY_NAME).toString());
        if (viewHolder.type != null) {
            Object o = obj.get(KEY_TYPE);
            if (o != null)
                viewHolder.type.setText(o.toString());
        }
        viewHolder.mListItemDescription.setText((String) obj.get(KEY_DESCRIPTION_SHORT));
        viewHolder.school.setText((String) obj.get(KEY_SCHOOL));
        viewHolder.mViewOverlay.setBackground(EuclidActivity.sOverlayShape);

        viewHolder.mViewOverlay.setTag(position);

        if (viewHolder.main_addInTheme_iv != null) {
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
        TextView type;
        TextView school;

        ImageView main_addInTheme_iv;
        ImageView main_like_iv;
        ImageView main_delete_iv;
    }
}
