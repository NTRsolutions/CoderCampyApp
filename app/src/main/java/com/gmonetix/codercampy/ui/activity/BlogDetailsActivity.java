package com.gmonetix.codercampy.ui.activity;

import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.gmonetix.codercampy.R;
import com.gmonetix.codercampy.dialog.BlogInfoDialog;
import com.gmonetix.codercampy.dialog.RatingDialog;
import com.gmonetix.codercampy.model.Blog;
import com.gmonetix.codercampy.ui.fragment.DiscussionPanel;
import com.gmonetix.codercampy.util.DesignUtil;
import com.gmonetix.codercampy.util.Device;
import com.gmonetix.codercampy.util.NestedWebView;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BlogDetailsActivity extends AppCompatActivity {

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.collapse_toolbar) CollapsingToolbarLayout collapsingToolbarLayout;
    @BindView(R.id.webview) NestedWebView webView;
    @BindView(R.id.imageView) ImageView imageView;

    private Blog blog;

    private DiscussionPanel discussionPanel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog_details);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        DesignUtil.applyFontForToolbarTitle(this);
        DesignUtil.applyFontForCollapsingToolbar(this,collapsingToolbarLayout);
        collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(R.color.colorPrimary));
        collapsingToolbarLayout.setCollapsedTitleTextColor(getResources().getColor(R.color.colorPrimary));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        blog = (Blog) getIntent().getSerializableExtra("blog");

        this.setTitle(blog.name);
        Glide.with(this).load(blog.image).into(imageView);

        String pish = "<html><head><style type=\"text/css\">@font-face {font-family: MyFont;src: url(\"file:///android_asset/open_sans_regular.ttf\")}body {font-family: MyFont;font-size: medium;text-align: justify;}</style></head><body>";
        String pas = "</body></html>";
        String myHtmlString = pish + blog.data + pas;

        webView.loadDataWithBaseURL(null,myHtmlString, "text/html", "UTF-8", null);
        discussionPanel = DiscussionPanel.newInstance(blog.id,false);

        AdRequest request = new AdRequest.Builder()
                .addTestDevice(Device.getId(this))  // An example device ID
                .build();

        AdView adView = (AdView) findViewById(R.id.ad);
        adView.loadAd(request);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.blog_details_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.menu_info:
                new BlogInfoDialog(this,blog.name,blog.instructor, blog.time, blog.category, blog.languages).show();
                break;

            case R.id.menu_rating:
                new RatingDialog(this,blog.id,false).show();
                break;

            case R.id.menu_discussions:
                discussionPanel.show(getSupportFragmentManager(),"TAG");
                break;

        }
        return super.onOptionsItemSelected(item);
    }

}
