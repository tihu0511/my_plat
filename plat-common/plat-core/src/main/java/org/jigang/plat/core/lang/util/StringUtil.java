package org.jigang.plat.core.lang.util;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Created by wujigang on 16/8/19.
 */
public class StringUtil {
    /**
     * 判断字符串是否不为空
     *
     * @param str
     * @return
     */
    public static boolean hasLength(String str) {
        return hasLength((CharSequence) str);
    }

    public static boolean hasLength(CharSequence str) {
        return (str != null && str.length() > 0);
    }

    /**
     * 使用多个分隔符分割字符串
     *
     * @param str
     * @param delimiters 分隔符
     * @return
     */
    public static String[] tokenizeToStringArray(String str, String delimiters) {
        return tokenizeToStringArray(str, delimiters, true, true);
    }

    /**
     * 使用多个分隔符分割字符串
     *
     * @param str
     * @param delimiters
     * @param trimTokens
     * @param ignoreEmptyTokens
     * @return
     */
    public static String[] tokenizeToStringArray(
            String str, String delimiters, boolean trimTokens, boolean ignoreEmptyTokens) {

        if (str == null) {
            return null;
        }
        StringTokenizer st = new StringTokenizer(str, delimiters);
        List<String> tokens = new ArrayList<String>();
        while (st.hasMoreTokens()) {
            String token = st.nextToken();
            if (trimTokens) {
                token = token.trim();
            }
            if (!ignoreEmptyTokens || token.length() > 0) {
                tokens.add(token);
            }
        }
        return toStringArray(tokens);
    }

    /**
     * 集合转换为字符串数组
     * @param collection
     * @return
     */
    public static String[] toStringArray(Collection<String> collection) {
        if (collection == null) {
            return null;
        }
        return collection.toArray(new String[collection.size()]);
    }

    /**
     * 字符串替换
     *
     * @param inString 输入的字符串
     * @param oldPattern 需要被替换的子字符串
     * @param newPattern 需要被替换成的新字符串
     * @return
     */
    public static String replace(String inString, String oldPattern, String newPattern) {
        if (!hasLength(inString) || !hasLength(oldPattern) || newPattern == null) {
            return inString;
        }
        StringBuilder sb = new StringBuilder();
        int pos = 0; // our position in the old string
        int index = inString.indexOf(oldPattern);
        // the index of an occurrence we've found, or -1
        int patLen = oldPattern.length();
        while (index >= 0) {
            sb.append(inString.substring(pos, index));
            sb.append(newPattern);
            pos = index + patLen;
            index = inString.indexOf(oldPattern, pos);
        }
        sb.append(inString.substring(pos));
        // remember to append any characters to the right of a match
        return sb.toString();
    }

    /**
     * 判断子字符串是否匹配
     * copy from spring
     *
     * @param str the original string (or StringBuilder)
     * @param index 开始匹配的下标
     * @param substring the substring to match at the given index
     */
    public static boolean substringMatch(CharSequence str, int index, CharSequence substring) {
        for (int j = 0; j < substring.length(); j++) {
            int i = index + j;
            if (i >= str.length() || str.charAt(i) != substring.charAt(j)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 获取字符串左边len长度字符
     *
     * @param str
     * @param len
     * @return
     */
    public static String left(final String str, final int len) {
        return StringUtils.left(str, len);
    }
}
