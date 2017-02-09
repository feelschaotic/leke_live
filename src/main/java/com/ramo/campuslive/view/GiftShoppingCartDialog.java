package com.ramo.campuslive.view;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ListView;

import com.ramo.campuslive.R;
import com.ramo.campuslive.adapter.DialogAdapter;
import com.ramo.campuslive.bean.Gift;

import java.util.List;


public class GiftShoppingCartDialog extends Dialog {

    public GiftShoppingCartDialog(Context context) {
        super(context);
    }

    public GiftShoppingCartDialog(Context context, int theme) {
        super(context, theme);
    }

    public static class Builder {
        private Context context;
        private View contentView;
        private List<Gift> giftList;
        private DialogAdapter adapter;
        private ListView dialog_shopping_cart_lv;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder setContentView(View v) {
            this.contentView = v;
            return this;
        }

        public void setData(List<Gift> giftList) {
            this.giftList = giftList;
            adapter = new DialogAdapter(context, giftList);

        }


        public GiftShoppingCartDialog create() {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            final GiftShoppingCartDialog dialog = new GiftShoppingCartDialog(context, R.style.Dialog);
            View layout = inflater.inflate(R.layout.dialog_gift_shopping_cart, null);


            dialog.addContentView(layout, new LayoutParams(
                    LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));

            dialog_shopping_cart_lv = (ListView) layout.findViewById(R.id.dialog_shopping_cart_lv);
            if (adapter != null) {
                dialog_shopping_cart_lv.setAdapter(adapter);
            }

            dialog.setContentView(layout);
            return dialog;
        }

    }
}
