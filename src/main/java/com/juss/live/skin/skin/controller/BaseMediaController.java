package com.juss.live.skin.skin.controller;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

import com.juss.live.skin.skin.BaseView;
import com.juss.live.skin.skin.BaseViewGourp;
import com.juss.live.skin.skin.utils.UIPlayContext;
import com.lecloud.leutils.ReUtils;
import com.letv.controller.interfacev1.ILetvPlayerController;

import java.util.ArrayList;

public abstract class BaseMediaController extends BaseViewGourp {

    protected ArrayList<BaseView> childViews;
    protected String layoutId;
    protected ArrayList<String> childId;

    public BaseMediaController(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public BaseMediaController(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BaseMediaController(Context context) {
        super(context);
    }

    @Override
    protected void onAttachUIPlayControl(ILetvPlayerController playerControl) {
        if (childViews != null&&childViews.size()>0) {
            for (int i = 0; i < childViews.size(); i++) {
                BaseView child = childViews.get(i);
                child.attachUIPlayControl(playerControl);
            }
        }
    }

    @Override
    protected void onAttachUIPlayContext(UIPlayContext uiPlayContext) {
        if (childViews != null&&childViews.size()>0) {
            for (int i = 0; i < childViews.size(); i++) {
                BaseView child = childViews.get(i);
                child.attachUIContext(uiPlayContext);
            }
        }
        
        if(uiPlayContext!=null){
            if(uiPlayContext.isPlayingAd()){
                setVisibility(View.GONE);
            }else{
                setVisibility(View.VISIBLE);
            }
        }
    }
    
    private void setLayoutId(){
        childViews=new ArrayList<BaseView>();
        childId=new ArrayList<String>();
        onSetLayoutId();
    }
    
    protected abstract void onSetLayoutId();
    
    protected abstract void onInitView();

    @Override
    protected void initView(Context context) {
        setLayoutId();
        LayoutInflater.from(context).inflate(ReUtils.getLayoutId(context, layoutId), this);
        if(childId!=null&&childViews!=null){
            for (int i = 0; i < childId.size(); i++) {
                String id = childId.get(i);
                childViews.add((BaseView) findViewById(ReUtils.getId(context, id)));
            }
        }
        onInitView();
        
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                
            }
        });
    }

}
