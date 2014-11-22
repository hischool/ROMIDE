package com.romide.main.ide;

import android.app.*;
import android.content.*;
import android.os.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;
import com.romide.main.*;
import com.romide.main.ide.utils.*;
//import de.keyboardsurfer.android.widget.crouton.*;
import java.io.*;
import me.drakeet.materialdialog.*;

import com.romide.main.R;
import java.lang.Process;
import com.romide.plugin.widget.crouton.*;

public class IDEInstall extends BaseActivity implements OnClickListener
{

	private Button mInstall;


	//private String sdcard = Environment.getExternalStorageDirectory() +"/";
	private static final int CLOSE_DIALOG = 0;
	private static final int SHOW_DIALOG = 1;
	private static final int ERROR = -1;
	private static final String ENVZIP = "system.zip";

	
	private static final Style RED = Style.ALERT;
	private static final Style GREEN = Style.CONFIRM;
	private static final Style BLUE = Style.INFO;
	public void doToast(String text,Style s,OnClickListener l){
		Crouton.makeText(IDEInstall.this,text,s).setOnClickListener(l).show();
	}
	
	
	public Handler mInstallHandler = new Handler() {
        @Override
        public void handleMessage(Message msg)
		{
            if (msg.what == CLOSE_DIALOG)
			{
                mProgressDialog.dismiss();
            }
			else if (msg.what == SHOW_DIALOG)
			{
				if(new File(IDEGui.sdcard+"ROM-IDE/.installed").exists()||new File(IDEInstall.this.getFilesDir().getAbsolutePath()+File.separator+"ROM-IDE/.installed").exists()){
				    doToast("安装成功！请重启 ROMIDE", BLUE, new OnClickListener(){

						@Override
						public void onClick(View p1)
						{
							// TODO: Implement this method
							IDEInstall.this.finish();
						}
					});
				}else{
					doToast("安装失败！", BLUE, null);
				}
					
			}
			else if (msg.what == ERROR){
				doToast("安装出现问题！"+errMsg,RED,null);
			}
        }
    };

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		// TODO: Implement this method
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ide_install);

		init();
		
	}

    ProgressDialog mProgressDialog;
    public void ProgressDialog(int style, String Message)
	{
    	mProgressDialog = new ProgressDialog(this);
    	//mProgressDialog.setTitle("提示");
    	mProgressDialog.setMessage(Message);
    	mProgressDialog.setCancelable(false);
    	mProgressDialog.show();
    }

	public void init()
	{
		mInstall = (Button) findViewById(R.id.install);
		mInstall.setOnClickListener(this);
		
		
		Actionbar();
	}

	public void onClick(View v)
	{
		int id = v.getId();

		switch (id)
		{
			case R.id.install:
				new AlertDialog.Builder(this)
				    .setTitle("选择安装方式")
					.setMessage("为什么会有两种安装方式？\n因为部分设备上无法安装至SD卡，并且无法挂载SD可执行权限(可能使用的是内置)，所以我们推出2种安装方式选择\n请您放心，无论任何一种方法都不会破坏您的设备，不过这需要root权限\n如果你要安装在SD卡，请进入 设置--存储 更改默认存储设备为SD卡然后重启手机再次打开本应用安装\n\n注意：如果SD卡安装失败，请选择安装至DATA")
					.setPositiveButton("安装到 SD", new DialogInterface.OnClickListener(){

						@Override
						public void onClick(DialogInterface p1,int p2)
						{
							// TODO: Implement this method
							doInstallSd();
						}
					})
					.setNegativeButton("安装到 DATA", new DialogInterface.OnClickListener(){

						@Override
						public void onClick(DialogInterface p1,int pw)
						{
							// TODO: Implement this method
							doInstallData();
						}
					})
					.show();
				break;
		}
	}

	Message msg = mInstallHandler.obtainMessage();
    String errMsg = "无错误";
	private void doInstallSd()
	{
		// TODO: Implement this method
		ProgressDialog(0,"正在安装");
		new Thread(){
		    public void run()
			{

				try
				{
					Process pp;

					String busybox = CopyFile(IDEInstall.this, "busybox");
					String system = CopyFile(IDEInstall.this, ENVZIP);
					String shell = CopyFile(IDEInstall.this, "install.sh");

					pp = Runtime.getRuntime().exec("su");
					DataOutputStream os = new DataOutputStream(pp.getOutputStream());
					
				    os.write(("chmod 777 " + busybox + " && " + "chmod 777 " + shell + " && " + shell  + " " + busybox + " " + system ).getBytes());
					
					os.flush();
					os.close();
					
					pp.waitFor();
					
					mProgressDialog.dismiss();
					msg.what = SHOW_DIALOG;
					msg.sendToTarget();
				}
				catch (Exception e)
				{
					errMsg = e.toString();
					msg.what = ERROR;
					msg.sendToTarget();
				}
		    }
		}.start();
	}
	
	
	private void doInstallData()
	{
		// TODO: Implement this method
		ProgressDialog(0,"正在安装");
		new Thread(){
		    public void run()
			{

				try
				{
					Process pp;

					String busybox = CopyFile(IDEInstall.this, "busybox");
					String system = CopyFile(IDEInstall.this, ENVZIP);
					String shell = CopyFile(IDEInstall.this, "install.data.sh");

					pp = Runtime.getRuntime().exec("su");
					DataOutputStream os = new DataOutputStream(pp.getOutputStream());

				    os.write(("chmod 777 " + busybox + " && " + "chmod 777 " + shell + " && " + shell + " " + IDEInstall.this.getFilesDir().getAbsolutePath()  + " " + busybox + " " + system ).getBytes());

					os.flush();
					os.close();

					pp.waitFor();
					mProgressDialog.dismiss();
					msg.what = SHOW_DIALOG;
					msg.sendToTarget();
				}
				catch (Exception e)
				{
					errMsg = e.toString();
					msg.what = ERROR;
					msg.sendToTarget();
				}
		    }
		}.start();
	}
	

	private void doShowChangeLog()
	{
		// TODO: Implement this method
		MaterialDialog d = new MaterialDialog(IDEInstall.this);
		    d.setTitle("更新日志");
			d.setMessage(getFromAssets(IDEInstall.this, "change_log.txt"));
			d.setPositiveButton("关闭", null);
			d.show();
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
	
}
