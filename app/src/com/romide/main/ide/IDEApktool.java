package com.romide.main.ide;

import android.app.*;
import android.content.*;
import android.net.*;
import android.os.*;
import android.preference.*;
import android.util.*;
import android.view.*;
import com.ipaulpro.afilechooser.utils.*;
import com.romide.main.*;
import com.romide.main.ide.utils.*;
import java.io.*;
import me.drakeet.materialdialog.*;

import com.romide.main.R;

public class IDEApktool extends PreferenceActivity implements Preference.OnPreferenceClickListener
{

	

	
	
	//Apktool
	private Preference mApktoolDApk;
	private String mApktoolDApkKey = "apktool_d_apk";

	private Preference mApktoolBApk;
	private String mApktoolBApkKey = "apktool_b_apk";

	
	private Preference mApktoolDDex;
	private String mApktoolDDexKey = "apktool_d_dex";
	
	
	private Preference mApktoolBDex;
	private String mApktoolBDexKey = "apktool_b_dex";
	
	
	private Preference mLockRom;
	private String mLockRomKey = "lock_rom";


	private Preference mUnlockRom;
	private String mUnlockRomKey = "unlock_rom";

	
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO: Implement this method
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.gui_apktool);
		
		init();
	}
	
	
	public void init(){
		Actionbar();
		//apktool
		mApktoolDApk = findPreference(mApktoolDApkKey);
		mApktoolDApk.setOnPreferenceClickListener(this);

		mApktoolBApk = findPreference(mApktoolBApkKey);
		mApktoolBApk.setOnPreferenceClickListener(this);

		mApktoolDDex = findPreference(mApktoolDDexKey);
		mApktoolDDex.setOnPreferenceClickListener(this);
		
		mApktoolBDex = findPreference(mApktoolBDexKey);
		mApktoolBDex.setOnPreferenceClickListener(this);
		
		mLockRom = findPreference(mLockRomKey);
		mLockRom.setOnPreferenceClickListener(this);
		
		mUnlockRom = findPreference(mUnlockRomKey);
		mUnlockRom.setOnPreferenceClickListener(this);
	}
	
	
	
	@Override
	public boolean onPreferenceClick(Preference p1)
	{
		String key = p1.getKey();
		// TODO: Implement this method
		if (key.equals(mApktoolDApkKey)){
			showChooser(Const.APKTOOL_D_APK);
		}
		//b
		else if (key.equals(mApktoolBApkKey)){
			showChooser(Const.APKTOOL_B_APK);
		}
		else if(key.equals(mApktoolDDexKey)){
			showChooser(Const.APKTOOL_D_DEX);
		}
		else if(key.equals(mApktoolBDexKey)){
			showChooser(Const.APKTOOL_B_DEX);
		}
		else if(key.equals(mLockRomKey)){
			showChooser(Const.LOCK_ROM);
		}
		else if(key.equals(mUnlockRomKey)){
			showChooser(Const.UNLOCK_ROM);
		}
		return false;
	}
	
	private void showChooser(int code)
	{
        // Use the GET_CONTENT intent from the utility class
        Intent target = FileUtils.createGetContentIntent();
        // Create the chooser Intent
        Intent intent = Intent.createChooser(
			target, getString(R.string.chooser_title));
        try
		{
            startActivityForResult(intent, code);
        }
		catch (ActivityNotFoundException e)
		{
            // The reason for the existence of aFileChooser
        }
    }




	@Override
	protected void onActivityResult(final int requestCode, int resultCode, Intent data)
	{
		// TODO: Implement this method
		// If the file selection was successful
		String path = null;
		if (resultCode != RESULT_OK)
		{
			return;
		}
		if (data == null)
		{
			return;
		}
		// Get the URI of the selected file
		final Uri uri = data.getData();
		try
		{
			// Get the file path from the URI
			path = FileUtils.getPath(this, uri);
		}
		catch (Exception e)
		{
			Log.e("IDEGui", "文件选择错误：", e);
		}
		if (path == null)
		{
			return;
		}

		/*
		 判断操作类型
		 */

		// 反编译 apk
		if(requestCode == Const.APKTOOL_D_APK){
			final String file = path;
			final MaterialDialog d = new MaterialDialog(this);
			    d.setTitle("反编译 Apk");
				d.setMessage("将要反编译Apk:\n"+file);
				d.setPositiveButton("确定", new View.OnClickListener(){

					@Override
					public void onClick(View v)
					{
						// TODO: Implement this method
						doApktoolD(file);
						d.dismiss();
					}
				});
				d.setNegativeButton("取消",null);
				d.show();

		}
		//回编译 apk
		else if(requestCode == Const.APKTOOL_B_APK){
			final String file = new File(path).getParent();
			final MaterialDialog d = new MaterialDialog(this);
			    d.setTitle("回编译 Apk");
				d.setMessage("将要回编译Apk:\n"+file);
				d.setPositiveButton("确定", new View.OnClickListener(){

					@Override
					public void onClick(View v)
					{
						// TODO: Implement this method
						doApktoolB(file);
						d.dismiss();
					}
				});
				d.setNegativeButton("取消",null);
				d.show();
		}
		//反编译 dex
		else if(requestCode == Const.APKTOOL_D_DEX){
			final String file = path;
			final MaterialDialog d = new MaterialDialog(this);
			    d.setTitle("反编译 Dex");
				d.setMessage("将要反编译Dex:\n"+file);
				d.setPositiveButton("确定", new View.OnClickListener(){

					@Override
					public void onClick(View v)
					{
						// TODO: Implement this method
						doApktoolDDex(file);
						d.dismiss();
					}
				});
				d.setNegativeButton("取消",null);
				d.show();

		}
		//回编译 dex
		else if(requestCode == Const.APKTOOL_B_DEX){
			final String file = new File(path).getParent();
			final MaterialDialog d = new MaterialDialog(this);
			    d.setTitle("回编译 Dex");
				d.setMessage("将要回编译Dex:\n"+file);
				d.setPositiveButton("确定", new View.OnClickListener(){

					@Override
					public void onClick(View c)
					{
						// TODO: Implement this method
						doApktoolBDex(file);
						d.dismiss();
					}
				});
				d.setNegativeButton("取消",null);
				d.show();
		}
		//加密zip
		else if(requestCode == Const.LOCK_ROM){
			final String file = path;
			final MaterialDialog d = new MaterialDialog(this);
			    d.setTitle("加密 Zip/Apk");
				d.setMessage("将要加密:\n"+file);
				d.setPositiveButton("确定", new View.OnClickListener(){

					@Override
					public void onClick(View v)
					{
						// TODO: Implement this method
						doLockRom(file);
						d.dismiss();
					}
				});
				d.setNegativeButton("取消",null);
				d.show();

		}
		//解密zip
		else if(requestCode == Const.UNLOCK_ROM){
			final String file = new File(path).getAbsolutePath();
			final MaterialDialog d = new MaterialDialog(this);
			    d.setTitle("解密 Zip/Apk");
				d.setMessage("将要解密:\n"+file);
				d.setPositiveButton("确定", new View.OnClickListener(){

					@Override
					public void onClick(View v)
					{
						// TODO: Implement this method
						doUnlockRom(file);
						d.dismiss();
					}
				});
				d.setNegativeButton("取消",null);
				d.show();
		}
	}
	
	
	private void doApktoolD(String file){
		runIDE("正在反编译 Apk:"+file, "decode_apk "+file);
	}

	private void doApktoolB(String file){
		runIDE("正在回编译 Apk:"+file, "build_apk "+file);
	}

	private void doApktoolDDex(String file){
		runIDE("正在反编译 Dex:"+file, "decode_dex "+file);
	}

	private void doApktoolBDex(String file){
		runIDE("正在回编译 Dex:"+file, "build_dex "+file);
	}
	
	private void doLockRom(String file){
		runIDE("正在加密:"+file, "lock_rom "+file);
	}

	private void doUnlockRom(String file){
		runIDE("正在解密:"+file, "unlock_rom "+file);
	}
	
	


	public boolean runIDE(String title,String command){
		String script = this.getFilesDir().getAbsolutePath()+File.separator+"romide";
		return Utils.runIDECommand(this,new AlertDialog.Builder(this),new ProgressDialog(this),script,title,command);
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
