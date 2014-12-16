package com.romide.main.ide;

import android.app.*;
import android.content.*;
import android.os.*;
import com.romide.main.*;

public class IDEService extends Service
{

	private final IBinder binder = new IDEBinder();
	public class IDEBinder extends Binder{
		IDEService getService(){
			return IDEService.this;
		}
	}
	
	@Override
	public IBinder onBind(Intent p1)
	{
		// TODO: Implement this method
		return binder;
	}

	@Override
	public void onCreate()
	{
		// TODO: Implement this method
		//super.onCreate();
		
		Intent intent = new Intent(this,IDEMain.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    	PendingIntent mPI = PendingIntent.getActivity(this,0,intent,0);
		
		
		Notification mNotification = new Notification(R.drawable.ic_ide,"欢迎使用 ROM IDE",System.currentTimeMillis());
		mNotification.flags = Notification.FLAG_ONGOING_EVENT;
		mNotification.flags = Notification.FLAG_NO_CLEAR;
		
    	
    	mNotification.setLatestEventInfo(this, "ROM-IDE","点击打开", mPI);
    	if(mNF == null){
    		mNF = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
    	}
    	mNF.notify(R.string.application_ide, mNotification);
	}

	@Override
	public void onStart(Intent intent, int startId)
	{
		// TODO: Implement this method
		//super.onStart(intent, startId);
	}

	
	@Override
	public void onDestroy()
	{
		// TODO: Implement this method
		//super.onDestroy();
		mNF.cancelAll();
	}
	
	
	


	
	
	NotificationManager mNF;
}
