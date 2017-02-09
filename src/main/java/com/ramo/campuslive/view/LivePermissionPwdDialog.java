package com.ramo.campuslive.view;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.ramo.campuslive.R;
import com.ramo.campuslive.utils.L;

/**
 * Created by ramo on 2016/9/2.
 */
public class LivePermissionPwdDialog extends Dialog {
    public LivePermissionPwdDialog(Context context) {
        super(context);
    }

    public LivePermissionPwdDialog(Context context, int theme) {
        super(context, theme);
    }

    public static class Builder {
        private Context context;
        private String title;
        private View contentView;
        private OnClickListener negativeButtonClickListener;
        View layout;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder setTitle(int title) {
            this.title = (String) context.getText(title);
            return this;
        }

        public Builder setContentView(View v) {
            this.contentView = v;
            return this;
        }

        public Builder setNegativeButton(
                OnClickListener listener) {
            this.negativeButtonClickListener = listener;
            return this;
        }


        public LivePermissionPwdDialog create() {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final LivePermissionPwdDialog dialog = new LivePermissionPwdDialog(context, R.style.Dialog);
            layout = inflater.inflate(R.layout.dialog_live_permission_pwd, null);
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.addContentView(layout, params);
            ((TextView) layout.findViewById(R.id.title)).setText(title);

            if (negativeButtonClickListener != null) {
                ((ImageView) layout.findViewById(R.id.negativeButton))
                        .setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {
                                negativeButtonClickListener.onClick(dialog,
                                        DialogInterface.BUTTON_NEGATIVE);
                            }
                        });
            }
            onListener();
            dialog.setContentView(layout);
            return dialog;
        }

        protected int[] live_permissions_tv_ids = {
                R.id.live_permissions_tv_pass1,
                R.id.live_permissions_tv_pass2,
                R.id.live_permissions_tv_pass3,
                R.id.live_permissions_tv_pass4};

        protected EditText[] live_permissions_tvs;

        private void onListener() {
            live_permissions_tvs = new EditText[live_permissions_tv_ids.length];
            for (int i = 0; i < live_permissions_tv_ids.length; i++) {
                live_permissions_tvs[i] = (EditText) layout.findViewById(live_permissions_tv_ids[i]);
            }
            final boolean[] pwIsNotNull = new boolean[live_permissions_tvs.length];

            for (int i = 0; i < live_permissions_tvs.length; i++) {
                final int j = i + 1;
                live_permissions_tvs[i].addTextChangedListener(new TextWatcher() {
                    private CharSequence temp;

                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        temp = s;
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        L.e("temp.length()" + temp.length());
                        if (temp.length() == 1) {
                            pwIsNotNull[j - 1] = true;
                            if (j < live_permissions_tv_ids.length)
                                setFocus(j);
                        } else
                            pwIsNotNull[j - 1] = false;
                    }

                });
                live_permissions_tvs[i].setOnKeyListener(new View.OnKeyListener() {
                    @Override
                    public boolean onKey(View v, int keyCode, KeyEvent event) {
                        if (keyCode == KeyEvent.KEYCODE_DEL
                                && event.getAction() == KeyEvent.ACTION_DOWN) {
                            if (pwIsNotNull[j - 1] && j - 1 >= 0)
                                resetEditText(j - 1);
                            else if (!pwIsNotNull[j - 1] && j - 2 >= 0)
                                resetEditText(j - 2);

                            return true;
                        }
                        return false;
                    }

                    private void resetEditText(int num) {
                        live_permissions_tvs[num].setText("");
                        setFocus(num);
                        pwIsNotNull[num] = false;
                    }
                });
            }
        }

        public void setFocus(int j) {
            live_permissions_tvs[j].setFocusable(true);
            live_permissions_tvs[j].setFocusableInTouchMode(true);
            live_permissions_tvs[j].requestFocus();
        }
    }
}