package com.daixinmini.base.util;

import com.daixinmini.base.exception.BizException;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.util.StringUtils;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

public class BasicUtil {

    /**
     * 是否空字符串（会trim）。
     *
     * @param text
     * @return true：null、空格、多个空格
     */
    public static boolean isBlank(String text) {
        return text == null || text.length() == 0 || "".equals(text.trim());
    }

    public static boolean isNotBlank(String text) {
        return !isBlank(text);
    }

    /**
     * 是否空字符串（不trim）。
     *
     * @param text
     * @return true：null、空格
     */
    public static boolean isEmpty(String text) {
        return text == null || text.length() == 0 || "".equals(text);
    }

    public static boolean isNotEmpty(String text) {
        return !isEmpty(text);
    }

    public static String toString(Throwable t) {
        StringWriter sw = null;
        PrintWriter pw = null;
        try {
            sw = new StringWriter();
            pw = new PrintWriter(sw);
            t.printStackTrace(pw);
        } finally {
            try {
                pw.close();
            } catch (Exception ex) {
                // IGNORED
            }
            try {
                sw.close();
            } catch (Exception ex) {
                // IGNORED
            }
        }
        return sw.toString();
    }

    public static String randomNo() {
        return randomNo(8);
    }

    public static String randomNo(int size) {
        return UUID.randomUUID().toString().replaceAll("-", "").substring(0, size);
    }

    public static String uuid() {
        return UUID.randomUUID().toString();
    }

    /**
     * 是否合法值
     */
    public static boolean isValidValue(Object value) {
        boolean invalid = value == null || "".equals(value) || "null".equals(value) || "undefined".equals(value);
        return !invalid;
    }

    public static boolean isInteger(String arg) {
        if (isEmpty(arg)) {
            return false;
        }
        return arg.matches("^(0|[1-9][0-9]*)$");
    }

    public static BigDecimal decimal(Object obj) {
        return asDecimal(obj);
    }

    public static BigDecimal decimal(Object obj, BigDecimal defaultValue) {
        BigDecimal value = asDecimal(obj);
        return value != null ? value : defaultValue;
    }

    private static BigDecimal asDecimal(Object obj) {
        if (obj == null) {
            return null;
        } else if (obj instanceof BigDecimal) {
            return (BigDecimal) obj;
        } else if (obj instanceof JsonNode) {
            String text = ((JsonNode) obj).asText();
            if (!isValidValue(text)) {
                return null;
            } else {
                try {
                    return new BigDecimal(text);
                } catch (Exception ex) {
                    return null;
                }
            }
        } else {
            try {
                return new BigDecimal(obj.toString());
            } catch (Exception ex) {
                return null;
            }
        }
    }

    public static boolean bool(Object obj) {
        return bool(obj, false);
    }

    public static boolean bool(Object obj, boolean defaultValue) {
        Boolean value = asBoolean(obj);
        return value == null ? defaultValue : value;
    }

    private static Boolean asBoolean(Object obj) {
        if (obj == null) {
            return null;
        } else if (obj instanceof Boolean) {
            return (Boolean) obj;
        } else {
            String text = obj.toString();
            // if (obj instanceof JsonNode) {
            // text = ((JsonNode) obj).asText();
            // } else {
            // text = obj.toString();
            // }

            if (!isValidValue(text)) {
                return null;
            } else {
                return Boolean.valueOf(text);
            }
        }
    }

    public static Integer integer(Object obj) {
        return asInteger(obj);
    }

    public static int integer(Object obj, int defaultValue) {
        Integer value = asInteger(obj);
        return value != null ? value : defaultValue;
    }

    private static Integer asInteger(Object obj) {
        if (obj == null) {
            return null;
        } else if (obj instanceof Integer) {
            return (Integer) obj;
        } else if (obj instanceof BigDecimal) {
            return ((BigDecimal) obj).intValue();
        } else {
            String text = obj.toString();
            // if (obj instanceof JsonNode) {
            // text = ((JsonNode) obj).asText();
            // } else {
            // text = obj.toString();
            // }

            if (!isValidValue(text)) {
                return null;
            } else {
                try {
                    return Integer.valueOf(text);
                } catch (NumberFormatException e) {
                    return null;
                }
            }
        }
    }

    public static String string(Object obj) {
        return string(obj, null);
    }

    public static String string(Object obj, String defaultValue) {
        String value = asString(obj);
        return !isEmpty(value) ? value : defaultValue;
    }

    private static String asString(Object obj) {
        if (obj == null) {
            return null;
        } else {
            String text = obj.toString();
            if (obj instanceof JsonNode) {
                text = ((JsonNode) obj).asText();
            } else {
                text = obj.toString();
            }

            if (!isValidValue(text)) {
                return null;
            } else {
                return text;
            }
        }
    }

    public static boolean bothNullOrEquals(Object a, Object b) {
        return (a == null && b == null) || (a != null && b != null && a.equals(b));
    }

