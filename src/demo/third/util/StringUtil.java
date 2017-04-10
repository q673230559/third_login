package demo.third.util;

/**
 * 字符串工具类
 * 
 * @author Administrator
 *
 */
public class StringUtil {

    private StringUtil() {
    }

    /**
     * 是否为空,
     * 
     * @param str
     * @return null,trim后为空串返回true，否则返回false
     */
    public static boolean isEmpty(String str) {
        if (str == null) {
            return true;
        }

        return str.trim().length() == 0;
    }
}
