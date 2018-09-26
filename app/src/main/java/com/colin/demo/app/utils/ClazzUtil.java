package com.colin.demo.app.utils;

import android.database.Cursor;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * 描述：
 * <p>
 * 作者：Colin
 * 时间：2018/9/26
 */
public class ClazzUtil {

    /**
     * Android SDK21 在Object中增加了2个变量，这样在反射的时候就会多出来一个int类型和class类型的字段，
     * 导致错误。
     * private transient Class<?> shadow$_klass_;
     * private transient int shadow$_monitor_;
     * <p>
     * 从，查询游标中取出查询数据
     *
     * @param classz
     * @param cursor
     * @return List<Object>
     * https://blog.csdn.net/huyuchaoheaven/article/details/48708281?utm_source=copy
     */
    private List<Object> rawQuery(Class classz, Cursor cursor) {
        List<Object> resultObj = new ArrayList<Object>();
        // 获取对象的字段
        Field[] fields = getField(classz);
        // 判断是否存在查询的字段
        if (fields.length == 0)
            return null;

        String fieldName = null;
        String fieldType = null;
        Object objName = null;

        int fieldIndex = 0;
        int i = 0;
        int max = fields.length;

        // 循环取出查询到的结果
        while (cursor.moveToNext()) {

            LogUtil.e("max fields = " + max);

            // 构造新的实例对象
            try {
                objName = classz.newInstance();
            } catch (IllegalAccessException e1) {
                e1.printStackTrace();
            } catch (InstantiationException e1) {
                e1.printStackTrace();
            }

            // fields = objName.getClass().getFields();
            try {
                for (i = 0; i < max; i++) {
                    fields[i].setAccessible(true);
                    fieldName = fields[i].getName();
                    fieldIndex = cursor.getColumnIndex(fieldName);
                    fieldType = fields[i].getType().getSimpleName();
                    LogUtil.e(" objName = " + objName + "fieldType  = " + fieldType + "  fieldIndex = " + fieldIndex + " fieldName = " + fieldName);
                    if ("byte".equals(fieldType))
                        fields[i].setByte(objName,
                                (byte) cursor.getInt(fieldIndex));

                    else if ("String".equals(fieldType))
                        fields[i].set(objName, cursor.getString(fieldIndex));

                    else if ("int".equals(fieldType))
                        // android 5.0 之后会有错误。
                        if (!fieldName.equals("shadow$_monitor_")) {
                            fields[i].setInt(objName, cursor.getInt(fieldIndex));
                        } else if ("boolean".equals(fieldType)) {
                            if ("true".equals(cursor.getString(fieldIndex)))
                                fields[i].setBoolean(objName, true);
                            else
                                fields[i].setBoolean(objName, false);
                        } else if ("long".equals(fieldType)) {
                            fields[i].setLong(objName, cursor.getLong(fieldIndex));
                        } else if ("float".equals(fieldType))
                            fields[i]
                                    .setFloat(objName, cursor.getFloat(fieldIndex));

                        else if ("double".equals(fieldType))
                            fields[i].setDouble(objName,
                                    cursor.getDouble(fieldIndex));

                        else if ("short".equals(fieldType))
                            fields[i]
                                    .setShort(objName, cursor.getShort(fieldIndex));

                        else if ("char".equals(fieldType))
                            fields[i].setChar(objName, cursor.getString(fieldIndex)
                                    .charAt(0));

                }

                // 保存当前的实例对象
                resultObj.add(objName);
//              showMsg("resultObj==" + resultObj.size());
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        cursor.close();
        return resultObj;
    }


    /**
     * @param @param clazz
     * @Description: 获得对象的所有字段，包括其父类（如果有）
     * https://blog.csdn.net/huyuchaoheaven/article/details/48708281?utm_source=copy
     */
    private Field[] getField(Class clazz) {
        Field[] childfields = clazz.getDeclaredFields();
        Field[] superFields = clazz.getSuperclass().getDeclaredFields();
        Field[] fields = new Field[childfields.length + superFields.length];
        System.arraycopy(childfields, 0, fields, 0, childfields.length);
        System.arraycopy(superFields, 0, fields, childfields.length,
                superFields.length);
        return fields;
    }

}
