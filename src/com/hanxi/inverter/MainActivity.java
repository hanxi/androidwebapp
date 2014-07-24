package com.hanxi.inverter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;

import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Toast;
import android.widget.TextView;
import android.widget.ImageButton;
import android.widget.Button;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.Gravity;
import android.view.KeyEvent;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Log;
import android.text.Html;
import android.graphics.Bitmap;
import android.graphics.Canvas;

import java.io.File;
import java.io.FileOutputStream;
import java.util.LinkedList;
import java.util.List;

import com.wandoujia.ads.sdk.Ads;
import com.wandoujia.ads.sdk.loader.Fetcher;

public class MainActivity extends Activity {
    private static Activity s_instance;
    private static WebView m_webView;
    private static View m_welcomeView;

    private static final String ADS_APP_ID = "100010445";
    private static final String ADS_SECRET_KEY = "58c46df5e72a23e02b0676232808033e";
    private static final String TAG_LIST = "a0d159b85e4bd454d9acf1a752e97cba";
    private static final String TAG_INTERSTITIAL_FULLSCREEN = "f58af9d12c5d74edb65ea39fc74722ff";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        s_instance = this;
        SysApplication.getInstance().addActivity(this);

        m_webView = new WebView(this);
        m_webView.getSettings().setJavaScriptEnabled(true);
        m_webView.getSettings().setSupportZoom(false);
        m_webView.getSettings().setAppCacheEnabled(true);

        m_webView.loadUrl("file:///android_asset/game.html");
        //m_webView.loadUrl("http://192.168.16.60:8000/game.html");

        m_webView.addJavascriptInterface(new Object() {
            public void showAdsAppWall() {
                s_instance.runOnUiThread(new Runnable() {
                    public void run() {
                        Ads.showAppWall(s_instance, TAG_LIST);
                    }
                });
            }
            public void showAdsFull(final String html) {
                s_instance.runOnUiThread(new Runnable() {
                    public void run() {
                        final Dialog dialog = new Dialog(s_instance,R.style.NoBoundDialog);
                        LinearLayout layout = new LinearLayout(s_instance);
                        layout.setOrientation(LinearLayout.VERTICAL);

                        View adsView = Ads.showAppWidget(s_instance, null, TAG_INTERSTITIAL_FULLSCREEN, Ads.ShowMode.WIDGET);

                        ImageButton btn = (ImageButton)adsView.findViewById(R.id.app_widget_close_button);
                        btn.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.dismiss();
                            }
                        });
                        layout.addView(adsView);

                        TextView tv = new TextView(s_instance);
                        tv.setText(Html.fromHtml(html));
                        tv.setPadding(16,0,16,0);
                        layout.addView(tv);

                        LayoutParams lytp = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
                        dialog.setContentView(layout,lytp);
                        dialog.show();

                    }
                });
            }
            public void share (final String title, final String txt, final String dialogTitle) {
                s_instance.runOnUiThread(new Runnable() {
                    public void run() {
                        String imgName = "share.png";
                        try {
                            Bitmap bitmap = Bitmap.createBitmap(m_webView.getWidth(),
                                                m_webView.getHeight(),
                                                Bitmap.Config.ARGB_8888);
                            Canvas canvas = new Canvas();
                            canvas.setBitmap(bitmap);
                            m_webView.draw(canvas);

                            File f = new File(s_instance.getFilesDir(), imgName);
                            FileOutputStream fos = new FileOutputStream(f);
                            bitmap.compress(Bitmap.CompressFormat.PNG, 90, fos);
                            fos.close();
                        } catch (Exception e) {
                            Toast.makeText(MainActivity.this, "截图失败，请邮件联系:hanxi.info@gmail.com", Toast.LENGTH_LONG).show();
                        }

                        String fname = s_instance.getFilesDir()+"/"+imgName;
                        try {
                            Runtime.getRuntime().exec("chmod 777 " + fname);
                        } catch (Exception e) {}

                        String filePath = "file:///"+fname;
                        Intent intent = new Intent("android.intent.action.SEND");
                        intent.setType("image/*");
                        intent.putExtra(Intent.EXTRA_SUBJECT, title);
                        intent.putExtra(Intent.EXTRA_TEXT, txt);
                        intent.putExtra(Intent.EXTRA_STREAM,Uri.parse(filePath));
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        s_instance.startActivity(Intent.createChooser(intent, dialogTitle));
                    }
                });
            }

            public void setCookie(String cookie) {
                SharedPreferences sp = s_instance.getSharedPreferences("SP", MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.putString("cookie", cookie);
                editor.commit();
            }

            public String getCookie() {
                SharedPreferences sp = s_instance.getSharedPreferences("SP", MODE_PRIVATE);
                //Toast.makeText(MainActivity.this, sp.getString("cookie","no cookie"), Toast.LENGTH_LONG).show();
                return sp.getString("cookie", "");
            }

        }, "android");

        m_welcomeView = (View)View.inflate(this, R.layout.welcome, null);
        this.setContentView(m_webView);
        LayoutParams lp = new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.FILL_PARENT);
        this.addContentView(m_welcomeView,lp);
        //使用pastDelayed方法延时
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //结束欢迎界面
                m_welcomeView.setVisibility(View.GONE);
                Ads.preLoad(s_instance, Fetcher.AdFormat.appwall, TAG_LIST);
                Ads.preLoad(s_instance, Fetcher.AdFormat.interstitial,TAG_INTERSTITIAL_FULLSCREEN);
            }
        }, 1000);

        try {
            Ads.init(this, ADS_APP_ID, ADS_SECRET_KEY);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showAdsFull(final String html) {
   }

    public void showExitDialog() {
        final Dialog dialog = new Dialog(s_instance,R.style.NoBoundDialog);
        LinearLayout layout = new LinearLayout(s_instance);
        layout.setOrientation(LinearLayout.VERTICAL);

        View adsView = Ads.showAppWidget(s_instance, null, TAG_INTERSTITIAL_FULLSCREEN, Ads.ShowMode.WIDGET);

        ImageButton btn = (ImageButton)adsView.findViewById(R.id.app_widget_close_button);
        btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        layout.addView(adsView);

        Button exitButtn = new Button(this);
        exitButtn.setText("退出游戏");
        exitButtn.setTextSize(20);
        exitButtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                SysApplication.getInstance().exit();
            }
        });
        layout.addView(exitButtn);

        LayoutParams lytp = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
        dialog.setContentView(layout,lytp);
        dialog.show();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            showExitDialog();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}

class SysApplication extends Application {
    private List<Activity> mList = new LinkedList<Activity>();
    private static SysApplication instance;

    private SysApplication() {
    }
    public synchronized static SysApplication getInstance() {
        if (null == instance) {
            instance = new SysApplication();
        }
        return instance;
    }

    // add Activity
    public void addActivity(Activity activity) {
        mList.add(activity);
    }

    public void exit() {
        try {
            for (Activity activity : mList) {
                if (activity != null)
                    activity.finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.exit(0);
        }
    }

    public void onLowMemory() {
        super.onLowMemory();
        System.gc();
    }
}

