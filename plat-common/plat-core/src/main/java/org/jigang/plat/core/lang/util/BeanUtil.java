package org.jigang.plat.core.lang.util;

import org.apache.commons.beanutils.BeanUtils;

import java.io.*;
import java.lang.reflect.InvocationTargetException;

/**
 * Bean工具类
 */
public class BeanUtil {
    /**
     * 深度复制对象
     * @param src
     * @return
     * @throws RuntimeException
     */
    @SuppressWarnings("unchecked")
    public static <T> T cloneTo(T src) throws RuntimeException {
        ByteArrayOutputStream memoryBuffer = new ByteArrayOutputStream();
        ObjectOutputStream out = null;
        ObjectInputStream in = null;
        T dist = null;
        try {
            out = new ObjectOutputStream(memoryBuffer);
            out.writeObject(src);
            out.flush();
            in = new ObjectInputStream(new ByteArrayInputStream(memoryBuffer.toByteArray()));
            dist = (T) in.readObject();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return dist;
    }

    /**
     *拷贝对象属性
     * @param dest 需要拷贝到的新对象
     * @param orig 原对象
     *  */

    public static void copyProperties(Object dest, Object orig){
        if(dest == null){
            return;
        }
        if(orig == null){
            return;
        }
        try {
            BeanUtils.copyProperties(dest, orig);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
