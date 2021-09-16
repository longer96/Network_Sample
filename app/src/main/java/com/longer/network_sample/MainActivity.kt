package com.longer.network_sample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.longer.library.NetworkManager
import com.longer.library.annotation.Network
import com.longer.library.type.NetType
import com.longer.library.type.NetType.*
import com.longer.library.utils.Constants

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        NetworkManager.getInstance().registObserver(this)
    }


    @Network(netType = AUTO)
    fun test1(netType: NetType) {
        print("test1 run")
        when (netType) {
            WIFI -> Log.e(Constants.LOG_TAG, "MainActivity >>> WIFI")
            CMNET, CMWAP ->                 //流量网络
                Log.e(Constants.LOG_TAG, "MainActivity >>> " + netType.name)
            NONE ->                 //没哟网络
                Log.e(Constants.LOG_TAG, "MainActivity >>> 没有网络")
            AUTO -> Log.e(Constants.LOG_TAG, "MainActivity >>> Auto")
        }
    }

    @Network
    fun test2(netType: NetType) {
        Log.e(Constants.LOG_TAG, "执行方法test2")
    }

    override fun onDestroy() {
        super.onDestroy()
        NetworkManager.getInstance().unRegistObserver(this)
    }


}