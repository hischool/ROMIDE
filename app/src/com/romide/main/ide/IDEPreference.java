package com.romide.main.ide;

import android.app.*;
import android.content.*;
import android.net.*;
import android.os.*;
import android.preference.*;
import android.view.*;
import android.widget.*;
import com.romide.main.*;
import com.romide.main.ide.utils.*;


public class IDEPreference extends PreferenceActivity implements Preference.OnPreferenceClickListener
{

	@Override
	public boolean onPreferenceClick(Preference p1)
	{
		// TODO: Implement this method
		String key = p1.getKey();
		if(key.equals(mGotoAppHelloKey)){
			Intent i = new Intent(this,IDEHello.class);
			i.putExtra("need_rule",false);
			startActivity(i);
		}
		if(key.equals(mGotoRomideComKey)){
			openUrl("http://www.romide.com");
		}
		if(key.equals(mGotoRomerzjComKey)){
			openUrl("http://bbs.romerzj.com");
		}
		if(key.equals(mJoinQQGroupKey)){
			if(!joinQQGroup("IvEjvcEGgs50euRu-qTkto_LKFBybDiU")){
				Toast.makeText(this,"未安装QQ或者版本太低，无法打开",Toast.LENGTH_SHORT).show();
			}
		}
		return false;
	}

	private void openUrl(String url)
	{
		try
		{
			// TODO: Implement this method
			Intent it = new Intent(Intent.ACTION_VIEW, Uri.parse(url)); 
			it.setClassName("com.android.browser", "com.android.browser.BrowserActivity"); 
			startActivity(it);
		}
		catch (Exception e)
		{
			Toast.makeText(this, "未安装浏览器！无法打开", 0).show();
		}
	}
	
	
	/****************
	 *
	 * 发起添加群流程。群号：ROM IDE(342742792) 的 key 为： IvEjvcEGgs50euRu-qTkto_LKFBybDiU
	 * 调用 joinQQGroup(IvEjvcEGgs50euRu-qTkto_LKFBybDiU) 即可发起手Q客户端申请加群 ROM IDE(342742792)
	 *
	 * @param key 由官网生成的key
	 * @return 返回true表示呼起手Q成功，返回fals表示呼起失败
	 ******************/
	public boolean joinQQGroup(String key) {
		Intent intent = new Intent();
		intent.setData(Uri.parse("mqqopensdkapi://bizAgent/qm/qr?url=http%3A%2F%2Fqm.qq.com%2Fcgi-bin%2Fqm%2Fqr%3Ffrom%3Dapp%26p%3Dandroid%26k%3D" + key));
		// 此Flag可根据具体产品需要自定义，如设置，则在加群界面按返回，返回手Q主界面，不设置，按返回会返回到呼起产品界面    //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
		try {
			startActivity(intent);
			return true;
		} catch (Exception e) {
			// 未安装手Q或安装的版本不支持
			return false;
		}
	}
	

	
	private Preference mGotoAppHello;
	private String mGotoAppHelloKey = "goto_hello";

	private Preference mGotoRomideCom;
	private String mGotoRomideComKey = "goto_romide_com";
	
	private Preference mGotoRomerzjCom;
	private String mGotoRomerzjComKey = "goto_romerzj_com";

	
	private Preference mJoinQQGroup;
	private String mJoinQQGroupKey = "join_qq_group";
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO: Implement this method
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.ide_preference);
		
		Actionbar();
		mGotoAppHello = findPreference(mGotoAppHelloKey);
		mGotoAppHello.setOnPreferenceClickListener(this);
		
		mJoinQQGroup = findPreference(mJoinQQGroupKey);
		mJoinQQGroup.setOnPreferenceClickListener(this);
		
		mGotoRomideCom = findPreference(mGotoRomideComKey);
		mGotoRomideCom.setOnPreferenceClickListener(this);
		
		mGotoRomerzjCom = findPreference(mGotoRomerzjComKey);
		mGotoRomerzjCom.setOnPreferenceClickListener(this);
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
