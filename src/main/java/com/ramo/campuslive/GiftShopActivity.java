package com.ramo.campuslive;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ramo.campuslive.collection.SortByPrices;
import com.ramo.campuslive.collection.SortByPricesDesc;
import com.ramo.campuslive.adapter.OnClickGiftListener;
import com.ramo.campuslive.adapter.SelectGiftAdapter;
import com.ramo.campuslive.bean.Gift;
import com.ramo.campuslive.utils.ImageManageUtil;
import com.ramo.campuslive.utils.L;
import com.ramo.campuslive.view.GiftShoppingCartDialog;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by ramo on 2016/6/22.
 */
@EActivity(R.layout.activity_gift)
public class GiftShopActivity extends SwipeBackActivity {

    @ViewById
    static
    LinearLayout gift_shop_shopping_cart_ll;
    static TextView gift_shop_selected_gift_num;
    TextView gift_shop_buy;
    private static Animation selected_gife_num_anim;
    private Animation select_gift_anim;
    public static List<Gift> buy_list;
    @ViewById
    ImageView anim_iv;
    @ViewById
    RecyclerView gift_gifts_gv;
    @ViewById
    TextView gift_sortBtn;
    private boolean sortDesc = true;

    private static List<Gift> giftList;
    private static SelectGiftAdapter selectGiftAdapter;
    public static int check_tag[];

    @AfterViews
    protected void init() {

        initCart();
        initData();
        select_gift_anim = AnimationUtils.loadAnimation(this, R.anim.select_gift_mobile_animation);
        gift_gifts_gv.setLayoutManager(new GridLayoutManager(this, 4));
        selectGiftAdapter = new SelectGiftAdapter(giftList);
        gift_gifts_gv.setAdapter(selectGiftAdapter);
        initListener();
    }

    private void initData() {
        giftList = new ArrayList<>();
        Gift gift1 = new Gift(ImageManageUtil.drawable2Byte(ImageManageUtil.RToDrawable(R.drawable.gift1)), 15, "爱心");
        Gift gift2 = new Gift(ImageManageUtil.drawable2Byte(ImageManageUtil.RToDrawable(R.drawable.gift2)), 20, "火箭");
        Gift gift3 = new Gift(ImageManageUtil.drawable2Byte(ImageManageUtil.RToDrawable(R.drawable.gift3)), 100, "飞船");
        Gift gift4 = new Gift(ImageManageUtil.drawable2Byte(ImageManageUtil.RToDrawable(R.drawable.gift4)), 200, "飞吻");
        Gift gift5 = new Gift(ImageManageUtil.drawable2Byte(ImageManageUtil.RToDrawable(R.drawable.gift5)), 150, "鲜花");
        giftList.add(gift1);
        giftList.add(gift2);
        giftList.add(gift3);
        giftList.add(gift4);
        giftList.add(gift5);
        check_tag = new int[giftList.size()];

        buy_list = new ArrayList();
    }

    private void initCart() {

        gift_shop_selected_gift_num = (TextView) gift_shop_shopping_cart_ll.findViewById(R.id.gift_shop_selected_gift_num);
        gift_shop_buy = (TextView) gift_shop_shopping_cart_ll.findViewById(R.id.gift_shop_buy);
        selected_gife_num_anim = AnimationUtils.loadAnimation(this, R.anim.selected_gift_num_anim);
    }


    private void initListener() {

        gift_shop_buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });

        gift_shop_selected_gift_num.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GiftShoppingCartDialog.Builder builder = new GiftShoppingCartDialog.Builder(GiftShopActivity.this);
                builder.setData(buy_list);
                builder.create().show();
            }
        });
        selectGiftAdapter.setOnItemClickListener(new OnClickGiftListener() {
            @Override
            public void onCheckClick(View v, Integer tag) {
                RelativeLayout parent = (RelativeLayout) v.getParent();
                ImageView check = (ImageView) parent.findViewById(R.id.select_gift_check);
                if (check_tag[tag] == 0) {
                    check.setVisibility(View.VISIBLE);
                    check_tag[tag] = 1;
                    addSendListAndChangeStyle((ImageView) parent.findViewById(R.id.select_gift_img), giftList.get(tag));
                } else {
                    check.setVisibility(View.GONE);
                    check_tag[tag] = 0;
                    removeSendListAndChangeStyle(giftList.get(tag));
                }
            }
        });


        gift_sortBtn.setOnClickListener(new View.OnClickListener() {

            private Drawable drawable;

            @Override
            public void onClick(View arg0) {
                if (sortDesc) {
                    drawable = getResources().getDrawable(
                            R.drawable.rank_btn_selected_normal);
                    Collections.sort(giftList, new SortByPrices());
                } else {
                    drawable = getResources().getDrawable(
                            R.drawable.rank_btn_normal);

                    Collections.sort(giftList, new SortByPricesDesc());
                }
                // / 这一步必须要做,否则不会显示.
                drawable.setBounds(0, 0, drawable.getMinimumWidth(),
                        drawable.getMinimumHeight());
                gift_sortBtn.setCompoundDrawables(drawable, null, null,
                        null);
                sortDesc = !sortDesc;
                selectGiftAdapter.notifyDataSetChanged();
            }
        });
    }
    public static void changeSendFileNum() {
        gift_shop_selected_gift_num.setText("已选（" + buy_list.size() + "）");
        gift_shop_selected_gift_num.startAnimation(selected_gife_num_anim);
        L.e(gift_shop_shopping_cart_ll.getTranslationY() + "");
        if (buy_list.size() == 0)
            ObjectAnimator.ofFloat(gift_shop_shopping_cart_ll, "translationY", gift_shop_shopping_cart_ll.getTranslationY(), gift_shop_shopping_cart_ll.getTranslationY() + gift_shop_shopping_cart_ll.getHeight()).setDuration(300).start();
    }

    public void removeSendListAndChangeStyle(Gift gift) {
        buy_list.remove(gift);
        changeSendFileNum();
    }

    public void addSendListAndChangeStyle(ImageView imageView, Gift gift) {
        if (!buy_list.contains(gift)) {
            buy_list.add(gift);
        }
        changeSendFileNum();
        isListOneAnim();
        changeStyle(imageView);
    }

    public void changeStyle(ImageView imageView) {
        imageView.setDrawingCacheEnabled(true);
        anim_iv.setImageBitmap(Bitmap.createBitmap(imageView.getDrawingCache()));
        imageView.setDrawingCacheEnabled(false);

        int[] location = new int[2];
        imageView.getLocationOnScreen(location);
        int x = location[0];
        int y = location[1];
        //  L.e("left and top:" + x + ":" + y);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(imageView.getWidth(), imageView.getHeight());
        layoutParams.setMargins(x, y, 0, 0);
        anim_iv.setLayoutParams(layoutParams);
        anim_iv.setVisibility(View.VISIBLE);

        anim_iv.startAnimation(select_gift_anim);
    }


    public void isListOneAnim() {
        if (buy_list.size() == 1)
            ObjectAnimator.ofFloat(gift_shop_shopping_cart_ll, "translationY", gift_shop_shopping_cart_ll.getTranslationY(), gift_shop_shopping_cart_ll.getTranslationY() - gift_shop_shopping_cart_ll.getHeight()).setDuration(300).start();
    }

    public static void cartEvent(Gift g) {
        int positionInList = giftList.indexOf(g);
        if (positionInList != -1) {
            check_tag[positionInList] = 0;
            selectGiftAdapter.notifyDataSetChanged();
            changeSendFileNum();
        }
    }
}
