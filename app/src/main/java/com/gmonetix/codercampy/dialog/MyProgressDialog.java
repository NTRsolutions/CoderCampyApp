package com.gmonetix.codercampy.dialog;

import android.content.Context;

import com.gmonetix.codercampy.R;

/**
 * Created by Gaurav Bordoloi on 2/19/2018.
 */

public class MyProgressDialog extends android.app.ProgressDialog {

    public MyProgressDialog(Context context, String text) {
        super(context, R.style.AppTheme_Dark_Dialog);
        setIndeterminate(true);
        setCancelable(false);
        setCanceledOnTouchOutside(false);
        setMessage(text);
    }

}
