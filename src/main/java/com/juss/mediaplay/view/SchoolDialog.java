package com.juss.mediaplay.view;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.google.gson.Gson;
import com.juss.mediaplay.entity.SchoolJson;
import com.juss.mediaplay.entity.SchoolVo;
import com.juss.mediaplay.listener.HttpCallbackListener;
import com.juss.mediaplay.listener.MyOnclickStener;
import com.juss.mediaplay.po.Type;
import com.juss.mediaplay.utils.NetUtil;
import com.ramo.campuslive.R;
import com.ramo.campuslive.server.ServerConstants;
import com.ramo.campuslive.utils.L;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lenovo on 2016/7/28.
 */
public class SchoolDialog extends Dialog {
    public static Map<String,Integer> proviceMap=new HashMap<>();
    public SchoolDialog(Context context) {
        super(context);
    }
    public SchoolDialog(Context context, int theme) {
        super(context, theme);
    }


    public static class Builder {
        private Context context;
        private View contentView;

//        private DialogAdapter adapter;
        private SimpleAdapter adapter;
        private ListView dialog_lv;
        private MyOnclickStener listener;
        private AbsListView.OnScrollListener myScrolllistener;
        private List<Map<String,Object>> mapList=new ArrayList<>();
        private String title;
        private String message;
        private String positiveButtonText;
        private OnClickListener positiveButtonClickListener;
        private String negativeButtonText;
        private OnClickListener negativeButtonClickListener;
        private int resouse;
        private int lastItem;

        public int itemResouse;
        public String[] nameFrome;
        public int[] resouseTo;
        private int pagenum=1;
        private int pagall=1;
        private String address;

        public Builder(Context context) {
            this.context = context;
        }

        private Handler handler = new Handler(){


            @Override
            public void handleMessage(Message msg) {
                switch (msg.what){
                    case 1:
                        L.i("okoj");
                        if(adapter!=null){
                            adapter.notifyDataSetChanged();
                        }else{
                            adapter = new SimpleAdapter(context,mapList, itemResouse, nameFrome, resouseTo);
                            dialog_lv.setAdapter(adapter);
                        }
                        break;
                }

                super.handleMessage(msg);
            }
        };

        public Builder setContentView(View v) {
            this.contentView = v;
            return this;
        }

        public Builder setMessage(String message) {
            this.message = message;
            return this;
        }

        /**
         * Set the Dialog message from resource
         *
         * @return
         */
        public Builder setMessage(int message) {
            this.message = (String) context.getText(message);
            return this;
        }

        /**
         * Set the Dialog title from resource
         *
         * @param title
         * @return
         */
        public Builder setTitle(int title) {
            this.title = (String) context.getText(title);
            return this;
        }

        /**
         * Set the Dialog title from String
         *
         * @param title
         * @return
         */

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }
        /**
         * Set the positive button resource and it's listener
         *
         * @param positiveButtonText
         * @return
         */
        public Builder setPositiveButton(int positiveButtonText,
                                         OnClickListener listener) {
            this.positiveButtonText = (String) context
                    .getText(positiveButtonText);
            this.positiveButtonClickListener = listener;
            return this;
        }
        public Builder setPositiveButton(String positiveButtonText,
                                          OnClickListener listener) {
            this.positiveButtonText = positiveButtonText;
            this.positiveButtonClickListener = listener;
            return this;
        }
        public Builder setNegativeButton(int negativeButtonText,
                                         OnClickListener listener) {
            this.negativeButtonText = (String) context
                    .getText(negativeButtonText);
            this.negativeButtonClickListener = listener;
            return this;
        }

