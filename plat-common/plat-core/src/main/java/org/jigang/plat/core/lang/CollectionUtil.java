package org.jigang.plat.core.lang;

import java.util.Collection;

/**
 * Created by wujigang on 2016/5/18.
 */
public class CollectionUtil {
    public static boolean isEmpty(Collection c) {
        return null == c || c.size() <= 0;
    }
}
