package com.colin.demo.app.data;

import android.os.Environment;

import com.colin.demo.app.R;

import java.io.File;

/**
 * 描述：
 * <p>
 * 作者：Colin
 * 时间：2018/6/21
 */
public abstract class Constants {

    public static final int FRAGMENT_MAIN_VIEW = 0;
    public static final int FRAGMENT_MAIN_METHOD = 1;
    public static final int FRAGMENT_MAIN_OTHER = 2;
    //权限
    public static final int REQUEST_CODE_PERMISSION_LOCATION = 1;      //定位权限

    public static final String[] FRAGMENT_MIAN_TITLE_ARRAY = new String[]{"控件", "方法", "其他"};

    public static final Integer[] IMAGE_ARRAY_SHOW = new Integer[]{R.drawable.ic_launcher_background
            , R.drawable.ic_launcher_background, R.drawable.ic_launcher_background, R.drawable.ic_launcher_background
            , R.drawable.ic_launcher_background, R.drawable.ic_launcher_background, R.drawable.ic_launcher_background};


    public static final int REQUEST_CODE_INSTALL = 0;               //安装app
    public static final int REQUEST_CODE_OPEN_BLUETOOTH = 1;        //打开蓝牙

    //十张图片网址
    public static final String[] IMAGE_URL = new String[]{
            "http://g.hiphotos.baidu.com/image/h%3D360/sign=8cb0e6191a178a82d13c79a6c602737f/6c224f4a20a446230761b9b79c22720e0df3d7bf.jpg",
            "http://c.hiphotos.baidu.com/image/h%3D300/sign=4c2ae377f1246b60640eb474dbf81a35/b90e7bec54e736d1dd0d312c9c504fc2d56269fc.jpg",
            "http://g.hiphotos.baidu.com/image/h%3D300/sign=daf4f50ad51b0ef473e89e5eedc451a1/b151f8198618367aa482c2f929738bd4b31ce58f.jpg",
            "http://f.hiphotos.baidu.com/image/h%3D360/sign=c189db24d01b0ef473e89e58edc451a1/b151f8198618367abfffecd72c738bd4b31ce542.jpg",
            "http://c.hiphotos.baidu.com/image/h%3D360/sign=a0f6c3040ed162d99aee641a21dda950/b7003af33a87e95097d47eac14385343faf2b42b.jpg",
            "http://b.hiphotos.baidu.com/image/h%3D360/sign=4205dfdc014f78f09f0b9cf549330a83/63d0f703918fa0ecb1122585249759ee3c6ddb65.jpg",
            "http://c.hiphotos.baidu.com/image/h%3D360/sign=7474f0b09c510fb367197191e932c893/b999a9014c086e068f2c814200087bf40ad1cb37.jpg",
            "http://g.hiphotos.baidu.com/image/h%3D360/sign=f7f14f1ac45c10383b7ec8c48210931c/2cf5e0fe9925bc3142c14e985cdf8db1cb137015.jpg",
            "http://g.hiphotos.baidu.com/image/h%3D360/sign=e749bf13c45c10383b7ec8c48211931c/2cf5e0fe9925bc315279be915cdf8db1cb137096.jpg",
            "http://h.hiphotos.baidu.com/image/h%3D360/sign=d5eb92930ef431ada3d2453f7b37ac0f/d058ccbf6c81800a7e84fe0fb33533fa828b4749.jpg"
    };

    public static String getFilePath() {
        return Environment.getExternalStorageDirectory() + File.separator + "DemoApp" + File.separator + "app" + File.separator;
    }

}