    public static String isNullThenDefault(String val, String defaultVal) {
        if (val != null) {
            return val;
        } else {
            return defaultVal;
        }
    }

    public static String isNullOrEmptyThenDefault(String val, String defaultVal) {
        if (!isNullOrEmpty(val)) {
            return val;
        } else {
            return defaultVal;
        }
    }

    public static String isNullThenEmpty(String val) {
        return val == null ? "" : val;
    }

    public static BigDecimal isNullThenZero(BigDecimal val) {
        return val == null ? BigDecimal.ZERO : val;
    }

    public static Boolean isNullOrEmpty(String str) {
        Boolean isNullOrEmpty = true;
        if (str != null) {
            str = str.trim();
            if (!("".equals(str))) {
                isNullOrEmpty = false;
            }
        }
        return isNullOrEmpty;
    }

    public static String substring(String text, int maxLength) {
        if (text == null) {
            return null;
        } else if (text.length() <= maxLength) {
            return text;
        } else {
            return text.substring(0, maxLength - 1);
        }
    }

    /**
     * 添加picker匹配规则 (map格式：pickerXXX, Y/N)
     * (1) 4位以上纯数字，匹配电话号码
     * @param picker
     * @return
     */
    public static Map<String, Object> getPickerMatchMap(String picker) {
        Map<String, Object> map = new HashMap<>();
        String yearRegex = "^(?:(?!0000)[0-9]{4}(?:(?:0[1-9]|1[0-2])(?:0[1-9]|1[0-9]|2[0-8])|(?:0[13-9]|1[0-2])(?:29|30)|(?:0[13578]|1[02])31)|(?:[0-9]{2}(?:0[48]|[2468][048]|[13579][26])|(?:0[48]|[2468][048]|[13579][26])00)0229)$";
        String dateRegex = "^(?:(0[1-9]|1[0-2])(0[1-9]|1[0-9]|2[0-9])|(0[13-9]|1[0-2])30|(0[13578]|1[02])31)$";

        if (picker != null && !"".equals(picker.trim())) {
            if (picker.matches("^[\\d]{4,}$")) { // 4位以上纯数字，匹配电话号码
                map.put("pickerPhone", "Y");
            }
            // 正确的手机号码
            if (picker.matches("\\d{11}")) {
                map.put("validTelephone", "Y");
            }
            if (picker.matches("^[\\d]{6,}$")) { // 6位以上纯数字，匹配证件号
                map.put("pickerNo", "Y");
            }
            // 年月日
            if (picker.matches(yearRegex)) {
                map.put("pickerYear", "Y");
            }
            // 月日
            if (picker.matches(dateRegex)) {
                map.put("pickerDate", "Y");
            }
        }
        return map;
    }

    public static BigDecimal isZeroThenNull(BigDecimal val) {
        if (val != null && BigDecimal.ZERO.compareTo(val) == 0) {
            return null;
        } else {
            return val;
        }
    }

    public static String concat(List<String> strings, String separator) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < strings.size(); i++) {
            sb.append(strings.get(i));
            if (i != strings.size() - 1) {
                sb.append(separator);
            }
        }
        return sb.toString();
    }

    public static String matchHtml(String str) {
        return StringUtils.replace(str, "<\\/?.+?\\/?>", "");
    }

    public static boolean isLike(String source, String target) {
        if (source != null && target != null) {
            source = source.toUpperCase();
            target = target.toUpperCase();
            return source.contains(target) || target.contains(source);
        } else {
            return false;
        }
    }

    /**
     * ; 或者 ；分隔字符串
     * @param value
     * @return
     */
    public static String[] splitBySemicolon(String value) {
        if (value == null) {
            return null;
        }
        String[] split = StringUtils.split(value,";");
        if (split.length > 1) {
            return split;
        } else {
            return StringUtils.split(value, "；");
        }
    }

    public static String isEmptyThenNull(String text) {
        return isEmpty(text) ? null : text;
    }

    // 计算有多少页
    public static int getTotalPage(double rowCount, double size) {
        return (int) Math.ceil(rowCount / size);
    }

    public static <T> void isNullThenThrow(T r, String msg) {
        if (r == null) {
            throw new BizException(msg);
        }
    }

    public static <T> void isEmptyThenThrow(String r, String msg) {
        if (isEmpty(r)) {
            throw new BizException(msg);
        }
    }

    public static void isTrueThenThrow(boolean expression, String msg) {
        if (expression) {
            throw new BizException(msg);
        }
    }

    public static void isTrueThenThrow(boolean expression, String code, String msg) {
        if (expression) {
            throw new BizException(msg);
        }
    }


    public static String base64Encode(String string, boolean reverse) {
        // base64加密
        Base64.Encoder encoder = Base64.getEncoder();
        String encoderStr = encoder.encodeToString(string.getBytes());
        if (reverse && isNotBlank(encoderStr)) {
            String[] encodeSplit = encoderStr.split("==");
            encoderStr = new StringBuffer(encodeSplit[0]).reverse().toString();
        }
        return encoderStr;
    }


}
