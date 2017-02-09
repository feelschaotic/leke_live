package com.juss.mediaplay.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.juss.mediaplay.po.Province;

import java.util.List;

/**
 * Created by lenovo on 2016/7/23.
 */
public class ListProvinceAdapter extends ArrayAdapter {
    public ListProvinceAdapter(Context context, int resource) {
        super(context, resource);
    }

   /* private Context context;
    private List<Province> provinceList;
    ListProvinceAdapter(Context context,List<Province> provinceList){
        this.context=context;
        this.provinceList=provinceList;
    }*/
   /* @Override
    public int getCount() {
        return provinceList.size();
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }

    @Override
    public Object getItem(int position) {
        return provinceList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }*/

    class HolderdView{
        TextView provinceId;
        TextView provincename;

    }
}
