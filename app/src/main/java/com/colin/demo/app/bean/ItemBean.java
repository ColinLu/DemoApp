package com.colin.demo.app.bean;

import android.app.Activity;

/**
 * 描述：
 * <p>
 * 作者：Colin
 * 时间：2018/6/21
 */
public class ItemBean extends Object {
    public int id;
    public String content;
    public Class<? extends Activity> clazz;

    public ItemBean(int id, String content) {
        this.id = id;
        this.content = content;
    }

    public ItemBean(int id, String content, Class<? extends Activity> clazz) {
        this.id = id;
        this.content = content;
        this.clazz = clazz;
    }
}
