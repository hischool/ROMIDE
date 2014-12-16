package com.romide.main.ide;

import android.app.*;
import android.os.*;
import android.util.*;
import com.romide.main.ide.utils.*;
import java.io.*;
import java.util.*;
import me.drakeet.materialdialog.*;
import android.view.*;



public class IDEUpdateTask extends AsyncTask<String,MaterialDialog,Double>
{

	private MaterialDialog dialog;
	private double d;
	
	private static final String UPDATE_FILE = "http://xtoos.an56.org/romide/update.op";
	private static String save;
	
	private static Activity act;
	
	//下载链接
	private static String newInUrl;
	//更新说明
	private static String info;
	//更新版本
	private static int version;
	
	//当前版本
	private int nowCode = 0;

	public IDEUpdateTask(MaterialDialog dialog){
		this(dialog, "检测到更新", "立即更新", new View.OnClickListener(){

				@Override
				public void onClick(View v)
				{
					// TODO: Implement this method
					doUpdate(newInUrl);
				}
			}, "放弃更新", null);
	}
	
	public IDEUpdateTask(MaterialDialog dialog,String title,String posButton,View.OnClickListener posListener,String negButton, View.OnClickListener negListener)
	{
		this.dialog = dialog;
		this.dialog.setTitle(title);
		this.dialog.setCanceledOnTouchOutside(false);
		this.dialog.setPositiveButton(posButton,posListener);
		this.dialog.setNegativeButton(negButton,negListener);
	}
	
	
	public void setVersionCode(int code){
		this.nowCode = code;
	}

	public void setActivity(Activity ac){
		IDEUpdateTask.act = ac;
	}
	
	
	public static void doUpdate(final String url){
		try
		{
			new Thread(){
				@Override
				public void run(){
					try
					{
						HttpUtil.downloadFile(save, url);
						File f = new File(save);
						if(!f.exists()) return;
						//Runtime.getRuntime().exec("am start -a android.intent.action.VIEW -t application/vnd.android.package-archive -d file://"+save);
						//Intent i = new Intent();
						//i.setAction("android.intent.action.VIEW");
						//i.setType("application/vnd.android.package-archive" );
						//i.setData(Uri.parse("file://"+save));
						//act.startActivity(i);
					}
					catch (Exception e)
					{
						Log.e("ROMIDE_UPDATE",e.getMessage());
					}
				}
			}.start();
		}
		catch (Exception e)
		{
			Log.e("UROMIDE_PDATE",e.getMessage());
		}
	}



	@Override
	public Double doInBackground(String[] s)
	{
		// TODO: Implement this method
		try{
			IDEUpdateTask.save = s[0];
			String file = IDEGui.sdcard + "romide.update";
			File f = new File(file);
			//下载文件
			if(f.exists()) f.delete();
			HttpUtil.downloadFile(file,UPDATE_FILE);
			if(!f.exists()) return -1d;
			//加载文件
			Properties prop = new Properties();
			FileInputStream is = new FileInputStream(f);
			prop.load(is);
			is.close();
			//获取信息
			IDEUpdateTask.info = prop.getProperty("info","未知");
			IDEUpdateTask.newInUrl = prop.getProperty("url","未知");
			IDEUpdateTask.version = Integer.parseInt(prop.getProperty("version","0"));
			
			//判断版本号
			if(nowCode < version){
				publishProgress(dialog);
			}
			
		}catch(Exception e){
			
		}
		return d;
	}


	@Override
	protected void onPostExecute(Double result)
	{
		// TODO: Implement this method
		super.onPostExecute(result);
		Log.d("UpdateTask", "onPostExecute");
	}

	@Override
	protected void onPreExecute()
	{
		// TODO: Implement this method
		super.onPreExecute();
		Log.d("UpdateTask", "onPreExecute");
	}

	@Override
	protected void onProgressUpdate(MaterialDialog[] values)
	{
		// TODO: Implement this method
		super.onProgressUpdate(values);
		values[0].setMessage("更新版本:"+version+"\n\n"+"更新说明:"+"\n"+info+"\n\n");
		values[0].show();
	}


}
