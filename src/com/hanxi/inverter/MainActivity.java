import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application; 
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

import android.view.View;
import android.view.View.OnClickListener;
import android.view.Gravity;
import android.view.KeyEvent;

import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import java.util.LinkedList; 
import java.util.List; 


public class MainActivity extends Activity {
    private static Activity s_instance;
    private static WebView m_webView;
    private static View m_welcomeView;

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

        m_webView.addJavascriptInterface(new Object() {
            public void clickOnAndroid() {
                s_instance.runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(MainActivity.this, "测试调用java", Toast.LENGTH_LONG).show();
                    }
                });
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
            }
        }, 2000);
    }

    @Override 
    public boolean onKeyDown(int keyCode, KeyEvent event) { 
        if (keyCode == KeyEvent.KEYCODE_BACK) { 
            pressAgainExit();
            return true;
        } 
        return super.onKeyDown(keyCode, event); 
    } 

    private void pressAgainExit() { 
        if (Exit.isExit()) { 
            SysApplication.getInstance().exit();
        } else { 
            Toast.makeText(getApplicationContext(), R.string.exit_again, 
                    Integer.valueOf(R.string.exit_time)).show(); 
            Exit.doExitInOneSecond(); 
        } 
    }
}

class Exit {
    private static boolean isExit = false;

    private static Runnable task = new Runnable() {
        @Override
        public void run() {
            isExit = false;
        }
    };

    public static void doExitInOneSecond() {
        isExit = true;
        HandlerThread thread = new HandlerThread("doTask");
            thread.start();
            new Handler(thread.getLooper()).postDelayed(task, Integer.valueOf(R.string.exit_time));
    }

    public static boolean isExit() {
        return isExit;
    }

    public static void setExit(boolean exit) {
        isExit = exit;
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

