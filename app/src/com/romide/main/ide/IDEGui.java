package com.romide.main.ide;

import android.app.*;
import android.content.*;
import android.net.*;
import android.os.*;
import android.preference.*;
import android.util.*;
import android.view.*;
import android.view.View.*;
import com.ipaulpro.afilechooser.utils.*;
import com.romide.main.ide.utils.*;
//import de.keyboardsurfer.android.widget.crouton.*;
import java.io.*;
import me.drakeet.materialdialog.*;

import com.romide.main.R;
import com.romide.plugin.widget.crouton.*;


public class IDEGui extends PreferenceActivity implements Preference.OnPreferenceClickListener
{

	//内核工具
	private Preference mMTKUnpackBoot;
	private String mMTKUnpackBootKey = "mtk_unpack_boot";

	private Preference mGTUnpackBoot;
	private String mGTUnpackBootKey = "gt_unpack_boot";

	private Preference mSCUnpackBoot;
	private String mSCUnpackBootKey = "sc_unpack_boot";

	private Preference mRepackBoot;
	private String mRepackBootKey = "repack_boot";

	//移植工具
	private Preference mMTKPortBoot;
	private String mMTKPortBootKey = "mtk_port_boot";

	private Preference mGTPortBoot;
	private String mGTPortBootKey = "gt_port_boot";

	private Preference mSCPortBoot;
	private String mSCPortBootKey = "sc_port_boot";
	
	private Preference mPortRom;
	private String mPortRomKey = "port_rom";
	
	
	//apk zip
	private Preference mSign;
	private String mSignKey = "sign_file";
	
	private Preference mGetSign;
	private String mGetSignKey = "get_sign";
	
	private Preference mDelSign;
	private String mDelSignKey = "del_sign";
	
	private Preference mZipalign;
	private String mZipalignKey = "zipalign_app";
	
	private Preference mOdex;
	private String mOdexKey = "odex_app";
	
	
	
	
	//插件
	private Preference mApktool;
	private String mApktoolKey = "apktool";
	
	private Preference mKitchen;
	private String mKitchenKey = "kitchen";

	
	
	//其他工具
	private Preference mClearWorking;
	private String mClearWorkingKey = "clear_working";

	private Preference mBackupWorking;
	private String mBackupWorkingKey = "backup_working";

	private Preference mRestoreWorking;
	private String mRestoreWorkingKey = "restore_working";

	private Preference mClearLog;
	private String mClearLogKey = "clear_log";

	private Preference mClearBak;
	private String mClearBakKey = "clear_bak";

	//private Command com;

	public static final String sdcard = Environment.getExternalStorageDirectory().toString() + "/";
	private String script; //脚本
	private String ideDir; //脚本所在目录
	private String inDataBootOut; //boot输出目录
	//是否有后台任务
	private boolean hasPerm = false;


	private static final Style RED = Style.ALERT;
	private static final Style GREEN = Style.CONFIRM;
	private static final Style BLUE = Style.INFO;
	public void doToast(String text, Style s, OnClickListener l)
	{
		Crouton.makeText(IDEGui.this, text, s).setOnClickListener(l).show();
	}

	@Override
	protected void onCreate(Bundle icicle)
	{
		// TODO: Implement this method
		super.onCreate(icicle);
		addPreferencesFromResource(R.xml.gui_main);

		init();
	}

