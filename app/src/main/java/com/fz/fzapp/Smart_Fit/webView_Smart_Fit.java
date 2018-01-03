package com.fz.fzapp.Smart_Fit;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.fz.fzapp.R;

/**
 * Created by Heru Permana on 12/13/2017.
 */

public class webView_Smart_Fit extends AppCompatActivity {
    WebView webview;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview_smart_fit);

        Bundle bundle = getIntent().getExtras();
        String webUrl = bundle.getString("webUrl");
        //webview use to call own site
        webview = (WebView) findViewById(R.id.webView);

        webview.setWebViewClient(new WebViewClient());
        webview.getSettings().setJavaScriptEnabled(true);
        webview.getSettings().setDomStorageEnabled(true);
        webview.loadUrl(webUrl);
    }
}
