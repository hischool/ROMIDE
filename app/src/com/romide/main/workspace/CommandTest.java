package com.romide.main.workspace;
import java.io.*;

public class CommandTest
{
	
	public String exec(String[] commands){
		//创建命令行
		StringBuffer com = new StringBuffer();
		for(String s : commands){
			com.append(s);
			com.append("\n");
		}
		
		//创建流
		BufferedReader stdout = null;
		BufferedReader stderr = null;
		DataOutputStream os = null;
		Process process = null;
		StringBuffer outputText = new StringBuffer();
		
		try{
			//创建shell
			process = Runtime.getRuntime().exec("su"); 
			
			//获取流
			os = new DataOutputStream(process.getOutputStream());
			os.write(com.toString().getBytes());
			stdout = new BufferedReader(new InputStreamReader(process.getInputStream()));
			stderr = new BufferedReader(new InputStreamReader(process.getErrorStream()));
			
			//获取输出
			String line = null;
			while((line = stdout.readLine()) != null){
				outputText.append(line);
				outputText.append("\n");
				line = null;
			}
			
			
			while((line = stderr.readLine()) != null){
				outputText.append(line);
				outputText.append("\n");
				line = null;
			}
			
			process.waitFor();
		}
		catch (Exception e){
			 
		}
		finally{
			try{
				if(os != null) os.close();
				if(stdout != null) stdout.close();
				if(stderr != null) stderr.close();
				if(process != null) process.destroy();
			}
			catch (Exception e){}
		}
		return outputText.toString();
	}
	
}
