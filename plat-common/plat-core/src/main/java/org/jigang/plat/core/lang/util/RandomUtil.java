package org.jigang.plat.core.lang.util;

import org.apache.commons.lang3.RandomUtils;

/**
 * Created by wjg on 2016/12/12.
 */
public class RandomUtil {
    private static char[] RANDOM_CHARS = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'A',
            'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z' };


    /**
     * 随机生成字符串[0-9a-zA-Z]{length}
     *
     * @param length
     * @return
     */
    public static String randomString(int length) {
        char[] randBuffer = new char[length];
        for (int i = 0; i < randBuffer.length; i++) {
            randBuffer[i] = RANDOM_CHARS[RandomUtils.nextInt(0, RANDOM_CHARS.length)];
        }
        return new String(randBuffer);
    }
}
