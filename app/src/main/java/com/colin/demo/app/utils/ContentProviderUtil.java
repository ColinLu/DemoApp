package com.colin.demo.app.utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 描述：ContentProvider 查询工具类
 * <p>
 * 作者：Colin
 * 时间：2017/8/17
 */

public class ContentProviderUtil {
    //短信协议
    public static final String SMS_URI_ALL = "content://sms/";
    //收件箱
    public static final String SMS_URI_INBOX = "content://sms/inbox";
    //已发送
    public static final String SMS_URI_SEND = "content://sms/sent";
    //草稿箱
    public static final String SMS_URI_DRAFT = "content://sms/draft";
    //全部短信 URI
    public final static Uri SMS_URI = Uri.parse(SMS_URI_ALL);
    //短信查询数据
//    public final static String[] SMS_PROJECTION = new String[]{"_id", "address", "body", "person", "date", "read", "status", "type", "thread_id"};
    public final static String[] SMS_PROJECTION = new String[]{"address", "body", "person", "date"};
    //短信排序
    public final static String SMS_ORDER = "DATE DESC";
    //短信内容查询数据
    public final static String[] SMS_BODY_PROJECTION = new String[]{"address", "body", "date"};
    ////////////////////////////////////////////////////////////////////////////////////短信/////////////////////////////////////////////////////////////////
    //通话记录URI
    public final static Uri CALL_URI = CallLog.Calls.CONTENT_URI;
    //通话记录查询数据
    //    public final static String[] CALL_PROJECTION = new String[]{
//            CallLog.Calls._ID
//            , CallLog.Calls.CACHED_NAME// 通话记录的联系人
//            , CallLog.Calls.NUMBER     // 通话记录的电话号码
//            , CallLog.Calls.DATE       // 通话记录的日期
//            , CallLog.Calls.DURATION   // 通话时长
//            , CallLog.Calls.TYPE       // 通话类型
//    };
    public final static String[] CALL_PROJECTION = new String[]{
            CallLog.Calls.CACHED_NAME// 通话记录的联系人
            , CallLog.Calls.NUMBER     // 通话记录的电话号码
            , CallLog.Calls.DATE       // 通话记录的日期
    };
    //通话记录排序
    public final static String CALL_ORDER = CallLog.Calls.DEFAULT_SORT_ORDER;

    ////////////////////////////////////////////////////////////////////////////////////通话记录/////////////////////////////////////////////////////////////////


    //联系人
    public final static Uri CONTACT_URI = ContactsContract.Contacts.CONTENT_URI;
    //联系人过滤条件
    public final static String[] CONTACT_PROJECTION = new String[]{
            ContactsContract.Contacts._ID
            , ContactsContract.Contacts.DISPLAY_NAME
    };
    public final static String[] CONTACT_PHONE_PROJECTION = new String[]{
            ContactsContract.Contacts._ID
            , ContactsContract.CommonDataKinds.Phone.NUMBER
            , ContactsContract.Contacts.DISPLAY_NAME
    };

    ////////////////////////////////////////////////////////////////////////////////////联系人/////////////////////////////////////////////////////////////////

    /**
     * 获取手机中的电话号码
     *
     * @param activity
     * @return
     */
    public static List<Map<String, String>> getSmsAll(Activity activity) {
        List<Map<String, String>> list = new ArrayList<>();

        if (null == activity) {
            return list;
        }
        ContentResolver contentResolver = activity.getContentResolver();
        Cursor cursor = contentResolver.query(Uri.parse(SMS_URI_ALL), SMS_PROJECTION, null, null, SMS_ORDER);
        if (null == cursor || cursor.getCount() == 0) {
            closeCursor(cursor);
            return list;
        }
        Map<String, String> map = null;

        while (cursor.moveToNext()) {
            map = new HashMap<>();
            String address = cursor.getString(cursor.getColumnIndex("address"));
            String body = cursor.getString(cursor.getColumnIndex("body"));
            String person = cursor.getString(cursor.getColumnIndex("person"));
            String date = cursor.getString(cursor.getColumnIndex("date"));
            map.put("address", address);
            map.put("body", body);
            map.put("person", person);
            map.put("date", date);
            list.add(map);
        }
        closeCursor(cursor);
        return list;
    }


