package com.mycar;

import java.io.PrintWriter;
import java.io.StringWriter;

import com.mycar.config.AppConfig;
import com.mycar.nohttp.CallServer;
import com.yolanda.nohttp.Logger;
import com.yolanda.nohttp.NoHttp;

import android.widget.Toast;



public class Application extends android.app.Application {

    private static Application _instance;

    @Override
    public void onCreate() {
        super.onCreate();
        _instance = this;

        NoHttp.initialize(this);

        Logger.setTag("NoHttpSample");
        Logger.setDebug(true);// 开始NoHttp的调试模式, 这样就能看到请求过程和日志

        AppConfig.getInstance();
      
		Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandler());

    }

    public static Application getInstance() {
        return _instance;
    }

    /**
     * 捕获全局异常
     * @author Administrator
     *
     */
    class UncaughtExceptionHandler implements Thread.UncaughtExceptionHandler {

		public UncaughtExceptionHandler() {
		}

		@Override
		public void uncaughtException(final Thread thread, final Throwable ex) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			ex.printStackTrace(pw);
			String bugStr = sw.toString();
			Toast.makeText(getApplicationContext(), bugStr, Toast.LENGTH_LONG).show();
	
			// 程序退出时取消所有请求
	        CallServer.getRequestInstance().cancelAll();
	        // 程序退出时停止请求队列，如果这里的NoHttpRequestQueue是单例模式，NoHttp所在的进程没杀死而停止了队列，会导致再打开app不能请求网络
	        CallServer.getRequestInstance().stopAll();
	        android.os.Process.killProcess(android.os.Process.myPid());
		}
	}

}
