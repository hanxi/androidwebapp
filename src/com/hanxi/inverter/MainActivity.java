package com.hanxi.inverter;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.view.Gravity;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

public class MainActivity extends Activity {
    static private WebView m_webView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //web layout
        //m_webLayout = new LinearLayout(this);
        //LinearLayout.LayoutParams lytp = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.FILL_PARENT);
        //this.setContentView(m_webLayout, lytp);
        //m_webLayout.setOrientation(LinearLayout.VERTICAL);

        m_webView = new WebView(this);
        this.setContentView(m_webView);
        m_webView.getSettings().setJavaScriptEnabled(true);
        m_webView.getSettings().setSupportZoom(false);
        //m_webLayout.addView(m_webView);
        m_webView.getSettings().setAppCacheEnabled(true);

        m_webView.loadUrl("file:///android_asset/game.html");
    }
}
