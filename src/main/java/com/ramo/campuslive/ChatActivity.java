package com.ramo.campuslive;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.ramo.campuslive.adapter.ChatMsgAdapter;
import com.ramo.campuslive.bean.ChatMessage;
import com.ramo.campuslive.utils.T;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by ramo on 2016/7/4.
 */
@EActivity(R.layout.activity_chat_layout)
public class ChatActivity extends SwipeBackActivity {
    @ViewById(R.id.chat_listview)
    ListView msgs;
    private ChatMsgAdapter chatMsgAdapter;
    private List<ChatMessage> list;
    @ViewById(R.id.chat_send_info)
    EditText send_input;
    @ViewById(R.id.chat_send_msg_btn)
    Button send_btn;
    @ViewById
    Button chat_add_btn;
    @ViewById
    Button chat_face_btn;
    @ViewById
    ViewPager chat_face_viewPager;
    @ViewById
    LinearLayout chat_add_and_face_ll;
    @ViewById
    LinearLayout chat_send_bottm_bar;
    @ViewById
    GridLayout chat_add_gridlayout;

    private List<View> chat_faces_tabs;
    private PagerAdapter chat_face_adapter;

    private String face_prefix = "[face_";

    private Bitmap bitmap;
    private ImageSpan imageSpan;
    private SpannableString spannableString;

    private boolean isChatAddAndFaceLLVisible = false;

    @AfterViews
    public void init() {
        initData();
        initViewPager();
        initListener();
    }

