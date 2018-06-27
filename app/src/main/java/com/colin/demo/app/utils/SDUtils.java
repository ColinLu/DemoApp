package com.colin.demo.app.utils;

import android.graphics.Bitmap;
import android.os.Environment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * sd卡的工具类。用来，存图片
 */
public class SDUtils {

    //文件夹的名字
    private static final String FOLDER_NAME = "Fenqi";
    private static File LAST_NAME = null;

    /**
     * 获取到文件夹
     *
     * @return 文件对象
     */
    public static File getFolder(String s) {
        if (Environment.MEDIA_MOUNTED.equals(
                Environment.getExternalStorageState())) {
            //判断当前SD卡，是否挂载
            File dir = Environment.getExternalStorageDirectory();

            //根路径下的文件夹
            File file = new File(
                    dir.getAbsolutePath() +
                            File.separator +
                            FOLDER_NAME + File.separator + s);
            if (!file.exists() || file.isFile()) {
                file.mkdirs();
            }

            return file;
        }

        return null;

    }


    /**
     * 往文件里面写内容
     *
     * @param bitmap   待保存的图片
     * @param fileName 文件名
     * @return 是否成功
     */
    public static String write(Bitmap bitmap, String fileName, String s) {
        String ret = null;
        File folder = getFolder(s);
        if (folder == null) {
            //如果没有挂载，无法获取到文件夹，那么直接返回失败
            return "";
        }
        if (LAST_NAME != null) {
            LAST_NAME.delete();
        }
        //实例化这个文件
        File file = new File(folder, fileName);
        LAST_NAME = file;
        try {
            //实例化一个文件输出流。
            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();
            ret = file.getAbsolutePath();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ret;
    }

    /**
     * 往文件里面写内容
     *
     * @param bitmap   待保存的图片
     * @param fileName 文件名
     * @return 是否成功
     */
    public static File writes(Bitmap bitmap, String fileName, String s) {
        File ret = null;
        File folder = getFolder(s);
        if (folder == null) {
            //如果没有挂载，无法获取到文件夹，那么直接返回失败
            return null;
        }
        if (LAST_NAME != null) {
            LAST_NAME.delete();
        }
        //实例化这个文件
        File file = new File(folder, fileName);
        LAST_NAME = file;
        try {
            //实例化一个文件输出流。
            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();
            ret = file;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ret;
    }


}
