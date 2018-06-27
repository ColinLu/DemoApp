package com.colin.demo.app.utils;

import android.support.annotation.NonNull;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 正则表达式帮助类
 * <p>
 * "^\d+$"　　//非负整数（正整数 + 0）
 * "^[0-9]*[1-9][0-9]*$"　　//正整数
 * "^((-\d+)|(0+))$"　　//非正整数（负整数 + 0）
 * "^-[0-9]*[1-9][0-9]*$"　　//负整数
 * "^-?\d+$"　　　　//整数
 * "^\d+(\.\d+)?$"　　//非负浮点数（正浮点数 + 0）
 * "^(([0-9]+\.[0-9]*[1-9][0-9]*)|([0-9]*[1-9][0-9]*\.[0-9]+)|([0-9]*[1-9][0-9]*))$"　　//正浮点数
 * "^((-\d+(\.\d+)?)|(0+(\.0+)?))$"　　//非正浮点数（负浮点数 + 0）
 * "^(-(([0-9]+\.[0-9]*[1-9][0-9]*)|([0-9]*[1-9][0-9]*\.[0-9]+)|([0-9]*[1-9][0-9]*)))$"　　//负浮点
 * 数
 * "^(-?\d+)(\.\d+)?$"  //浮点数
 * "^[A-Za-z]+$"  //由26个英文字母组成的字符串
 * "^[A-Z]+$"  //由26个英文字母的大写组成的字符串
 * "^[a-z]+$"  //由26个英文字母的小写组成的字符串
 * "^[A-Za-z0-9]+$" //由数字和26个英文字母组成的字符串
 * "^\w+$" //由数字、26个英文字母或者下划线组成的字符串
 * "^[\w-]+(\.[\w-]+)*@[\w-]+(\.[\w-]+)+$" //email地址
 * "^[a-zA-z]+://(\w+(-\w+)*)(\.(\w+(-\w+)*))*(\?\S*)?$" //url
 * <p>
 * 正则表达式--验证手机号码:13[0-9]{9}
 * 实现手机号前带86或是+86的情况:^((\+86)|(86))?(13)\d{9}$
 * 电话号码与手机号码同时验证:(^(\d{3,4}-)?\d{7,8})$|(13[0-9]{9})
 * 提取信息中的网络链接:(h|H)(r|R)(e|E)(f|F)  *=  *('|")?(\w|\\|\/|\.)+('|"|  *|>)?
 * 提取信息中的邮件地址:\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*
 * 提取信息中的图片链接:(s|S)(r|R)(c|C)  *=  *('|")?(\w|\\|\/|\.)+('|"|  *|>)?
 * 提取信息中的IP地址:(\d+)\.(\d+)\.(\d+)\.(\d+)
 * 提取信息中的中国手机号码:(86)*0*13\d{9}
 * 提取信息中的中国固定电话号码:(\(\d{3,4}\)|\d{3,4}-|\s)?\d{8}
 * 提取信息中的中国电话号码（包括移动和固定电话）:(\(\d{3,4}\)|\d{3,4}-|\s)?\d{7,14}
 * 提取信息中的中国邮政编码:[1-9]{1}(\d+){5}
 * 提取信息中的中国身份证号码:\d{18}|\d{15}
 * 提取信息中的整数：\d+
 * 提取信息中的浮点数（即小数）：(-?\d*)\.?\d+
 * 提取信息中的任何数字  ：(-?\d*)(\.\d+)?
 * 提取信息中的中文字符串：[\u4e00-\u9fa5]*
 * 提取信息中的双字节字符串  (汉字)：[^\x00-\xff]*
 */
public class RegulatorUtil {


    public static class Holder {
        static RegulatorUtil instance = new RegulatorUtil();
    }

    public static RegulatorUtil getInstance() {
        return Holder.instance;
    }


    private RegulatorUtil() {
    }


    /**
     * 判断是不是一个合法的电子邮件地址
     *
     * @param email_string
     * @return
     */
    public boolean isEmail(@NonNull String email_string) {
        if (StringUtil.isEmpty(email_string)) {
            return false;
        }

        Pattern email_pattern = Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");
        return email_pattern.matcher(email_string).matches();
    }

    /**
     * 判断是不是一个合法的车牌号码
     *
     * @param plateCode_string
     * @return
     */
    public boolean isPlateCode(@NonNull String plateCode_string) {
        if (StringUtil.isEmpty(plateCode_string)) {
            return false;
        }
        Pattern email_pattern = Pattern.compile("^[\\u4e00-\\u9fa5]{1}[A-Z][A-Z_0-9]{5}");
        return email_pattern.matcher(plateCode_string).matches();
    }

    /**
     * 判断是不是一个合法的IP地址
     * 1.  为什么三位数的匹配放在二位数/一位数的前面？因为正则表达式规则之一：最先开始的匹配拥有最高的优先权。
     * 2.  0.0.0.0和255.255.255.255是合法存在的IP地址，你知道是为什么吗？
     * 3.  192.169.01.108这种数字前面多带了个0的类型的，在这里不是合法的，为什么要这样？
     * 4.  为什么前面在最前面要有?:呢？它在这里有什么用？
     *
     * @param ip_string
     * @return
     */
    public boolean isIPAddress(@NonNull String ip_string) {
        if (StringUtil.isEmpty(ip_string)) {
            return false;
        }
        Pattern ip_pattern = Pattern.compile("((?:(?:25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d)))\\.){3}(?:25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d))))");
        return ip_pattern.matcher(ip_string).matches();
    }

    /**
     * 验证输入是否电话号码
     *
     * @param str
     * @return 如果是符合格式的字符串, 返回 <b>true </b>,否则为 <b>false </b>
     */
    public boolean isMobilePhone(@NonNull String str) {
        if (StringUtil.isEmpty(str)) {
            return false;
        }
        String regex = "[1]\\d{10}";
        return match(regex, str);
    }

    /**
     * 验证输入是否电话号码
     *
     * @param str
     * @return 如果是符合格式的字符串, 返回 <b>true </b>,否则为 <b>false </b>
     */
    public boolean isPhone(@NonNull String str) {
        if (StringUtil.isEmpty(str)) {
            return false;
        }
        return str.matches("^(0?(13[0-9]|15[012356789]|17[013678]|18[0-9]|14[57])[0-9]{8})|(400|800)([0-9\\\\-]{7,10})|(([0-9]{4}|[0-9]{3})(-| )?)?([0-9]{7,8})((-| |转)*([0-9]{1,4}))?$");
    }

    /**
     * 验证验证输入汉字
     *
     * @param str
     * @return 如果是符合格式的字符串, 返回 <b>true </b>,否则为 <b>false </b>
     */
    public boolean IsChinese(@NonNull String str) {
        if (StringUtil.isEmpty(str)) {
            return false;
        }
        String regex = "^[\u4e00-\u9fa5],{0,}$";
        return match(regex, str);
    }


    /**
     * 验证全是数字组成的字符串(含小数)
     *
     * @param value
     * @return
     */
    public boolean isNumber(@NonNull String value) {
        if (StringUtil.isEmpty(value)) {
            return false;
        }
        String regex = "(-?\\d*)(\\.\\d+)?";
        return match(regex, value);
    }

    /**
     * 判断字符串中是否含有数字
     *
     * @param string
     * @return
     */
    public boolean isHaveNumber(String string) {

        if (null == string || "null".equals(string) || string.trim().length() == 0) {
            return false;
        }
        return string.matches(".*\\d+.*");
    }


    /**
     * 是整数
     *
     * @param value
     * @return
     */
    public boolean isInteger(@NonNull String value) {
        if (StringUtil.isEmpty(value)) {
            return false;
        }
        String regex = "^-?[0-9]\\d*$";
        return match(regex, value);
    }

    /**
     * 是正整数
     *
     * @param value
     * @return
     */
    public boolean isSignlessInteger(@NonNull String value) {
        if (StringUtil.isEmpty(value)) {
            return false;
        }
        String regex = "^-[0-9]\\d*$";
        return match(regex, value);
    }

    /**
     * 是负整数
     *
     * @param value
     * @return
     */
    public boolean isNegativeInteger(@NonNull String value) {
        if (StringUtil.isEmpty(value)) {
            return false;
        }
        String regex = "^-[0-9]\\d*$";
        return match(regex, value);
    }

    /**
     * 是浮点数
     *
     * @param value
     * @return
     */
    public boolean isFloat(@NonNull String value) {
        if (StringUtil.isEmpty(value)) {
            return false;
        }
        String regex = "^-?[0-9]\\d*\\.\\d*|0\\.\\d*[1-9]\\d*$";
        return match(regex, value);
    }


    /**
     * 验证输入两位小数
     *
     * @param str
     * @return 如果是符合格式的字符串, 返回 <b>true </b>,否则为 <b>false </b>
     */
    public boolean isDecimal(@NonNull String str) {
        if (StringUtil.isEmpty(str)) {
            return false;
        }
        String regex = "^[0-9]+(.[0-9]{2})?$";
        return match(regex, str);
    }

    /**
     * 验证身份证
     * 正则只能验证身份证的格式是否正确，是验证不了真伪的，只有公安系统里才能验证身份证号真假。
     *
     * @param id
     * @return
     */
    public boolean isIDCard(String id) {
        if (StringUtil.isEmpty(id)) {
            return false;
        }
        String regex = "^[1-9]\\d{7}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}$|^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}([0-9]|X)$";
        return match(regex, id);
    }

    /**
     * 验证密码 必须含有数字和字母
     * <p>
     * 强：字母+数字+特殊字符
     * ^(?![a-zA-z]+$)(?!\d+$)(?![!@#$%^&*]+$)(?![a-zA-z\d]+$)(?![a-zA-z!@#$%^&*]+$)(?![\d!@#$%^&*]+$)[a-zA-Z\d!@#$%^&*]+$
     * <p>
     * 中：字母+数字，字母+特殊字符，数字+特殊字符
     * ^(?![a-zA-z]+$)(?!\d+$)(?![!@#$%^&*]+$)[a-zA-Z\d!@#$%^&*]+$
     * <p>
     * 弱：纯数字，纯字母，纯特殊字符
     * ^(?:\d+|[a-zA-Z]+|[!@#$%^&*]+)$
     *
     * @param password
     * @return
     */
    public boolean passwordOk(String password) {
        if (StringUtil.isEmpty(password)) {
            return false;
        }
        String regex = "^(?![0-9]+$)(?![a-zA-Z]+$)[a-zA-Z0-9]{6,20}";
        return match(regex, password);
    }

    public boolean nickOK(String name) {
        if (StringUtil.isEmpty(name)) {
            return false;
        }
        String regex = "^(?![0-9]+$)(?![a-zA-Z]+$)[a-zA-Z0-9]{6,16}";
        return match(regex, name);
    }

    /**
     * @param regex 正则表达式字符串
     * @param str   要匹配的字符串
     * @return 如果str 符合 regex的正则表达式格式,返回true, 否则返回 false;
     */
    private boolean match(@NonNull String regex, @NonNull String str) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);
        return matcher.matches();
    }
}
