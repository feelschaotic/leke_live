package com.juss.mediaplay.view;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.juss.mediaplay.listener.MyOnclickStener;
import com.juss.mediaplay.po.Type;
import com.ramo.campuslive.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lenovo on 2016/7/28.
 */
public class TypeDialog extends Dialog {
    public TypeDialog(Context context) {
        super(context);
    }
    public TypeDialog(Context context, int theme) {
        super(context, theme);
    }


    public static class Builder {
        private Context context;
        private View contentView;

//        private DialogAdapter adapter;
        private SimpleAdapter adapter;
        private ListView dialog_lv;
        private MyOnclickStener listener;
        private List<Map<String,Object>> mapList;
        private String title;
        private String message;
        private String positiveButtonText;
        private OnClickListener positiveButtonClickListener;
        private String negativeButtonText;
        private OnClickListener negativeButtonClickListener;
        public Builder(Context context) {
            this.context = context;
        }

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
        public TypeDialog create() {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            final TypeDialog dialog = new TypeDialog(context, R.style.Dialog);
            View layout = inflater.inflate(R.layout.dialog_type, null);
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


            dialog_lv = (ListView) layout.findViewById(R.id.dialog_type_lv);
            if (adapter != null) {
                dialog_lv.setAdapter(adapter);
            }
//            dialog_lv.setOnItemClickListener(listener);
            dialog.setContentView(layout);
            return dialog;
        }
    }
}