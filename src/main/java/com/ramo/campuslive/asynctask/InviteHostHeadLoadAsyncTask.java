package com.ramo.campuslive.asynctask;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.GridView;

import com.ramo.campuslive.R;
import com.ramo.campuslive.adapter.ThemeCreateHeadAdapter;
import com.ramo.campuslive.utils.ImageManageUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ramo on 2016/7/11.
 */
public class InviteHostHeadLoadAsyncTask extends AsyncTask<Void, Void, Void> {
    private Context context;
    private List<byte[]> imgList;
    private GridView gv;

    public InviteHostHeadLoadAsyncTask(Context context, GridView gv) {
        this.context = context;
        this.gv = gv;
    }

    @Override
    protected Void doInBackground(Void... params) {
        imgList = new ArrayList<>();
        imgList.add(ImageManageUtil.RToByte(R.drawable.head2));
        imgList.add(ImageManageUtil.RToByte(R.drawable.head2));
        imgList.add(ImageManageUtil.RToByte(R.drawable.head2));
        imgList.add(ImageManageUtil.RToByte(R.drawable.head2));

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        gv.setAdapter(new ThemeCreateHeadAdapter(context, imgList));
        super.onPostExecute(aVoid);
    }
}
