package com.colin.demo.app.utils;

import android.content.Context;

import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory;
import com.bumptech.glide.load.engine.cache.LruResourceCache;
import com.bumptech.glide.module.AppGlideModule;

/**
 * 描述：
 * <p>
 * 作者：Colin
 * 时间：2018/6/13
 */
public class GlideImageCache extends AppGlideModule {

    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
//        super.applyOptions(context, builder);
        int memoryCacheSizeBytes = 1024 * 1024 * 20; // 20mb
        builder.setMemoryCache(new LruResourceCache(memoryCacheSizeBytes));
        int diskCacheSizeBytes = 1024 * 1024 * 100; // 100 MB
        builder.setDiskCache(new InternalCacheDiskCacheFactory(context, diskCacheSizeBytes));
    }
    @Override
    public boolean isManifestParsingEnabled() {
        return false;
    }
}
