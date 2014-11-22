package com.romide.main;

import android.app.*;
import android.content.*;
import android.os.*;
import android.view.*;
import java.util.*;
import java.io.*;
import android.widget.*;


public class Virtual extends Activity
{

	//布局文件
	private String layout_file;
	
	//根布局
	private LinearLayout root;
	
	private Button mainBtn;
	
	//分析流
	private Properties prop;
	private FileInputStream is;
	
	private Intent it;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		// TODO: Implement this method
		super.onCreate(savedInstanceState);
		
		it = getIntent();
		layout_file = it.getStringExtra("layout_file");
		if(layout_file == null || layout_file.equals("")){
			finish();
		}
		
		try
		{
			is = new FileInputStream(layout_file);
			prop.load(is);
			is.close();
		}
		catch (Exception e)
		{
			finish();
		}
		
		
		root = new LinearLayout(this);
		mainBtn = new Button(this);
		mainBtn.setText(getMainButtonText());
		mainBtn.setHeight(RelativeLayout.LayoutParams.WRAP_CONTENT);
		mainBtn.setWidth(RelativeLayout.LayoutParams.WRAP_CONTENT);
		root.addView(mainBtn);
		setContentView(root);
		
	}

	public String getMainButtonText(){
		return prop.getProperty("MainButtonText","Button");
	}
}