	private void init()
	{
		// TODO: Implement this method
		//内核工具
		mMTKUnpackBoot = findPreference(mMTKUnpackBootKey);
		mMTKUnpackBoot.setOnPreferenceClickListener(this);

		mGTUnpackBoot = findPreference(mGTUnpackBootKey);
		mGTUnpackBoot.setOnPreferenceClickListener(this);

		mRepackBoot = findPreference(mRepackBootKey);
		mRepackBoot.setOnPreferenceClickListener(this);

		mSCUnpackBoot = findPreference(mSCUnpackBootKey);
		mSCUnpackBoot.setOnPreferenceClickListener(this);

		//移植工具
		mMTKPortBoot = findPreference(mMTKPortBootKey);
		mMTKPortBoot.setOnPreferenceClickListener(this);

		mGTPortBoot = findPreference(mGTPortBootKey);
		mGTPortBoot.setOnPreferenceClickListener(this);

		mSCPortBoot = findPreference(mSCPortBootKey);
		mSCPortBoot.setOnPreferenceClickListener(this);

		mPortRom = findPreference(mPortRomKey);
		mPortRom.setOnPreferenceClickListener(this);
		
		//apk zip
		mSign = findPreference(mSignKey);
		mSign.setOnPreferenceClickListener(this);
		
		mGetSign = findPreference(mGetSignKey);
		mGetSign.setOnPreferenceClickListener(this);
		
		mDelSign = findPreference(mDelSignKey);
		mDelSign.setOnPreferenceClickListener(this);
		
		mZipalign = findPreference(mZipalignKey);
		mZipalign.setOnPreferenceClickListener(this);
		
		mOdex = findPreference(mOdexKey);
		mOdex.setOnPreferenceClickListener(this);
		
		
		//插件
		mApktool = findPreference(mApktoolKey);
		mApktool.setOnPreferenceClickListener(this);
		
		mKitchen = findPreference(mKitchenKey);
		mKitchen.setOnPreferenceClickListener(this);
		
		//其他工具
		mClearWorking = findPreference(mClearWorkingKey);
		mClearWorking.setOnPreferenceClickListener(this);

		mBackupWorking = findPreference(mBackupWorkingKey);
		mBackupWorking.setOnPreferenceClickListener(this);

		mRestoreWorking = findPreference(mRestoreWorkingKey);
		mRestoreWorking.setOnPreferenceClickListener(this);

		mClearLog = findPreference(mClearLogKey);
		mClearLog.setOnPreferenceClickListener(this);

		mClearBak = findPreference(mClearBakKey);
		mClearBak.setOnPreferenceClickListener(this);

		//com = new Command("/system/bin/bash", "/system/bin/busybox", true);
		ideDir = this.getDataFilesDir(IDEGui.this);
		script = ideDir + "romide";
		inDataBootOut = ideDir + "work/boot";
		
		Actionbar();
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
	
	@Override
	public boolean onPreferenceClick(Preference p1)
	{
		// TODO: Implement this method
		String key = p1.getKey();
		//内核工具
		//解包 mtk
		if (key.equals(mMTKUnpackBootKey))
		{
			showChooser(Const.MTK_UNPACK);
		}
		//解包 高通
		else if (key.equals(mGTUnpackBootKey))
		{
			showChooser(Const.GT_UNPACK);
		}
		//解包 展训
		else if (key.equals(mSCUnpackBootKey))
		{
			showChooser(Const.SC_UNPACK);
		}
		//打包
		else if (key.equals(mRepackBootKey))
		{
			final String[] items = Utils.getItem(Const.BOOT_OUT);
			//final String[] items = new File(inDataBootOut).list();
			new AlertDialog.Builder(IDEGui.this)
			    .setTitle("选择一个项目打包")
				.setSingleChoiceItems(items, 0, new DialogInterface.OnClickListener(){
					@Override
					public void onClick(DialogInterface p1, int p2)
					{
						// TODO: Implement this method
						final String path = items[p2];
						final MaterialDialog d = new MaterialDialog(IDEGui.this);
						    d.setTitle("提示");
							d.setMessage("确定打包:\n" + path);
							d.setNegativeButton("取消", null);
							d.setPositiveButton("确定", new View.OnClickListener(){
								@Override
								public void onClick(View e)
								{
									// TODO: Implement this method
									doRepack(Const.BOOT_OUT + "/" + path);
									d.dismiss();
								}
							});
							d.show();
						p1.dismiss();
					}
				})
				.setNegativeButton("取消", null)
				.show();
		}
		
		//apk zip
		if(key.equals(mSignKey)){
			startActivity(new Intent(this,T_Signer.class));
		}
		//提取签名
		else if(key.equals(mGetSignKey)){
			showChooser(Const.GET_SIGN);
		}
		//删除签名
		else if(key.equals(mDelSignKey)){
			showChooser(Const.DEL_SIGN);
		}
		//zipalign
		else if(key.equals(mZipalignKey)){
			showChooser(Const.ZIPALIGN_APP);
		}
		//odex
		else if(key.equals(mOdexKey)){
			showChooser(Const.ODEX_APP);
		}
		
		//插件
		if(key.equals(mApktoolKey)){
			startActivity(new Intent(this,IDEApktool.class));
		}
		else if(key.equals(mKitchenKey)){
			doOpenKitchen();
		}

		//移植工具
		//mtk boot
		if (key.equals(mMTKPortBootKey))
		{
			//showChooser(MTK_PORT_BOOT_base);
			//doPortBoot(CPU_MTK);
			Intent it = new Intent(this,T_PortBoot.class);
			it.putExtra("cpu",Const.CPU_MTK);
			startActivity(it);
		}
		//高通 boot
		else if (key.equals(mGTPortBootKey))
		{
			//showChooser(GT_PORT_BOOT_base);
			//doPortBoot(CPU_GT);
			Intent it = new Intent(this,T_PortBoot.class);
			it.putExtra("cpu",Const.CPU_GT);
			startActivity(it);
		}
		//展训 boot
		else if (key.equals(mSCPortBootKey))
		{
			//showChooser(SC_PORT_BOOT_base);
			//doPortBoot(CPU_SC);
			Intent it = new Intent(this,T_PortBoot.class);
			it.putExtra("cpu",Const.CPU_SC);
			startActivity(it);
		}
		//移植ROM
		else if(key.equals(mPortRomKey)){
			Intent it = new Intent(this,T_PortRom.class);
			it.putExtra("cpu",Const.CPU_MTK);
			startActivity(it);
		}

		//其他工具
		//清理工作目录
		if (key.equals(mClearWorkingKey))
		{
			runIDE("正在清理工作目录 ", "clear_working");
		}
		//备份工作目录
		else if (key.equals(mBackupWorkingKey))
		{
			runIDE("正在备份工作目录", "backup_working");
		}
		//恢复工作目录
		else if (key.equals(mRestoreWorkingKey))
		{
			runIDE("正在恢复工作目录", "restore_working");
		} 
		//清理log
		else if (key.equals(mClearLogKey))
		{
			runIDE("正在清理 log ", "clear_log");
		}
		//清理bak
		else if (key.equals(mClearBakKey))
		{
			runIDE("正在清理 bak ", "clear_bak");
		}


		return false;
	}

	private void doOpenKitchen()
	{
		// TODO: Implement this method
		//runIDE("正在启动...","open_kitchen");
		String cmd = this.getFilesDir().getAbsolutePath()+ File.separator + "romide" + " open_kitchen";
		Intent intent = new Intent(Intent.ACTION_MAIN); 
		intent.setComponent(new ComponentName("com.romide.terminal", "com.romide.terminal.RemoteInterface")); 
		intent.setAction("jackpal.androidterm.RUN_SCRIPT"); 
		intent.putExtra("jackpal.androidterm.iInitialCommand", cmd);

		try{
	    	startActivity(intent);
			//Toast.makeText(MainActivity.this, "请输入\"sh " + opendFile + "\"并回车", Toast.LENGTH_LONG).show();
		} catch (Exception e){
			new AlertDialog.Builder(IDEGui.this)
			    .setTitle("打开失败")
				.setMessage("很抱歉无法打开厨房工具，可能是因为您没有安装终端模拟器\n\n错误信息：\n"+e.toString())
			    .setNegativeButton("关闭",null)
				.setPositiveButton("安装终端模拟器", new DialogInterface.OnClickListener(){
					@Override
					public void onClick(DialogInterface p1, int p2)
					{
						// TODO: Implement this method
						String apk = Environment.getExternalStorageDirectory().getAbsolutePath()+"/ROM-IDE/tools/config/term.apk";
						//打开apk安装器
						Intent intent = new Intent();
						intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						intent.setAction(Intent.ACTION_VIEW);
						intent.setDataAndType(Uri.fromFile(new File(apk)), "application/vnd.android.package-archive");
						startActivity(intent);
					}
				})
				.show();
		}
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
		 
		
		//属于解包
		if (requestCode == Const.MTK_UNPACK || requestCode == Const.SC_UNPACK || requestCode == Const.GT_UNPACK)
		{
			if (path == null)
			{
				return;
			}
			final String filePath = path;
			final MaterialDialog d = new MaterialDialog(this);
				d.setTitle("提示");
				d.setMessage("确定解包:\n" + path);
				d.setNegativeButton("取消", null);
				d.setPositiveButton("确定", new View.OnClickListener(){
					@Override
					public void onClick(View c)
					{
						String cpu = "";
						switch (requestCode)
						{
							case Const.MTK_UNPACK:
								cpu = Const.CPU_MTK;
								break;
							case Const.GT_UNPACK:
								cpu = Const.CPU_GT;
								break;
							case Const.SC_UNPACK:
								cpu = Const.CPU_SC;
								break;
						}
						doUnpack(cpu, filePath);

						d.dismiss();
						return;
					}
				});
				d.show();
		}
		
		if(requestCode == Const.GET_SIGN){
			runIDE("正在提取签名","get_sign "+path);
		}
		
		if(requestCode == Const.DEL_SIGN){
			runIDE("正在删除签名","del_sign "+path);
		}
		
		if(requestCode == Const.ZIPALIGN_APP){
			runIDE("正在 zipalign","zipalign_app "+path);
		}
		
		if(requestCode == Const.ODEX_APP){
			runIDE("正在odex","odex_app "+path);
		}
		
		super.onActivityResult(requestCode, resultCode, data);
	}


	//解包
	boolean ok = false;
	private void doUnpack(String cpu, String path)
	{
		// TODO: Implement this method
		if (cpu == null || cpu.equals(""))
		{
			doToast("CPU类型未定义！无法解包", RED, null);
			return;
		}
		runIDE("正在解包:" + path, "unpack_boot " + cpu + " " + path); 
	}

	//打包，无论cpu，都是这样
	private void doRepack(String path)
	{
		// TODO: Implement this method
		runIDE("正在打包:" + path, "repack_boot " + path); 
	}
	
	
	public boolean runIDE(String title,String command){
		String script = this.getFilesDir().getAbsolutePath()+File.separator+"romide";
		return Utils.runIDECommand(this,new AlertDialog.Builder(this),new ProgressDialog(this),script,title,command);
	}
	

	public String getDataFilesDir(Context c)
	{
		return c.getFilesDir().getAbsolutePath() + File.separator;
	}

	public String getSdcardFilesDir(Context c)
	{
		return c.getExternalFilesDir(null).getAbsolutePath() + File.separator;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		// TODO: Implement this method
		if (hasPerm)
		{
			doInfoExit();
		}
		else
		{
			IDEGui.this.finish();
		}
		return super.onKeyDown(keyCode, event);
	}

	private void doInfoExit()
	{
		// TODO: Implement this method
		final MaterialDialog d = new MaterialDialog(this);
		    d.setTitle("警告");
		    d.setMessage("当前有后台任务在运行，退出将同时结束后台的所有任务，是否继续退出？");
			d.setPositiveButton("继续退出", new View.OnClickListener(){
				@Override
				public void onClick(View v)
				{
					// TODO: Implement this method
					IDEGui.this.finish();
				}
			});
			d.setNegativeButton("取消", null);
			d.show();
	}


	
}
