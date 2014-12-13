package com.romide.main.ide;

import com.romide.main.ide.utils.*;
import android.app.*;

public final class Const extends Activity
{
	
	//onActivityResult 中的 requestCode
	public static final int UNPACK             			= 1000;
	public static final int REPACK          		    = 1001;
	public static final int PORT               			= 1002;
	public static final int INSTALL         		    = 1003;
	public static final int APKTOOL_D_APK      			= 1004;
	public static final int APKTOOL_B_APK      			= 1005;
	public static final int APKTOOL_D_DEX      			= 1006;
	public static final int APKTOOL_B_DEX      			= 1007;
	public static final int LOCK_ROM                    = 1008;
	public static final int UNLOCK_ROM                  = 1009;
	public static final int OPEN_FILE                   = 1010;
	public static final int DEL_SIGN                    = 1011;
	public static final int GET_SIGN                    = 1012;
	public static final int ZIPALIGN_APP                = 1013;
	public static final int ODEX_APP                    = 1014;
	public static final int DEX2JAR            			= 1015;
	public static final int JAR2DEX 					= 1016;
	public static final int APKTOOL_IF                  = 1017;
	public static final int OTHER                       = 9999;
	
	public static final int WK_CREATE_NEW               = 8000;
	
	public static final int APK_CRACK                   = 2000;
	
	public static final String CPU_MTK     = "MTK";
	public static final String CPU_GT      = "GT";
	public static final String CPU_SC      = "SC";
	public static final String BOOT_OUT    = "/data/ROM-IDE/work/boot";
	
	public static final int MTK_UNPACK     = 0;
	public static final int GT_UNPACK      = 1;
	public static final int SC_UNPACK      = 2;
	
	public static final int CHOOSE_BASE    = 20000;
	public static final int CHOOSE_SAM     = 20001;
	public static final int CHOOSE_INP     = 20002;
	public static final int CHOOSE_OUT     = 20003;
	
	
	public static final String port_method_list_file = "/sdcard/ROM-IDE/tools/config/Port/port_method.list";
}