    private void initViewPager() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View tab1 = inflater.inflate(R.layout.chat_face_item1, null);
        View tab2 = inflater.inflate(R.layout.chat_face_item2, null);
        View tab3 = inflater.inflate(R.layout.chat_face_item3, null);
        chat_faces_tabs = new ArrayList<View>();
        chat_faces_tabs.add(tab1);
        chat_faces_tabs.add(tab2);
        chat_faces_tabs.add(tab3);
        chat_face_adapter = new PagerAdapter() {
            @Override
            public void destroyItem(ViewGroup container, int position,
                                    Object object) {
                chat_face_viewPager.removeView(chat_faces_tabs.get(position));
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                View view = chat_faces_tabs.get(position);
                chat_face_viewPager.addView(view);
                return view;
            }

            @Override
            public boolean isViewFromObject(View arg0, Object arg1) {
                return arg0 == arg1;
            }

            @Override
            public int getCount() {
                return chat_faces_tabs.size();
            }
        };
        chat_face_viewPager.setAdapter(chat_face_adapter);
    }

    private void initListener() {
        send_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                final String toMsg = send_input.getText().toString();
                if (TextUtils.isEmpty(toMsg)) {
                    T.showShort(ChatActivity.this, "发送消息不能为空");
                    return;
                }
                ChatMessage toMsgObj = new ChatMessage();
                toMsgObj.setDate(new Date());
                toMsgObj.setMsg(toMsg);
                toMsgObj.setType(ChatMessage.Type.OUTCOME);
                list.add(toMsgObj);
                chatMsgAdapter.notifyDataSetChanged();
                send_input.setText("");
            }

        });
        chat_face_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                chat_face_viewPager.setVisibility(View.VISIBLE);
                chat_add_gridlayout.setVisibility(View.GONE);
                if (isChatAddAndFaceLLVisible) {
                    chat_add_and_face_llAnimDown();
                    chat_face_btn.setBackgroundResource(R.drawable.chat_face_btn_bg);
                    keyboardStyleAndETFocus(true);
                } else {
                    chat_add_and_face_llAnimUp();
                    chat_face_btn.setBackgroundResource(R.drawable.chat_text_input_btn_bg);
                    keyboardStyleAndETFocus(false);
                }
            }
        });
        chat_add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chat_add_gridlayout.setVisibility(View.VISIBLE);
                chat_face_viewPager.setVisibility(View.GONE);
                if (!isChatAddAndFaceLLVisible)
                    chat_add_and_face_llAnimUp();

            }
        });

    }

    private void keyboardStyleAndETFocus(boolean b) {
        send_input.setFocusable(b);
        send_input.setFocusableInTouchMode(b);
        if (!b)
            send_input.clearFocus();
        else
            send_input.requestFocus();
        new Timer().schedule(new TimerTask() {
                                 public void run() {
                                     InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                     imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                                 }

                             },
                300);

    }

    private void initData() {
        list = new ArrayList();
        ChatMessage formMsgObj = new ChatMessage();
        formMsgObj.setDate(new Date());
        String robotName = getResources().getString(R.string.userName);
        formMsgObj.setMsg("您好，我是" + robotName );
        formMsgObj.setType(ChatMessage.Type.INCOME);
        list.add(formMsgObj);

        chatMsgAdapter = new ChatMsgAdapter(this, list);
        msgs.setAdapter(chatMsgAdapter);
    }


    public void faceClick(View v) {

        Field field;
        int resourceId = 0;
        String num = "1";
        switch (v.getId()) {
            case R.id.moji_text_1_black:
                num = "1";
                break;
            case R.id.moji_text_2_black:
                num = "2";
                break;
            case R.id.moji_text_3_black:
                num = "3";
                break;
            case R.id.moji_text_4_black:
                num = "4";
                break;
            case R.id.moji_text_5_black:
                num = "5";
                break;
            case R.id.moji_text_6_black:
                num = "6";
                break;
            case R.id.moji_text_7_black:
                num = "7";
                break;
            case R.id.moji_text_8_black:
                num = "8";
                break;
            case R.id.moji_text_9_black:
                num = "9";
                break;
            case R.id.moji_text_10_black:
                num = "10";
                break;
            case R.id.moji_text_11_black:
                num = "11";
                break;
            case R.id.moji_text_12_black:
                num = "12";
                break;
            case R.id.moji_text_13_black:
                num = "13";
                break;
            case R.id.moji_text_14_black:
                num = "14";
                break;
            case R.id.moji_text_15_black:
                num = "15";
                break;
            case R.id.moji_text_16_black:
                num = "16";
                break;
            case R.id.moji_text_17_black:
                num = "17";
                break;
            case R.id.moji_text_18_black:
                num = "18";
                break;
            case R.id.moji_text_19_black:
                num = "19";
                break;
            case R.id.moji_text_20_black:
                num = "20";
                break;
            case R.id.moji_text_21_black:
                num = "21";
                break;
            case R.id.moji_text_22_black:
                num = "22";
                break;
            case R.id.moji_text_23_black:
                num = "23";
                break;
            case R.id.moji_text_24_black:
                num = "24";
                break;
            case R.id.moji_text_delete_black:
                num = "0";
                int selectionStart = send_input.getSelectionStart();// 获取光标的位置
                if (selectionStart > 0) {
                    String body = send_input.getText().toString();
                    if (!TextUtils.isEmpty(body)) {
                        String tempStr = body.substring(0, selectionStart);
                        int i = tempStr.lastIndexOf("[");// 获取最后一个表情的位置
                        if (i != -1) {
                            CharSequence cs = tempStr
                                    .subSequence(i, selectionStart);
                            send_input.getEditableText().delete(i,
                                    selectionStart);
                            return;
                        }
                        send_input.getEditableText().delete(
                                tempStr.length() - 1, selectionStart);
                    }
                }
                break;

            default:
                break;
        }
        if (!("0".equals(num))) {
            try {
                field = R.drawable.class.getDeclaredField("moji_text_" + num + "_black");
                resourceId = Integer.parseInt(field.get(null).toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
            bitmap = BitmapFactory.decodeResource(getResources(), resourceId);
            setImgtoText(num);
        }
    }

    private void setImgtoText(String num) {
        imageSpan = new ImageSpan(ChatActivity.this, bitmap);

        String source = face_prefix + num + "]";
        spannableString = new SpannableString(source);
        spannableString.setSpan(imageSpan, 0, source.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        send_input.append(spannableString);
    }

    private void chat_add_and_face_llAnimDown() {
        isChatAddAndFaceLLVisible = false;
        ObjectAnimator.ofFloat(chat_add_and_face_ll, "translationY", chat_add_and_face_ll.getTranslationY(), chat_add_and_face_ll.getTranslationY() + chat_add_and_face_ll.getHeight()).setDuration(300).start();
        ObjectAnimator.ofFloat(chat_send_bottm_bar, "translationY", chat_send_bottm_bar.getTranslationY(), chat_send_bottm_bar.getTranslationY() + chat_add_and_face_ll.getHeight()).setDuration(300).start();
    }

    private void chat_add_and_face_llAnimUp() {
        isChatAddAndFaceLLVisible = true;
        ObjectAnimator.ofFloat(chat_add_and_face_ll, "translationY", chat_add_and_face_ll.getTranslationY(), chat_add_and_face_ll.getTranslationY() - chat_add_and_face_ll.getHeight()).setDuration(300).start();
        ObjectAnimator.ofFloat(chat_send_bottm_bar, "translationY", chat_send_bottm_bar.getTranslationY(), chat_send_bottm_bar.getTranslationY() - chat_add_and_face_ll.getHeight()).setDuration(300).start();
    }

    /*Event of chat_add_gridlayout Btn  */
    @Click(R.id.chat_add_gridlayout_giftBtn)
    public void giftBtnEvent(){
        startActivity(new Intent(ChatActivity.this,GiftShopActivity_.class));
    }
    @Click(R.id.chat_add_gridlayout_guessBtn)
    public void guessBtnEvent(){
        startActivity(new Intent(ChatActivity.this,GuessingActivity_.class));

    }


}
