package com.ramo.campuslive.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ramo.campuslive.R;
import com.ramo.campuslive.view.CheckSwitchButton;

public class SetAdapter extends BaseAdapter{

	private Context context;
	private String[] items;
	private LayoutInflater inflater;

	public SetAdapter(Context context, String[] systemText) {
		this.context = context;
		this.items = systemText;
		this.inflater = LayoutInflater.from(context);
	}
	public int getCount() {
		return items.length;
	}

	public Object getItem(int arg0) {
		return items[arg0];
	}

	public long getItemId(int arg0) {
		return arg0;
	}
	
	public View getView(final int position, View convertView, final ViewGroup parent) {
		final View view;
		final ViewHolder holder;
		if (convertView != null) {
			view = convertView;
			holder = (ViewHolder) view.getTag(position);
		} else {

			view = inflater.inflate(R.layout.activity_set_item, parent, false);
			holder = new ViewHolder();
			holder.text = (TextView) view.findViewById(R.id.other_set_text);
			holder.btn = (CheckSwitchButton) view.findViewById(R.id.other_set_switchBtn);
			holder.img = (ImageView) view.findViewById(R.id.other_set_right);
			if(position>=3){
				holder.img.setVisibility(View.GONE);
			}
			else{
				holder.btn.setVisibility(View.GONE);
			}
			view.setTag(holder);
		}

		if (holder != null) {
			holder.text.setText(items[position]);
			holder.text.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					switch (position) {
					case 0:
					//	context.startActivity(new Intent(context,.class));
						break;
					case 1:
					//	context.startActivity(new Intent(context,.class));
						break;
					case 2:
						parent.getChildAt(3).setVisibility(View.GONE);
						break;
					default:
						break;
					}
				}
			});
			
			holder.btn.setOnCheckedChangeListener(new OnCheckedChangeListener() {

				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
						if(position==2){
							if(holder.btn.isChecked()==false){
								/*Intent intent=new Intent(context,VoiceAirService.class);
								context.stopService(intent);*/
								Toast.makeText(context, "已关闭",0).show();
							}else{
							/*	Intent intent=new Intent(context,VoiceAirService.class);
								context.startService(intent);*/
								Toast.makeText(context, "已开启",0).show();
							}
						}
				}
			});
		}

		return view;
	}
	class ViewHolder {
		TextView text;
		CheckSwitchButton btn;
		ImageView img;
	}

}
