package com.juss.live.skin.ui;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.DialogInterface.OnKeyListener;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.letv.recorder.bean.LivesInfo;
import com.letv.recorder.util.ReUtils;

import java.util.ArrayList;

public class RecorderDialogBuilder {

	public static final String debug_ip = "debugIp";
	public static final String key_machine = "liveInfo";
	private static int backNum = 0;

	public static Dialog showMobileNetworkWarningDialog(Context context, OnClickListener positiveListener,OnClickListener negetiveListener) {
		String content = "当前非wifi环境，将产生运营商流量费，是否继续?";
		return showCommentDialog(context, content, "确定","取消", positiveListener,negetiveListener);
	}

	public static Dialog showCommentDialog(Context context, String content, String btnName,String btnCancelName, OnClickListener positiveListener, OnClickListener negitiveListener) {

		View view = LayoutInflater.from(context).inflate(ReUtils.getLayoutId(context, "letv_recorder_common_dialog"), null);

		TextView textV = (TextView) view.findViewById(ReUtils.getId(context, "letv_recorder_dialog_text"));
		textV.setText(content);

		TextView btn = (TextView) view.findViewById(ReUtils.getId(context, "letv_recorder_dialog_positive"));
		btn.setText(btnName);
		btn.setOnClickListener(positiveListener);

		TextView btnCancel = (TextView) view.findViewById(ReUtils.getId(context, "letv_recorder_dialog_negtive"));
		if (negitiveListener != null) {
			btnCancel.setVisibility(View.VISIBLE);
			btnCancel.setText(btnCancelName);
			btnCancel.setOnClickListener(negitiveListener);
		} else {
			btnCancel.setVisibility(View.GONE);
		}

		Dialog dialog = new Dialog(context, ReUtils.getStyleId(context, "letvRecorderDialog"));
		Window win = dialog.getWindow();
		LayoutParams lp = win.getAttributes();
		lp.width = LayoutParams.WRAP_CONTENT;
		lp.height = LayoutParams.WRAP_CONTENT;
		dialog.setContentView(view, lp);
		dialog.setCanceledOnTouchOutside(false);
		dialog.show();

		return dialog;

	}
	public static Dialog showLoadDialog(final Context context,String msg){
		ProgressDialog loadDialog = new ProgressDialog(context);
		loadDialog.setCanceledOnTouchOutside(false);
		loadDialog.setOnKeyListener(new OnKeyListener() {
			
			@Override
			public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_BACK &&  event.getRepeatCount() == 0) {
					if(context instanceof Activity){
						if(backNum == 2 ){
							backNum = 0;
							((Activity)context).finish();
						}else{
							Toast.makeText(context, "再按一次退出!", 0).show();
							backNum ++;
						}
					}
					return true;
				} else {
					return false;
				}
			}
		});
		loadDialog.setMessage(msg);
		loadDialog.show();
		return loadDialog;
	}

	public static void showDebugDialog(Context context, Bundle bundle) {
		View view = LayoutInflater.from(context).inflate(ReUtils.getLayoutId(context, "letv_recorder_debug_view"), null);

		TextView debugIp = (TextView) view.findViewById(ReUtils.getId(context, "letv_debug_ip"));
		debugIp.setText(bundle.getString(debug_ip));

		Dialog dialog = new Dialog(context, ReUtils.getStyleId(context, "letvRecorderDialog"));

		Window win = dialog.getWindow();
		LayoutParams lp = win.getAttributes();
		lp.width = LayoutParams.WRAP_CONTENT;
		lp.height = LayoutParams.WRAP_CONTENT;
		dialog.setContentView(view);
		dialog.show();

	}

	public static RecorderDialog showMachineDialog(Context context, Bundle bundle) {

		RecorderMachineView view = new RecorderMachineView(context);
		if (bundle != null) {
			view.setRecorderInfo((ArrayList<LivesInfo>) bundle.get(key_machine));
		}

		RecorderDialog dialog = new RecorderDialog(context, ReUtils.getStyleId(context, "letvRecorderDialog"));

		Window win = dialog.getWindow();
		LayoutParams lp = win.getAttributes();
		lp.width = LayoutParams.WRAP_CONTENT;
		lp.height = LayoutParams.WRAP_CONTENT;
		dialog.setContentView(view);
		dialog.setObservable(view.getMachineObserable());

		/**
		 * 消失时的监听
		 */
		dialog.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss(DialogInterface dialog) {

			}
		});
		dialog.show();

		return dialog;
	}

	////////////////////////////////////////////////////////////////////////
}
