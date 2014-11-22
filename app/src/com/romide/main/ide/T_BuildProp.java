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
import com.romide.main.ide.*;
import com.romide.main.ide.utils.*;
import com.romide.plugin.widget.crouton.*;
import java.io.*;
import java.util.*;
import me.drakeet.materialdialog.*;

import com.romide.main.R;
import java.lang.Process;

public class T_BuildProp extends BaseActivity implements View.OnClickListener,AdapterView.OnItemClickListener
{

	@Override
	public void onItemClick(AdapterView<?> p1, View p2, final int p3, long p4)
	{
		final String key = list.get(p3).get("name").toString();
		String value = list.get(p3).get("info").toString();
		final EditText edit = new EditText(this);
		edit.setText(value);
		new AlertDialog.Builder(this)
		    .setTitle(key)
			.setView(edit)
			.setPositiveButton("修改", new DialogInterface.OnClickListener(){

				@Override
				public void onClick(DialogInterface p1, int p2)
				{
					// TODO: Implement this method
					String svalue = edit.getText().toString();
					deleteItem(p3, key);
					addItem(key, svalue);
					p1.dismiss();
				}
			})
			.setNeutralButton("取消",null)
			.setNegativeButton("删除", new DialogInterface.OnClickListener(){

				@Override
				public void onClick(DialogInterface p1,int p2)
				{
					// TODO: Implement this method
					deleteItem(p3, key);
					p1.dismiss();
				}
			})
			.show();

	}


	@Override
	public void onClick(View p1)
	{
		// TODO: Implement this method
		int id = p1.getId();
		if (id == mFile.getId())
		{
			showChooser(0);
		}
	}


	private SharedPreferences sp;
	private Properties prop;
	private TextView mFile;
	private ListView lv;
	private SimpleAdapter sa;
	private ArrayList<HashMap<String,Object>> list = new ArrayList<HashMap<String,Object>>();
	private HashMap<String,Object> map = new HashMap<String,Object>();

