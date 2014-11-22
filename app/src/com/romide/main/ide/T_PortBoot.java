package com.romide.main.ide;

import android.app.*;
import android.content.*;
import android.net.*;
import android.os.*;
import android.util.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;
import com.ipaulpro.afilechooser.utils.*;
import com.romide.main.*;
import com.romide.main.ide.utils.*;
//import de.keyboardsurfer.android.widget.crouton.*;
import java.io.*;
import me.drakeet.materialdialog.*;

import com.romide.main.R;
import com.romide.plugin.widget.crouton.*;


public class T_PortBoot extends BaseActivity implements View.OnClickListener
{

	private EditText base_file;
	private EditText sam_file;
	private Button find_base;
	private Button find_sam;
	private Button start;
	private RadioButton mtk;
	private RadioButton gt;
	private RadioButton sc;
	
	private String sdcard = Environment.getExternalStorageDirectory().toString() + "/";
	private String script; //脚本
	private String ideDir; //脚本所在目录
	private String inDataBootOut; //boot输出目录
	//是否有后台任务
	private boolean hasPerm = false;
	
	private String cpu;
	private Command com;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		// TODO: Implement this method
		super.onCreate(savedInstanceState);
		setContentView(R.layout.port_layout);
		try{
		cpu = getIntent().getStringExtra("cpu");
		init();
		
		if(cpu.equals(Const.CPU_MTK)){
			mtk.setChecked(true);
		}
		else if(cpu.equals(Const.CPU_GT)){
			gt.setChecked(true);
		}
		else if(cpu.equals(Const.CPU_SC)){
			sc.setChecked(true);
		}
		} catch (Exception e){
			//dialog(e.toString());
		}
	}

	private void init()
	{
		// TODO: Implement this method
		base_file = (EditText) findViewById(R.id.base_file);
		sam_file = (EditText) findViewById(R.id.sam_file);
		
		find_sam = (Button) findViewById(R.id.find_sam);
		find_sam.setOnClickListener(this);
		find_base = (Button) findViewById(R.id.find_base);
		find_base.setOnClickListener(this);
		start = (Button) findViewById(R.id.start);
		start.setOnClickListener(this);
		
		mtk = (RadioButton) findViewById(R.id.cpu_mtk);
		gt = (RadioButton) findViewById(R.id.cpu_gt);
		sc = (RadioButton) findViewById(R.id.cpu_sc);
		
		com = new Command("/system/bin/bash", "/system/bin/busybox", true);
		ideDir = this.getDataFilesDir(T_PortBoot.this);
		script = ideDir + "romide";
		inDataBootOut = ideDir + "work/boot";
		
		Actionbar();
	}
	
	
	@Override
	public void onClick(View p1)
	{
		// TODO: Implement this method
		int id = p1.getId();
		switch(id){
			case R.id.find_base:
				showChooser(Const.CHOOSE_BASE);
				break;
			case R.id.find_sam:
				showChooser(Const.CHOOSE_SAM);
				break;
			case R.id.start:
				doPort();
				break;
		}
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

	
	
	public String getDataFilesDir(Context c)
	{
		return c.getFilesDir().getAbsolutePath() + File.separator;
	}

	public String getSdcardFilesDir(Context c)
	{
		return c.getExternalFilesDir(null).getAbsolutePath() + File.separator;
	}
	
	private void doPort()
	{
		// TODO: Implement this method
		String now = "unknown";
		if(mtk.isChecked()) now = "MTK";
		else if(gt.isChecked()) now = "GT";
		else if(sc.isChecked()) now = "SC";
		String base = base_file.getText().toString();
		String sam = sam_file.getText().toString();
		if(base != null && sam != null && !base.equals("") && !sam.equals("")){
		    runIDE("正在进行移植...","port_boot "+now+" "+base+" "+sam.toString());
	    }
		else{
			doToast("您未正确选择文件！",RED,null);
		}
	}
	
	
	public boolean runIDE(String title,String command){
		String script = this.getFilesDir().getAbsolutePath()+File.separator+"romide";
		return Utils.runIDECommand(this,new AlertDialog.Builder(this),new ProgressDialog(this),script,title,command);
	}
	
	
	
	private static final Style RED = Style.ALERT;
	private static final Style GREEN = Style.CONFIRM;
	private static final Style BLUE = Style.INFO;
	public void doToast(String text, Style s, OnClickListener l)
	{
		Crouton.makeText(T_PortBoot.this, text, s).setOnClickListener(l).show();
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
			Log.e("T_PortBoot", "文件选择错误：", e);
		}
		if (path == null)
		{
			return;
		}
		
		if(requestCode == Const.CHOOSE_BASE){
			base_file.setText(path);
		}
		else if(requestCode == Const.CHOOSE_SAM){
			sam_file.setText(path);
		}
	}
	
	
	
}
