package com.gmonetix.codercampy.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.TextView;

import com.gmonetix.codercampy.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by Gaurav Bordoloi on 3/30/2018.
 */

public class DesignUtil {

    public static void applyFontToMenu(Menu menu, Context context) {
        for (int i=0;i<menu.size();i++) {
            MenuItem mi = menu.getItem(i);
            SubMenu subMenu = mi.getSubMenu();
            if (subMenu!=null && subMenu.size() >0 ) {
                for (int j=0; j <subMenu.size();j++) {
                    MenuItem subMenuItem = subMenu.getItem(j);
                    applyFontToMenuItem(subMenuItem,context);
                }
            }
            applyFontToMenuItem(mi,context);
        }
    }

    public static void showIconInMenuItem(Menu menu) {
        Method m = null;
        try {
            m = menu.getClass().getDeclaredMethod(
                    "setOptionalIconsVisible", Boolean.TYPE);
            m.setAccessible(true);
            m.invoke(menu, true);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static Typeface getTypeFace(Context context) {
        return ResourcesCompat.getFont(context,R.font.main_font);
    }

    private static void applyFontToMenuItem(MenuItem mi, Context context) {
        Typeface font = getTypeFace(context);
        SpannableString mNewTitle = new SpannableString(mi.getTitle());
        mNewTitle.setSpan(new CustomTypefaceSpan("" , font), 0 , mNewTitle.length(),  Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        mi.setTitle(mNewTitle);
    }

    public static void setFont(Context _context, TextView textView) {
        Typeface roboto = getTypeFace(_context);
        textView.setTypeface(roboto);
    }

    public static void applyFontForToolbarTitle(Activity activity){
        Toolbar toolbar = (Toolbar) activity.findViewById(R.id.toolbar);
        for(int i = 0; i < toolbar.getChildCount(); i++){
            View view = toolbar.getChildAt(i);
            if(view instanceof TextView){
                TextView tv = (TextView) view;
                if(tv.getText().equals(toolbar.getTitle())){
                    setFont(activity,tv);
                    break;
                }
            }
        }
    }

    public static void applyFontForCollapsingToolbar(Context context, CollapsingToolbarLayout toolbarLayout){
        Typeface roboto = getTypeFace(context);
        toolbarLayout.setExpandedTitleTypeface(roboto);
        toolbarLayout.setCollapsedTitleTypeface(roboto);
    }

    public static void showSnackbar(View view,String message) {
        Snackbar snackbar = Snackbar.make(view,message, Snackbar.LENGTH_SHORT);
        snackbar.show();
    }

    public static boolean isGoogleServicesAvailable(Context context) {
        GoogleApiAvailability api = GoogleApiAvailability.getInstance();
        int code = api.isGooglePlayServicesAvailable(context);
        return code == ConnectionResult.SUCCESS ;
    }

}
