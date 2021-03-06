package com.romide.main.ide;

import android.app.*;
import android.content.*;
import android.content.pm.*;
import com.romide.main.ide.utils.*;

import java.io.*;
import java.util.*;
import me.drakeet.materialdialog.*;
import com.romide.plugin.tasks.*;

public class Utils
{

	public static void main(String[] args){
		System.out.println(args.toString());
		System.err.println("ERROR!"); 
	}
	
	
	
	
	
	

	public static String[] getItem(String dir)
	{
		Command m = new Command("/system/bin/bash", "/system/bin/busybox", true);
		m.exec("ls " + dir);
		String out = m.getStdout();
		String[] item = out.split(" ");
		StringBuffer list = new StringBuffer();;
		for (int i=0;i < item.length;i++)
		{
			list.append(item[i]);
			list.append("\n");
		}
		return list.toString().split("\\n");
	}

	
	static {
		System.loadLibrary("romide");
	}
	
	public static int isTrulyApk(Context c){
		
		if (IDEDebug.verify_true) {
			return 0;
		}
		
		int code = -1;
		try
		{
			PackageInfo packageInfo = c.getPackageManager().getPackageInfo(c.getPackageName(), PackageManager.GET_SIGNATURES);
			Signature[] signs = packageInfo.signatures;
			Signature sign = signs[0];
			code = sign.hashCode();
		}
		catch (PackageManager.NameNotFoundException e)
		{}
		
		return isTrulyApkReal(code);
	}
	
	public static native int isTrulyApkReal(int code);

	public static void showCommandResult(Context con, Command c, String t)
	{
		MaterialDialog d = new MaterialDialog(con);
		d.setTitle(t);
		d.setMessage("标准输出：\n" + c.getStdout() + "\n更多输出：\n" + c.getStderr());
		d.setPositiveButton("关闭", null);
		d.show();
	}


	public static boolean toBoolean(String str)
	{
		return str.equals("true");
	}

	public static int getAndroidSdk()
	{
		return com.romide.main.ide.utils.AndroidCompat.SDK;
	}


	public static boolean runIDECommand(Context com, AlertDialog.Builder d, ProgressDialog dialog, String script, String title, String command)
	{
		IDECommandTask task = new IDECommandTask(dialog, title, "请稍等");
		task.setContext(com);
		task.setResultDialog(d);
		task.execute(new String[]{
						 script + " " + command
					 });
 		return true;
	}


	public static String getFileLast(File file)
	{
		return file.getName().substring(file.getName().lastIndexOf(".") + 1).toLowerCase();
	}

	public static String getPropByKey(Properties prop, String key)
	{
		if (prop == null) return "";
		String res = prop.getProperty(key,"");
		String s = "";
		try
		{
			s = new String(res.getBytes("ISO-8859-1"),"utf-8");
		}
		catch (UnsupportedEncodingException e)
		{
			return "";
		}
		return s;
	}

	
	
	public static void cp(String from, String to)
	{ 
		try
		{  
		    if (!new File(to).exists())
			{ 
				new File(to).createNewFile();
			}
			FileInputStream fromFileIs = new FileInputStream(from);
			int length = fromFileIs.available(); 
			//获取文件的字节数 
			byte[] buffer = new byte[length]; 
			//创建byte数组 
			FileOutputStream fileOutputStream = new FileOutputStream(to); 
			//字节输入流 
			BufferedInputStream bufferedInputStream = new BufferedInputStream(fromFileIs); 
			BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream); 
			int len = bufferedInputStream.read(buffer); 
			while (len != -1)
			{ 
			    bufferedOutputStream.write(buffer, 0, len); 
				len = bufferedInputStream.read(buffer); 
			} 
			bufferedInputStream.close(); 
			bufferedOutputStream.close(); fromFileIs.close(); fileOutputStream.close(); }
		catch (Exception e)
		{
			e.printStackTrace();
		}

	}
	
}
