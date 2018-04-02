package com.gmonetix.codercampy.util;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.support.customtabs.CustomTabsIntent;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.gmonetix.codercampy.R;
import com.gmonetix.codercampy.chrome.ChromeTabActionBroadcastReceiver;
import com.gmonetix.codercampy.chrome.CustomTabActivityHelper;

/**
 * Created by Gaurav Bordoloi on 3/28/2018.
 */

public class IntentUtil {

    public static void openCustomChromeTab(Context context, Uri uri) {
        CustomTabsIntent.Builder intentBuilder = new CustomTabsIntent.Builder();

        // set toolbar colors
        intentBuilder.setToolbarColor(ContextCompat.getColor(context, R.color.white));
        intentBuilder.setSecondaryToolbarColor(ContextCompat.getColor(context, R.color.white));

        // add menu items
        intentBuilder.addMenuItem("Share to WhatsApp", createPendingIntent(context,ChromeTabActionBroadcastReceiver.ACTION_MENU_ITEM_WHATSAPP));
        intentBuilder.addMenuItem("Share to Facebook",
                createPendingIntent(context,ChromeTabActionBroadcastReceiver.ACTION_MENU_ITEM_FACEBOOK));

        // set action button
        intentBuilder.setActionButton(BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_share_png), "Share",
                createPendingIntent(context,ChromeTabActionBroadcastReceiver.ACTION_ACTION_BUTTON_SHARE));

        // set start and exit animations
        intentBuilder.setStartAnimations(context, R.anim.slide_in_right, R.anim.slide_out_left);
        intentBuilder.setExitAnimations(context, android.R.anim.slide_in_left,
                android.R.anim.slide_out_right);

        // build custom tabs intent
        CustomTabsIntent customTabsIntent = intentBuilder.build();

        // call helper to open custom tab
        CustomTabActivityHelper.openCustomTab((Activity) context, customTabsIntent, uri, new CustomTabActivityHelper.CustomTabFallback() {
            @Override
            public void openUri(Activity activity, Uri uri) {
                // fall back, call open open webview
                openLink(activity, String.valueOf(uri));
            }
        });
    }

    private static PendingIntent createPendingIntent(Context context, int actionSource) {
        Intent actionIntent = new Intent(context, ChromeTabActionBroadcastReceiver.class);
        actionIntent.putExtra(ChromeTabActionBroadcastReceiver.KEY_ACTION_SOURCE, actionSource);
        return PendingIntent.getBroadcast(context, actionSource, actionIntent, 0);
    }

    public static void whatsAppUs(Context context) {
        Intent sendIntent = new Intent("android.intent.action.MAIN");
        sendIntent.setAction(Intent.ACTION_VIEW);
        sendIntent.setPackage("com.whatsapp");
        String url = "https://api.whatsapp.com/send?phone=" + CoderCampy.OFFICIAL_PHONE + "&text=";
        sendIntent.setData(Uri.parse(url));
        context.startActivity(sendIntent);
    }

    public static void callUs(Context context) {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:+917478870112"));
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(context, "Permission not granted", Toast.LENGTH_SHORT).show();
            //TODO set permission
        } else context.startActivity(callIntent);
    }

    public static Intent openFbPageIntent(Context context) {
        try {
            context.getPackageManager().getPackageInfo("com.facebook.katana", 0);
            return new Intent(Intent.ACTION_VIEW, Uri.parse("fb://page/"+CoderCampy.FACEBOOK_PAGE_ID));
        } catch (Exception e) {
            return new Intent(Intent.ACTION_VIEW,Uri.parse("https://www.facebook.com/"+CoderCampy.FACEBOOK_PAGE_NAME));
        }
    }

    public static void openLink(Context context, String url){
        try {
            Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            context.startActivity(Intent.createChooser(i, "choose one"));
        } catch(Exception e) {
            Log.e("ERROR",""+e.getMessage());
        }
    }

    public static void sendEmail(Context context) {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"contact@codercampy.com"});
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "");
        try {
            context.startActivity(Intent.createChooser(emailIntent, "choose one"));
        } catch (android.content.ActivityNotFoundException ex) {
            Log.e("ERROR","error");
        }
    }

    public static void shareApp(Context context){
        try {
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("text/plain");
            i.putExtra(Intent.EXTRA_SUBJECT, "CoderCampy");
            i.putExtra(Intent.EXTRA_TEXT, "CoderCampy - where code creates magic!\n\nDownload the app now!\n\n" +
                    CoderCampy.PLAYSTORE_LINK);
            context.startActivity(Intent.createChooser(i, "choose one"));
        } catch(Exception e) {
            Log.e("ERROR",""+e.getMessage());
        }
    }

    public static void shareText(Context context, String message){
        try {
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("text/plain");
            i.putExtra(Intent.EXTRA_SUBJECT, "CoderCampy");
            i.putExtra(Intent.EXTRA_TEXT, message);
            context.startActivity(Intent.createChooser(i, "choose one"));
        } catch(Exception e) {
            Log.e("ERROR",""+e.getMessage());
        }
    }

    public static void shareViaFacebook(Context context, String text) {
        try {
            Intent intent1 = new Intent();
            intent1.setPackage("com.facebook.katana");
            intent1.setAction("android.intent.action.SEND");
            intent1.setType("text/plain");
            intent1.putExtra("android.intent.extra.TEXT", text);
            context.startActivity(intent1);
        } catch (Exception e) {
            String sharerUrl = "https://www.facebook.com/sharer/sharer.php?u=" + text;
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(sharerUrl));
            context.startActivity(intent);
        }
    }

    public static void shareViaWhatsApp(Context context, String text) {
        Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
        whatsappIntent.setType("text/plain");
        whatsappIntent.setPackage("com.whatsapp");
        whatsappIntent.setAction("android.intent.action.SEND");
        whatsappIntent.putExtra(Intent.EXTRA_TEXT, text);
        try {
            context.startActivity(whatsappIntent);
        } catch (android.content.ActivityNotFoundException ex) {
            ex.printStackTrace();
            Toast.makeText(context,"WhatsApp not installed on this device !", Toast.LENGTH_SHORT).show();
        }
    }

}
