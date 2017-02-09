package com.juss.mediaplay.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.juss.mediaplay.asynetask.LoadingImage;
import com.juss.mediaplay.entity.Question;

import java.util.List;
import java.util.zip.Inflater;

/**
 * Created by lenovo on 2016/7/17.
 */
public class QustionAdapter extends BaseAdapter implements AbsListView.OnScrollListener {
    private List<Question> questionList;
    private LayoutInflater mInflater;
    private LoadingImage mImageLoder;

    private int mStart;
    private int mEnd;
    private boolean isFristLoad = true;

    public static  String[] URlS;
    QustionAdapter(Context context,List<Question> data,ListView listView){
        questionList = data;
        mInflater = LayoutInflater.from(context);
        mImageLoder=new LoadingImage(listView);
        URlS = new String[data.size()];
        for (int i = 0; i <data.size() ; i++) {
           // mUrls[i]=data.get(i).
        }
        listView.setOnScrollListener(this);
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder=null;
        if(convertView!=null){
            viewHolder= (ViewHolder) convertView.getTag();
        }else{
            viewHolder = new ViewHolder();

        }
       /* viewHolder.imageView=convertView.findViewById();
       //设置item的唯一身份信息
        viewHolder.imageView.setTag("imgurl");*/



        return null;
    }

    /**
     * 只有在滚动状态改变时调用
     * @param view
     * @param scrollState
     */
    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        //如果处于滚动停止状态 加载可见项
        if(scrollState == SCROLL_STATE_IDLE){
          //  mImageLoder.loadImages(mStart,mEnd);
        }else {
            //停止任务
           // mImageLoder.cancalAllTask();
        }

    }

    /**
     * 滚动都会调用
     * @param view
     * @param firstVisibleItem 第一个可见项
     * @param visibleItemCount 可见元素长度
     * @param totalItemCount
     */
    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        mStart = firstVisibleItem;
        mEnd = firstVisibleItem +visibleItemCount;
        //首次加载
        if(isFristLoad &&firstVisibleItem >0){
            isFristLoad=false;
         //   mImageLoder.loadImages(mStart,mEnd);
        }
    }

    class ViewHolder{
        ImageView imageView;
    }
}
