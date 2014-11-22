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
//import de.keyboardsurfer.android.widget.crouton.*;
import java.io.*;
import java.util.*;
import me.drakeet.materialdialog.*;

import com.romide.main.R;
import com.romide.plugin.widget.crouton.*;

public class IDEPortMethodManager extends BaseActivity implements View.OnClickListener,AdapterView.OnItemClickListener,AdapterView.OnItemLongClickListener
{

	

	private MyProp prop;
	private ListView lv;
	private SimpleAdapter sa;
	private ArrayList<HashMap<String,Object>> list = new ArrayList<HashMap<String,Object>>();
	private ArrayList<Object> raw_key = new ArrayList<Object>();
	private HashMap<String,Object> map = new HashMap<String,Object>();

	private String file = Const.port_method_list_file;
	private boolean changed  = false;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		// TODO: Implement this method
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ide_port_method_manager);
		init();


	}

	private void init()
	{
		// TODO: Implement this method
		prop = new MyProp();
		lv = (ListView) findViewById(R.id.port_method_list);
		lv.setOnItemClickListener(this);
		lv.setOnItemLongClickListener(this);
		
		try {
		    loadProp(file);
		} catch (Exception e){
			doToast("加载列表发生错误",RED,null);
		}

		Actionbar();
	}
	
	
	
	
	
	private void doEdit(String key, String value)
	{
		// TODO: Implement this method
	}


	private void doExport(String key,String value){
		if(!new File(value).exists()){
			doToast("方案文件已丢失！",RED,null);
			return;
		}
		String out = IDEGui.sdcard + new File(value).getName();
		Utils.cp(value,out);
		doToast("已导出到:"+out,GREEN,null);
	}


	
	
	@Override
	public boolean onItemLongClick(AdapterView<?> p1, View p2, final int p3, long p4)
	{
		// TODO: Implement this method
		final String key = list.get(p3).get("name").toString();
		final String value = list.get(p3).get("info").toString();

		final String[] item = new String[]{"编辑","删除","导出"};
		AlertDialog.Builder dialog = new AlertDialog.Builder(this);
		dialog.setTitle(key);
		dialog.setItems(item, new DialogInterface.OnClickListener(){
				@Override
				public void onClick(DialogInterface d, int pos)
				{
					// TODO: Implement this method
					String com = item[pos];
					if(com.equals("编辑")){
						doEdit(key,value);
					} 
					else if(com.equals("删除")){
						new AlertDialog.Builder(IDEPortMethodManager.this)
							.setTitle(key)
							.setMessage("确定删除方案："+key+"?\n将无法恢复！")
							.setPositiveButton("确定", new DialogInterface.OnClickListener(){

								@Override
								public void onClick(DialogInterface p1, int p2)
								{
									// TODO: Implement this method
									deleteItem(p3);
								}
							})
							.setNegativeButton("取消",null)
							.show();
					}
					else if(com.equals("导出")){
						doExport(key,value);
					}
					d.dismiss();
				}
			});
		dialog.setNegativeButton("取消",null);
		dialog.show();
		return true;
	}


	@Override
	public void onItemClick(AdapterView<?> p1, View p2, final int p3, long p4)
	{
		final String key = list.get(p3).get("name").toString();
		final String value = list.get(p3).get("info").toString();
		doEdit(key,value);
	}




	@Override
	public void onClick(View p1)
	{
		// TODO: Implement this method
		int id = p1.getId();
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

		//deleteItem();

		for (Object key : prop.keySet())
		{
			String value = prop.getProperty(key.toString());
			String key_res = key.toString();
			try{
				key_res = new String(key_res.getBytes("ISO-8859-1"),"utf-8");
			}
			catch (Exception e){
				
			}
			map = new HashMap<String,Object>();
			map.put("icon", R.drawable.build_prop_logo);
			map.put("name", key_res);
			map.put("info", value);
			list.add(map);
			raw_key.add(key);
		}

		sa = new SimpleAdapter(this,list,R.layout.t_buildprop_item,new String[]{"icon","name","info"},new int[]{R.id.icon,R.id.name,R.id.info});
		lv.setAdapter(sa);
		//sp.edit().putString("file_name", file).commit();
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

	private void deleteItem(final int index)
	{
		String key = raw_key.get(index).toString();
		list.remove(index);
		raw_key.remove(index);
		prop.remove(key);
		sa.notifyDataSetChanged();
		changed = true;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// TODO: Implement this method
		getMenuInflater().inflate(R.menu.port_method_manager, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		// TODO: Implement this method
		int id = item.getItemId();
		if(id==ActionBarCompat.ID_HOME){
			ask_exit();
		}
		return super.onOptionsItemSelected(item);
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
				prop.store(os, null);
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


	private void ask_exit()
	{
		// TODO: Implement this method
		if (changed)
		{
			MaterialDialog d = new MaterialDialog(IDEPortMethodManager.this);
			d.setTitle("退出");
			d.setMessage("是否保存？");
			d.setNegativeButton("取消", new View.OnClickListener(){

					@Override
					public void onClick(View p1)
					{
						// TODO: Implement this method
						IDEPortMethodManager.this.finish();
					}
				});
			d.setPositiveButton("保存", new View.OnClickListener(){

					@Override
					public void onClick(View v)
					{
						// TODO: Implement this method
						saveProp(file);
						IDEPortMethodManager.this.finish();
					}
				});
			d.show();
		}
		else{
			finish();
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
		Crouton.makeText(this, text, s).setOnClickListener(l).show();
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
