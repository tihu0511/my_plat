package org.jigang.plat.core.lang.invoke;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodType;

import static java.lang.invoke.MethodHandles.lookup;

/**
 * 使用jdk java.lang.invoke包的特性
 * Created by wjg on 2016/12/19.
 */
public class MethodHandleUtil {
    /**
     * 获取执行方法
     *
     * @param methodName 方法名称
     * @param obj 对象，表示从该对象获取methodName执行方法
     * @param returnClazz 返回对象class，如void.class
     * @param argsClazz 参数类型class
     * @return 执行方法对象，可以通过MethodHandle.invokeExact()执行方法逻辑
     * @throws NoSuchMethodException
     * @throws IllegalAccessException
     */
    public static MethodHandle getMethodHandle(String methodName, Object obj, Class returnClazz, Class ... argsClazz)
            throws NoSuchMethodException, IllegalAccessException {
        MethodType mt = MethodType.methodType(returnClazz, argsClazz);
        return lookup().findVirtual(obj.getClass(), methodName, mt).bindTo(obj);
    }

}
