package com.romide.main.ide;

import android.util.Log;

public class IDEDebug {
	public static final boolean debug = true;
	public static final boolean verify_true = true;
	
	public static final String TAG = "com.romide.main";
	
	public static void log(String msg){
		Log.e(TAG, msg);
	}
}
