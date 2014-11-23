package com.romide.main.ide;

import android.app.*;
import android.content.*;
import android.content.res.*;
import android.net.*;
import android.os.*;
import android.preference.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;
import com.romide.main.*;
import com.romide.main.ide.utils.*;
import com.romide.main.workspace.*;
import com.umeng.fb.*;
import com.umeng.update.*;
//import de.keyboardsurfer.android.widget.crouton.*;
import java.io.*;
import java.util.*;
import me.drakeet.materialdialog.*;

import com.romide.main.R;
import java.lang.Process;
import com.romide.plugin.widget.crouton.*;
import android.content.pm.*;

public class IDEMain extends BaseActivity implements AdapterView.OnItemClickListener
{

	@Override
	public void onItemClick(AdapterView<?> p1, View p2, int p3, long p4)
	{
		// TODO: Implement this method
		String key = list.get(p3).get(TEXT_KEY).toString();
		if (key.equals(BASIC)) doHome();
		if (key.equals(MORE))  doMore();
		if (key.equals(TEACH)) doTeach();
		if (key.equals(WORKSPACE)) doWorkSpace();
		if (key.equals(ABOUT)) doAbout();
		if (key.equals(PLUGIN)) doPlugin();
		if (key.equals(TELLME)) doTellme();
		if (key.equals(SETTINGS)) doSettings();
	}

	public static final int SHOW = 0;
	public static final int CHECK_UPDATE = 1;

	private Settings set;
	private SharedPreferences sp;

	private FeedbackAgent fb;

	private GridView gv;
	private SimpleAdapter sa;
	private ArrayList<HashMap<String,Object>> list;

	private static final String ICON_KEY = "ICON";
	private static final String TEXT_KEY = "TEXT";

	private static String BASIC = "基础功能";
	private static String MORE  = "高级功能";
	private static String TEACH = "ROM 教程";
	private static String WORKSPACE = "ROM 工作";
	private static String ABOUT = "关于";
	private static String PLUGIN = "插件管理";
	private static String TELLME = "意见反馈";
	private static String SETTINGS = "系统设置";



	private static final Style RED = Style.ALERT;
	private static final Style GREEN = Style.CONFIRM;
	private static final Style BLUE = Style.INFO;

	@Override
	public void onCreate(Bundle icicle)
	{
		// TODO: Implement this method
		super.onCreate(icicle);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.ide_main);


		init();
		doInitSystem();

		startService(new Intent(this, IDEService.class));

		if (set.isAutoCheckUpdate())
		{
		    //doCheckUpdate();
			UmengUpdateAgent.setUpdateOnlyWifi(false); 
			UmengUpdateAgent.setDeltaUpdate(false);
			UmengUpdateAgent.update(this);
		}

		fb = new FeedbackAgent(this);

