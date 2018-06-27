package com.colin.demo.app.utils;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.colin.demo.app.R;
import com.colin.demo.app.callback.IWebViewCallBack;
import com.colin.demo.app.client.MyWebChromeClient;
import com.colin.demo.app.client.MyWebViewClient;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by ColinLu on 2016/8/5.
 */
public class InitViewUtil {

    /**
     * @param swipeRefreshLayout
     */
//    @SuppressWarnings("ResourceAsColor")
    public static void initSwipeRefreshLayout(@NonNull SwipeRefreshLayout swipeRefreshLayout) {
        //设置 SwipeRefreshLayout 的尺寸
        swipeRefreshLayout.setSize(SwipeRefreshLayout.DEFAULT);
        //设置 SwipeRefreshLayout 刷新时的颜色切换（可以有无数种）
        swipeRefreshLayout.setColorSchemeColors(swipeRefreshLayout.getContext().getResources().getColor(R.color.colorAccent));
        //设置 SwipeRefreshLayout 的背景色
//        swipeRefreshLayout.setBackgroundColor(Color.parseColor("#303F9F"));
        //设置 SwipeRefreshLayout 的下拉距离
//        swipeRefreshLayout.setDistanceToTriggerSync(100);
    }

    public static void initRecyclerView(RecyclerView recyclerView, RecyclerView.Adapter adapter) {
        initRecyclerView(recyclerView, 1, adapter, false);
    }



    /**
     * @param recyclerView
     * @param grid
     * @param adapter
     */
    public static void initRecyclerView(RecyclerView recyclerView, int grid, RecyclerView.Adapter adapter) {
        initRecyclerView(recyclerView, grid, adapter, false);
    }

    /**
     * @param recyclerView
     * @param grid
     * @param adapter
     * @param haveScrollView
     */
    public static void initRecyclerView(RecyclerView recyclerView, int grid, RecyclerView.Adapter adapter, boolean haveScrollView) {
        if (grid > 1) {
            initRecyclerView(recyclerView, new GridLayoutManager(recyclerView.getContext(), grid), adapter, haveScrollView);
        } else {
            initRecyclerView(recyclerView, new LinearLayoutManager(recyclerView.getContext()), adapter, haveScrollView);
        }
    }
    public static void initRecyclerView(RecyclerView recyclerView, RecyclerView.LayoutManager layoutManager, RecyclerView.Adapter adapter) {
        initRecyclerView(recyclerView, layoutManager, adapter, false);
    }
    /**
     * @param recyclerView
     * @param layoutManager
     * @param adapter
     * @param haveScrollView
     */
    public static void initRecyclerView(RecyclerView recyclerView, RecyclerView.LayoutManager layoutManager, RecyclerView.Adapter adapter, boolean haveScrollView) {
        if (null == recyclerView || null == layoutManager || null == adapter) {
            return;
        }
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setFocusable(false);
        recyclerView.setNestedScrollingEnabled(!haveScrollView);
    }

