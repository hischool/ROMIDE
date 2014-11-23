#include <jni.h>

jint
Java_com_romide_main_ide_Utils_isTrulyApkReal( JNIEnv* env,jobject thiz,jint code )
{
	const jint key = -1923360755;
	
	if(code==key)
        return 0;
	else
		return 1;
}
