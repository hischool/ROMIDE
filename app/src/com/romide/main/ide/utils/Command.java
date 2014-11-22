package com.romide.main.ide.utils;

import android.app.*;
import com.romide.main.ide.*;
import java.io.*;
import com.romide.plugin.tasks.*;

public class Command
{
	private String bashPath;
	private String busyboxPause;
	private boolean asRoot;

	private BufferedReader soin;
	private BufferedReader sein;

	private String stdout;
	private String stderr;
	private int exitValue;


	public Command()
	{
		this("/system/bin/bash", "/system/bin/busybox");
	}

	public Command(String bash)
	{
		this(bash, "/system/bin/busybox");
	}

	public Command(String bash, String busybox)
	{
		this(bash, busybox, true);
	}

	public Command(String bash, String busybox, boolean root)
	{
		this.bashPath = bash;
		this.busyboxPause = busybox;
		this.asRoot = root;
	}

	public static boolean runIt(String command)
	{
		return new Command().exec(command);
	}



	public String getOutput()
	{
//		String res = "";
//		try
//		{
//			if (soin != null)
//			{
//				res += soin.readLine();
//				res += "\n";
//			}
//			if(sein != null){
//				res += sein.readLine();
//			}
//		}
//		catch (Exception e)
//		{
//
//		}
		return this.getStdout() + "\n" + this.getStderr();
	}


	public void exec(String[] command, ProgressDialog dialog)
	{
		this.exec(command, dialog, "正在准备");
	}

	public void exec(String[] command, ProgressDialog dialog, String info)
	{
		IDECommandTask task = new IDECommandTask(dialog, info);
		task.execute(command);
	}

	public void exec(String command, ProgressDialog dialog)
	{
		this.exec(command, dialog, "正在准备");
	}

	public void exec(String command, ProgressDialog dialog, String info)
	{
		this.exec(new String[]{command}, dialog, info);
	}

	public void exec(String[] command)
	{
		for (int i=0;i < command.length;i++)
		{
			this.exec(command[i]);
		}
	}



	public String getNow()
	{
		String s = "";
		try
		{
			if (soin != null)
			{
				s += soin.readLine() + "\n";
			}
			if (sein !=null){
				s += sein.readLine() + "\n";
			}
		}
		catch (Exception e)
		{

		}
		return s;
	}

	public boolean exec(String command)
	{
	    Process p;
		DataOutputStream os = null;
		this.stdout = "";
		this.stderr = "";
	    try
		{
			String su = this.getBashPath();
			if (this.isAsRoot())
			{
				su = "su";
			}
			p = Runtime.getRuntime().exec(su);
			os = new DataOutputStream(p.getOutputStream());
			os.write(command.getBytes());
			os.flush();
			os.close();

			soin = new BufferedReader(new InputStreamReader(p.getInputStream()));
			sein = new BufferedReader(new InputStreamReader(p.getErrorStream()));

			try
			{
				p.waitFor();

				//获取stdout

				String line = null;  
				while ((line = soin.readLine()) != null)
				{  
					this.stdout += line + "\n";                 
				}

				//获取stderr

				String line2 = null;  
				while ((line2 = sein.readLine()) != null)
				{  
					this.stderr += line2 + "\n";                 
				}

				//p.waitFor();
				this.exitValue = p.exitValue();
				//this.stdout = so;
				//this.stderr = se;

				if (p.exitValue() != 255)
				{
					return true;
				}
				else
				{
					return false;
				}
			}
			catch (InterruptedException e)
			{
				return false;
			}

		}
		catch (IOException e)
		{
			return false;
		}
	}




	public String getStdout()
	{
		return stdout;
	}

	public String getStderr()
	{
		return stderr;
	}

	public void setBashPath(String bashPath)
	{
		this.bashPath = bashPath;
	}

	public String getBashPath()
	{
		return bashPath;
	}

	public void setBusyboxPause(String busyboxPause)
	{
		this.busyboxPause = busyboxPause;
	}

	public String getBusyboxPause()
	{
		return busyboxPause;
	}

	public void setAsRoot(boolean asRoot)
	{
		this.asRoot = asRoot;
	}

	public boolean isAsRoot()
	{
		return asRoot;
	}


	public int getExitValue()
	{
		return exitValue;
	}
}