	private String file;
	private boolean changed  = false;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		// TODO: Implement this method
		super.onCreate(savedInstanceState);
		setContentView(R.layout.t_buildprop);
		init();


	}

	private void init()
	{
		// TODO: Implement this method
		mFile = (TextView) findViewById(R.id.buildprop_file_name);
		mFile.setOnClickListener(this);
		prop = new Properties();
		lv = (ListView) findViewById(R.id.buildprop_list);
		lv.setOnItemClickListener(this);

		sp = getSharedPreferences("build_prop_editor", 0);
		String f = sp.getString("file_name", "no_file_select");
		if (!f.equals("no_file_select"))
		{
			file = f;
			mFile.setText(file);
			try
			{
				loadProp(file);
			}
			catch (Exception e)
			{
				doToast("加载文件时发生错误！", RED, null);
			}
		}

		Actionbar();
	}


	private void loadProp(String file) throws Exception
	{
		if (!new File(file).exists() && !new File(file).canRead())
		{
			return;
		}
		FileInputStream is = new FileInputStream(file);
		prop.load(is);
		is.close();
		mFile.setText(file);

		//deleteItem();

		for (Object key : prop.keySet())
		{
			map = new HashMap<String,Object>();
			map.put("icon", R.drawable.build_prop_logo);
			map.put("name", key);
			map.put("info", prop.getProperty((String)key));
			list.add(map);
		}

		sa = new SimpleAdapter
		(this,
		 list,
		 R.layout.t_buildprop_item,
		 new String[]{
			 "icon",
			 "name",
			 "info"
		 },
		 new int[]{
			 R.id.icon,
			 R.id.name,
			 R.id.info
		 }
		 );
		lv.setAdapter(sa);
		sp.edit().putString("file_name", file).commit();
	}



	private void addItem(String key, String value)
	{
		HashMap<String,Object> mmap = new HashMap<String,Object>();
		mmap.put("icon", R.drawable.ic_ide);
		mmap.put("name", key);
		mmap.put("info", value);

		list.add(mmap);
		sa.notifyDataSetChanged();
		prop.setProperty(key, value);
		changed = true;
	}

	private void deleteItem(int index, String key)
	{
		list.remove(index);
		prop.remove(key);
		sa.notifyDataSetChanged();
		changed = true;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// TODO: Implement this method
		getMenuInflater().inflate(R.menu.build_prop_menu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		// TODO: Implement this method
		int id = item.getItemId();
		if (id == R.id.add)
		{
			final EditText keye = new EditText(this);
			keye.setHint("输入属性名");
			final EditText valuee = new EditText(this);
			valuee.setHint("输入属性值");
			LinearLayout ll = new LinearLayout(this);
			ll.setOrientation(1);
			ll.addView(keye);
			ll.addView(valuee);
			MaterialDialog d = new MaterialDialog(this);
			    d.setTitle("添加");
				d.setView(ll);
				d.setPositiveButton("添加", new View.OnClickListener(){
					@Override
					public void onClick(View v)
					{
						// TODO: Implement this method
						addItem(keye.getText().toString(), valuee.getText().toString());
					}
				});
				d.setNegativeButton("取消", null);
				d.show();
		}
		else if (id == R.id.save)
		{
			saveProp(file);
		}
		else if (id == R.id.open)
		{
			showChooser(0);
		}
		else if (id == R.id.open_system)
		{
			try
			{
				Process p = Runtime.getRuntime().exec("su");
				DataOutputStream os = new DataOutputStream(p.getOutputStream());
				os.write("busybox cp -f /system/build.prop /sdcard/ROM-IDE/build.prop".getBytes());
				os.flush();
				os.close();
				p.waitFor();
				file = "/sdcard/ROM-IDE/build.prop";
				loadProp(file);
			}
			catch (Exception e)
			{
				doToast("复制文件时发生错误！", RED, null);
			}
		}
		if (id == ActionBarCompat.ID_HOME)
		{
			ask_exit();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void ask_exit()
	{
		// TODO: Implement this method
		if (changed)
		{
			MaterialDialog d = new MaterialDialog(T_BuildProp.this);
				d.setTitle("退出");
				d.setMessage("是否保存？");
			d.setNegativeButton("取消", new View.OnClickListener(){

					@Override
					public void onClick(View p1)
					{
						// TODO: Implement this method
						T_BuildProp.this.finish();
					}
				});
				d.setPositiveButton("保存", new View.OnClickListener(){

					@Override
					public void onClick(View v)
					{
						// TODO: Implement this method
						saveProp(file);
						T_BuildProp.this.finish();
					}
				});
				d.show();
		}
		else{
			finish();
		}
	}

	private void saveProp(String file)
	{
		File f = new File(file);
		FileOutputStream os = null;
		try
		{
			if (!f.exists()) f.createNewFile();
			if (f.canWrite())
			{
				os = new FileOutputStream(f);
				prop.store(os, "Edit with ROMIDE ");
				changed = false;
				doToast("保存成功！", GREEN, null);
			}
		}
		catch (Exception e)
		{
			doToast("保存时发生错误", RED, null);
		}
		finally
		{
			if (os != null)
			{
				try
				{
					os.close();
				}
				catch (Exception e)
				{

				}
			}
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		// TODO: Implement this method
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == event.ACTION_DOWN)
		{
			ask_exit();
		}
		return super.onKeyDown(keyCode, event);
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
			changed = false;
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
			Log.e("T_BuildProp", "文件选择错误：", e);
		}
		if (path == null)
		{
			return;
		}

		file = path;
		try
		{
			loadProp(file);
		}
		catch (Exception e)
		{
			doToast("加载文件时发生错误！", RED, null);
		}
	}


	private static final Style RED = Style.ALERT;
	private static final Style GREEN = Style.CONFIRM;
	private static final Style BLUE = Style.INFO;
	public void doToast(String text, Style s, OnClickListener l)
	{
		Crouton.makeText(T_BuildProp.this, text, s).setOnClickListener(l).show();
	}




	private void Actionbar()
	{
		ActionBarCompat bar = ActivityCompat.getActionBar(this);
		if (bar != null)
		{
			bar.setDisplayOptions(ActionBarCompat.DISPLAY_HOME_AS_UP, ActionBarCompat.DISPLAY_HOME_AS_UP);
		}
	}


}
