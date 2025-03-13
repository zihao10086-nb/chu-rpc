package com.qiaochu.churpc.proxy;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Mock服务代理 （JDK动态代理）
 */
@Slf4j
public class MockServiceProxy implements InvocationHandler {

    /**
     * 调用代理
     *
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        //根据方法分返回值类型，生成特定的默认值对象
        Class<?> methodReturnType = method.getReturnType();
        log.info("mock invoke {}",method.getName());
        return getDefaultObject(methodReturnType);
    }

    /**
     * 根据返回值类型生成默认值对象
     * @param returnType
     * @return
     */
    private Object getDefaultObject(Class<?> returnType) {
        //基本类型
        if (returnType.isPrimitive()){
            if (returnType == int.class){
                return 0;
            }else if (returnType == boolean.class){
                return false;
            }else if (returnType == long.class){
                return 0L;
            }else if (returnType == double.class){
                return 0.0;
            }else if (returnType == short.class){
                return (short) 0;
            }
        }
        return null;
    }
}
