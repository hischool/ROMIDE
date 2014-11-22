package com.romide.main.ide;

import android.app.*;
import android.content.*;
import android.os.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;
import android.widget.AdapterView.*;
import com.romide.main.*;
import com.romide.main.ide.utils.*;
import java.io.*;
import java.util.*;
import me.drakeet.materialdialog.*;

import android.view.View.OnClickListener;
import com.romide.main.R;

public class IDEPlugin extends BaseActivity implements OnItemSelectedListener,OnClickListener
{


	//private ListView plugin;
	private Spinner sp;
	private Spinner spinsd;
	private Button install;
	private Button uninstall;
	private ArrayAdapter<String> adapter;
	private ArrayAdapter<String> adapterinsd;
	private List<String> full_list;
	private List<String> list;
	private List<String> listinsd;

	private int pos = 0;
	private int posinsd = 0;

	private String insdList = IDEGui.sdcard + "ROM-IDE/tools/config/pluginmgr/plglist.lst"; 


	private String dir = IDEGui.sdcard + "ROM-IDE";
	private String dir2 = IDEGui.sdcard + "ROM-IDE-Developer";

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		// TODO: Implement this method
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ide_plugin);

		Actionbar();
		
		//plugin = (ListView) findViewById(R.id.plugin_list);
		sp = (Spinner) findViewById(R.id.plugin_spinner);
		spinsd = (Spinner) findViewById(R.id.plugin_spinner_insd);
		install = (Button) findViewById(R.id.btn_plugin_install);
		install.setOnClickListener(this);
		uninstall = (Button) findViewById(R.id.btn_plugin_uninstall);
		uninstall.setOnClickListener(this);

		list =      new ArrayList<String>();
		listinsd =  new ArrayList<String>();
		full_list = new ArrayList<String>();

		try
		{
			//获取目录下的插件包
			File f = new File(dir);
			for (File file : f.listFiles())
			{
				if (file.isFile())
				{
					if (Utils.getFileLast(file).equals("rip"))
					{
						full_list.add(file.getAbsolutePath());
						list.add(file.getName());
					}
				}
			}

			File f2 = new File(dir2);
			for (File file : f2.listFiles())
			{
				if (file.isFile())
				{
					if (Utils.getFileLast(file).equals("rip"))
					{
						full_list.add(file.getAbsolutePath());
						list.add(file.getName());
					}
				}
			}

		}
		catch (Exception e)
		{

		}

		//获取已经安装插件列表
		try
		{
			BufferedReader reader = new BufferedReader(new FileReader(insdList)); 
			String line;
			while ((line = reader.readLine()) != null)
			{
				if (line.equals("")) continue;
				listinsd.add(line);
			} 
			reader.close();
		}
		catch (Exception e)
		{
			Toast.makeText(this, "获取已安装插件列表失败！\n" + e.getMessage(), 0).show();
		}

		adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		adapterinsd = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listinsd);
		adapterinsd.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		sp.setAdapter(adapter);
		sp.setOnItemSelectedListener(this);

		spinsd.setAdapter(adapterinsd);
		spinsd.setOnItemSelectedListener(new OnItemSelectedListener(){

				@Override
				public void onItemSelected(AdapterView<?> p1, View p2, int p3, long p4)
				{
					// TODO: Implement this method
					posinsd = p3;
				}

				@Override
				public void onNothingSelected(AdapterView<?> p1)
				{
					// TODO: Implement this method
				}
			});
	}



	@Override
	public void onItemSelected(AdapterView<?> p1, View p2, int p3, long p4)
	{
		// TODO: Implement this method
		pos = p3;
	}



	@Override
	public void onNothingSelected(AdapterView<?> p1)
	{
		// TODO: Implement this method
	}


	@Override
	public void onClick(View p1)
	{
		// TODO: Implement this method
		if (p1 == install)
		{
			try
			{
				final String file = list.get(pos).toString();
				final String full = full_list.get(pos).toString();
				final MaterialDialog d = new MaterialDialog(IDEPlugin.this);
					d.setTitle("提示");
					d.setMessage("确定安装:" + file + "\n\n" + "完整路径:" + full);
					d.setNegativeButton("取消", null);
					d.setPositiveButton("安装！", new View.OnClickListener(){

						@Override
						public void onClick(View v)
						{
							// TODO: Implement this method
							doInstallPlugin(full);
							d.dismiss();
						}
					});
					d.show();
			}
			catch (Exception e)
			{

			}
		}
		else if (p1 == uninstall)
		{
			try
			{
				final String file = listinsd.get(posinsd).toString();
				if (file == null || file.equals("")) return;
				final MaterialDialog d = new MaterialDialog(IDEPlugin.this);
					d.setTitle("提示");
					d.setMessage("确定卸载:" + file);
					d.setNegativeButton("取消", null);
					d.setPositiveButton("卸载！", new View.OnClickListener(){

						@Override
						public void onClick(View V)
						{
							// TODO: Implement this method
							doUninstallPlugin(file);
							d.dismiss();
						}

					});
					d.show();
			}
			catch (Exception e)
			{

			}
		}
	}


	public void doInstallPlugin(String file)
	{
		runIDE("正在安装...", "plugin --no-ask --no-color --install " + file);
		//ProgressDialog dialog = new ProgressDialog(this);
		//com.exec(script + " " + "plugin --no-ask --no-color --install " + file,dialog,"正在安装...");
	}


	private void doUninstallPlugin(String name)
	{
		// TODO: Implement this method
		listinsd.remove(posinsd);
		posinsd = 0;
		adapterinsd.notifyDataSetChanged();
		runIDE("正在卸载...", "plugin --no-ask --no-color --remove " + name);
	}


	public boolean runIDE(String title,String command){
		String script = this.getFilesDir().getAbsolutePath()+File.separator+"romide";
		return Utils.runIDECommand(this,new AlertDialog.Builder(this),new ProgressDialog(this),script,title,command);
	}



	private void Actionbar()
	{
		ActionBarCompat bar = ActivityCompat.getActionBar(this);
		if (bar != null)
		{
			bar.setDisplayOptions(ActionBarCompat.DISPLAY_HOME_AS_UP,ActionBarCompat.DISPLAY_HOME_AS_UP);
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		// TODO: Implement this method
		int id = item.getItemId();
		switch(id){
			case ActionBarCompat.ID_HOME:
				finish();
				return true;
		}
		return super.onOptionsItemSelected(item);
	}



	

}
