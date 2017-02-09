package com.ramo.campuslive;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;


public class SwipeBackActivity extends Activity {
//	protected SwipeBackLayout layout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	/*	layout = (SwipeBackLayout) LayoutInflater.from(this).inflate(
				R.layout.swipe_back_base, null);
		layout.attachToActivity(this);*/
	}
	
	
	@Override
	public void startActivity(Intent intent) {
		super.startActivity(intent);
		overridePendingTransition(R.anim.in_from_right, R.anim.keep);
	}

	// Press the back button in mobile phone
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		overridePendingTransition(0, R.anim.base_slide_right_out);
	}


	@Override
	public void finish() {
		super.finish();
		overridePendingTransition(0, R.anim.base_slide_right_out);
	}
}
