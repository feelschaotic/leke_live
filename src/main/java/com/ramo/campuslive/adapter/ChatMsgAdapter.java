package com.ramo.campuslive.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ramo.campuslive.R;
import com.ramo.campuslive.bean.ChatMessage;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.List;


public class ChatMsgAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private List<ChatMessage> msgs;
    private Context context;

    public ChatMsgAdapter(Context context, List<ChatMessage> msgs) {
        inflater = LayoutInflater.from(context);
        this.msgs = msgs;
        this.context = context;

    }

    @Override
    public int getCount() {
        return msgs.size();
    }

    @Override
    public Object getItem(int arg0) {
        return msgs.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        return arg0;
    }

    @Override
    public int getItemViewType(int position) {

        ChatMessage msg = msgs.get(position);
        if (msg.getType() == ChatMessage.Type.INCOME)
            return 0;
        return 1;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ChatMessage msg = msgs.get(position);
        ViewHolder vh = null;
        if (convertView == null) {
            vh = new ViewHolder();
            if (getItemViewType(position) == 1) {
                convertView = inflater.inflate(R.layout.chat_item_to, parent, false);
                vh.date = (TextView) convertView.findViewById(R.id.item_to_msg_date);
                vh.msg = (TextView) convertView.findViewById(R.id.to_msg_text);
            } else {
                convertView = inflater.inflate(R.layout.chat_item_form, parent, false);
                vh.date = (TextView) convertView.findViewById(R.id.item_form_msg_date);
                vh.msg = (TextView) convertView.findViewById(R.id.form_msg_text);
            }
            convertView.setTag(vh);
        } else
            vh = (ViewHolder) convertView.getTag();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        vh.date.setText(sdf.format(msg.getDate()));
        filterFace(vh.msg, msg.getMsg());

        return convertView;
    }

    private ImageSpan imageSpan;
    private SpannableString spannableString;
    private Bitmap bitmap;
    private String face_prefix = "[face_";
    private int resourceId = 0;


    private void filterFace(TextView msgTv, String msgMsg) {
        msgTv.setText("");
        String[] arry = msgMsg.split("]");
        for (int i = 0; i < arry.length; i++) {
            int faceBeginIndex = arry[i].indexOf(face_prefix);
            if (faceBeginIndex == -1) {
                msgTv.append(arry[i]);
                continue;
            }
           int num = Integer.parseInt(arry[i].substring(faceBeginIndex + face_prefix.length()));
            msgTv.append(arry[i].substring(0, faceBeginIndex));
            try {
                Field field = R.drawable.class.getDeclaredField("moji_text_" + num + "_black");
                resourceId = Integer.parseInt(field.get(null).toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
            bitmap = BitmapFactory.decodeResource(context.getResources(), resourceId);
            imageSpan = new ImageSpan(context, bitmap);

            String source = face_prefix + num + "]";
            spannableString = new SpannableString(source);
            spannableString.setSpan(imageSpan, 0, source.length(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            msgTv.append(spannableString);

        }

    }

    private final class ViewHolder {
        TextView date;
        TextView msg;
    }

}
