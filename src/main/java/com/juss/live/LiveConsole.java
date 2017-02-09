package com.juss.live;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;
import com.juss.mediaplay.danmaku.BackgroundCacheStuffer;
import com.juss.mediaplay.entity.GuessingVO;
import com.juss.mediaplay.entity.VideoItemJson;
import com.juss.mediaplay.listener.HttpCallbackListener;
import com.juss.mediaplay.utils.BarrageUtil;
import com.juss.mediaplay.utils.GsonDateUtil;
import com.juss.mediaplay.utils.NetUtil;
import com.letv.recorder.util.ReUtils;
import com.ramo.campuslive.GuessingActivity_;
import com.ramo.campuslive.R;
import com.ramo.campuslive.adapter.LiveGiftAdapter;
import com.ramo.campuslive.adapter.LiveGuessAdapter;
import com.ramo.campuslive.bean.Gift;
import com.ramo.campuslive.server.ServerConstants;
import com.ramo.campuslive.utils.ImageManageUtil;
import com.ramo.campuslive.utils.L;
import com.ramo.campuslive.utils.T;
import com.ramo.campuslive.view.CustomDialog;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import master.flame.danmaku.controller.IDanmakuView;
import master.flame.danmaku.danmaku.loader.ILoader;
import master.flame.danmaku.danmaku.loader.IllegalDataException;
import master.flame.danmaku.danmaku.loader.android.DanmakuLoaderFactory;
import master.flame.danmaku.danmaku.model.BaseDanmaku;
import master.flame.danmaku.danmaku.model.DanmakuTimer;
import master.flame.danmaku.danmaku.model.IDisplayer;
import master.flame.danmaku.danmaku.model.android.BaseCacheStuffer;
import master.flame.danmaku.danmaku.model.android.DanmakuContext;
import master.flame.danmaku.danmaku.model.android.Danmakus;
import master.flame.danmaku.danmaku.parser.BaseDanmakuParser;
import master.flame.danmaku.danmaku.parser.IDataSource;
import master.flame.danmaku.danmaku.parser.android.BiliDanmukuParser;
import master.flame.danmaku.danmaku.util.IOUtils;
import master.flame.danmaku.ui.widget.DanmakuView;

/**
 * Created by ramo on 2016/8/4.
 */
public class LiveConsole {
    private static final int OPEN = 1;
    private static final int CLOSE = 0;
    /*底部导航栏弹幕控件*/
    private Button btn_swich;
    private Button add_dang;
    private Button btn_dang;
    private Button btn_seter;
    private EditText dangtext;
    private boolean isSeting;
    private boolean isdang;

    private BaseDanmakuParser mParser;//解析器对象
    private IDanmakuView mDanmakuView;
    private DanmakuContext danmakuContext;
    private BarrageUtil barrageUtil;
    private int dancolor = 16777215;

    private RadioGroup dang_color;
    private Button dang_close;
    private RadioGroup scrollto;
    private RadioButton dang_all;
    private RadioButton dang_top;
    private RadioButton dang_bottom;
    private LinearLayout dangmu_layout;

    private LinearLayout setLaout;
    /*end*/
    private Context context;
    private View activity;

    private ImageView giftView;
    private ImageView guessView;
    /*竞猜对象*/
    private ListView live_guess_lv;
    private LinearLayout guessListLL = null;
    /*送礼对象*/
    private RelativeLayout giftListRL = null;
    private String giftName;
    ImageView iv;
    /**/

