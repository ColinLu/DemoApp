package com.colin.demo.app.bean;

import com.colin.demo.app.utils.LogUtil;

import java.lang.reflect.Field;

/**
 * 描述：验证反射-->>用作子类  集成 ParentModel
 * <p>
 * 作者：Colin
 * 时间：2018/9/26
 */
public class Model extends ParentModel {
    private String privateField = "private--修饰--子类";
    protected String protectedField = "protected--修饰--子类";
    public String publicField = "public--修饰--子类";


    public static void main(String[] args) {
        Field[] fs = Model.class.getFields();
        Field[] fs1 = Model.class.getDeclaredFields();
        for (Field f : fs) {
            LogUtil.d("getFields", "getFields---" + f.getName());
        }

        for (Field f : fs1) {
            LogUtil.d("getDeclaredFields", "getDeclaredFields---" + f.getName());
        }

    }


}


