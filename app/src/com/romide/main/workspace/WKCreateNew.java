package com.romide.main.workspace;

import android.app.*;
import android.content.*;
import android.net.*;
import android.os.*;
import android.util.*;
import android.view.*;
import android.widget.*;
import com.ipaulpro.afilechooser.utils.*;
import com.romide.main.*;
import com.romide.main.ide.*;
import java.io.*;
import com.romide.plugin.tasks.*;

public class WKCreateNew extends Activity implements View.OnClickListener,CompoundButton.OnCheckedChangeListener
{

	@Override
	public void onCheckedChanged(CompoundButton p1, boolean p2)
	{
		// TODO: Implement this method
		if (p1 == use_boot)
		{
			if (use_boot.isChecked())
			{
				use_boot_layout.setVisibility(View.VISIBLE);
			}
			else
			{
				use_boot_layout.setVisibility(View.GONE);
			}
		}

		else if (p1 == dump_system)
		{
			if (dump_system.isChecked())
			{
				dump_system_layout.setVisibility(View.VISIBLE);
			}
			else
			{
				dump_system_layout.setVisibility(View.GONE);
			}
		}
	}


	@Override
	public void onClick(View p1)
	{
		// TODO: Implement this method
		if (p1 == use_boot_find)
		{
			showChooser(Const.CHOOSE_BASE);
		}
		else if (p1 == btn_finish)
		{
			rom.setName(edit_name.getText().toString());
			rom.setVersion(edit_version.getText().toString());
			rom.setAnother(edit_another.getText().toString());
			rom.createProject();
			
			String cmd = "sh " + WKMain.SCRIPTDIR.getAbsolutePath() + "/mkrom.init_rom "+rom.getRomdir() + " && ";
		
			//判断操作
			
			//清除数据
			if(format_data.isChecked()){
				cmd += "sh " + WKMain.SCRIPTDIR.getAbsolutePath() + "/mkrom.format_data " + rom.getRomdir() + " && ";
			}
			
			if(format_system.isChecked()){
				cmd += "sh " + WKMain.SCRIPTDIR.getAbsolutePath() + "/mkrom.format_system " + rom.getRomdir() + " && ";
			}
			
			//使用boot.img
			if(use_boot.isChecked()){
				String bootpath;
				bootpath = use_boot_edit_path.getText().toString();
				cmd += "sh " + WKMain.SCRIPTDIR.getAbsolutePath() + "/mkrom.use_boot "+rom.getRomdir()+ " " + bootpath + " && ";
			}
			
			//提取system
			if(dump_system.isChecked()){
				String compress = "tar";
				if(system_tar.isChecked()) compress = "tar";
				if(system_gz.isChecked())  compress = "gz";
				if(system_bz2.isChecked()) compress = "bz2";
				
				cmd += "sh " + WKMain.SCRIPTDIR.getAbsolutePath() + "/mkrom.dump_system "+rom.getRomdir()+ " " + compress + " && ";
			}
			
			
			cmd += "echo";
			
			IDECommandTask task = new IDECommandTask(new ProgressDialog(this),"正在应用配置","请稍等");
			task.setContext(this);
			task.setResultDialog(new AlertDialog.Builder(this));
			task.setNeedCopy(false);
 			task.execute(cmd);

			
			Intent i = new Intent();
			Uri uri = Uri.fromFile(new File(rom.getProfile()));
			i.setData(uri);
			this.setResult(RESULT_OK, i);
			//finish();
		}
	}


	// 基础信息
	private EditText edit_name, edit_another, edit_version;

	//cpu类型
	private RadioButton cpu_mtk, cpu_gt, cpu_sc;

	//分区类型
	//private RadioButton prac_mtd, prac_emmc;

	
	//格式化
	private CheckBox format_data,format_system;
	
	//提取system
	private CheckBox dump_system;
	private RadioGroup dump_system_layout;
	private RadioButton system_tar, system_gz, system_bz2;