    private Handler changeDanMuStyleHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    reseDanmuView(msg.arg1);
                    break;
                default:
                    break;
            }

        }
    };
    private List<GuessingVO> guessingVOList;
    private Handler guessListHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                GsonDateUtil dateUtil = new GsonDateUtil();
                guessingVOList = dateUtil.getGsonDate().fromJson((String) msg.obj, new TypeToken<List<GuessingVO>>() {
                }.getType());
                //  T.showShort(context, "竞猜列表加载成功");
                live_guess_lv.setAdapter(new LiveGuessAdapter(context, guessingVOList));
            } else {
                T.showShort(context, "竞猜加载失败");
            }
        }
    };

    /*end*/
    private Handler giftAnimHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            iv.destroyDrawingCache();
            ((RelativeLayout) activity).removeView(iv);
            //加弹幕
            addGiftDanMu();
        }
    };
    private Button live_add_guess;


    public LiveConsole(Context context, View activity) {
        this.context = context;
        this.activity = activity;
        isSeting = false;
        isdang = false;
        SpeechUtility.createUtility(context, SpeechConstant.APPID + "=57a92eb8");

    }

    public void initMyDanMuView() {
        guessListLL = (LinearLayout) activity.findViewById(ReUtils.getId(context, "guessListLL"));
        giftListRL = (RelativeLayout) activity.findViewById(ReUtils.getId(context, "giftListRL"));
        live_guess_lv = (ListView) guessListLL.findViewById(R.id.live_guess_lv);

        btn_swich = (Button) activity.findViewById(R.id.btn_swich);
        dangtext = (EditText) activity.findViewById(R.id.dan_text);
        btn_dang = (Button) activity.findViewById(R.id.btn_dang);
        btn_seter = (Button) activity.findViewById(R.id.btn_seter);
        add_dang = (Button) activity.findViewById(R.id.add_dang);
        setLaout = (LinearLayout) activity.findViewById(R.id.seting);
        dang_close = (Button) activity.findViewById(R.id.dang_close);

        dang_color = (RadioGroup) activity.findViewById(R.id.dang_color);
        scrollto = (RadioGroup) activity.findViewById(R.id.scrollto);

        initGiftAndGuessView();
        initGuessListData();

        initdanmu();
        barrageUtil = new BarrageUtil(mDanmakuView, mParser, danmakuContext);
        initListener();
    }

    private void initGiftAndGuessView() {
        guessView = (ImageView) activity.findViewById(ReUtils.getId(context, "imgV_guess"));
        giftView = (ImageView) activity.findViewById(ReUtils.getId(context, "imgV_gift"));
        live_add_guess = (Button) guessListLL.findViewById(ReUtils.getId(context, "live_add_guess"));
    }

    private void initListener() {


        btn_seter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                L.e("btn_seter click");
                isSeting = !isSeting;
                if (isSeting) {
                    btn_seter.setBackgroundResource(R.drawable.myyouku_setting02);
                    setLaout.setVisibility(View.VISIBLE);
                } else {
                    btn_seter.setBackgroundResource(R.drawable.myyouku_setting);
                    setLaout.setVisibility(View.GONE);
                }
            }
        });

        add_dang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String dangcontent = dangtext.getText().toString();
                if (null != dangcontent) {
                    barrageUtil.myaddDanmaku(dangcontent, dancolor, true);
                    dangtext.setText("");
                }
                barrageUtil.addDanmaku(true);
            }
        });

        btn_swich.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tospeker();
            }
        });
        btn_dang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isdang = !isdang;

                if (isdang) {
                    mDanmakuView.show();
                    btn_dang.setBackgroundResource(R.drawable.dang_on);
                } else {
                    btn_dang.setBackgroundResource(R.drawable.dang_off);
                    mDanmakuView.hide();
                }

            }
        });

        /*弹幕*/
        dang_color.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.red:
                        dancolor = 16711680;
                        break;
                    case R.id.white:
                        dancolor = 16777215;
                        break;
                    case R.id.blue:
                        dancolor = 255;
                        break;
                    case R.id.gree:
                        dancolor = 65280;
                        break;
                    case R.id.lv:
                        dancolor = 65535;
                        break;
                    case R.id.mured:
                        dancolor = 16711935;
                        break;
                }
            }
        });
        scrollto.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                Message msg = new Message();
                msg.what = 1;
                if (checkedId == R.id.dang_all) {
                    //danHandler.sendEmptyMessage(1);
                    msg.arg1 = 1;

                } else if (checkedId == R.id.dang_bottom) {
                    //danHandler.sendEmptyMessage(2);
                    msg.arg1 = 2;
                } else if (checkedId == R.id.dang_top) {
                    //danHandler.sendEmptyMessage(3);
                    msg.arg1 = 3;

                }
                changeDanMuStyleHandler.sendMessage(msg);

            }
        });
        dang_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isdang = !isdang;
                btn_seter.setBackgroundResource(R.drawable.myyouku_setting);
                setLaout.setVisibility(View.GONE);
            }
        });

        initGiftAndGuessListener();
        initGiftListData();
    }

    private void initGiftAndGuessListener() {
        guessListLL.findViewById(R.id.live_guess_closeBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                objectAnim(guessListLL, CLOSE);
            }
        });

        guessView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                objectAnim(guessListLL, OPEN);
            }
        });
        giftView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                objectAnim(giftListRL, OPEN);
            }
        });
        giftListRL.findViewById(R.id.live_gift_closeBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                objectAnim(giftListRL, CLOSE);
            }
        });
        live_guess_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // GuessingVO guessingVO = guessingVOList.get(position);
            }
        });
        live_add_guess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, GuessingActivity_.class));
            }
        });
    }

    private void objectAnim(View finalGuessListLL, int tag) {
        if (tag == CLOSE)
            ObjectAnimator.ofFloat(finalGuessListLL, "translationX", finalGuessListLL.getTranslationX(), finalGuessListLL.getTranslationX() + finalGuessListLL.getWidth()).setDuration(300).start();
        else if (tag == OPEN)
            ObjectAnimator.ofFloat(finalGuessListLL, "translationX", finalGuessListLL.getTranslationX(), finalGuessListLL.getTranslationX() - finalGuessListLL.getWidth()).setDuration(300).start();
    }

    private void addGiftDanMu() {
        String text = "江湖感觉乱送给主播一个" + giftName;
        barrageUtil.myaddDanmaku(text, Color.parseColor("#ff0000"), true);
        barrageUtil.addDanmaku(true);
    }

    private void initdanmu() {
        //实例化
        mDanmakuView = (IDanmakuView) activity.findViewById(R.id.my_danmaku);
        danmakuContext = DanmakuContext.create();

        // 设置弹幕的最大显示行数
        HashMap<Integer, Integer> maxLinesPair = new HashMap<Integer, Integer>();
        //maxLinesPair.put(BaseDanmaku.TYPE_SCROLL_RL, 2); // 滚动弹幕最大显示3行
        maxLinesPair.put(BaseDanmaku.TYPE_FIX_BOTTOM, 1); // 滚动弹幕最大显示3行
        maxLinesPair.put(BaseDanmaku.TYPE_FIX_TOP, 1); // 滚动弹幕最大显示3行

        // 设置是否禁止重叠
        HashMap<Integer, Boolean> overlappingEnablePair = new HashMap<Integer, Boolean>();
        overlappingEnablePair.put(BaseDanmaku.TYPE_SCROLL_LR, true);
        overlappingEnablePair.put(BaseDanmaku.TYPE_FIX_BOTTOM, true);
        overlappingEnablePair.put(BaseDanmaku.TYPE_FIX_TOP, true);

        danmakuContext.setDanmakuStyle(IDisplayer.DANMAKU_STYLE_STROKEN, 3) //设置描边样式
                .setDuplicateMergingEnabled(false)
                .setScrollSpeedFactor(1.2f) //是否启用合并重复弹幕
                .setScaleTextSize(1.2f) //设置弹幕滚动速度系数,只对滚动弹幕有效
                .setCacheStuffer(new BackgroundCacheStuffer(), mCacheStufferAdapter)
                        // 图文混排使用SpannedCacheStuffer  设置缓存绘制填充器，
                        // 默认使用{@link SimpleTextCacheStuffer}只支持纯文字显示,
                        // 如果需要图文混排请设置{@link SpannedCacheStuffer}
                        // 如果需要定制其他样式请扩展{@link SimpleTextCacheStuffer}|{@link SpannedCacheStuffer}
                .setMaximumLines(maxLinesPair) //设置最大显示行数
                .preventOverlapping(overlappingEnablePair); //设置防弹幕重叠，null为允许重叠

        if (mDanmakuView != null) {

            mParser = createParser(activity.getResources().openRawResource(R.raw.comments)); //创建解析器对象，从raw资源目录下解析comments.xml文本
            mDanmakuView.setCallback(new master.flame.danmaku.controller.DrawHandler.Callback() {
                @Override
                public void updateTimer(DanmakuTimer timer) {
                }

                @Override
                public void drawingFinished() {
                }

                @Override
                public void danmakuShown(BaseDanmaku danmaku) {
                }

                @Override
                public void prepared() {
                    mDanmakuView.start();
                    mDanmakuView.hide();
                }
            });
            mDanmakuView.prepare(mParser, danmakuContext);
            mDanmakuView.showFPS(false); //是否显示FPS
            mDanmakuView.enableDanmakuDrawingCache(true);
        }
    }

    /**
     * 创建图文混排的填充适配器
     */
    private BaseCacheStuffer.Proxy mCacheStufferAdapter = new BaseCacheStuffer.Proxy() {
        private Drawable mDrawable;

        /**
         * 在弹幕显示前使用新的text,使用新的text
         * @param danmaku
         * @param fromWorkerThread 是否在工作(非UI)线程,在true的情况下可以做一些耗时操作(例如更新Span的drawblae或者其他IO操作)
         * @return 如果不需重置，直接返回danmaku.text
         */
        @Override
        public void prepareDrawing(final BaseDanmaku danmaku, boolean fromWorkerThread) {
            if (danmaku.text instanceof Spanned) { // 根据你的条件检查是否需要需要更新弹幕
                // FIXME 这里只是简单启个线程来加载远程url图片，请使用你自己的异步线程池，最好加上你的缓存池
                new Thread() {

                    @Override
                    public void run() {
                        String url = "http://www.bilibili.com/favicon.ico";
                        InputStream inputStream = null;
                        Drawable drawable = mDrawable;
                        if (drawable == null) {
                            try {
                                URLConnection urlConnection = new URL(url).openConnection();
                                inputStream = urlConnection.getInputStream();
                                drawable = BitmapDrawable.createFromStream(inputStream, "bitmap");
                                mDrawable = drawable;
                            } catch (MalformedURLException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            } finally {
                                IOUtils.closeQuietly(inputStream);
                            }
                        }
                        if (drawable != null) {
                            drawable.setBounds(0, 0, 100, 100);
                            SpannableStringBuilder spannable = barrageUtil.createSpannable(drawable);
                            danmaku.text = spannable;
                            if (mDanmakuView != null) {
                                mDanmakuView.invalidateDanmaku(danmaku, false);
                            }
                            return;
                        }
                    }
                }.start();
            }
        }

        @Override
        public void releaseResource(BaseDanmaku danmaku) {
            // TODO 重要:清理含有ImageSpan的text中的一些占用内存的资源 例如drawable
            if (mDrawable != null) {
                mDrawable.setCallback(null);
            }
        }
    };

    /**
     * 创建解析器对象，解析输入流
     *
     * @param stream
     * @return
     */
    public static BaseDanmakuParser createParser(InputStream stream) {

        if (stream == null) {
            return new BaseDanmakuParser() {

                @Override
                protected Danmakus parse() {
                    return new Danmakus();
                }
            };
        }

        ILoader loader = DanmakuLoaderFactory.create(DanmakuLoaderFactory.TAG_BILI);
        //  DanmakuLoaderFactory.create(DanmakuLoaderFactory.TAG_BILI) //xml解析
        //   DanmakuLoaderFactory.create(DanmakuLoaderFactory.TAG_ACFUN) //json文件格式解析
        try {
            loader.load(stream);
        } catch (IllegalDataException e) {
            e.printStackTrace();
        }
        BaseDanmakuParser parser = new BiliDanmukuParser();
        IDataSource<?> dataSource = loader.getDataSource();
        parser.load(dataSource);
        return parser;

    }

    public void tospeker() {
        //1.创建RecognizerDialog对象
        RecognizerDialog mDialog = new RecognizerDialog(context, new InitListener() {
            @Override
            public void onInit(int i) {

            }
        });
        //2.设置accent、language等参数
        mDialog.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
        mDialog.setParameter(SpeechConstant.ACCENT, "mandarin");
        //若要将UI控件用于语义理解,必须添加以下参数设置,设置之后onResult回调返回将是语义理解 //结果
        // mDialog.setParameter("asr_sch", "1");
        // mDialog.setParameter("nlp_version", "2.0");
        //3.设置回调接口
        mDialog.setListener(new RecognizerDialogListener() {
            StringBuffer resultCode = new StringBuffer();

            @Override
            public void onResult(RecognizerResult recognizerResult, boolean b) {
                String item = josntoString(recognizerResult.getResultString());
                resultCode.append(item);
                if (b) {
                    //spekertext.setText(resultCode.toString());
                    dangtext.setText(resultCode.toString());
                }
            }

            @Override
            public void onError(SpeechError speechError) {

            }
        });
        // 4.显示dialog,接收语音输入
        mDialog.show();
    }

    public String josntoString(String josn) {
        StringBuffer result = new StringBuffer();

        Gson gson = new Gson();
        VideoItemJson video = gson.fromJson(josn, VideoItemJson.class);
        ArrayList<VideoItemJson.WS> ws = video.getWs();
        for (VideoItemJson.WS v : ws) {
            result.append(v.cw.get(0).getW());

        }

        return result.toString();
    }

    private void reseDanmuView(int resest) {
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) ((DanmakuView) mDanmakuView).getLayoutParams();
        if (resest == 1) {//全屏
            params.width = FrameLayout.LayoutParams.MATCH_PARENT;
            params.height = FrameLayout.LayoutParams.MATCH_PARENT;
        } else if (resest == 2) {//下方方弹幕
            params.width = FrameLayout.LayoutParams.MATCH_PARENT;
            params.height = 300;
            params.gravity = Gravity.BOTTOM;

        } else if (resest == 3) {//上方弹幕
            params.width = FrameLayout.LayoutParams.MATCH_PARENT;
            params.height = 300;
            params.gravity = Gravity.AXIS_PULL_AFTER;
        }

        ((DanmakuView) mDanmakuView).setLayoutParams(params);
    }

    private List<Gift> giftList;
    private LiveGiftAdapter liveGiftAdapter;

    private void initGiftListData() {
        GridView giftGridView = (GridView) giftListRL.findViewById(R.id.live_gift_gv);
        giftList = new ArrayList<>();
        Gift gift1 = new Gift(ImageManageUtil.RToByte(R.drawable.gift5), 15, "飞吻");
        Gift gift2 = new Gift(ImageManageUtil.RToByte(R.drawable.gift4), 20, "爱心");
        Gift gift3 = new Gift(ImageManageUtil.RToByte(R.drawable.gift3), 100, "小熊");
        Gift gift4 = new Gift(ImageManageUtil.RToByte(R.drawable.gift2), 200, "鲜花2");
        Gift gift5 = new Gift(ImageManageUtil.RToByte(R.drawable.gift1), 150, "鲜花");
        giftList.add(gift1);
        giftList.add(gift2);
        giftList.add(gift3);
        giftList.add(gift4);
        giftList.add(gift5);

        liveGiftAdapter = new LiveGiftAdapter(context, giftList);

        giftGridView.setAdapter(liveGiftAdapter);
        giftGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, final int position, long id) {
                final Double price = Double.parseDouble(((TextView) view.findViewById(R.id.select_gift_price)).getText().toString());

                giftName = ((TextView) view.findViewById(R.id.select_gift_name)).getText().toString();
                CustomDialog.Builder builder = new CustomDialog.Builder(context);
                builder.setMessage("确定花费" + price + "个金币？");
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        dialog.dismiss();
                    }
                });
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        TextView myMoney_tv = (TextView) activity.findViewById(R.id.live_gift_my_money);

                        double myMoney = Double.parseDouble(myMoney_tv.getText().toString());
                        if (myMoney >= price) {
                            myMoney_tv.setText(String.valueOf(myMoney - price));
                            objectAnim(giftListRL, CLOSE);

                            AnimationDrawable animation;
                            if (position > 2)
                                animation = addAnimViewAndStartAnim(R.drawable.gift_fireworks_list);
                            else
                                animation = addAnimViewAndStartAnim(R.drawable.gift_heart);
                            closeAnim(animation);

                        } else {
                            CustomDialog.Builder builderMoney = new CustomDialog.Builder(context);
                            builderMoney.setMessage("金币不足，请先充值");
                            builderMoney.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int i) {
                                    dialog.dismiss();
                                }
                            });
                            builderMoney.create().show();
                        }
                        dialog.dismiss();
                    }
                });
                builder.create().show();

            }
        });

    }

    @NonNull
    private AnimationDrawable addAnimViewAndStartAnim(int drawableR) {
        iv = new ImageView(context);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
        iv.setLayoutParams(params);
        iv.setBackgroundResource(drawableR);

        ((RelativeLayout) activity).addView(iv);
        AnimationDrawable animation = (AnimationDrawable) iv.getBackground();
        animation.start();
        return animation;
    }

    private void closeAnim(AnimationDrawable animation) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                    Message msg = new Message();
                    giftAnimHandler.sendMessage(msg);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void initGuessListData() {
        String[] name = {"liveId"};
        String[] value = {"1005"};
        L.e("get data:");
        NetUtil.sendRequestToUrl(ServerConstants.LIVEGuessListUrl,
                name, value, new HttpCallbackListener() {
                    @Override
                    public void onSucc(String response) {
                        L.e("response:" + response);

                        Message msg = new Message();
                        msg.obj = response;
                        msg.what = 1;
                        guessListHandler.sendMessage(msg);
                    }

                    @Override
                    public void onError(Exception e) {
                        guessListHandler.sendEmptyMessage(0);
                    }
                });

    }
}
