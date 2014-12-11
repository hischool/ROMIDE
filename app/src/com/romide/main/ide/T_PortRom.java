package com.romide.main.ide;

import android.app.*;
import android.content.*;
import android.net.*;
import android.os.*;
import android.util.*;
import android.view.*;
import android.view.View.*;
import com.ipaulpro.afilechooser.utils.*;
import com.romide.main.*;
import com.romide.plugin.widget.crouton.*;
import java.io.*;
import android.support.v4.view.*;
import java.util.*;
import android.widget.*;


public class T_PortRom extends Activity implements View.OnClickListener
{
	private ViewPager pager;
	private PagerTabStrip titleStrip;
	private ArrayList<View> views;
	private ArrayList<String> titles;

	private EditText output;

	private EditText basefile;
	private EditText samfile;
	private Button findbase;
	private Button findsam;
	private Button findpm;
	private Button start;
	private RadioButton cpu_mtk;
	private RadioButton cpu_gt;
	private RadioButton cpu_sc;



	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ide_port_rom);

		initViews();
	}

	void initViews()
	{
		try
		{
			views = new ArrayList<View>();
			titles = new ArrayList<String>();
			
			pager = (ViewPager) findViewById(R.id.portrom_viewpager);
			titleStrip = (PagerTabStrip) findViewById(R.id.portrom_pagertitle);

			View choose = getLayoutInflater().inflate(R.layout.ide_port_rom_choosefile, null);
			View running = getLayoutInflater().inflate(R.layout.ide_port_rom_running, null);

			views.add(choose);
			views.add(running);
			
			titles.add(getString(R.string.portrom_choosefile));
			titles.add(getString(R.string.portrom_running));

			MyPagerAdapter adapter = new MyPagerAdapter();
			pager.setAdapter(adapter);
		}
		catch (Exception e)
		{
			new AlertDialog.Builder(this).setMessage(e.toString()).show();
		}
	}

	@Override
	public void onClick(View p1)
	{
		// TODO: Implement this method
		if (p1 == titleStrip){
			
		}
	}


	class MyPagerAdapter extends PagerAdapter
	{
		@Override
		public int getCount()
		{
			// TODO: Implement this method
			return views.size();
		}

		@Override
		public boolean isViewFromObject(View p1, Object p2)
		{
			// TODO: Implement this method
			return p1 == p2;
		}

		@Override
		public CharSequence getPageTitle(int position)
		{
			return titles.get(position);
		}

		@Override
		public void destroyItem(View container, int position, Object object)
		{
			((ViewPager)container).removeView(views.get(position));
		}

		@Override
		public Object instantiateItem(View container, int position)
		{
			((ViewPager)container).addView(views.get(position));
			return views.get(position);
		}

	}





	public boolean runIDE(String title, String command)
	{
		String script = this.getFilesDir().getAbsolutePath() + File.separator + "romide";
		return Utils.runIDECommand(this, new AlertDialog.Builder(this), new ProgressDialog(this), script, title, command);
	}

	private static final Style RED = Style.ALERT;
	private static final Style GREEN = Style.CONFIRM;
	private static final Style BLUE = Style.INFO;
	public void doToast(String text, Style s, OnClickListener l)
	{
		Crouton.makeText(T_PortRom.this, text, s).setOnClickListener(l).show();
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
		String path = null;
		if (resultCode != RESULT_OK)
		{
			return;
		}
		if (data == null)
		{
			return;
		}
		final Uri uri = data.getData();
		try
		{
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

	}


}
