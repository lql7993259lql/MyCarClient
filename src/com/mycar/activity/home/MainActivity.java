package com.mycar.activity.home;

import com.mycar.R;
import com.mycar.activity.base.BaseActivity;
import com.mycar.nohttp.CallServer;
import com.mycar.nohttp.HttpListener;
import com.mycar.util.Constants;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.Response;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import butterknife.InjectView;


public class MainActivity extends BaseActivity implements HttpListener<String>{
	@InjectView(R.id.tv) TextView tv;
	@InjectView(R.id.btn) Button btn;
	@Override
	protected void onActivityCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_main);
		
		
		 Request<String> request = NoHttp.createStringRequest(Constants.URL_NOHTTP_CACHE_STRING);
		 CallServer.getRequestInstance().add(this, 0, request, this, false, true);
		
	}

	@Override
    public void onBackPressed() {
        // 程序退出时取消所有请求
        CallServer.getRequestInstance().cancelAll();
        // 程序退出时停止请求队列，如果这里的NoHttpRequestQueue是单例模式，NoHttp所在的进程没杀死而停止了队列，会导致再打开app不能请求网络
        CallServer.getRequestInstance().stopAll();
        android.os.Process.killProcess(android.os.Process.myPid());
    }

	
	
	@Override
	public void onSucceed(int what, Response<String> response) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onFailed(int what, String url, Object tag, Exception exception, int responseCode, long networkMillis) {
		// TODO Auto-generated method stub
		
	}

	
	
}
