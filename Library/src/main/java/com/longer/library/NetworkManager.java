package com.longer.library;

import android.app.Application;
import android.content.IntentFilter;

import com.longer.library.utils.Constants;

public class NetworkManager {

    private static volatile NetworkManager instance;
    private static NetStateReceiver receiver;
    private Application application;

    private NetworkManager() {
        receiver = new NetStateReceiver();
    }


    public static NetworkManager getInstance() {
        if (instance == null) {
            synchronized (NetworkManager.class) {
                if (instance == null) {
                    instance = new NetworkManager();
                }
            }

        }
        return instance;
    }

    public Application getApplication() {
        return application;
    }

    public void init(Application application) {
        this.application = application;
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constants.ANDROID_NET_CHANGE_ACTION);
        application.registerReceiver(receiver, filter);
    }

    public void registObserver(Object object) {
        receiver.addObserver(object);
    }

    public void unRegistObserver(Object object) {
        receiver.removeObserver(object);
    }


}
