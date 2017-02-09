package com.ramo.campuslive;

import android.widget.ListView;

import com.ramo.campuslive.adapter.SetAdapter;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_set_layout)
public class SetActivity extends SwipeBackActivity{
	
	private ListView other_set_listview;
	private String systemText[] = {  
			"-------------","XXXXXXXX","通知","接收推荐推送","主播开播" };
	
	@AfterViews
	public void initView() {
		
		other_set_listview = (ListView) findViewById(R.id.other_set_listview);
		other_set_listview.setAdapter(new SetAdapter(SetActivity.this, systemText));
	}

	
}
