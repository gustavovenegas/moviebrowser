package com.gustavovenegas.moviebrowser;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by gustavovenegas on 08.12.16.
 * Manager for checking network status
 */

public class NetworkManager {
    Context context;

    public NetworkManager(Context context){
        this.context = context;
    }
    public boolean networkAvailable(){
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        } else {
            return false;
        }
    }
}
