package com.romide.main.ide.utils;

import android.content.*;
import android.content.res.*;
import com.romide.main.R;


public class Settings
{
	
	
	private SharedPreferences sp;
	
	//自动检查更新
	private boolean auto_check_update;
	private String key_auto_check_update = "auto_check_update";
	
	//启动动画
	private boolean use_splash;
	private String key_use_splash = "use_splash";
	
	
	
	public Settings(Resources res, SharedPreferences prefs) {
		this.sp = prefs;
        readDefaultPrefs(res);
        readPrefs(sp);
    }


	//获取默认
	private void readDefaultPrefs(Resources res)
	{
		// TODO: Implement this method
		auto_check_update = res.getBoolean(R.bool.default_auto_check_update);
		use_splash = res.getBoolean(R.bool.default_use_splash);
	}
	
	
	//获取当前
	private void readPrefs(SharedPreferences prefs)
	{
		// TODO: Implement this method
		auto_check_update = prefs.getBoolean(key_auto_check_update,auto_check_update);
		use_splash = prefs.getBoolean(key_use_splash,use_splash);
	}

	
	
	
	public boolean isAutoCheckUpdate()
	{
		return auto_check_update;
	}
	
	
	
	public boolean isUseSplash()
	{
		return use_splash;
	}
	
}
