package com.romide.main.ide;

import android.app.*;
import android.content.*;
import android.graphics.*;
import android.os.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;
import at.markushi.ui.*;
import com.romide.main.*;

import com.romide.main.R;

public class IDEHello extends Activity implements OnClickListener
{

	private RevealColorView colorView;
	private Button ui;
	private Button ac;
	private Button se;
	private Button co;
	private Button next;

	private TextView tv;

	private View my;
	private int backgroundColor;

	private boolean need_rule;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO: Implement this method
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.ide_hello);

		init();
	}

	private void init()
	{
		// TODO: Implement this method
		colorView = (RevealColorView) findViewById(R.id.colorView);

		ui = (Button) findViewById(R.id.b1);
		ac = (Button) findViewById(R.id.b2);
		se = (Button) findViewById(R.id.b3);
		co = (Button) findViewById(R.id.b4);
		next = (Button) findViewById(R.id.next);
		tv = (TextView) findViewById(R.id.text);

		backgroundColor = Color.parseColor("#212121");

		need_rule = getIntent().getBooleanExtra("need_rule", true);

		ui.setOnClickListener(this);
		ac.setOnClickListener(this);
		se.setOnClickListener(this);
		co.setOnClickListener(this);
		next.setOnClickListener(this);
	}

	@Override
	public void onClick(View v)
	{
		final int color = getColor(v);
		final Point p = getLocationInView(colorView, v);
		if (my == v)
		{
			colorView.hide(p.x, p.y, backgroundColor, 0, 300, null);
			my = null;
		}
		else
		{
			colorView.reveal(p.x, p.y, color, v.getHeight() / 2, 1000, null);
			my = v;
		}
		if (v == ui)
		{
			tv.setText("ROM-IDE 以简洁强大为设计中心\n平凡的UI下藏着一颗强大的心\n在使用ROM-IDE的过程中，你会发现一切是那么的简单\n");
		}
		if (v == ac)
		{
			tv.setText("ROM-IDE 是一个强大的ROM制作工具！\n包含了日常常用的功能。您还可以在意见反馈中告诉我们，你想要什么功能！你能想到的，我们都有！");
		}
		if (v == se)
		{
			tv.setText("ROM-IDE 拥有一套完善的服务体系\n从在线更新到功能使用，都包含了我们对它的定义：\n\n让DIY安卓更加容易，让成为大神不再困难！");
		}
		if (v == co)
		{
			tv.setText("ROM-IDE 有我们精心设计的SHELL底层，核心无人能敌！\n称之为ROM脚本之王也毫不为过！\n更新效率高，漏洞修复快，拓展性极强，堪称完美！");
		}
		if (v == next)
		{
			if (need_rule)
			    startActivity(new Intent(this, IDERule.class));
			else
				finish();
			finish();
		}
	}




	private Point getLocationInView(View src, View target)
	{
		final int[] l0 = new int[2];
		src.getLocationOnScreen(l0);
		final int[] l1 = new int[2];
		target.getLocationOnScreen(l1);

		l1[0] = l1[0] - l0[0] + target.getWidth() / 2;
		l1[1] = l1[1] - l0[1] + target.getHeight() / 2;

		return new Point(l1[0], l1[1]);
	}

	private int getColor(View view)
	{
		return Color.parseColor((String) view.getTag());
	}

}
