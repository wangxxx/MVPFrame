package com.wangxing.code.utils;

import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class CheckUtil {

    public static boolean checkPasswordLength(String password) {
        if (password == null) return false;

        return password.length() >= 6 && password.length() <= 20;
    }

    public static boolean checkPasswordContent(String password) {
        if (password == null) return false;

        return password.matches("^[0-9a-zA-Z]*$");
    }

    public static boolean checkUsernameLenhth(String content) {

        if (content == null) return false;
        String tempStr;
        int count = 0;
        for (int i = 0; i < content.length(); i++) {
            tempStr = String.valueOf(content.charAt(i));
            if (tempStr.getBytes().length > 1) {
                count++;
            }
        }
        return count <= 12;
    }

    public static boolean checkUsername(String content) {
        if (content == null) return false;

        return content.matches("^.*[\\u4e00-\\u9fa5].*[\\u4e00-\\u9fa5].*$");

    }

    public static boolean checkPhone(String mobiles) {
        String telRegex = "[1][34578]\\d{9}";
        // "[1]"代表第1位为数字1，"[3578]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        if (TextUtils.isEmpty(mobiles)) {
            return false;
        } else
            return mobiles.matches(telRegex);
    }


    /**
     * 验证手机号码
     * <p>
     *
     * @param cellphone
     * @return
     */

    public static boolean isChinaPhoneLegal(String cellphone) throws PatternSyntaxException {
        String regExp = "^(13[0-9]{9})|(18[0-9]{9})|(14[0-9]{9})|(17[0-9]{9})|(15[0-9]{9})$";
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(cellphone);
        return m.matches();
    }

}
