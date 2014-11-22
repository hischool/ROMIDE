package com.romide.main.ide;

import java.util.Properties;

public class MyProp extends Properties
{

	@Override
	public String getProperty(String name)
	{
		// TODO: Implement this method
		return this.getProperty(name,"");
	}

	@Override
	public String getProperty(String name, String defaultValue)
	{
		// TODO: Implement this method
		String res =  super.getProperty(name, defaultValue);
		String s = "";
		try{
		    s = new String(res.getBytes("ISO-8859-1"),"utf-8");
		}catch (Exception e){
			
		}
		return s;
	}
	
	
}
