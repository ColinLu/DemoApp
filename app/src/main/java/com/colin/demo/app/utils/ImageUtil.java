package com.colin.demo.app.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.widget.Toast;

import java.io.File;

/**
 * 描述：权限 自行处理 拍照 与 相册获取图片
 * <p>
 * 作者：Colin
 * 时间：2018/8/13
 */
public class ImageUtil {
    private static final String[] CHOOSE_DIALOG_IMAGE = new String[]{"相机", "相册"};
    private static final int REQUEST_CODE_CAMERA = 10001;
    private static final int REQUEST_CODE_ALBUM = 10002;
    private Activity mActivity;
    @SuppressLint("StaticFieldLeak")
    private volatile static ImageUtil ourInstance = null;

    public static ImageUtil getInstance(Activity activity) {
        if (null == ourInstance) {
            synchronized (ImageUtil.class) {
                if (null == ourInstance) {
                    ourInstance = new ImageUtil(activity);
                }
            }
        }
        return ourInstance;
    }

    private ImageUtil(Activity activity) {
        this.mActivity = activity;
    }


    public void showChooseImageDialog() {
        if (null == mActivity) {
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity).setItems(CHOOSE_DIALOG_IMAGE, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        openCamera();
                        break;
                    case 1:
                        openAlbum();
                        break;
                }
            }


        });
        builder.create().show();
    }

    /**
     * 打开相机 选择图片
     */
    public void openCamera() {
        if (null == mActivity) {
            return;
        }
        openCamera(getImageFile());
    }


    /**
     * 打开相机 选择图片
     *
     * @param file
     */
    public void openCamera(File file) {
        if (null == mActivity) {
            return;
        }
        if (null == file) {
            Toast.makeText(mActivity, "图片名称不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        //权限
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, getImageUri(file));
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        mActivity.startActivityForResult(intent, REQUEST_CODE_CAMERA);
    }

    /**
     * 打开相册 获取图片
     */
    public void openAlbum() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");//相片类型
        mActivity.startActivityForResult(intent, REQUEST_CODE_ALBUM);
    }

    /**
     * 相机获取 保存图片文件
     *
     * @return
     */
    @SuppressLint("DefaultLocale")
    public File getImageFile() {
        return getImageFile(String.format("%d,.jpg", System.currentTimeMillis()));
    }

    /**
     * 相机获取 保存图片文件
     *
     * @param imageName
     * @return
     */
    @SuppressLint("DefaultLocale")
    public File getImageFile(String imageName) {
        String name = TextUtils.isEmpty(imageName) ? String.format("%d,.jpg", System.currentTimeMillis()) : imageName;
        File imageFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), name);
        if (!imageFile.exists()) {
            imageFile.getParentFile().mkdirs();
        }
        return imageFile;
    }

    /**
     * 适配 Android 7.0
     *
     * @param file
     * @return
     */
    public Uri getImageUri(File file) {
        if (null == file) {
            return null;
        }
        if (Build.VERSION.SDK_INT >= 24) {
            return FileProvider.getUriForFile(mActivity, mActivity.getPackageName(), file);
        } else {
            return Uri.fromFile(file);
        }
    }

    /**
     * 从相册中获取图片地址路径
     *
     * @param context
     * @param uri
     * @return
     */
    public String getAlbumImagePath(Context context, Uri uri) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            return getPath_above19(context, uri);
        } else {
            return getFilePath_below19(context, uri);
        }
    }

    /**
     * API19以下获取图片路径的方法
     *
     * @param uri
     */
    public String getFilePath_below19(Context context, Uri uri) {
        //这里开始的第二部分，获取图片的路径：低版本的是没问题的，但是sdk>19会获取不到
        String[] proj = {MediaStore.Images.Media.DATA};
        //好像是android多媒体数据库的封装接口，具体的看Android文档
        Cursor cursor = context.getContentResolver().query(uri, proj, null, null, null);
        //获得用户选择的图片的索引值
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        //将光标移至开头 ，这个很重要，不小心很容易引起越界
        cursor.moveToFirst();
        //最后根据索引值获取图片路径   结果类似：/mnt/sdcard/DCIM/Camera/IMG_20151124_013332.jpg
        String path = cursor.getString(column_index);
        cursor.close();
        return path;
    }

    /**
     * 高于版本19获取图片路径的方法
     *
     * @param context
     * @param uri
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public String getPath_above19(final Context context, final Uri uri) {
        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if ("com.android.externalstorage.documents".equals(uri.getAuthority())) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            }
            // DownloadsProvider
            else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{split[1]};
                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            // Return the remote address
            if ("com.google.android.apps.photos.content".equals(uri.getAuthority()))
                return uri.getLastPathSegment();
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    private String getDataColumn(Context context, Uri uri, String selection,
                                 String[] selectionArgs) {
        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }
}
