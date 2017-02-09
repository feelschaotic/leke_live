package com.ramo.campuslive;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.juss.mediaplay.utils.UpLoadFiles;
import com.juss.mediaplay.utils.Utils;
import com.ramo.campuslive.server.ServerConstants;
import com.ramo.campuslive.utils.L;
import com.ramo.campuslive.utils.T;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ramo on 2016/6/19.
 */
@EActivity(R.layout.activity_apply_host_layout)
public class ApplyForHostActivity extends SwipeBackActivity {


    @ViewById
    ImageView apply_host_upload_idcard_iv;
    @ViewById
    ImageView apply_host_upload_professional_certificates_iv;
    @ViewById
    ImageView apply_host_upload_cover_iv;

    //申请当主播的真实姓名与身份证号
    private EditText realName;
    private EditText idCard;
    private TextView appaytohost;
    //申请当主播的请求字段
    private String[] paramsval;
    private ArrayList<File> filesList;
    private File idcarImg;
    private File scendIdcardImg;
    private File coverImg;
    private String[] filePath=new String[3];


    private boolean hasShootPic = false;
    private final int REQUEST_IDCARD_CODE = 10;
    private final int REQUEST_PRO_CERT_CODE = 11;
    private final int REQUEST_COVER_CODE = 12;

    @AfterViews
    public void init() {
        realName = (EditText) findViewById(R.id.realName);
        idCard = (EditText) findViewById(R.id.idCard);
        appaytohost = (TextView) findViewById(R.id.tv_apply_host);
        initListener();

    }
    private void initListener() {
        apply_host_upload_idcard_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSysPhotoAlbum(REQUEST_IDCARD_CODE);
            }
        });
        apply_host_upload_professional_certificates_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSysPhotoAlbum(REQUEST_PRO_CERT_CODE);
            }
        });
        apply_host_upload_cover_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSysPhotoAlbum(REQUEST_COVER_CODE);
            }
        });
        //提交申请当主播请求
        appaytohost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParams();
                L.i("ok");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        //,Map<String,String> params,String[] paramskey,ArrayList<File> files,String[] filesNames
                        filesList = new ArrayList<File>();
                        for (int i = 0; i <filePath.length ; i++) {
                            String proPath = filePath[i].substring(0,filePath[i].lastIndexOf('/'));
                            String name = filePath[i].substring(filePath[i].lastIndexOf('/')+1);
                            File file = new File(proPath,name);
                            filesList.add(file);
                        }
                        /*//参数内容
                        String[] param ={"10003","10003","sijing"};*/
                        //参数名称
                        String[] paramskey = {"userId", "IdCard", "realName"};
                        //文件名称
                        String[] filesNames = {"IdCardImg", "proImg", "liveImg"};
                        new UpLoadFiles().uploadFiles( ServerConstants.ApplyForHostURL, paramsval, paramskey, filesList, filesNames);
//                        new UpLoadFiles().uploadFiles(ServerConstants.ProUrl+ServerConstants.ApplyForHostURL, params, filesList);
                    }
                }).start();
            }
        });
    }

    /**
     * 身份证正则判断
     * @param text
     * @return
     */
    public boolean personIdValidation(String text) {
        String regx = "[0-9]{17}x";
        String reg1 = "[0-9]{15}";
        String regex = "[0-9]{18}";
        return text.matches(regx) || text.matches(reg1) || text.matches(regex);
    }
    public void getParams(){

        String idcard = idCard.getText().toString().trim();
        String realname = realName.getText().toString().trim();
       //对用户输入的身份证号和真实姓名进行判断
        if(realname.equals("")){
            T.showShort(ApplyForHostActivity.this,"请输入您的真实姓名");
            return ;
        }else if(idcard.equals("")){
            T.showShort(ApplyForHostActivity.this,"请输入您的二代身份证号码");
            return ;
        }else if(!personIdValidation(idcard)){
                ///^(\d{15}$|^\d{18}$|^\d{17}(\d|X|x))$/
            L.i("请输入正确的身份证号码"+personIdValidation(idcard));
            return;
        }else{
            paramsval=new String[3];
            paramsval[0]="10003";//用户ID
            paramsval[1]=idcard;
            paramsval[2]=realname;
        }
       // L.i(Utils.getIsIdcar(idcard)+"");


        /*params = new HashMap<String,String>();
       // String sIdcard = idCard.getText().toString(), srealName = realName.getText().toString();
        params.put("userId", "10003");
        params.put("IdCard", "hhhhh");
        params.put("realName", "bbbb");

        filesList = new ArrayList<File>();
        File file = new File("/storage/emulated/0/baidu/searchbox/preset/preset4.2/pic","quick_search_widget_night.jpg");
        File file2 = new File("/storage/emulated/0/baidu/searchbox/preset/preset4.2/pic","quick_search_widget_morning.jpg");
       // File file3 = new File("/storage/emulated/0/baidu/searchbox/preset/preset4.2/pic","quick_search_widget_evening.jpg");
        filesList.add(file);
        filesList.add(file2);
        //filesList.add(file3);
        L.i(file.toString());*/
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

           /* L.i(picPath.substring(0,picPath.lastIndexOf('/')));
            L.i(picPath.substring(picPath.lastIndexOf('/')+1));*/
            InputStream inputStream = null;
           // L.i(Environment.getExternalStorageDirectory().toString());
            try {
                inputStream = getContentResolver().openInputStream(uri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
              //  L.i(requestCode+"");
                switch (requestCode) {
                    case REQUEST_IDCARD_CODE:
                        filePath[0]=picPath;
                        apply_host_upload_idcard_iv.setImageBitmap(bitmap);
                        break;
                    case REQUEST_PRO_CERT_CODE:
                        filePath[1]=picPath;
                        apply_host_upload_professional_certificates_iv.setImageBitmap(bitmap);
                        break;
                    case REQUEST_COVER_CODE:
                        filePath[2]=picPath;
                        apply_host_upload_cover_iv.setImageBitmap(bitmap);
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



    /**
     * 选择图片后，获取图片的路径
     * @param requestCode
     * @param data
     */
    private void doPhoto(int requestCode,Intent data)
    {
        Uri photoUri;
        String picPath;
        /*if(requestCode == SELECT_PIC_BY_PICK_PHOTO )  //从相册取图片，有些手机有异常情况，请注意
        {*/
            if(data == null)
            {
                Toast.makeText(this, "选择图片文件出错", Toast.LENGTH_LONG).show();
                return;
            }
            photoUri = data.getData();
            if(photoUri == null )
            {
                Toast.makeText(this, "选择图片文件出错", Toast.LENGTH_LONG).show();
                return;
            }
//        }
        String[] pojo = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(photoUri, pojo, null, null,null);
        if(cursor != null )
        {
            int columnIndex = cursor.getColumnIndexOrThrow(pojo[0]);
            cursor.moveToFirst();
            picPath = cursor.getString(columnIndex);
            cursor.close();
        }
       // Log.i(TAG, "imagePath = "+picPath);
       /* if(picPath != null && ( picPath.endsWith(".png") || picPath.endsWith(".PNG") ||picPath.endsWith(".jpg") ||picPath.endsWith(".JPG")  ))
        {
            lastIntent.putExtra(KEY_PHOTO_PATH, picPath);
            setResult(Activity.RESULT_OK, lastIntent);
            finish();
        }else{
            Toast.makeText(this, "选择图片文件不正确", Toast.LENGTH_LONG).show();
        }*/
    }
}
