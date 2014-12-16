package com.romide.main.ide;

import android.os.*;
import android.util.*;
import com.romide.main.ide.utils.*;
import java.io.*;
import me.drakeet.materialdialog.*;
import android.view.*;

public class IDEDownloadTask extends AsyncTask<String,MaterialDialog,Double>
{
	private MaterialDialog dialog;
	private double d;

	private static String url;
	private static String save;

	public IDEDownloadTask(MaterialDialog dialog){
		this(dialog, "下载完毕", "打开",null, "取消", null);
	}

	public IDEDownloadTask(MaterialDialog dialog,String title,String posButton,View.OnClickListener posListener,String negButton, View.OnClickListener negListener)
	{
		this.dialog = dialog;
		this.dialog.setTitle(title);
		this.dialog.setCanceledOnTouchOutside(false);
		this.dialog.setPositiveButton(posButton,posListener);
		this.dialog.setNegativeButton(negButton,negListener);
	}


	@Override
	public Double doInBackground(String[] s)
	{
		// TODO: Implement this method
		try{
			IDEDownloadTask.url = s[1];
			IDEDownloadTask.save = s[0];
			File f = new File(save);
			//下载文件
			if(f.exists()) f.delete();
			HttpUtil.downloadFile(save,url);
			if(!f.exists()) return -1d;
			publishProgress(dialog);
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
		values[0].setMessage("保存在:\n"+save);
		values[0].show();
	}
}
