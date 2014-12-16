package com.romide.main.ide;

import android.app.*;
import android.content.*;
import android.os.*;
import android.view.*;
import com.romide.main.R;
import com.romide.main.ide.utils.*;
import android.preference.*;

public class IDESplash extends Activity
{
	
	private SharedPreferences sp;
	private int ms = 0;
	boolean isFirst = false;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
        super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.ide_splash);
		
		sp = getSharedPreferences("splash",0);
		//判断是不是第一次运行，如果读取不到，则是第一次
		isFirst = sp.getBoolean("first",true);
		//isFirst = true;
		
		Settings set = new Settings(getResources(),PreferenceManager.getDefaultSharedPreferences(this));
		if (set.isUseSplash()) ms = 1000;
		
		//延迟1秒执行跳转
		new Handler().postDelayed(new Runnable() { 
        	@Override
			public void run() {
				//是第一次跳转到 ViewPager
				if(isFirst){
					sp.edit().putBoolean("first",false).commit();
					Intent mainIntent = new Intent(IDESplash.this, IDEHello.class); 
					mainIntent.putExtra("need_rule",true);
	        	    startActivity(mainIntent);
	        	    finish(); 
				}
				//不是第一次跳转到主界面
				else {
	            	Intent mainIntent = new Intent(IDESplash.this, IDEMain.class); 
	        	    startActivity(mainIntent);
	        	    finish(); 
				}
        	}
		}, ms);
    }
}
