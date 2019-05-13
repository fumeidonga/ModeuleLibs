package com.rebeau.base.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Administrator on 2018/3/14.
 */

public class RBNetworkUtil {

    private RBNetworkUtil(){
        throw new UnsupportedOperationException("RBNetworkUtil can't instantiate me...");
    }

    public static boolean checkNetwork(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity == null) {
            return false;
        }
        @SuppressLint("MissingPermission")
        NetworkInfo info = connectivity.getActiveNetworkInfo();
        return info != null && info.isConnected();
    }
}
