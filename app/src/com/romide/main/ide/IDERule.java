package com.romide.main.ide;

import android.app.*;
import android.content.*;
import android.os.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;
import com.romide.main.*;
import com.romide.plugin.widget.crouton.*;
//import de.keyboardsurfer.android.widget.crouton.*;

public class IDERule extends Activity
{

	private TextView tv;
	private CheckBox agree;
	private Button btn;
	
	
	@Override
    public void onCreate(Bundle savedInstanceState)
	{
		// TODO: Implement this method
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.ide_rule);
		
		tv = (TextView)findViewById(R.id.rule_tv);
		agree = (CheckBox) findViewById(R.id.rule_agree);
		btn = (Button) findViewById(R.id.rule_next);
		
		doSetRules();
		btn.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View p1)
				{
					// TODO: Implement this method
					if(!agree.isChecked()){
						doToast("请先同意条约!",RED,null);
						return;
					}
					Intent i = new Intent(IDERule.this,IDEMain.class);
					startActivity(i);
					finish();
				}
			});
	}

	private void doSetRules()
	{
		// TODO: Implement this method
		String rule ="ROM IDE 使用须知\n"+
		             "  1.本软件需要ROOT权限，使用过程中注意授权\n"+
					 "  2.本软件是在手机端开发ROM，所以稳定性肯定不如电脑，请勿对我们进行恶意的言语攻击\n"+
					 "  3.本软件需要大约 20MB 的系统空间用于安装必备环境，并且大部分工作都在系统空间进行，请保证存储空间足够\n"+
					 "  4.如果您想让数据包全部存在于Sdcard，您必须在存储中更改默认存储为SD卡\n"+
					 "  5.本软件使用过程中造成的问题需要您自行承担\n"+
					 "\n";
		tv.setText(rule);
	}
	
	private static final Style RED = Style.ALERT;
	private static final Style GREEN = Style.CONFIRM;
	private static final Style BLUE = Style.INFO;
	public void doToast(String text, Style s, OnClickListener l)
	{
		Crouton.makeText(IDERule.this, text, s).setOnClickListener(l).show();
	}
	
}