    @SuppressLint("NewApi")
    public static void setTabViewLineWidth(final TabLayout tabLayout) {
        if (null == tabLayout || null == tabLayout.getContext()) {
            return;
        }
        if (tabLayout.getTabCount() < 1) {
            return;
        }
        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                try {
                    //拿到tabLayout的mTabStrip属性
                    Field mTabStripField = tabLayout.getClass().getDeclaredField("mTabStrip");
                    mTabStripField.setAccessible(true);

                    LinearLayout mTabStrip = (LinearLayout) mTabStripField.get(tabLayout);

                    int dp10 = MetricsUtil.dp2px(tabLayout.getContext(), 5);

                    for (int i = 0; i < mTabStrip.getChildCount(); i++) {
                        View tabView = mTabStrip.getChildAt(i);

                        //拿到tabView的mTextView属性
                        Field mTextViewField = tabView.getClass().getDeclaredField("mTextView");
                        mTextViewField.setAccessible(true);

                        TextView mTextView = (TextView) mTextViewField.get(tabView);

                        tabView.setPadding(0, 0, 0, 0);

                        //因为我想要的效果是   字多宽线就多宽，所以测量mTextView的宽度
                        int width = 0;
                        width = mTextView.getWidth();
                        if (width == 0) {
                            mTextView.measure(0, 0);
                            width = mTextView.getMeasuredWidth();
                        }
                        //设置tab左右间距为10dp  注意这里不能使用Padding 因为源码中线的宽度是根据 tabView的宽度来设置的
                        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) tabView.getLayoutParams();
                        int width1 = tabView.getWidth();
                        params.width = width;
                        params.leftMargin = (width1 - width) / 2;
                        params.rightMargin = (width1 - width) / 2;
                        tabView.setLayoutParams(params);
                        tabView.invalidate();
                    }
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    LogUtil.e(e.getMessage());
                    e.printStackTrace();
                }
            }
        });
    }




    @SuppressLint({"SetJavaScriptEnabled", "JavascriptInterface", "AddJavascriptInterface"})
    public static void initWebView(WebView webview, String url, IWebViewCallBack iWebViewCallBack) {
        // 设置支持JavaScript脚本
        WebSettings webSettings = webview.getSettings();
        webSettings.setDefaultTextEncodingName("UTF-8");
        webSettings.setJavaScriptEnabled(true);// 设置可以运行JS脚本
        // 设置可以访问文件
        webSettings.setAllowFileAccess(true);
        // 设置可以支持缩放
        webSettings.setSupportZoom(false);
        // 设置出现缩放工具
        webSettings.setBuiltInZoomControls(false);
        // 设置默认缩放方式尺寸是far
        webSettings.setDefaultZoom(WebSettings.ZoomDensity.MEDIUM);
//        webSettings.setDefaultFontSize(20);
        // 允许js弹出窗口
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setSavePassword(false);
        webSettings.setUseWideViewPort(true);// 打开页面时， 自适应屏幕
        webSettings.setLoadWithOverviewMode(true);// 打开页面时， 自适应屏幕
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webview.setBackgroundResource(android.R.color.transparent);
        webview.addJavascriptInterface(webview.getContext(), "android");
        // 添加js交互接口类，并起别名 imagelistner
        webview.setWebViewClient(new MyWebViewClient(iWebViewCallBack));
        webview.setWebChromeClient(new MyWebChromeClient(iWebViewCallBack));
        webview.loadUrl(url, addWebHttpHeader());
    }



    public static void initWebViewLoadHtml(WebView webview, String html, IWebViewCallBack iWebViewCallBack) {
        initWebViewLoadHtml(webview, html, true, iWebViewCallBack);
    }

    @SuppressLint({"SetJavaScriptEnabled", "JavascriptInterface", "AddJavascriptInterface"})
    public static void initWebViewLoadHtml(WebView webview, String html, boolean supportZoom, IWebViewCallBack iWebViewCallBack) {
        // 设置支持JavaScript脚本
        WebSettings webSettings = webview.getSettings();
        webSettings.setDefaultTextEncodingName("UTF-8");
        webSettings.setJavaScriptEnabled(true);// 设置可以运行JS脚本
        // 设置可以访问文件
        webSettings.setAllowFileAccess(true);
        // 设置可以支持缩放
        webSettings.setSupportZoom(supportZoom);
        // 设置出现缩放工具
        webSettings.setBuiltInZoomControls(supportZoom);
        // 设置默认缩放方式尺寸是far
        webSettings.setDefaultZoom(WebSettings.ZoomDensity.MEDIUM);
//        webSettings.setDefaultFontSize(20);
        // 允许js弹出窗口
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setSavePassword(false);
        if (supportZoom) {
            webSettings.setUseWideViewPort(true);// 打开页面时， 自适应屏幕
            webSettings.setLoadWithOverviewMode(true);// 打开页面时， 自适应屏幕
        }
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webview.setBackgroundResource(android.R.color.transparent);

        webview.addJavascriptInterface(webview.getContext(), "android");
        // 添加js交互接口类，并起别名 imagelistner
        webview.setWebViewClient(new MyWebViewClient(iWebViewCallBack));
        webview.setWebChromeClient(new MyWebChromeClient(iWebViewCallBack));
        webview.loadDataWithBaseURL("file://", html, "text/html", "UTF-8", "about:blank");
    }


    public static Map<String, String> addWebHttpHeader() {
        Map<String, String> headerMap = new HashMap<>();
//        headerMap.put("access-token", Constants.access_token);
//        headerMap.put("front", String.valueOf(Constants.APP_FRONT_TYPE));
//        headerMap.put("version", Constants.version);
        return headerMap;
    }


    /**
     * 特殊url 添加头
     *
     * @param webView
     * @param url
     */
    public static void webViewLoadUrl(ViewGroup webView, String url) {
        if (null == webView || StringUtil.isEmpty(url)) {
            return;
        }

        String host = StringUtil.getHost(url);
        LogUtil.e("host-->>" + host);
        LogUtil.e("url--->>" + url);
        ((WebView) webView).loadUrl(url, addWebHttpHeader());
    }

    public static void destroyWebView(final WebView webView) {
        if (null == webView) {
            return;
        }
        webView.getSettings().setBuiltInZoomControls(true);
        webView.setVisibility(View.GONE);// 把destroy()延后
        final long timeout = ViewConfiguration.getZoomControlsTimeout();
        webView.postDelayed(new Runnable() {
            @Override
            public void run() {
                webView.clearCache(true);
                webView.clearHistory();
                webView.destroy();
            }
        }, timeout);
    }


    /**
     * 利用反射效果  关闭SearchView
     *
     * @param searchView
     */
    public static void closeSearchView(SearchView searchView) {
        if (null == searchView) {
            return;
        }
        searchView.setQuery("", false);
        try {
            Method method = searchView.getClass().getDeclaredMethod("onCloseClicked");
            method.setAccessible(true);
            method.invoke(searchView);
        } catch (Exception e) {
            LogUtil.e(e.getMessage());
        }

    }

    /**
     * 利用反射效果让菜单按钮显示文字与图片
     *
     * @param menu
     */
    public static void setMenuShowImageText(Menu menu) {
        if (null == menu) {
            return;
        }
        if (menu.getClass().getSimpleName().equalsIgnoreCase("MenuBuilder")) {
            try {
                @SuppressLint("PrivateApi") Method method = menu.getClass().getDeclaredMethod("setOptionalIconsVisible", Boolean.TYPE);
                method.setAccessible(true);
                method.invoke(menu, true);
            } catch (Exception e) {
                LogUtil.e(e.getMessage());
            }
        }

    }

    /**
     * recyclerView 是否滚动到顶部或者底部
     * 判断是否滑动到底部， recyclerView.canScrollVertically(1);返回false表示不能往上滑动，即代表到底部了；
     * 判断是否滑动到顶部， recyclerView.canScrollVertically(-1);返回false表示不能往下滑动，即代表到顶部了；
     *
     * @param isTop
     * @return
     */
    public static boolean recyclerScroll(RecyclerView recyclerView, boolean isTop) {
        if (null == recyclerView) {
            return false;
        }
        return !recyclerView.canScrollHorizontally(isTop ? -1 : 1);
    }

    /**
     * 是否可以编辑输入框
     *
     * @param editText
     * @param isInputText
     */
    public static void editEditAble(EditText editText, boolean isInputText) {
        editText.setCursorVisible(isInputText);
        editText.setFocusable(isInputText);
        editText.setFocusableInTouchMode(isInputText);
    }
}
