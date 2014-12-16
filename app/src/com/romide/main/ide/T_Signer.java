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
import com.romide.main.ide.utils.*;
import com.romide.plugin.widget.crouton.*;
import java.io.*;
import kellinwood.security.zipsigner.*;
import me.drakeet.materialdialog.*;

import com.romide.main.R;

public class T_Signer extends BaseActivity implements View.OnClickListener
{

	
	@Override
	public void onClick(View p1)
	{
		// TODO: Implement this method
		int id = p1.getId();
		switch(id){
			case R.id.signer_find_in:
				showChooser(Const.CHOOSE_INP);
				break;
			case R.id.signer_find_out:
				showChooser(Const.CHOOSE_OUT);
				break;
			case R.id.signer_start:
				doSign();
				break;
		}
	}

	private void doSign()
	{
		// TODO: Implement this method
		String inp = in.getText().toString();
		String outp = out.getText().toString();
		if (inp.equals("") || outp.equals("")){
			doToast("您未选择正确文件",RED,null);
			return;
		}
		
		T_SignerTask t = new T_SignerTask("正在签名...",new ProgressDialog(this), new MaterialDialog(this),zs);
		t.execute(inp,outp);
	}
	
	
	private ZipSigner zs;
	
	private Button find_in;
	private Button find_out;
	private Button start;
	private EditText in;
	private EditText out;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		// TODO: Implement this method
		super.onCreate(savedInstanceState);
		setContentView(R.layout.t_signer);
		
		init();
	}
	
	
	private void init(){
		find_in = (Button) findViewById(R.id.signer_find_in);
		find_out = (Button) findViewById(R.id.signer_find_out);
		start = (Button) findViewById(R.id.signer_start);
		
		find_in.setOnClickListener(this);
		find_out.setOnClickListener(this);
		start.setOnClickListener(this);
		
		in = (EditText) findViewById(R.id.signer_in);
		out = (EditText) findViewById(R.id.signer_out);
		
		zs = new ZipSigner();
		try
		{
			zs.setKeymode(ZipSigner.MODE_AUTO_TESTKEY);
		}
		catch (Exception e)
		{
			dialog("设置keymode失败",e.toString()+"\n"+e.getMessage());
		}
		
		Actionbar();
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
	
	
	
	public void dialog(String s,String s2){
		MaterialDialog d = new MaterialDialog(this);
		    d.setTitle(s);
			d.setMessage(s2);
			d.setPositiveButton("关闭",null);
			d.show();
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
			Log.e("T_Signer", "文件选择错误：", e);
		}
		if (path == null)
		{
			return;
		}

		if(requestCode == Const.CHOOSE_INP){
			in.setText(path);
			out.setText(path + "_signed." + Utils.getFileLast(new File(path)));
		}
		else if(requestCode == Const.CHOOSE_OUT){
			out.setText(path);
		}
	}
	
	
	private static final Style RED = Style.ALERT;
	private static final Style GREEN = Style.CONFIRM;
	private static final Style BLUE = Style.INFO;
	public void doToast(String text, Style s, OnClickListener l)
	{
		Crouton.makeText(this, text, s).setOnClickListener(l).show();
	}
}

