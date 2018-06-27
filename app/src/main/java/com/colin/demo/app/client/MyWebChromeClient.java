package com.colin.demo.app.client;

import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import com.colin.demo.app.callback.IWebViewCallBack;


/**
 * Created by Administrator on 2017/7/18.
 */

public class MyWebChromeClient extends WebChromeClient {
    private IWebViewCallBack mIWebViewCallBack;

    public MyWebChromeClient() {
    }

    public MyWebChromeClient(IWebViewCallBack IWebViewCallBack) {
        mIWebViewCallBack = IWebViewCallBack;
    }

    @Override
    // 处理javascript中的alert
    public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
        result.confirm();//这里必须调用，否则页面会阻塞造成假死
        return true;
    }

    @Override
    // 处理javascript中的confirm
    public boolean onJsConfirm(WebView view, String url, String message, final JsResult result) {
        return super.onJsConfirm(view, url, message, result);
    }


    @Override
    // 处理javascript中的prompt
    public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, final JsPromptResult result) {
        return super.onJsPrompt(view, url, message, defaultValue, result);
    }


    // 设置网页加载的进度条
    @Override
    public void onProgressChanged(WebView view, int newProgress) {
        super.onProgressChanged(view, newProgress);
        if (null != mIWebViewCallBack) {
            mIWebViewCallBack.loadProgress(newProgress);
        }
    }

    // 设置程序的Title
    @Override
    public void onReceivedTitle(WebView view, String title) {
        super.onReceivedTitle(view, title);
        if (null != mIWebViewCallBack) {
            mIWebViewCallBack.setTitle(title);
        }
    }
}
