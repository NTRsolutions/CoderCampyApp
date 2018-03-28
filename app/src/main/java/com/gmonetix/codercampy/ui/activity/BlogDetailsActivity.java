package com.gmonetix.codercampy.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.webkit.WebView;

import com.gmonetix.codercampy.R;
import com.gmonetix.codercampy.model.Blog;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BlogDetailsActivity extends AppCompatActivity {

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.webview) WebView webView;

    private Blog blog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog_details);
        ButterKnife.bind(this);

        blog = (Blog) getIntent().getSerializableExtra("blog");

        webView.loadData(blog.data,"text/html","UTF-8");

    }
}
