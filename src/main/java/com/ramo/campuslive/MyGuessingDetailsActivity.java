package com.ramo.campuslive;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.widget.GridView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.juss.mediaplay.entity.GuessingOption;
import com.juss.mediaplay.entity.MyGuess;
import com.juss.mediaplay.listener.HttpCallbackListener;
import com.juss.mediaplay.utils.NetUtil;
import com.ramo.campuslive.adapter.MyGuessingDetailsOptionsAdapter;
import com.ramo.campuslive.application.MyApplication;
import com.ramo.campuslive.server.ServerConstants;
import com.ramo.campuslive.utils.L;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by ramo on 2016/7/6.
 */
@EActivity(R.layout.activity_my_guessing_details)
public class MyGuessingDetailsActivity extends SwipeBackActivity {

    @ViewById
    GridView my_guess_gv;
    @ViewById
    TextView my_guessimg_title;
    @ViewById
    TextView my_guessimg_time;
    @ViewById
    TextView my_guessimg_joinNum;
    @ViewById
    TextView my_guess_state;

    private final int SUCCESS=1;
    private int gussingId;
    private String guess_name;
    private Handler myGuessingHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case SUCCESS:
                    String request = (String) msg.obj;
                    try {
                        JSONObject object = new JSONObject(request);
                        String ob = object.getString("options");
                        Gson gson = new Gson();
                        List<GuessingOption> options = gson.fromJson(ob,new TypeToken<List<GuessingOption>>(){}.getType());
                        L.e(options.size()+"");
                        my_guess_gv.setAdapter(new MyGuessingDetailsOptionsAdapter(MyGuessingDetailsActivity.this, options,guess_name));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
            }



            super.handleMessage(msg);
        }
    };

    @AfterViews
    public void init() {
        MyGuess myGuessing = getIntent().getParcelableExtra(MyApplication.MY_GUESS_DETAILS_EXTRA);
        if (myGuessing == null)
            return;

        Intent intent = getIntent();
        gussingId=intent.getIntExtra("gussingId",0);
        guess_name = intent.getStringExtra("guess_name");

        L.e(gussingId+"");
        L.e(guess_name+"");

        my_guessimg_time.setText(myGuessing.getBetTime().toLocaleString());

        my_guessimg_title.setText(myGuessing.getGuessingTitle());
        my_guess_state.setText(myGuessing.getGuessingResult());
        String[] name= new String[]{"guessingId"};
        String[] val= new String[]{gussingId+""};
        NetUtil.sendRequestToUrl(ServerConstants.GessingDetailsURL, name, val, new HttpCallbackListener() {
            @Override
            public void onSucc(String response) {
                L.e(response);
                Message msg = Message.obtain();
                msg.obj=response;
                msg.what=SUCCESS;
                myGuessingHandler.sendMessage(msg);

            }

            @Override
            public void onError(Exception e) {
                L.e(e.getMessage());

                myGuessingHandler.sendEmptyMessage(0);

            }
        });


//       my_guess_gv.setAdapter(new MyGuessingDetailsOptionsAdapter(this, myGuessing));
    }
}