        public Builder setNegativeButton(String negativeButtonText,
                                         OnClickListener listener) {
            this.negativeButtonText = negativeButtonText;
            this.negativeButtonClickListener = listener;
            return this;
        }
        public void setData(List<Type> list,MyOnclickStener listener){
            mapList = new ArrayList<>();
            this.listener = listener;
            for(Type type:list){
                Map<String,Object> map = new HashMap<>();
                map.put("name", type.getTypeName());
                mapList.add(map);
            }
            adapter = new SimpleAdapter(context, mapList, R.layout.item_test, new String[]{"name" }, new int[]{R.id.id_name_tv});
//            adapter = new DialogAdapter(context, List);
        }
        public void setDataone(String address,int resouse,int itemResouse,String[] nameFrome,int[] resouseTo,MyOnclickStener listener){
            this.address=address;
            this.listener = listener;
            this.resouse=resouse;
            this.itemResouse = itemResouse;
            this.nameFrome=nameFrome;
            this.resouseTo=resouseTo;
//            adapter = new SimpleAdapter(context, mapList, itemResouse, nameFrome, resouseTo);
//            adapter = new DialogAdapter(context, List);
        }
        public SchoolDialog create() {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            final SchoolDialog dialog = new SchoolDialog(context, R.style.Dialog);
//            View layout = inflater.inflate(R.layout.dialog_type, null);
            View layout = inflater.inflate(resouse, null);
            dialog.addContentView(layout, new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

            if (positiveButtonText != null) {
                ((Button) layout.findViewById(R.id.positiveButton))
                        .setText(positiveButtonText);
                if (positiveButtonClickListener != null) {
                    ((Button) layout.findViewById(R.id.positiveButton))
                            .setOnClickListener(new View.OnClickListener() {
                                public void onClick(View v) {
                                    positiveButtonClickListener.onClick(dialog,
                                            DialogInterface.BUTTON_POSITIVE);

                                }
                            });
                }
            } else {
                // if no confirm button just set the visibility to GONE
                layout.findViewById(R.id.positiveButton).setVisibility(
                        View.GONE);
            }
            // set the cancel button
            if (negativeButtonText != null) {
                ((Button) layout.findViewById(R.id.negativeButton))
                        .setText(negativeButtonText);
                if (negativeButtonClickListener != null) {
                    ((Button) layout.findViewById(R.id.negativeButton))
                            .setOnClickListener(new View.OnClickListener() {
                                public void onClick(View v) {
                                    negativeButtonClickListener.onClick(dialog,
                                            DialogInterface.BUTTON_NEGATIVE);
                                }
                            });
                }
            } else {
                // if no confirm button just set the visibility to GONE
                layout.findViewById(R.id.negativeButton).setVisibility(
                        View.GONE);
            }
            getData();
            dialog_lv = (ListView) layout.findViewById(R.id.dialog_lv);
            if (adapter != null) {
                dialog_lv.setAdapter(adapter);
            }
            dialog_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Map<String,Object> map2= mapList.get(position);
                    String name = (String) map2.get("name");
                    L.e(name);
                    listener.getName(proviceMap.get(name),name);
//                    listener.getName(name);
                }
            });
            dialog_lv.setOnScrollListener(new AbsListView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(AbsListView view, int scrollState) {
                    if (lastItem == adapter.getCount() && scrollState == SCROLL_STATE_IDLE &&pagenum<pagall) {
                        getData();
                    }
                    L.e(scrollState + "----" + adapter.getCount());
                    L.i(lastItem + "");
                }
                @Override
                public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                    lastItem = firstVisibleItem + visibleItemCount;
                }
            });
            dialog.setContentView(layout);
            return dialog;
        }

        private void getData() {
            /*for (int i = 0; i < 10; i++) {
                Map<String, Object> map = new HashMap<>();
                map.put("name", "name22" + i);
                mapList.add(map);
                proviceMap.put("name22" + i, i);
            }
            handler.sendEmptyMessage(1);*/

            NetUtil.sendRequestToUrl(ServerConstants.GetSchoolURL, new String[]{"province","pagenum"}, new String[]{address,pagenum+""}, new HttpCallbackListener() {
                @Override
                public void onSucc(String response) {
                    Gson gson = new Gson();
                    SchoolJson provectList = gson.fromJson(response, SchoolJson.class);
                    pagenum = provectList.getPagenum() + 1;
                    pagall = provectList.getTotalrecord();
                    for (SchoolVo p : provectList.getList()) {
                        Map<String, Object> map = new HashMap<>();
                        map.put("name", p.getSchoolName());
                        mapList.add(map);
                        proviceMap.put(p.getSchoolName(), p.getSchoolId());
                    }
                    handler.sendEmptyMessage(1);
                }
                @Override
                public void onError(Exception e) {
                    L.e(e.getMessage());
                }
            });
        }
    }

}
