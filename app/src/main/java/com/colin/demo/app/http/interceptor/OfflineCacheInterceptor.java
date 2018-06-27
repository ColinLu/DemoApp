package com.colin.demo.app.http.interceptor;

import android.support.annotation.NonNull;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;

/**
 * 描述：无网络拦截
 * <p>
 * 作者：Colin
 * 时间：2018/6/27
 */
public class OfflineCacheInterceptor implements Interceptor {
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        return null;
    }
}
