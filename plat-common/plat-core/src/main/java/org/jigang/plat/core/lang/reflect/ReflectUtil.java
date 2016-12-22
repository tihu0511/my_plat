package org.jigang.plat.core.lang.reflect;

import org.jigang.plat.core.lang.util.DateUtil;
import org.jigang.plat.core.lang.util.StringUtil;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.text.ParseException;

/**
 *
 * Created by wjg on 2016/6/23.
 */
public class ReflectUtil {

    private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    /**
     * 利用反射机制设置成员变量值
     * 仅支持基本包装类型、String、Date，其中Date默认格式为yyyy-MM-dd HH:mm:ss
     * @param field
     * @param instance
     * @param value
     * @param <T>
     * @throws IllegalAccessException
     */
    public static <T> void setFieldValue(Field field, Object instance, String value) throws IllegalAccessException, ParseException {
        Type type = field.getGenericType();
        String typeStr = type.toString();

        if (typeStr.equals("class java.lang.String")) { // 如果type是类类型，则前面包含"class "，后面跟类名
            field.set(instance, value);
        } else if (typeStr.equals("class java.lang.Integer")) {
            field.set(instance, Integer.valueOf(value));
        } else if (typeStr.equals("class java.lang.Boolean")) {
            field.set(instance, Boolean.valueOf(value));
        } else if (typeStr.equals("class java.math.BigDecimal")) {
            field.set(instance, new BigDecimal(value));
        } else if (typeStr.equals("class java.util.Date")) {
            field.set(instance, DateUtil.parse(value, DATE_FORMAT));
        }
    }

    /**
     * 获取字段值
     * @param obj
     * @param fieldName
     * @return
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     */
    public static Object getFieldValue(Object obj, String fieldName) throws NoSuchFieldException, IllegalAccessException {
        if (null == obj || !StringUtil.hasLength(fieldName)) {
            return null;
        }
        Field field = getDeclaredField(obj, fieldName);
        field.setAccessible(true);
        return null == field ? null : field.get(obj);
    }

    /**
     * 获取对象的 DeclaredField
     * 可从父类中获取，如有重复，优先取子类中的字段
     *
     * @param obj : 对象
     * @param fieldName : 属性名
     * @return
     */
    public static Field getDeclaredField(Object obj, String fieldName){
        Field field = null ;
        Class<?> clazz = obj.getClass() ;
        while (clazz != Object.class) {
            try {
                field = clazz.getDeclaredField(fieldName) ;

                return field ;
            } catch (NoSuchFieldException e) {
                //忽略异常，继续寻找父类中的字段
            }
            clazz = clazz.getSuperclass();
        }
        return null;
    }

}
