package com.gmonetix.codercampy.chrome;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.gmonetix.codercampy.util.IntentUtil;

import es.dmoral.toasty.Toasty;

/**
 * Action Broadcast receiver for the custom chrome tab
 *
 * Created by segun.famisa on 04/06/2016.
 */

public class ChromeTabActionBroadcastReceiver extends BroadcastReceiver {
    public static final String KEY_ACTION_SOURCE = "org.chromium.customtabsdemos.ACTION_SOURCE";

    public static final int ACTION_MENU_ITEM_WHATSAPP = 1;
    public static final int ACTION_MENU_ITEM_FACEBOOK = 2;
    public static final int ACTION_ACTION_BUTTON_SHARE = 3;


    @Override
    public void onReceive(Context context, Intent intent) {
        String data = intent.getDataString();

        if (data != null) {
            doWork(context, intent.getIntExtra(KEY_ACTION_SOURCE, -1), data);
        }

    }


    private void doWork(Context context, int actionSource, String message) {
        switch (actionSource) {
            case ACTION_MENU_ITEM_WHATSAPP:
                IntentUtil.shareViaWhatsApp(context,message);
                break;
            case ACTION_MENU_ITEM_FACEBOOK:
                IntentUtil.shareViaFacebook(context,message);
                break;
            case ACTION_ACTION_BUTTON_SHARE:
                IntentUtil.shareText(context,message);
                break;
            default:
                break;
        }
    }
}
