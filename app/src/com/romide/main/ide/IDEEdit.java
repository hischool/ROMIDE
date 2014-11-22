package com.romide.main.ide;

import android.app.*;
import android.content.*;
import android.graphics.*;
import android.net.*;
import android.os.*;
import android.text.*;
import android.text.style.*;
import android.util.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;
import com.ipaulpro.afilechooser.utils.*;
import com.romide.main.*;
import com.romide.main.ide.utils.*;
import com.romide.main.ide.view.*;
//import de.keyboardsurfer.android.widget.crouton.*;
import java.io.*;
import java.util.regex.*;
import org.apache.http.util.*;
import com.romide.plugin.widget.crouton.*;

public class IDEEdit extends Activity
{


	public static final String[] codes = {"public","private"};
	
	private SyntaxHighlightEditText et;
	private TextView tv;

	private String file_name;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		// TODO: Implement this method
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ide_edit);

		initViews();
		initEdit();
	}



	public void initViews(){
		try{
		    et = (SyntaxHighlightEditText) findViewById(R.id.edit_edit);
			tv = (TextView) findViewById(R.id.edit_file_name);
		} catch (Exception e){
			new AlertDialog.Builder(this).setMessage(e.toString()).show();
	    }
		Actionbar();
	}

	private void initEdit()
	{
		file_name = getIntent().getStringExtra("file_name");
		if (file_name == null || file_name.equals(""))
		{
			file_name = "";
			tv.setText("无打开文件");
		}
		else
		{
			doOpenFile(file_name);
		}


	}
	


	private void loadFile(String f)
	{
		doToast("正在加载文件:" + f, GREEN, null);
		
		try
		{
			String res;
			FileInputStream fin =new FileInputStream(f);
			int length = fin.available();
			byte[] buffer =new byte[length];
			fin.read(buffer);
			res = EncodingUtils.getString(buffer, "UTF-8");
			res = new String(buffer);
			fin.close();
			et.setText(res);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}


	private int getFileLanguage(String name)
	{
		return 0;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// TODO: Implement this method
		getMenuInflater().inflate(R.menu.editor_menu, menu);
		return true;
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

		if (requestCode == Const.OPEN_FILE)
		{
			doOpenFile(path);
		}
	}


	private void Actionbar()
	{
		ActionBarCompat bar = ActivityCompat.getActionBar(this);
		if (bar != null)
		{
			bar.setDisplayOptions(ActionBarCompat.DISPLAY_HOME_AS_UP, ActionBarCompat.DISPLAY_HOME_AS_UP);
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		// TODO: Implement this method
		int id = item.getItemId();
		switch (id)
		{
			case ActionBarCompat.ID_HOME:
				finish();
				return true;
			case R.id.edit_file_new:
				doCreateNewFile();
				return true;
			case R.id.edit_file_open:
				openFile();
				return true;
			case R.id.edit_file_save:
				doSaveFile();
				return true;
			case R.id.edit_file_save_other:
				doSaveOtherFile();
				return true;
		}
		return super.onOptionsItemSelected(item);
	}


	
	private void doOpenFile(String path){
		this.file_name = path;
		this.tv.setText(path);
		this.loadFile(file_name);
		
	}
	
	
	private void doSave(String f)
	{
		try
		{
			File fc = new File(f);
			if(!fc.exists()) fc.createNewFile();
			FileOutputStream fout = new FileOutputStream(fc); 
			byte[] bytes = et.getText().toString().getBytes(); 
			fout.write(bytes); 
			fout.close();
			doToast("保存成功！",GREEN,null);
		}
		catch (Exception e)
		{
			doToast("保存出错！" + e.toString(), RED, null);
		}
	}

	private void doSaveOtherFile()
	{
		// TODO: Implement this method
		final EditText e = new EditText(this);
		new AlertDialog.Builder(this)
		    .setTitle("输入文件名")
			.setView(e)
			.setPositiveButton("确定", new DialogInterface.OnClickListener(){

				@Override
				public void onClick(DialogInterface p1, int p2)
				{
					// TODO: Implement this method
					doSave(e.getText().toString());
				}
			})
			.setNegativeButton("取消",null)
			.show();
	}

	private void doSaveFile()
	{
		// TODO: Implement this method
		if(file_name == null || file_name.equals("")){
			AlertDialog.Builder d = new AlertDialog.Builder(this);
			final EditText e = new EditText(this);
			d.setTitle("输入文件名");
			d.setView(e);
			d.setPositiveButton("保存", new DialogInterface.OnClickListener(){

					@Override
					public void onClick(DialogInterface p1, int p2)
					{
						// TODO: Implement this method
						doSave(e.getText().toString());
					}
				});
			d.setNegativeButton("取消",null);
			d.show();
		}
		else{
		    doSave(file_name);
		}
	}

	private void doCreateNewFile()
	{
		// TODO: Implement this method
		et.setText("");
		tv.setText("未命名");
		file_name = "";
	}

	private void openFile()
	{
		// TODO: Implement this method
		showChooser(Const.OPEN_FILE);
	}

	private static final Style RED = Style.ALERT;
	private static final Style GREEN = Style.CONFIRM;
	private static final Style BLUE = Style.INFO;
	public void doToast(String text, Style s, OnClickListener l)
	{
		Crouton.makeText(this, text, s).setOnClickListener(l).show();
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
	
}
