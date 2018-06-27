package com.colin.demo.app.callback;

import android.webkit.WebView;

/**
 * Created by Administrator on 2017/7/18.
 */

public interface IWebViewCallBack {

    boolean loadUrl(WebView webView, String url);


    void setTitle(String title);

    void loadProgress(int progress);

    void error(int errorCode, String description, String failingUrl);

    void injectJS(WebView webView, String url);
}
