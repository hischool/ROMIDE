package com.romide.main.ide;

import android.app.*;
import android.content.*;
import android.content.pm.*;
import android.widget.*;
import com.romide.main.ide.utils.*;

import java.io.*;
import java.util.*;
import me.drakeet.materialdialog.*;
import com.romide.plugin.tasks.*;

public class Utils
{

	public static void main()
	{
	}
	
	
	
	
	
	

	public static String[] getItem(String dir)
	{
		Command m = new Command("/system/bin/bash", "/system/bin/busybox", true);
		m.exec("ls " + dir);
		String out = m.getStdout();
		String[] item = out.split(" ");
		String list = "";
		for (int i=0;i < item.length;i++)
		{
			list += item[i] + "\n";
		}
		return list.split("\\n");
	}


	public static boolean isTrulyApk(Context c)
	{
		boolean is = false;
		final int key = -1923360755;
		try
		{
			PackageInfo packageInfo = c.getPackageManager().getPackageInfo(c.getPackageName(), PackageManager.GET_SIGNATURES);
			Signature[] signs = packageInfo.signatures;
			Signature sign = signs[0];
			int code = sign.hashCode();
			//EditText d = new EditText(c);
			//d.setText(code+"");
			//new AlertDialog.Builder(c).setView(d).show();
			if (code == key)
			{
				is = true;
			}
			else
			{
				is = false;
			}
		}
		catch (PackageManager.NameNotFoundException e)
		{
			is = false;
		} 
		return is;
	}

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
