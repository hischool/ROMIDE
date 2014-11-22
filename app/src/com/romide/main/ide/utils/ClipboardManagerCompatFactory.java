package com.romide.main.ide.utils;

import android.content.Context;

public class ClipboardManagerCompatFactory {

    private ClipboardManagerCompatFactory() {
        /* singleton */
    }

    public static ClipboardManagerCompat getManager(Context context) {
        if (AndroidCompat.SDK < 11) {
            return new ClipboardManagerCompatV1(context);
        } else {
            return new ClipboardManagerCompatV11(context);
        }
    }
}
