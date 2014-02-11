package com.codepath.apps.twitterclientredux;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class NetworkService {

    public static boolean hasInternet(Context context) {
    	
        NetworkInfo info = (NetworkInfo) ((ConnectivityManager)
        context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        if (info == null) {
        	Log.d("DEBUG","No internet connection");
        	return false;
        }
        else {
        	if (info.isConnected()) {
        	    Log.d("DEBUG","Internet connection available...");
        	    return true;
        	}
        	else {
        	    Log.d("DEBUG","No internet connection");
        	    return true;
        	}

        }
    	// return false;
    }

}
