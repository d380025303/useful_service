package com.daixinmini.base.util;

import java.io.IOException;
import java.util.Base64;


/**
 * <p>Project: ktcrm-starter-api</p>
 * <p>Description: base64加盐算法</p>
 * <p>Copyright (c) 2019 Karrytech (Shanghai/Beijing) Co., Ltd.</p>
 * <p>All Rights Reserved.</p>
 * @author <a href="mailto:yourname@karrytech.com">Zhou YanTao</a>
 */
public class Base64Util {

    /**
     * 字符串Base64位加密
     * @param string
     * @return
     */
    public static String base64Encode(String string) {
        return base64Encode(string, true);
    }

    /**
     * 字符串Base64位解密
     * @param str
     * @return
     */
    public static String base64Decode(String str) {
        return base64Decode(str, true);
    }

    private static String base64Encode(String string, boolean reverse) {
        // base64加密
        Base64.Encoder encoder = Base64.getEncoder();
        String encoderStr = encoder.encodeToString(string.getBytes());
        if (reverse && BasicUtil.isNotBlank(encoderStr)) {
            String[] encodeSplit = encoderStr.split("==");
            encoderStr = new StringBuffer(encodeSplit[0]).reverse().toString();
        }
        return encoderStr;
    }

    private static String base64Decode(String str, boolean reverse) {
        try {
            if (BasicUtil.isNotBlank(str)) {
                String string = str;
                if (reverse) {
                    string = new StringBuffer(string).reverse().toString();
                }
                Base64.Decoder decoder = Base64.getDecoder();
                byte[] decoderStr = decoder.decode(string);
                return new String(decoderStr, "UTF-8");
            } else {
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
}