package com.juss.mediaplay;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.app.Activity;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.ramo.campuslive.R;
import com.ramo.campuslive.utils.L;
import com.ramo.campuslive.utils.T;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class CharityAuthenticateActivity extends Activity {

    private EditText societies_name;
    private EditText societies_fu_name;
    private EditText societies_fu_cal;
    private EditText societies_introduction;

    private ImageView apply_welfare_school_iv;
    private ImageView apply_welfare_theach_iv;

    private TextView tv_apply_societies;

    private final int REQUEST_IDCARD_CODE = 10;
    private final int REQUEST_PRO_CERT_CODE = 11;
    private final int REQUEST_COVER_CODE = 12;

    private String[] filePath = new String[2];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authenticate);
        init();
    }

    private void init() {
        societies_name = (EditText) findViewById(R.id.societies_name);
        societies_fu_name = (EditText) findViewById(R.id.societies_fu_name);
        societies_fu_cal = (EditText) findViewById(R.id.societies_fu_cal);
        societies_introduction = (EditText) findViewById(R.id.societies_introduction);

        apply_welfare_school_iv = (ImageView) findViewById(R.id.apply_welfare_school_iv);
        apply_welfare_theach_iv = (ImageView) findViewById(R.id.apply_welfare_theach_iv);

        tv_apply_societies = (TextView) findViewById(R.id.tv_apply_societies);
        initListener();

    }

    private void initListener() {
        apply_welfare_school_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSysPhotoAlbum(REQUEST_IDCARD_CODE);
            }
        });
        apply_welfare_theach_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSysPhotoAlbum(REQUEST_PRO_CERT_CODE);
            }
        });

        tv_apply_societies.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParams();
                //异步保存至服务器
            }
        });
    }

    private void openSysPhotoAlbum(int code) {
        Intent intent = new Intent();
        intent.setType("image/*");
        if (Build.VERSION.SDK_INT < 19) {
            intent.setAction(Intent.ACTION_GET_CONTENT);
        } else {
            intent.setAction(Intent.ACTION_PICK);
        }
        startActivityForResult(intent, code);
    }
    public void getParams() {

        String societiesNname = societies_name.getText().toString().trim();
        String societiesFuName = societies_fu_name.getText().toString().trim();
        String societiesFuCal = societies_fu_cal.getText().toString().trim();
        String societiesIntroduction = societies_introduction.getText().toString().trim();

        if (societiesNname.equals("")) {
            T.showShort(CharityAuthenticateActivity.this, "请输入社团名称");
            return;
        } else if (societiesFuName.equals("")) {
            T.showShort(CharityAuthenticateActivity.this, "请输入社团负责人姓名");
            return;
        }else if (societiesFuCal.equals("")) {
            T.showShort(CharityAuthenticateActivity.this, "请输入社团负责人联系电话");
            return;
        }else if (societiesIntroduction.equals("")) {
            T.showShort(CharityAuthenticateActivity.this, "请输入社团简介");
            return;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (null != data) {
            Uri uri = data.getData();
            String picPath = null;
            L.d(uri.toString());
            try{
                String[] pojo = {MediaStore.Images.Media.DATA};
                Cursor cursor = managedQuery(uri, pojo, null, null,null);
                if(cursor != null )
                {
                    int columnIndex = cursor.getColumnIndexOrThrow(pojo[0]);
                    cursor.moveToFirst();
                    picPath = cursor.getString(columnIndex);
                    if(Build.VERSION.SDK_INT<14)
                        cursor.close();
                }

            }catch (Exception e){
                e.printStackTrace();
            }

            InputStream inputStream = null;
            // L.i(Environment.getExternalStorageDirectory().toString());
            try {
                inputStream = getContentResolver().openInputStream(uri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                //  L.i(requestCode+"");
                switch (requestCode) {
                    case REQUEST_IDCARD_CODE:
                        filePath[0]=picPath;
                        apply_welfare_school_iv.setImageBitmap(bitmap);
                        break;
                    case REQUEST_PRO_CERT_CODE:
                        filePath[1]=picPath;
                        apply_welfare_theach_iv.setImageBitmap(bitmap);
                        break;

                    default:
                        break;
                }
                //     L.i("ok");
            } catch (FileNotFoundException e) {
                L.i(e.toString());
                e.printStackTrace();
            }
        }
    }




}
