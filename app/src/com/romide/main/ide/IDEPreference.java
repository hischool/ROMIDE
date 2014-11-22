package com.romide.main.ide;

import android.app.*;
import android.content.*;
import android.net.*;
import android.os.*;
import android.preference.*;
import android.view.*;
import android.widget.*;
import com.romide.main.*;
import com.romide.main.ide.utils.*;


public class IDEPreference extends PreferenceActivity implements Preference.OnPreferenceClickListener
{

	@Override
	public boolean onPreferenceClick(Preference p1)
	{
		// TODO: Implement this method
		String key = p1.getKey();
		if(key.equals(mGotoAppHelloKey)){
			Intent i = new Intent(this,IDEHello.class);
			i.putExtra("need_rule",false);
			startActivity(i);
		}
		if(key.equals(mGotoRomideComKey)){
			openUrl("http://www.romide.com");
		}
		if(key.equals(mGotoRomerzjComKey)){
			openUrl("http://bbs.romerzj.com");
		}
		if(key.equals(mGotoRomFacKey)){
			Intent intent = new Intent();
			intent.setComponent(new ComponentName("com.rom","com.rom.factory.mainUI"));
			intent.setAction(Intent.ACTION_VIEW);
			try{
			    startActivity(intent);
			} catch (Exception e){
				new AlertDialog.Builder(this)
				   .setTitle("无法打开！")
				   .setMessage("无法打开 ROM-Factory！您可能未安装或者将其禁用！\n\n错误:\n"+e.toString())
				   .setPositiveButton("确定",null)
				   .show();
			}
		}
		return false;
	}

	private void openUrl(String url)
	{
		try
		{
			// TODO: Implement this method
			Intent it = new Intent(Intent.ACTION_VIEW, Uri.parse(url)); 
			it.setClassName("com.android.browser", "com.android.browser.BrowserActivity"); 
			startActivity(it);
		}
		catch (Exception e)
		{
			Toast.makeText(this, "未安装浏览器！无法打开", 0).show();
		}
	}

	
	private Preference mGotoAppHello;
	private String mGotoAppHelloKey = "goto_hello";

	private Preference mGotoRomideCom;
	private String mGotoRomideComKey = "goto_romide_com";
	
	private Preference mGotoRomerzjCom;
	private String mGotoRomerzjComKey = "goto_romerzj_com";

	
	private Preference mGotoRomFac;
	private String mGotoRomFacKey = "goto_rom_fac";
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO: Implement this method
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.ide_preference);
		
		Actionbar();
		mGotoAppHello = findPreference(mGotoAppHelloKey);
		mGotoAppHello.setOnPreferenceClickListener(this);
		
		mGotoRomFac = findPreference(mGotoRomFacKey);
		mGotoRomFac.setOnPreferenceClickListener(this);
		
		mGotoRomideCom = findPreference(mGotoRomideComKey);
		mGotoRomideCom.setOnPreferenceClickListener(this);
		
		mGotoRomerzjCom = findPreference(mGotoRomerzjComKey);
		mGotoRomerzjCom.setOnPreferenceClickListener(this);
	}
	
	
	private void Actionbar()
	{
		ActionBarCompat bar = ActivityCompat.getActionBar(this);
		if (bar != null)
		{
			bar.setDisplayOptions(ActionBarCompat.DISPLAY_HOME_AS_UP,ActionBarCompat.DISPLAY_HOME_AS_UP);
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		// TODO: Implement this method
		int id = item.getItemId();
		switch(id){
			case ActionBarCompat.ID_HOME:
				finish();
				return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
}