	//使用boot.img
	private CheckBox use_boot;
	private LinearLayout use_boot_layout;
	private EditText use_boot_edit_path;
	private Button use_boot_find;

	private Button btn_finish;

	private Bundle bundle;
	private String name;
	private String another;
	private String version;

	private ROM rom;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO: Implement this method
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wk_create_new);

		initData(); 
		initViews();
		initViewsData();
	}

	private void initData()
	{
		rom = new ROM();
		bundle = getIntent().getExtras();
		name = bundle.getString("name");
		another = bundle.getString("another");
		version = bundle.getString("version");
	}

	private void initViewsData()
	{
		// TODO: Implement this method
		edit_name.setText(name);
		edit_another.setText(another);
		edit_version.setText(version);

		dump_system.setOnCheckedChangeListener(this);

		use_boot.setOnCheckedChangeListener(this);
		use_boot_find.setOnClickListener(this);

		btn_finish.setOnClickListener(this);

	}

	private void initViews()
	{
		// TODO: Implement this method
		edit_name = (EditText) findViewById(R.id.wk_create_new_edit_name);
		edit_another = (EditText) findViewById(R.id.wk_create_new_edit_another);
		edit_version = (EditText) findViewById(R.id.wk_create_new_edit_version);

		cpu_mtk = (RadioButton) findViewById(R.id.wk_create_new_cpu_mtk);
		cpu_gt = (RadioButton) findViewById(R.id.wk_create_new_cpu_gt);
		cpu_sc = (RadioButton) findViewById(R.id.wk_create_new_cpu_sc);

		dump_system = (CheckBox) findViewById(R.id.wk_create_new_dumpsystem);
		dump_system_layout = (RadioGroup) findViewById(R.id.wk_create_new_dumpsystem_layout);
		system_tar = (RadioButton) findViewById(R.id.wk_create_new_system_tar);
		system_gz = (RadioButton) findViewById(R.id.wk_create_new_system_gz);
		system_bz2 = (RadioButton) findViewById(R.id.wk_create_new_system_bz2);

		format_data = (CheckBox) findViewById(R.id.wk_create_new_format_data);
		format_system = (CheckBox) findViewById(R.id.wk_create_new_format_system);
		
		use_boot = (CheckBox) findViewById(R.id.wk_create_new_useboot);
		use_boot_layout = (LinearLayout) findViewById(R.id.wk_create_new_useboot_layout);
		use_boot_edit_path = (EditText) findViewById(R.id.wk_create_new_useboot_edit_path);
		use_boot_find = (Button) findViewById(R.id.wk_create_new_useboot_find);

		btn_finish = (Button) findViewById(R.id.wk_create_new_finish);
	}


	private void showChooser(int code)
	{
        // Use the GET_CONTENT intent from the utility class
        Intent target = FileUtils.createGetContentIntent();
        // Create the chooser Intent
        Intent intent = Intent.createChooser(
			target, getString(R.string.chooser_title));
        try
		{
            startActivityForResult(intent, code);
        }
		catch (ActivityNotFoundException e)
		{
            // The reason for the existence of aFileChooser
        }
    }




	@Override
	protected void onActivityResult(final int requestCode, int resultCode, Intent data)
	{
		// TODO: Implement this method
		// If the file selection was successful
		String path = null;
		if (resultCode != RESULT_OK)
		{
			return;
		}
		if (data == null)
		{
			return;
		}
		// Get the URI of the selected file
		final Uri uri = data.getData();
		try
		{
			// Get the file path from the URI
			path = FileUtils.getPath(this, uri);
		}
		catch (Exception e)
		{
			Log.e("IDEGui", "文件选择错误：", e);
		}
		if (path == null)
		{
			return;
		}

		/*
		 判断操作类型
		 */

		if (requestCode == Const.CHOOSE_BASE)
		{
			use_boot_edit_path.setText(path);
		}
	}


}
