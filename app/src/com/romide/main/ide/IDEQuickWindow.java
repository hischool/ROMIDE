package com.romide.main.ide;

import android.app.*;
import android.content.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import com.romide.main.*;
import java.util.*;
import wei.mark.standout.*;
import wei.mark.standout.ui.*;

import com.romide.main.R;
import wei.mark.standout.ui.Window;

public class IDEQuickWindow extends IDEWindow
{

	@Override
	public String getAppName()
	{
		// TODO: Implement this method
		return "ROM-IDE 快捷窗口";
	}

	@Override
	public int getAppIcon()
	{
		// TODO: Implement this method
		return R.drawable.ic_ide;
	}

	@Override
	public String getHiddenNotificationTitle(int id)
	{
		// TODO: Implement this method
		return "ROM-IDE 快捷窗口已隐藏";
	}
	
	
	@Override
	public String getPersistentNotificationTitle(int id)
	{
		// TODO: Implement this method
		return "ROM-IDE 快捷窗口";
	}

	@Override
	public String getPersistentNotificationMessage(int id)
	{
		// TODO: Implement this method
		return "点击打开 ROM-IDE";
	}

	@Override
	public Intent getPersistentNotificationIntent(int id)
	{
		// TODO: Implement this method
		return new Intent(this,com.romide.main.ide.IDESplash.class);
	}


	
	@Override
	public void createAndAttachView(int id, FrameLayout frame)
	{
		// TODO: Implement this method
		LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.ide_quick_window, frame, true);
		Bundle data = new Bundle();
		data.putString("notify","notify");
		sendData(id, IDEQuickWindow.class, DEFAULT_ID, 0,data);
	}

	

	@Override
	public List<DropDownListItem> getDropDownItems(int id) {
		List<DropDownListItem> items = new ArrayList<DropDownListItem>();
		items.add(new DropDownListItem(android.R.drawable.ic_menu_help, "帮助", new Runnable(){
						  @Override
						  public void run()
						  {
							  // TODO: Implement this method
							  Toast.makeText(IDEQuickWindow.this,"这个创建可以在您进行操作的时候查看一些教程(比如移植教程)",0).show();
						  }
					  }));
		return items;
	}
	
	

	
	@Override
	public StandOutWindow.StandOutLayoutParams getParams(int id, Window window)
	{
		return new StandOutLayoutParams(id, 300, 500,
										StandOutLayoutParams.CENTER, StandOutLayoutParams.CENTER);
	}

}
