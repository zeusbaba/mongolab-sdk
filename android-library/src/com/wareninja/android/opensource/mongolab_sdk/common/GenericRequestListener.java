/***
	Copyright (c) 2011-2012 WareNinja.com
	Author: yg@wareninja.com
*/

package com.wareninja.android.opensource.mongolab_sdk.common;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;


import android.os.Bundle;
import android.util.Log;


public abstract class GenericRequestListener {
	
	private static final String TAG = "GenericRequestListener";
	
	// added by YG
	public void onBegin() {
		if(AppContext.isDebugMode())Log.d(TAG, "onBegin()");
	}
	public void onComplete(String response) {
		if(AppContext.isDebugMode())Log.d(TAG, "onComplete():" + response);
	}
	public void onComplete_wBundle(Bundle params) {
		if(AppContext.isDebugMode())Log.d(TAG, "onComplete_wBundle():" + params);
	}
	
    public void onError(String e) {
    	if(AppContext.isDebugMode())Log.d(TAG, "onError():" + e);
    }
    public void onError_wMeta(ResponseMeta responseMeta) {
    	if(AppContext.isDebugMode()) Log.d(TAG, "onError_wMeta():" + responseMeta);
    }
    
    public void onFileNotFoundException(FileNotFoundException e) {
        Log.e(TAG, e.getMessage());
        e.printStackTrace();
    }

    public void onIOException(IOException e) {
        Log.e(TAG, e.getMessage());
        e.printStackTrace();
    }

    public void onMalformedURLException(MalformedURLException e) {
        Log.e(TAG, e.getMessage());
        e.printStackTrace();
    }
    
    
    
}
