package com.romide.main.workspace;
import com.romide.main.ide.*;
import java.io.*;
import java.util.*;
import android.util.*;
import net.lingala.zip4j.core.*;
import net.lingala.zip4j.model.*;
import net.lingala.zip4j.util.*;


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
	//临时目录
	private String tmpDir;

	public ROM()
	{

	}

	private void setTmpDir(String tmpDir)
	{
		this.tmpDir = tmpDir;
	}

	public String getTmpDir()
	{
		return tmpDir;
	}

	private void setProfile(String profile)
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
	
	public String getBuildPropPath(){
		return this.getTmpDir() + File.separator + "build.prop";
	}
	
	public static List<String> getAllProjects(){
		List<String> list = new ArrayList<String>();
		File file = new File(HOME);
		
		for(File f : file.listFiles()){
			if(f.isDirectory()){
				//第一个字符是 . 表示隐藏文件
				if(f.getName().indexOf(".") == 0){
					continue;
				}
				list.add(f.getName());
			}
		}
		
		return list;
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
	public static final String KEY_TMPDIR = "KEY_7";

	public void createProject(String name, String another, String version)
	{
		this.init(name, another, version);
		File myHome = new File(HOME, this.getName());
		File romHome = new File(myHome, new File(this.getRomdir()).getName());
		File scriptDir = new File(romHome, "META-INF/com/google/android");
		File tmpDir = new File(this.getTmpDir());
		if (!myHome.exists()) myHome.mkdirs();
		if (!romHome.exists()) romHome.mkdirs();
		if (!scriptDir.exists()) scriptDir.mkdirs();
		if (!tmpDir.exists()) tmpDir.mkdirs();

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
			this.setTmpDir(prop.getProperty(KEY_TMPDIR));
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
			prop.setProperty(KEY_TMPDIR,this.getTmpDir());
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
		if (isEmpty(this.getTmpDir()))
		{
			File file = new File(this.getWorkspace(),".temp");
			if (!file.exists()) file.mkdirs();
			this.setTmpDir(file.getAbsolutePath());
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
	
	public String getOutputZip(){
		return (this.getWorkspace() + "/" + this.getName() + "-" + this.getVersion() + ".zip");
	}


	public void createZip() throws Exception
	{
		this.createZip(this.getOutputZip());
	}

	public void createZip(String output) throws Exception
	{
		this.fix();
		ZipFile zipFile = new ZipFile(output);
		
		ZipParameters para = new ZipParameters();
		para.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL);
		para.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);
		
		for(File file : new File(this.getRomdir()).listFiles()){
			if(file.isFile())
				zipFile.addFile(file,para);
			else if(file.isDirectory())
				zipFile.addFolder(file,para);
		}
	}
	
	
//	private ArrayList<File> addToArchive(ArrayList<File> list,File root){
//		for(File file: root.listFiles()){
//			if(file.isDirectory())
//				list = addToArchive(list,new File(root,file.getName()));
//			else
//				list.add(file);
//		}
//		return list;
//	}




}

