package com.juss.mediaplay.adapter;

import android.content.Context;
import android.widget.SimpleAdapter;

import com.juss.mediaplay.po.Type;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lenovo on 2016/7/28.
 */
public class TypeAdapter extends SimpleAdapter {
    private Context context;


    public TypeAdapter(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to) {
        super(context, data, resource, from, to);
    }

}
