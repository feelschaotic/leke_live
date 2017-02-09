package com.ramo.campuslive;

import android.webkit.WebChromeClient;
import android.webkit.WebView;

import com.ramo.campuslive.server.Constants;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

/**
 * Created by ramo on 2016/7/8.
 */
@EActivity(R.layout.activity_map_live)
public class MapLiveActivity extends SwipeBackActivity {
    @ViewById
    WebView map_webview;

    @AfterViews
    public void init() {
        map_webview.getSettings().setJavaScriptEnabled(true);
        map_webview.setWebChromeClient(new WebChromeClient());
        map_webview.loadUrl(Constants.MAP_LIVE_ADDRESS);
    }
}
