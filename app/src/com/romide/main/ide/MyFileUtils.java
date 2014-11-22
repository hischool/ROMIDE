package com.romide.main.ide;

import android.content.*;
import java.io.*;
import org.apache.http.util.*;

public class MyFileUtils
{
	public static String getDataFilesDir(Context c){
		return c.getFilesDir().getAbsolutePath() + File.separator;
	}

	public static String getSdcardFilesDir(Context c){
		return c.getExternalFilesDir(null).getAbsolutePath() + File.separator;
	}


	//从assets 文件夹中获取文件并读取 数据 
	public static String getFromAssets(Context con,String fileName)
	{
		String result = ""; 
		try
		{
			InputStream in = con.getResources().getAssets().open(fileName); 
			//获取文件的字节数 
			int lenght = in.available();
			//创建byte数组 
			byte[] buffer = new byte[lenght];
			//将文件中的数据读到byte数组中 
			in.read(buffer); 
			result = EncodingUtils.getString(buffer, "UTF-8");
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return result;
	}

	//写出sdcard，(文本格式)
	public static void WriteFile(String file, String message) throws IOException
	{
		File f = new File(file);
		if (!f.exists())
		{
			f.createNewFile();
		}
		FileOutputStream fout = new FileOutputStream(file); 
		byte[] bytes = message.getBytes(); 
		fout.write(bytes); 
		fout.close();
	}

    //运行脚本
	public static String runScript(String script, String arg)
	{
		String out = "脚本无输出";
		try
		{
			Process p = Runtime.getRuntime().exec("su -c bash " + script + " " + arg);
			p.waitFor();
			InputStreamReader isr = new InputStreamReader(p.getInputStream());
			char[] buffer = new char[1024];
			int read = isr.read(buffer);
			StringBuffer sb = new StringBuffer();
			while (read > 0)
			{
				sb.append(buffer, 0, read);
				read = isr.read(buffer);
			}
			out = sb.toString();
		}
		catch (Exception e)
		{
			out = e.toString();
		}
		return out;
	}


	//写出sdcard，(二进制格式)
	public static void copyTo(Context context,String fromPath, String toFile)
	{ 
		try
		{  
		    if (!new File(toFile).exists())
			{ 
				new File(toFile).createNewFile();
			}
			InputStream fromFileIs = context.getResources().getAssets().open(fromPath);
			int length = fromFileIs.available(); 
			//获取文件的字节数 
			byte[] buffer = new byte[length]; 
			//创建byte数组 
			FileOutputStream fileOutputStream = new FileOutputStream(toFile); 
			//字节输入流 
			BufferedInputStream bufferedInputStream = new BufferedInputStream(fromFileIs); 
			BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream); 
			int len = bufferedInputStream.read(buffer); 
			while (len != -1)
			{ 
			    bufferedOutputStream.write(buffer, 0, len); 
				len = bufferedInputStream.read(buffer); 
			} 
			bufferedInputStream.close(); bufferedOutputStream.close(); fromFileIs.close(); fileOutputStream.close(); }
		catch (Exception e)
		{
			e.printStackTrace();
		}

	}


	public static String CopyFile(Context con, String assetsFile) {
		String out = getDataFilesDir(con) + assetsFile;
		copyTo(con, assetsFile, out);
		File f = new File(out);
		if(! f.exists()){
			return null;
		}
		return out;
	}

	public static String CopyCacheFile(Context con, String assetsFile){
		String out = getSdcardFilesDir(con) + assetsFile;
		copyTo(con, assetsFile, out);
		File f = new File(out);
		if(! f.exists()){
			return null;
		}
		return out;
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
