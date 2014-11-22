package com.romide.main.ide;

import android.content.*;
import android.view.animation.*;
import android.widget.*;
import java.util.*;
import wei.mark.standout.*;
import wei.mark.standout.constants.*;
import wei.mark.standout.ui.*;

public class IDEWindow extends StandOutWindow
{

	@Override
	public String getAppName()
	{
		// TODO: Implement this method
		return "IDE Window";
	}

	@Override
	public int getAppIcon()
	{
		// TODO: Implement this method
		return R.drawable.ic_ide;
	}

	@Override
	public void createAndAttachView(int id, FrameLayout frame)
	{
		// TODO: Implement this method
	}

	@Override
	public StandOutWindow.StandOutLayoutParams getParams(int id, Window window)
	{
		return new StandOutLayoutParams(id, 300, 500,
										StandOutLayoutParams.CENTER, StandOutLayoutParams.CENTER);
	}

	
	@Override
	public int getFlags(int id) {
		return StandOutFlags.FLAG_DECORATION_SYSTEM
			| StandOutFlags.FLAG_BODY_MOVE_ENABLE
			| StandOutFlags.FLAG_WINDOW_HIDE_ENABLE
			| StandOutFlags.FLAG_WINDOW_BRING_TO_FRONT_ON_TAP
			| StandOutFlags.FLAG_WINDOW_EDGE_LIMITS_ENABLE
			| StandOutFlags.FLAG_WINDOW_PINCH_RESIZE_ENABLE;
	}


	@Override
	public Animation getShowAnimation(int id) {
		if (isExistingId(id)) {
			// restore
			return AnimationUtils.loadAnimation(this,
												android.R.anim.slide_in_left);
		} else {
			// show
			return super.getShowAnimation(id);
		}
	}

	@Override
	public Animation getHideAnimation(int id) {
		return AnimationUtils.loadAnimation(this,
											android.R.anim.slide_out_right);
	}
	
	
	@Override
	public Intent getHiddenNotificationIntent(int id) {
		return StandOutWindow.getShowIntent(this, getClass(), id);
	}

	@Override
	public String getHiddenNotificationTitle(int id)
	{
		// TODO: Implement this method
		return "窗口已隐藏";
	}

	@Override
	public String getHiddenNotificationMessage(int id)
	{
		// TODO: Implement this method
		return "点击重新显示";
	}
	
	
	@Override
	public List<DropDownListItem> getDropDownItems(int id) {
		List<DropDownListItem> items = new ArrayList<DropDownListItem>();
		
		return items;
	}
	
	
}