		//StandOutWindow.closeAll(this,IDEQuickWindow.class);
		//StandOutWindow.show(this,IDEQuickWindow.class,StandOutWindow.DEFAULT_ID);
	}

	public void init()
	{
		//初始化文字
		Resources r = getResources();
		BASIC = r.getString(R.string.ide_title_gui);
		MORE = r.getString(R.string.ide_title_more);
		TEACH = r.getString(R.string.main_romteach);
		WORKSPACE = r.getString(R.string.ide_title_workspace);
		ABOUT = r.getString(R.string.ide_title_about);
		PLUGIN = r.getString(R.string.ide_title_plugin);
		TELLME = r.getString(R.string.tellme);
		SETTINGS = r.getString(R.string.ide_title_preference);
	    final int[] icon = {
			R.drawable.main_basic,
			R.drawable.main_more,
			R.drawable.main_teach,
			R.drawable.main_workspace,
			R.drawable.main_install,
			R.drawable.main_plugin,
			R.drawable.main_tellme,
			R.drawable.main_settings,
		};
	    final String[] text = {
			BASIC,
			MORE,
			TEACH,
			WORKSPACE,
			ABOUT,
			PLUGIN,
			TELLME,
			SETTINGS,
		};


		gv = (GridView) findViewById(R.id.ide_main_gv);
		list = new ArrayList<HashMap<String,Object>>();
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
		gv.setOnItemClickListener(this);

		sp = PreferenceManager.getDefaultSharedPreferences(this);
		set = new Settings(getResources(), sp);

		checkInstall();
	}




	//检测环境
	private void checkInstall()
	{
		try
		{
			File sdcardDir = new File(Environment.getExternalStorageDirectory() + File.separator + "ROM-IDE");
			if (!sdcardDir.exists())
			{
				AlertDialog.Builder d = new AlertDialog.Builder(this);
				d.setTitle(R.string.error);
				d.setMessage(R.string.nodata_dir);
				d.setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener(){

						@Override
						public void onClick(DialogInterface p1, int p2)
						{
							// TODO: Implement this method
							finish();
						}
					});
				d.setCancelable(false);
				d.show();
				return;
			}
			File dataDir = this.getFilesDir();
			File dr = new File(dataDir, "ROM-IDE");
			if (!dr.exists())
			{
				dr.mkdirs();
				File work = new File(dr, "work");
				File tools = new File(dr, "tools");
				File bin = new File(dr, "bin");
				File bbdir = new File(bin, "bbdir");

				work.mkdirs();
				tools.mkdirs();

				File busybox = new File(dataDir, "busybox");
				Process p = Runtime.getRuntime().exec("su");
				DataOutputStream os = new DataOutputStream(p.getOutputStream());

				//复制 busybox
				os.write(("cat " + sdcardDir.getAbsolutePath() + "/tools/bin/busybox > " + busybox.getAbsolutePath() + "\n").getBytes());
				os.write(("chmod 777 " + busybox.getAbsolutePath() + "\n").getBytes());

				//复制 romide
				os.write(("cat " + sdcardDir.getAbsolutePath() + "/romide > " + dataDir.getAbsolutePath() + "/romide" + "\n").getBytes());
				os.write(("chmod 777 " + dataDir.getAbsolutePath() + "/romide" + "\n").getBytes());


				//复制 tools/bin 文件夹
				os.write((busybox.getAbsolutePath() + " cp -r " + sdcardDir.getAbsolutePath() + "/tools/bin " + dr.getAbsolutePath() + "\n").getBytes());

				//安装 busybox
				bbdir.mkdirs();
				os.write((busybox.getAbsolutePath() + " --install -s " + bbdir.getAbsolutePath() + "\n").getBytes());

				//链接到 /data
				os.write((busybox.getAbsolutePath() + " ln -sf " + dr.getAbsolutePath() + " /data " + "\n").getBytes());

				//设置最终权限
				os.write((busybox.getAbsolutePath() + " chmod -R 777 " + dr.getAbsolutePath() + "\n").getBytes());

				//设置自己的 su
				os.write((busybox.getAbsolutePath() + " chmod 7777 " + bin.getAbsolutePath() + "/su" + "\n").getBytes());

				os.flush();
				os.close();
				p.waitFor();
			}
		}
		catch (Exception e)
		{

		}
	}



	//检测apk
	public void doInitSystem()
	{
		if (!Utils.isTrulyApk(this))
		{
			MaterialDialog d = new MaterialDialog(this);
			d.setTitle(R.string.warning);
			d.setMessage(R.string.isnot_truly_apk);
			d.setCanceledOnTouchOutside(false);
			d.setPositiveButton(getResources().getString(R.string.download_truly), new View.OnClickListener(){

					@Override
					public void onClick(View v)
					{
						try
						{
							// TODO: Implement this method
							Intent it = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.romide.com")); 
							it.setClassName("com.android.browser", "com.android.browser.BrowserActivity"); 
							startActivity(it);
						}
						catch (Exception e)
						{
							Toast.makeText(IDEMain.this, R.string.cannot_download, 0).show();
						}
						IDEMain.this.finish();
						System.exit(0);
					}
				});
			d.setNegativeButton(getResources().getString(R.string.exit), new View.OnClickListener(){

					@Override
					public void onClick(View v)
					{
						// TODO: Implement this method
						IDEMain.this.finish();
						System.exit(0);
					}
				});
			d.show();
		}
		else
		{
			try
			{
				PackageInfo pi = this.getPackageManager().getPackageInfo(this.getPackageName(), 0);
				//当前信息
				int now = pi.versionCode;
				String nown = pi.versionName;
				//之前信息
				int la = sp.getInt("VersionCode", 0);
				String lan = sp.getString("VersionName",getResources().getString(R.string.unknown));
				if (now > la)
				{
					sp.edit().putInt("VersionCode", now).commit();
					sp.edit().putString("VersionName",nown).commit();
					AlertDialog.Builder d = new AlertDialog.Builder(this);
					d.setTitle(R.string.update_success);
					d.setMessage(getResources().getString(R.string.cur_version) + nown + "\n" + getResources().getString(R.string.last_version) + lan + "\n\n" + getResources().getString(R.string.update_log) + "\n" + getFromAssets(this, "change_log.txt"));
					d.setPositiveButton("关闭", null);
					d.show();
				}
			}
			catch (Exception e)
			{}
		}
	}



	private void doWorkSpace()
	{
		startActivity(new Intent(this, WKMain.class));
		//doToast("开发中...",GREEN,null);
	}

	private void doTeach()
	{
		doToast(getResources().getString(R.string.developing), GREEN, null);
	}


	private void doTellme()
	{
		fb.startFeedbackActivity();
	}

	private void doSettings()
	{
		// TODO: Implement this method
		startActivity(new Intent(this, IDEPreference.class));
	}

	public void doHome()
	{
		Intent it = new Intent(IDEMain.this, com.romide.main.ide.IDEGui.class);
		try
		{
			startActivity(it);
		}
		catch (Exception e)
		{
			dialog(IDEMain.this, "异常信息", e.toString());
		}
	}


	public void doMore()
	{
		Intent it = new Intent(IDEMain.this, com.romide.main.ide.IDEMore
							   .class);
		try
		{
			startActivity(it);
		}
		catch (Exception e)
		{
			dialog(IDEMain.this, "异常信息", e.toString());
		}
	}

	public void doPlugin()
	{
		startActivity(new Intent(this, IDEPlugin.class));
	}

	public void doToast(String text, Style s, OnClickListener l)
	{
		Crouton.makeText(IDEMain.this, text, s).setOnClickListener(l).show();
	}

	public void doExit()
	{
		final MaterialDialog d = new MaterialDialog(this);
		d.setTitle(R.string.sure_exit);
		d.setMessage(R.string.exit_msg);
		d.setPositiveButton(getResources().getString(R.string.no), null);
		d.setNegativeButton(getResources().getString(R.string.yes), new View.OnClickListener(){
				@Override
				public void onClick(View c)
				{
					// TODO: Implement this method
					IDEMain.this.finish();
					stopService(new Intent(IDEMain.this, IDEService.class));
				}
			});
		d.show();
	}


	public void doAbout()
	{
		Intent it = new Intent(IDEMain.this, com.romide.main.ide.IDEAbout.class);
		try
		{
			startActivity(it);
		}
		catch (Exception e)
		{
			dialog(IDEMain.this, "异常信息", e.toString());
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// TODO: Implement this metho
		getMenuInflater().inflate(R.menu.ide_menu_main, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		// TODO: Implement this method
		int id = item.getItemId();

		switch (id)
		{
			case R.id.ide_menu_exit: 
				doExit(); 
				break;
		}

		return super.onOptionsItemSelected(item);

	}

	@Override
	protected void onDestroy()
	{
		// TODO: Implement this method
		super.onDestroy();
		stopService(new Intent(this, IDEService.class));
	}





	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		// TODO: Implement this method
		if (keyCode == event.KEYCODE_BACK && event.getAction() == event.ACTION_DOWN)
		{
			doExit();
		}
		return super.onKeyDown(keyCode, event);
	}

}
