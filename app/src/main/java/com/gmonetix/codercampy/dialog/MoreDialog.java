package com.gmonetix.codercampy.dialog;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialog;
import android.view.Window;
import com.gmonetix.codercampy.R;
import com.gmonetix.codercampy.util.CoderCampy;
import com.gmonetix.codercampy.util.IntentUtil;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Gaurav Bordoloi on 2/25/2018.
 */

public class MoreDialog extends AppCompatDialog {

    private Context context;

    public MoreDialog(Context context) {
        super(context,R.style.Theme_AppCompat_Dialog);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_more);
        ButterKnife.bind(this);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    @OnClick(R.id.close_dialog)
    void closeDialog() {
        dismiss();
    }

    @OnClick(R.id.github)
    void openGithub() {
        IntentUtil.openCustomChromeTab(context,Uri.parse(CoderCampy.GITHUB));
    }

    @OnClick(R.id.fb_page)
    void openFbPage() {
        getContext().startActivity(IntentUtil.openFbPageIntent(getContext()));
    }

    @OnClick(R.id.youtube_channel)
    void openYoutubeChannel() {
        IntentUtil.openLink(getContext(), CoderCampy.YOUTUBE_PAGE);
    }

    @OnClick(R.id.email_us)
    void emailUs() {
        IntentUtil.sendEmail(getContext());
    }

    @OnClick(R.id.call_us)
    void callUs() {
        IntentUtil.callUs(getContext());
    }

    @OnClick(R.id.whatsapp_us)
    void whatsAppUs() {
        IntentUtil.whatsAppUs(getContext());
    }

    @OnClick(R.id.rate_us)
    void rateUs() {
        IntentUtil.openLink(getContext(), CoderCampy.PLAYSTORE_LINK);
    }

    @OnClick(R.id.share_app)
    void shareApp() {
        IntentUtil.shareApp(getContext());
    }

    @OnClick(R.id.donate_us)
    void donateUs() {

    }

}