    /**
     * 获取所有电话记录
     *
     * @param activity
     * @return
     */
    @SuppressLint("MissingPermission")
    public static List<Map<String, String>> getCallAll(Activity activity) {
        List<Map<String, String>> list = new ArrayList<>();
        // 获取联系人数据
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
            return list;
        }
        ContentResolver contentResolver = activity.getContentResolver();
        Cursor cursor = contentResolver.query(CALL_URI, CALL_PROJECTION, null, null, CALL_ORDER);
        if (null == cursor || cursor.getCount() == 0) {
            closeCursor(cursor);
            return list;
        }
        //最终要返回的数据
        Map<String, String> map = null;
        while (cursor.moveToNext()) {
            map = new HashMap<>();
            String name = cursor.getString(cursor.getColumnIndex(CallLog.Calls.CACHED_NAME));
            String number = cursor.getString(cursor.getColumnIndex(CallLog.Calls.NUMBER));
            String date = cursor.getString(cursor.getColumnIndex(CallLog.Calls.DATE));
            map.put("name", name);
            map.put("number", number);
            map.put("date", date);
            list.add(map);
        }
        closeCursor(cursor);
        return list;
    }


//    /**
//     * 获取手机里的所有联系人
//     *
//     * @param activity
//     * @return
//     */
//    public static List<ContactBean> getContactAll(Activity activity) {
//        List<ContactBean> contactBeanList = new ArrayList<>();
//        // 获取联系人数据
//        ContentResolver contentResolver = activity.getContentResolver();
//        Cursor cursor = contentResolver.query(CONTACT_URI, CONTACT_PROJECTION, null, null, null);
//        if (null == cursor || cursor.getCount() == 0) {
//            closeCursor(cursor);
//            return contactBeanList;
//        }
//        //最终要返回的数据
//        ContactBean contactBean = null;
//        while (cursor.moveToNext()) {
//            contactBean = new ContactBean();
//            int contactId = cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts._ID));
//            String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
//            contactBean.username = StringUtil.isEmpty(name) ? "" : name;
//            contactBean.phones = getContactPhoneListByID(activity, String.valueOf(contactId), name);
//            contactBeanList.add(contactBean);
//        }
//        closeCursor(cursor);
//        return contactBeanList;
//    }
//
//    /**
//     * 更具电话号码ID 和 显示姓名 获取相应的电话号码
//     *
//     * @param activity
//     * @param contactId
//     * @param name
//     * @return
//     */
//    public static List<String> getContactPhoneListByID(Activity activity, String contactId, String name) {
//        List<String> phoneList = new ArrayList<>();
//        ContentResolver contentResolver = activity.getContentResolver();
//        Cursor cursor = contentResolver.query(
//                ContactsContract.CommonDataKinds.Phone.CONTENT_URI
//                , CONTACT_PHONE_PROJECTION
//                , ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId
//                , null
//                , null);
//        if (null == cursor || cursor.getCount() == 0) {
//            closeCursor(cursor);
//            return phoneList;
//        }
//        while (cursor.moveToNext()) {
//            String phone = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
//            if (!phoneList.contains(phone)) {
//                if (!StringUtil.isEmpty(name)) {
//                    String phoneName = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
//                    if (phoneName.equals(name)) {
//                        phoneList.add(phone);
//                    }
//                } else {
//                    phoneList.add(phone);
//                }
//            }
//        }
//        closeCursor(cursor);
//        return phoneList;
//    }


    /**
     * 删除某个姓名下的所有电话号码
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public static void deleteAllPhone(Context context, String name) {
//根据姓名求id
        Uri uri = Uri.parse("content://com.android.contacts/raw_contacts");
        ContentResolver resolver = context.getContentResolver();
        Cursor cursor = resolver.query(uri, new String[]{ContactsContract.Data._ID}, "display_name=?", new String[]{name}, null);
        if (cursor.moveToFirst()) {
            int id = cursor.getInt(0);
//根据id删除data中的相应数据
            resolver.delete(uri, "display_name=?", new String[]{name});
            uri = Uri.parse("content://com.android.contacts/data");
            resolver.delete(uri, "raw_contact_id=?", new String[]{id + ""});

        }
        StringUtil.close(cursor);
    }

    /**
     * 关闭游标
     *
     * @param cursor
     */
    public static void closeCursor(Cursor cursor) {
        if (null != cursor) {
            cursor.close();
        }
    }


}
