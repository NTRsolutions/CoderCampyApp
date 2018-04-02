package com.gmonetix.codercampy.util;

import android.content.Context;
import com.afollestad.materialdialogs.MaterialDialog;

/**
 * Created by Gaurav Bordoloi on 3/31/2018.
 */
public class DialogUtil {

    public static void showTextDialog(Context context, String text) {

        new MaterialDialog.Builder(context)
                .typeface(DesignUtil.getTypeFace(context),DesignUtil.getTypeFace(context))
                .content(text)
                .show();

    }

}
