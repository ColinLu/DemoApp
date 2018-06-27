package com.colin.demo.app.client;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.colin.demo.app.callback.IWebViewCallBack;
import com.colin.demo.app.utils.LogUtil;


/**
 * Created by Administrator on 2017/7/18.
 */
@SuppressLint("SetJavaScriptEnabled")
public class MyWebViewClient extends WebViewClient {
    private IWebViewCallBack mIWebViewCallBack;
    private final static int URL_TAG = 6 << 24;

    public MyWebViewClient() {
    }

    public MyWebViewClient(IWebViewCallBack IWebViewCallBack) {
        mIWebViewCallBack = IWebViewCallBack;
    }

    // url拦截
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        LogUtil.e("url-->>" + url);
        //返回true 自己消费掉
        //返回false  系统自己在调用一次
        if (null != mIWebViewCallBack) {
            return mIWebViewCallBack.loadUrl(view, url);
        } else {
            view.loadUrl(url);
            return super.shouldOverrideUrlLoading(view, url);
        }
    }

    // 页面开始加载
    @Override
    @SuppressLint("SetJavaScriptEnabled")
    public void onPageStarted(WebView webView, String url, Bitmap favicon) {
        LogUtil.e("url-->>" + url);
        webView.getSettings().setJavaScriptEnabled(true);
        super.onPageStarted(webView, url, favicon);
        if (null != mIWebViewCallBack) {
            mIWebViewCallBack.loadProgress(0);
        }

    }

    // 页面加载完成
    @Override
    public void onPageFinished(WebView view, String url) {
        LogUtil.e("url-->>" + url);
        super.onPageFinished(view, url);
        if (null != mIWebViewCallBack) {
            //嵌入JS
            mIWebViewCallBack.injectJS(view, url);
            mIWebViewCallBack.loadProgress(100);
        }
        view.setVisibility(View.VISIBLE);

    }

    @Override
    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
        super.onReceivedError(view, errorCode, description, failingUrl);
        if (null != mIWebViewCallBack) {
            mIWebViewCallBack.loadProgress(100);
            mIWebViewCallBack.error(errorCode, description, failingUrl);
        }

    }

}
