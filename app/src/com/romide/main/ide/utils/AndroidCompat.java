package com.romide.main.ide.utils;


public class AndroidCompat {
    public final static int SDK = getSDK();

    private final static int getSDK() {
        int result;
        try {
            result = AndroidLevel4PlusCompat.getSDKInt();
        } catch (VerifyError e) {
            // We must be at an SDK level less than 4.
            try {
                result = Integer.valueOf(android.os.Build.VERSION.SDK);
            } catch (NumberFormatException e2) {
                // Couldn't parse string, assume the worst.
                result = 1;
            }
        }
        return result;
    }
}

class AndroidLevel4PlusCompat {
    static int getSDKInt() {
        return android.os.Build.VERSION.SDK_INT;
    }
}
