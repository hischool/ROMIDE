package com.romide.main.ide;

import android.os.*;
import android.view.*;
import android.webkit.*;
import com.romide.main.*;
import com.romide.main.ide.utils.*;


public class IDEAbout extends BaseActivity
{
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		// TODO: Implement this method
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ide_about);
		
		WebView web = (WebView)findViewById(R.id.ide_aboutWebView);
		web.getSettings().setJavaScriptEnabled(true);
		web.loadUrl("http://zt515.github.io/ROMIDE");
		
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
}
