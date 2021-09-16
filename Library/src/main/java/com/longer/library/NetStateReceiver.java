package com.longer.library;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.longer.library.annotation.Network;
import com.longer.library.bean.MethodManager;
import com.longer.library.type.NetType;
import com.longer.library.utils.Constants;
import com.longer.library.utils.NetWorkUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

// 广播
public class NetStateReceiver extends BroadcastReceiver {
    private NetType netType;
    private Map<Object, List<MethodManager>> networkList;


    public NetStateReceiver() {
        //初始化网络
        netType = NetType.NONE;
        networkList = new HashMap<>();
    }


    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent == null || intent.getAction() == null) {
            Log.d(Constants.LOG_TAG, "onReceive:error ");
            return;
        }

        if (intent.getAction().equalsIgnoreCase(Constants.ANDROID_NET_CHANGE_ACTION)) {
            netType = NetWorkUtils.getNetType();
            Log.e(Constants.LOG_TAG, "网络发生改变" + netType);
            if (NetWorkUtils.isNetworkAvailable()) {//所有能联网的方式循环判断
                Log.e(Constants.LOG_TAG, "网络连接成功！");

            } else {
                Log.e(Constants.LOG_TAG, "网络连接失败！");

            }
        }

        // 通知全局
        post(netType);

    }

    private void post(NetType netType) {
        // 获取方法
        Set<Object> objects = networkList.keySet();
        for (Object object : objects) {
            List<MethodManager> methodManagers = networkList.get(object);
            if(!methodManagers.isEmpty()){
                for (MethodManager methodManager : methodManagers) {
                    // invok
                    if(methodManager.getType().isAssignableFrom(netType.getClass())){
                        switch (methodManager.getNetType())
                        {
                            case AUTO:
                                invoke(methodManager,object,netType);
                                break;
                            case WIFI:
                                if(netType==NetType.WIFI||netType==NetType.NONE){
                                    invoke(methodManager,object,netType);
                                }
                                break;
                            case CMWAP:
                                if(netType==NetType.CMWAP||netType==NetType.NONE){
                                    invoke(methodManager,object,netType);
                                }
                                break;
                            case CMNET:
                                if(netType==NetType.CMNET||netType==NetType.NONE){
                                    invoke(methodManager,object,netType);
                                }
                                break;
                        }

                    }
                }
            }
        }
    }

    private void invoke(MethodManager methodManager, Object object, NetType netType) {
        try {
            methodManager.getMethod().invoke(object, netType);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addObserver(Object object) {
        // 收集需要通知的方法，放入数据结构
        // 拿到注解类的方法
        if (networkList.get(object) == null) {
            List<MethodManager> methods = findAnnotationMethods(object);
            networkList.put(object, methods);
        }
    }

    private List<MethodManager> findAnnotationMethods(Object object) {
        List<MethodManager> netMethods = new ArrayList<>();

        Class<?> clazz = object.getClass();
        Method[] methods = clazz.getMethods();
        for (Method method : methods) {
            Network annotation = method.getAnnotation(Network.class);
            if (annotation == null) {
                continue;
            }
            if (method.getParameterTypes().length != 1) {
                throw new RuntimeException("方法的参数只能有一个");
            }

            netMethods.add(new MethodManager(method.getParameterTypes()[0], annotation.netType(), method));
        }
        return netMethods;
    }

    public void removeObserver(Object object) {
        if (!networkList.isEmpty()) {
            networkList.remove(object);
        }
    }
}
