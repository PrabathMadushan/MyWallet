package com.prabath.mywallet;

import android.app.Application;
import android.content.IntentFilter;
import android.net.ConnectivityManager;

import com.prabath.mywallet.BroadcastReceivers.ConnectivityReceiver;

public class MyWallet extends Application {

    ConnectivityReceiver connectivityReceiver;

    @Override
    public void onCreate() {
        super.onCreate();
        connectivityReceiver = new ConnectivityReceiver();
        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(connectivityReceiver, intentFilter);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        unregisterReceiver(connectivityReceiver);
    }


}
