package com.romide.main.workspace;
import com.romide.main.ide.*;
import java.io.*;
import java.util.zip.*;
import java.util.*;
import android.util.*;


public class ROM extends Object
{
	public static final String HOME = IDEGui.sdcard + "ROM-Workspace";

	//名称
	private String name;
	//版本
	private String version;
	//作者
	private String another;
	//工作目录
	private String workspace;
	//rom文件目录
	private String romdir;
	//配置文件
	private String profile;

	public ROM()
	{

	}

	public void setProfile(String profile)
	{
		this.profile = profile;
	}

	public String getProfile()
	{
		return profile;
	}


	public void setRomdir(String romdir)
	{
		this.romdir = romdir;
	}

	public String getRomdir()
	{
		return romdir;
	}

	public void setVersion(String version)
	{
		this.version = version;
	}

	public void setVersion(int version)
	{
		this.version = String.valueOf(version);
	}

	public String getVersion()
	{
		return version;
	}

	public void setWorkspace(String workspace)
	{
		this.workspace = workspace;
	}

	public String getWorkspace()
	{
		return workspace;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getName()
	{
		return name;
	}

	public void setAnother(String another)
	{
		this.another = another;
	}

	public String getAnother()
	{
		return another;
	}






	public boolean isEmpty(String s)
	{
		if (s == null || s.equals("")) return true;

		return false;
	}


	public void createProject()
	{
		this.createProject(this.getName(), this.getAnother(), this.getVersion());
	}


	public static final String KEY_NAME = "KEY_1";
	public static final String KEY_ANOTHER = "KEY_2";
	public static final String KEY_VERSION = "KEY_3";
	public static final String KEY_PROFILE = "KEY_4";
	public static final String KEY_WORKSPACE = "KEY_5";
	public static final String KEY_ROMDIR = "KEY_6";

	public void createProject(String name, String another, String version)
	{
		this.init(name, another, version);
		File myHome = new File(HOME, this.getName());
		File romHome = new File(myHome, new File(this.getRomdir()).getName());
		File scriptDir = new File(romHome, "META-INF/com/google/android");
		if (!myHome.exists()) myHome.mkdirs();
		if (!romHome.exists()) romHome.mkdirs();
		if (!scriptDir.exists()) scriptDir.mkdirs();

		this.fix();
		//生成配置信息
		this.saveToProfile();
	}


	public void initFromProfile()
	{
		this.initFromProfile(this.getProfile());
	}
	public void initFromProfile(String profile)
	{
		Properties prop = new Properties();
		File file = new File(profile);
		try
		{
			prop.load(new FileInputStream(file));
			this.setName(prop.getProperty(KEY_NAME));
			this.setAnother(prop.getProperty(KEY_ANOTHER));
			this.setVersion(prop.getProperty(KEY_VERSION));
			this.setProfile(prop.getProperty(KEY_PROFILE));
			this.setWorkspace(prop.getProperty(KEY_WORKSPACE));
			this.setRomdir(prop.getProperty(KEY_ROMDIR));
		}
		catch (Exception e)
		{}
		this.fix();
	}

	public void saveToProfile()
	{
		this.saveToProfile(this.getProfile());
	}

	public void saveToProfile(String profile)
	{
		if (!new File(profile).exists())
		{
			return;
		}
		Properties prop = new Properties();
		File file = new File(profile);
		try
		{
			prop.load(new FileInputStream(file));
			prop.setProperty(KEY_NAME, this.getName());
			prop.setProperty(KEY_ANOTHER, this.getAnother());
			prop.setProperty(KEY_VERSION, this.getVersion());
			prop.setProperty(KEY_PROFILE, this.getProfile());
			prop.setProperty(KEY_WORKSPACE, this.getWorkspace());
			prop.setProperty(KEY_ROMDIR, this.getRomdir());
			prop.store(new FileOutputStream(file), "Created Time:" + String.valueOf(System.currentTimeMillis()));
		}
		catch (Exception e)
		{
			Log.e("ROM", e.toString());
		}
	}

	public void init(String name, String another, String version)
	{
		this.name = name;
		this.version = version;
		this.another = another;
		this.fix();
	}


	public void fix()
	{
		if (isEmpty(this.getName()))
		{
			this.setName("MyROM");
		}
		if (isEmpty(this.getVersion()))
		{
			this.setVersion(1);
		}
		if (isEmpty(this.getAnother()))
		{
			this.setAnother("ROM-IDE");
		}
		if (isEmpty(this.getWorkspace()))
		{
			File file = new File(HOME + File.separator + this.getName());
			if (!file.exists()) file.mkdirs();
			this.setWorkspace(file.getAbsolutePath());
		}
		if (isEmpty(this.getProfile()))
		{
			this.setProfile(this.getWorkspace() + "/.profile");
			File file = new File(this.getProfile());
			if (!file.exists())
			{
				try
				{
					file.createNewFile();
				}
				catch (Exception e)
				{}
			}
		}
		if (isEmpty(this.getRomdir()))
		{
			File file = new File(this.getWorkspace() + "/ROM");
			if (!file.exists()) file.mkdirs();
			this.setRomdir(file.getAbsolutePath());
		}
	}


	public void createZip() throws Exception
	{
		this.fix();
		this.createZip(this.getWorkspace() + "/" + this.getName() + "-" + this.getVersion() + ".zip", null);
	}

	public void createZip(String output, OnZipAddListener listener) throws Exception
	{
		this.fix();
		ZipCompressing z = new ZipCompressing();
		if (listener != null)
		    listener.onStart();
		z.zip(output, new File(this.getRomdir()), listener);
		if (listener != null)
		    listener.onFinish();
	}




}



interface OnZipAddListener
{
	void onStart();
	void onAdd(String name);
	void onFinish();
}




class ZipCompressing
{
	private int k = 1; // 定义递归次数变量

	public ZipCompressing()
	{
		// TODO Auto-generated constructor stub
	}


	public void zip(String zipFileName, File inputFile, OnZipAddListener listener) throws Exception
	{
		System.out.println("压缩中...");
		ZipOutputStream out = new ZipOutputStream(new FileOutputStream(
													  zipFileName));
		BufferedOutputStream bo = new BufferedOutputStream(out);
		zip(out, inputFile, inputFile.getName(), bo, listener);
		bo.close();
		out.close(); // 输出流关闭
		System.out.println("压缩完成");
	}

	public void zip(ZipOutputStream out, File f, String base,
					BufferedOutputStream bo, OnZipAddListener listener) throws Exception
	{ // 方法重载
		if (f.isDirectory())
		{
			File[] fl = f.listFiles();
			if (fl.length == 0)
			{
				out.putNextEntry(new ZipEntry(base + "/")); // 创建zip压缩进入点base
				System.out.println(base + "/");
			}
			for (int i = 0; i < fl.length; i++)
			{
				zip(out, fl[i], base + "/" + fl[i].getName(), bo, listener); // 递归遍历子文件夹
			}
			System.out.println("第" + k + "次递归");
			k++;
		}
		else
		{
			if (listener != null)
			    listener.onAdd(base);
			out.putNextEntry(new ZipEntry(base)); // 创建zip压缩进入点base
			System.out.println(base);
			FileInputStream in = new FileInputStream(f);
			BufferedInputStream bi = new BufferedInputStream(in);
			int b;
			while ((b = bi.read()) != -1)
			{
				bo.write(b); // 将字节流写入当前zip目录
			}
			bi.close();
			in.close(); // 输入流关闭
		}
	}
}


