package com.romide.main.ide;

import android.app.*;
import android.content.*;
import android.net.*;
import android.os.*;
import android.preference.*;
import android.view.*;
import android.view.View.*;
import com.romide.main.*;
import com.romide.main.ide.utils.*;
//import de.keyboardsurfer.android.widget.crouton.*;
import java.io.*;
import com.romide.plugin.widget.crouton.*;


public class IDEMore extends PreferenceActivity implements Preference.OnPreferenceClickListener
{

	private Preference mBuildProp;
	private String mBuildPropKey = "build_prop";
	
	private Preference mTerminal;
	private String mTerminalKey = "terminal";

	private Preference mEdit;
	private String mEditKey = "edit";

	private Preference mApkEditor;
	private String mApkEditorKey = "apk_editor";
	
	private Preference mPortMethodManager;
	private String mPortMethodManagerKey = "port_method_manager";
	

	private Preference mFileManager;
	private String mFileManagerKey = "file_man";

	
	
	public Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg)
		{
            
        }
    };


	private static final Style RED = Style.ALERT;
	private static final Style GREEN = Style.CONFIRM;
	private static final Style BLUE = Style.INFO;
	public void doToast(String text, Style s, OnClickListener l)
	{
		Crouton.makeText(IDEMore.this, text, s).setOnClickListener(l).show();
	}

	@Override
	protected void onCreate(Bundle icicle)
	{
		// TODO: Implement this method
		super.onCreate(icicle);
		addPreferencesFromResource(R.xml.gui_more);

		init();
	}

	private void init()
	{
		// TODO: Implement this method
		mBuildProp = findPreference(mBuildPropKey);
		mBuildProp.setOnPreferenceClickListener(this);
		
		mTerminal = findPreference(mTerminalKey);
		mTerminal.setOnPreferenceClickListener(this);
		
		mEdit = findPreference(mEditKey);
		mEdit.setOnPreferenceClickListener(this);
		
		mApkEditor = findPreference(mApkEditorKey);
		mApkEditor.setOnPreferenceClickListener(this);
		
		mPortMethodManager = findPreference(mPortMethodManagerKey);
		mPortMethodManager.setOnPreferenceClickListener(this);
		
		mFileManager = findPreference(mFileManagerKey);
		mFileManager.setOnPreferenceClickListener(this);
		Actionbar();
	}


	@Override
	public boolean onPreferenceClick(Preference p1)
	{
		// TODO: Implement this method
		String key = p1.getKey();
		if(key.equals(mBuildPropKey)){
			Intent i = new Intent(IDEMore.this,T_BuildProp.class);
			startActivity(i);
		}
		else if(key.equals(mTerminalKey)){
			//runIDE("正在准备","cmd '"+this.getFilesDir().getAbsolutePath()+File.separator+"romide term'");
			doOpenTermenv();
		}
		else if(key.equals(mEditKey)){
			startActivity(new Intent(this,IDEEdit.class));
			//startActivity(new Intent(this,com.myopicmobile.textwarrior.android.TextWarriorApplication.class));
		}
		else if(key.equalsIgnoreCase(mApkEditorKey)){
			startActivity(new Intent(this,mao.bytecode.FileBrowser.class));
		}
		else if(key.equals(mPortMethodManagerKey)){
			startActivity(new Intent(this,IDEPortMethodManager.class));
		}
		else if(key.equals(mFileManagerKey)){
			startActivity(new Intent(this,com.romide.filemanager.FileExplorerMainActivity.class));
		}
		return false;
	}

	private void doOpenTermenv()
	{
		// TODO: Implement this method
		String cmd = this.getFilesDir().getAbsolutePath()+ File.separator + "romide" + " term";
		Intent intent = new Intent(Intent.ACTION_MAIN); 
		intent.setComponent(new ComponentName("jackpal.androidterm", "jackpal.androidterm.RemoteInterface")); 
		intent.setAction("jackpal.androidterm.RUN_SCRIPT"); 
		intent.putExtra("jackpal.androidterm.iInitialCommand", cmd);

		try{
	    	startActivity(intent);
			//Toast.makeText(MainActivity.this, "请输入\"sh " + opendFile + "\"并回车", Toast.LENGTH_LONG).show();
		} catch (Exception e){
			new AlertDialog.Builder(IDEMore.this)
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
	
	
	public boolean runIDE(String title,String command){
		String script = this.getFilesDir().getAbsolutePath()+File.separator+"romide";
		return Utils.runIDECommand(this,new AlertDialog.Builder(this),new ProgressDialog(this),script,title,command);
	}
	
}
