package com.ramo.campuslive;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.ramo.campuslive.utils.L;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * Created by ramo on 2016/8/2.
 */
@EActivity(R.layout.activity_add_community)
public class AddCommunityActivity extends  SwipeBackActivity{
    @ViewById
    Button add_community_btn;

    @ViewById
    EditText socityName;
    @ViewById
    EditText socityDesc;
    @ViewById
    ImageView socityLogo;
    @ViewById
    ImageView begin_live_back_iv;
    @ViewById
    TextView add_comm_desc_length_tv;
    int maxDesc=200;

    private final int REQUEST_LOGO_CODE = 12;

    @AfterViews
    public void init() {
        add_comm_desc_length_tv.setText(socityDesc.length()+" / "+maxDesc);
        initListener();
    }

    private void initListener() {
        add_community_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addCommunityToServer();
            }
        });
        socityLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadIogo();
            }
        });
        begin_live_back_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
     //   socityDesc
    }

    private void uploadIogo() {
        Intent intent = new Intent();
        intent.setType("image/*");
        if (Build.VERSION.SDK_INT < 19) {
            intent.setAction(Intent.ACTION_GET_CONTENT);
        } else {
            intent.setAction(Intent.ACTION_PICK);
        }
        startActivityForResult(intent, REQUEST_LOGO_CODE);
    }

    private void addCommunityToServer() {
        String title=socityName.getText().toString();
        String desc =socityDesc.getText().toString();
        //socityLogo
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (null != data) {
            Uri uri = data.getData();
            String picPath = null;
            L.d(uri.toString());
            try {
                String[] pojo = {MediaStore.Images.Media.DATA};
                Cursor cursor = managedQuery(uri, pojo, null, null, null);
                if (cursor != null) {
                    int columnIndex = cursor.getColumnIndexOrThrow(pojo[0]);
                    cursor.moveToFirst();
                    picPath = cursor.getString(columnIndex);
                    if (Build.VERSION.SDK_INT < 14)
                        cursor.close();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            InputStream inputStream = null;

            try {
                inputStream = getContentResolver().openInputStream(uri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                switch (requestCode) {
                    case REQUEST_LOGO_CODE:

                        break;

                }

            } catch (FileNotFoundException e) {
                L.i(e.toString());
                e.printStackTrace();
            }
        }
    }
}
