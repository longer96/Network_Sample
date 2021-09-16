package com.longer.library.bean;

import com.longer.library.type.NetType;

import java.lang.reflect.Method;

public class MethodManager {


    // 参数类型  NetType
    private Class<?> type;

    // 注解网络类型 netType = NetType;
    private NetType netType;

    private Method method;

    public MethodManager(Class<?> type, NetType netType, Method method) {
        this.type = type;
        this.netType = netType;
        this.method = method;
    }

    public Class<?> getType() {
        return type;
    }

    public NetType getNetType() {
        return netType;
    }

    public Method getMethod() {
        return method;
    }
}
