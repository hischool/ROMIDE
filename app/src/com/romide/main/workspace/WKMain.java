package com.romide.main.workspace;

import android.app.*;
import android.content.*;
import android.net.*;
import android.os.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;
import com.romide.main.*;
import com.romide.main.ide.*;
import com.romide.main.ide.utils.*;
import com.romide.plugin.widget.crouton.*;
import java.io.*;
import java.util.*;

public class WKMain extends BaseActivity
{
	public static final boolean debug = true;

	private ROM rom;

	private static final String CREATE_NEW = "创建新的 ROM";
	private static final String EXPORT_ROM = "生成刷机包";
	private static final String CHOOSE_ROM = "切换 ROM 项目";

	public static final File HOMEDIR = new File(ROM.HOME);
	public static final File SCRIPTDIR = new File(ROM.HOME, ".mkrom");


	private String ICON_KEY = "ICON";
	private String TEXT_KEY = "TEXT";
	private static final String[] text = {
		CREATE_NEW,
		EXPORT_ROM,
		CHOOSE_ROM,
	};
	private static final int[] icon = {
		R.drawable.wk_create_new,
		R.drawable.wk_export,
		R.drawable.wk_create_new,
	};


	private SharedPreferences sp;

	private GridView gv;
	private TextView opening;
	public static String opening_work_profile;
	private List<HashMap<String,Object>> list = new ArrayList<HashMap<String,Object>>();
	private SimpleAdapter sa;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		// TODO: Implement this method
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.wk_main) ;
		Actionbar();
		init();
	}



	public String HAS_OPEN = "0";
	public String NOT_OPEN = "1";
	private void init()
	{
		rom = new ROM();

		sp = getSharedPreferences("workspace", 0);

		if (sp.getBoolean("first", true))
		{
			sp.edit().putBoolean("first", false).commit();
			AlertDialog.Builder d = new AlertDialog.Builder(this);
			d.setTitle("欢迎使用 ROM 工作室");
			d.setMessage("这是一个强大的ROM一键制作器，用于提取当前系统制作刷机包。制作过程中，您可以对其进行优化以及美化，这些，我们都会帮助你完成！\n\n使用前，请使用第一个选项创建一个新的刷机包工程，如果一切已经就绪，您可以使用\"生成刷机包\"选项生成属于您的ROM");
			d.setPositiveButton("我知道了", null);
			d.show();
		}

		if (!HOMEDIR.exists())
		{
			HOMEDIR.mkdirs();
		}
		if (!SCRIPTDIR.exists() || IDEMain.isUpdated)
		{
			SCRIPTDIR.mkdirs();
			File busybox = new File(this.getFilesDir().getAbsolutePath() + File.separator + "busybox");
			if (!busybox.exists())
			{
				busybox = new File(MyFileUtils.CopyFile(this, "busybox"));
				try
				{
					Runtime.getRuntime().exec("chmod 777 " + busybox.getAbsolutePath()).waitFor();
				}
				catch (Exception e)
				{

				}
			}
			File zip = new File(CopyFile(this, "mkrom.zip"));
			try
			{
				Runtime.getRuntime().exec(busybox.getAbsolutePath() + " unzip -o " + zip.getAbsolutePath() + " -d " + SCRIPTDIR.getAbsolutePath()).waitFor();
			}
			catch (Exception e)
			{

			}
		}
		opening = (TextView) findViewById(R.id.wk_main_opening_work);
		opening_work_profile = getCurrentWork();
		if (opening_work_profile == null || opening_work_profile.equals(""))
		{
			//无打开工程
			setCurrentWork(null);
		}
		else
		{
			//文件不存在
			if (!new File(opening_work_profile).exists())
			{
				setCurrentWork(null);
			}
			//存在
			else
			{
				setCurrentWork(opening_work_profile);
			}
		}

		gv = (GridView) findViewById(R.id.wk_main_gv);
		HashMap<String,Object> map;

		for (int i=0;i < text.length;i++)
		{
			map = new HashMap<String,Object>();
			map.put(ICON_KEY, icon[i]);
			map.put(TEXT_KEY, text[i]);
			list.add(map);
		}

		sa = new SimpleAdapter(this, list, R.layout.grid_view_item, new String[]{ICON_KEY,TEXT_KEY}, new int[]{R.id.gv_img,R.id.gv_tv});
		gv.setAdapter(sa);
		gv.setOnItemClickListener(new OnItemClick());
	}


	//单机事件
	private class OnItemClick implements AdapterView.OnItemClickListener
	{

		@Override
		public void onItemClick(AdapterView<?> adapter, View view, int position, long id)
		{
			// TODO: Implement this method
			String key = list.get(position).get(TEXT_KEY).toString();
			if (key.equals(CREATE_NEW)) 
				doCreateNew();
			else if (key.equals(CHOOSE_ROM))
				doChooseRom();
			else
			{
				//无打开工程
				if(opening.getTag().equals(NOT_OPEN)){
					dialog(WKMain.this,getString(R.string.error),"无打开工程！无法进行操作");
					return;
				}
				//有工程，可以操作
				if (key.equals(EXPORT_ROM)) doExportRom();
			}
		}

	}

	private void doChooseRom()
	{
		List<String> list = ROM.getAllProjects();
		//String[] array = new String[list.size()];
		
		final String[] items = list.toArray(new String[list.size()]);
		
		AlertDialog.Builder dialog = new AlertDialog.Builder(WKMain.this);
		dialog.setTitle("选择将要打开的项目");
		dialog.setNegativeButton(android.R.string.no,null);
		dialog.setItems(items, new DialogInterface.OnClickListener(){

				@Override
				public void onClick(DialogInterface p1, int p2)
				{
					// TODO: Implement this method
					String profile = ROM.HOME + File.separator + items[p2] + File.separatorChar +".profile";
					
					setCurrentWork(profile);
				}
			});
		dialog.show();
	}
	
	private void doExportRom()
	{
		final File file = new File(rom.getOutputZip());
		if (file.exists())
		{
			new AlertDialog.Builder(this)
			    .setTitle(R.string.warning)
				.setMessage("输出文件(" + file.getAbsolutePath() + ")已存在\n是否覆盖？")
				.setNegativeButton(R.string.no, null)
				.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener(){

					@Override
					public void onClick(DialogInterface p1, int p2)
					{
						file.delete();
						createZip();
					}
				})
				.show();
		}
		else
		{
			createZip();
		}
	}

	private int ZIP_FINISH = 0;
	Handler zipHandler = new Handler(){

		@Override
		public void handleMessage(Message msg)
		{
			// TODO: Implement this method
			super.handleMessage(msg);
			Bundle bundle = msg.getData();
			int what = msg.what;
			if(what == ZIP_FINISH){
				String title = bundle.getString("title");
				String message = bundle.getString("message");
				WKMain.this.dialog(WKMain.this, title,message);
			}
		}
		
	};
	private void createZip()
	{
		final ProgressDialog d = new ProgressDialog(WKMain.this);
		d.setCancelable(false);
		d.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		d.setMessage("正在生成 ROM");
		d.show();
		
		new Thread(){
			public void run(){
				try
				{
					rom.createZip();
					Bundle bundle = new Bundle();
					bundle.putString("title","恭喜");
					bundle.putString("message","创建完成:"+rom.getOutputZip());
					Message msg = new Message();
					msg.what = ZIP_FINISH;
					msg.setData(bundle);
					zipHandler.sendMessage(msg);
				}
				catch (Exception e)
				{}
				finally{
					d.dismiss();
				}
			}
		}.start();
	}

	private void doCreateNew()
	{
		final View v = inflate(R.layout.wk_dialog_create_new, R.id.wk_dialog_create_new_layout);
		AlertDialog.Builder d = new AlertDialog.Builder(this);
		d.setTitle("新建 ROM");
		d.setView(v);
		d.setPositiveButton("新建", new DialogInterface.OnClickListener(){

				@Override
				public void onClick(DialogInterface p1, int p2)
				{
					// TODO: Implement this method
					int i = 1;
					File result_File = new File(ROM.HOME,"MyROM");
					while(result_File.exists()){
						result_File = new File(ROM.HOME,"MyROM_"+i);
						i++;
					}
					
					final EditText name = (EditText) v.findViewById(R.id.wk_dialog_create_new_name);
					final EditText another = (EditText) v.findViewById(R.id.wk_dialog_create_new_another);
					final EditText version = (EditText) v.findViewById(R.id.wk_dialog_create_new_version);
					String sname = name.getText().toString();
					String sanother = another.getText().toString();
					String sversion = version.getText().toString();

					sname = sname.equals("") ? result_File.getName() : sname;
					sanother = sanother.equals("") ? "ROM-IDE" : sanother;
					sversion = sversion.equals("") ? "1.0" : sversion;

					Bundle bundle = new Bundle();
					bundle.putString("name", sname);
					bundle.putString("another", sanother);
					bundle.putString("version", sversion);
					Intent it = new Intent(WKMain.this, WKCreateNew.class);
					it.putExtras(bundle);
					startActivityForResult(it, Const.WK_CREATE_NEW);
				}
			});
		d.setNegativeButton("取消", null);
		d.show();
	}



	private View inflate(int res, int id)
	{
		LayoutInflater inflater = getLayoutInflater();
		final View view = inflater.inflate(res, (ViewGroup) findViewById(id));
		return view;
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
		}
		return super.onOptionsItemSelected(item);
	}

	private static final Style RED = Style.ALERT;
	private static final Style GREEN = Style.CONFIRM;
	private static final Style BLUE = Style.INFO;
	public void doToast(String text, Style s, OnClickListener l)
	{
		Crouton.makeText(this, text, s).setOnClickListener(l).show();
	}


	public boolean runIDE(String title, String command)
	{
		String script = this.getFilesDir().getAbsolutePath() + File.separator + "romide";
		return Utils.runIDECommand(this, new AlertDialog.Builder(this), new ProgressDialog(this), script, title, command);
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
		path = uri.getPath();

		if (requestCode == Const.WK_CREATE_NEW)
		{
		    if (debug) Toast.makeText(this, path, 0).show();
			setCurrentWork(path);
		}
	}

	
	

	public void setCurrentWork(String profileName)
	{
		sp.edit().putString("opening", profileName).commit();
		if(profileName == null){
			opening.setText("当前无打开工程\n赶快新建或打开一个吧");
			opening.setTag(NOT_OPEN);
		}
		else {
			
			rom.initFromProfile(profileName);
			opening.setTag(HAS_OPEN);
			opening.setText("当前所在工程:\n" + rom.getName());
		}
	}

	public String getCurrentWork()
	{
		return sp.getString("opening", null);
	}



}




