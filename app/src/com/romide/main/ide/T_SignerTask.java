package com.romide.main.ide;

import android.app.*;
import android.content.*;
import android.os.*;
import android.util.*;
import com.romide.main.ide.utils.*;
import java.io.*;
import java.util.*;
import kellinwood.security.zipsigner.*;
import java.security.*;
import me.drakeet.materialdialog.*;

public class T_SignerTask extends AsyncTask<String,ProgressDialog,Double> implements ProgressListener
{

	@Override
	public void onProgress(ProgressEvent p1)
	{
		// TODO: Implement this method
		this.dialog.setProgress(p1.getPercentDone());
	}


	private ProgressDialog dialog;
	private MaterialDialog d2;
	private ZipSigner zs;
	
	
	private String title = "";
	private String msg = "";
	
	public T_SignerTask(String title,ProgressDialog dialog, MaterialDialog d2,ZipSigner zs){
		this.zs = zs;
		this.d2 = d2;
		this.dialog = dialog;
		this.dialog.setTitle(title);
		this.dialog.setMax(100);
		this.dialog.setProgress(0);
		this.dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		this.dialog.show();
	}
	
	
	
	@Override
	protected Double doInBackground(String[] s)
	{
		// TODO: Implement this method
		this.title = "";
		this.msg = "";
		String in = s[0];
		String out = s[1];
		try
		{
			zs.addProgressListener(this);
			zs.signZip(in, out);
			this.title = "签名完成！";
			this.msg = "保存在："+out;
			publishProgress(dialog);
			this.dialog.dismiss();
			this.cancel(true);
		}
		catch (Exception e)
		{
			this.title = "签名失败！";
			this.msg = e.toString()+"\n"+e.getMessage();
		}
		return null;
	}

	@Override
	protected void onProgressUpdate(ProgressDialog[] values)
	{
		// TODO: Implement this method
		super.onProgressUpdate(values);
		values[0].setMessage("签名完成！");
		this.d2.setTitle(this.title);
		this.d2.setMessage(this.msg);
		this.d2.setPositiveButton("确定",null);
		this.d2.show();
	}

	
}


