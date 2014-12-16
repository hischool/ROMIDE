package com.romide.main.ide.utils;

import android.app.ActionBar;
import android.widget.SpinnerAdapter;

/**
 * Provides ActionBar APIs.
 */
public abstract class ActionBarCompat {
    public static final int NAVIGATION_MODE_STANDARD = 0;
    public static final int NAVIGATION_MODE_LIST = 1;
    public static final int NAVIGATION_MODE_TABS = 2;
    public static final int DISPLAY_USE_LOGO = 1;
    public static final int DISPLAY_SHOW_HOME = 2;
    public static final int DISPLAY_HOME_AS_UP = 4;
    public static final int DISPLAY_SHOW_TITLE = 8;
    public static final int DISPLAY_SHOW_CUSTOM = 16;

    // Provides android.R.id.home from API 11 and up
    public static final int ID_HOME = 0x0102002c;

    public interface OnNavigationListener {
        public abstract boolean onNavigationItemSelected(int position, long id);
    }

    public static ActionBarCompat wrap(Object actionBar) {
        if (actionBar != null) {
            if (AndroidCompat.SDK >= 11) {
                return new ActionBarApi11OrLater(actionBar);
            }
        }
        return null;
    }

    public abstract int getDisplayOptions();
    public abstract int getHeight();
    public abstract int getNavigationItemCount();
    public abstract int getNavigationMode();
    public abstract int getSelectedNavigationIndex();
    public abstract CharSequence getTitle();
    public abstract void hide();
    public abstract boolean isShowing();
    public abstract void setDisplayOptions(int options);
    public abstract void setDisplayOptions(int options, int mask);
    public abstract void setListNavigationCallbacks(SpinnerAdapter adapter, OnNavigationListener callback);
    public abstract void setNavigationMode(int mode);
    public abstract void setSelectedNavigationItem(int position);
    public abstract void setTitle(int resId);
    public abstract void setTitle(CharSequence title);
    public abstract void show();
}

class ActionBarApi11OrLater extends ActionBarCompat {
    private ActionBar bar;

    ActionBarApi11OrLater(Object bar) {
        this.bar = (ActionBar) bar;
    }

    private ActionBar.OnNavigationListener wrapOnNavigationCallback(OnNavigationListener callback) {
        final OnNavigationListener cb = callback;
        return new ActionBar.OnNavigationListener() {
            @Override
			public boolean onNavigationItemSelected(int position, long id) {
                return cb.onNavigationItemSelected(position, id);
            }
        };
    }

    @Override
	public int getDisplayOptions() {
        return bar.getDisplayOptions();
    }

    @Override
	public int getHeight() {
        return bar.getHeight();
    }

    @Override
	public int getNavigationItemCount() {
        return bar.getNavigationItemCount();
    }

    @Override
	public int getNavigationMode() {
        return bar.getNavigationMode();
    }

    @Override
	public int getSelectedNavigationIndex() {
        return bar.getSelectedNavigationIndex();
    }

    @Override
	public CharSequence getTitle() {
        return bar.getTitle();
    }

    @Override
	public void hide() {
        bar.hide();
    }

    @Override
	public boolean isShowing() {
        return bar.isShowing();
    }

    @Override
	public void setDisplayOptions(int options) {
        bar.setDisplayOptions(options);
    }

    @Override
	public void setDisplayOptions(int options, int mask) {
        bar.setDisplayOptions(options, mask);
    }

    @Override
	public void setListNavigationCallbacks(SpinnerAdapter adapter, OnNavigationListener callback) {
        bar.setListNavigationCallbacks(adapter, wrapOnNavigationCallback(callback));
    }

    @Override
	public void setNavigationMode(int mode) {
        bar.setNavigationMode(mode);
    }

    @Override
	public void setSelectedNavigationItem(int position) {
        bar.setSelectedNavigationItem(position);
    }

    @Override
	public void setTitle(int resId) {
        bar.setTitle(resId);
    }

    @Override
	public void setTitle(CharSequence title) {
        bar.setTitle(title);
    }

    @Override
	public void show() {
        bar.show();
    }
}

