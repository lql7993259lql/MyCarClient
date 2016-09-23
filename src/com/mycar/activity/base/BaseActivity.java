package com.mycar.activity.base;

import com.mycar.R;
import com.mycar.dialog.ImageDialog;
import com.mycar.dialog.WaitDialog;
import com.mycar.dialog.WebDialog;
import com.mycar.nohttp.CallServer;
import com.mycar.nohttp.HttpListener;
import com.yolanda.nohttp.Headers;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.rest.OnResponseListener;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.RequestQueue;
import com.yolanda.nohttp.rest.Response;
import com.yolanda.nohttp.rest.StringRequest;
import com.yolanda.nohttp.tools.HeaderParser;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import butterknife.ButterKnife;


public abstract  class BaseActivity extends AppCompatActivity {
	private CoordinatorLayout mCoordinatorLayout;
    private AppBarLayout mAppBarLayout;
    private Toolbar mToolbar;
    private FrameLayout mContentLayout;
    /**
     * 请求的时候等待框。
     */
    private WaitDialog mWaitDialog;

    /**
     * 请求队列。
     */
    private RequestQueue requestQueue;
    
    @Override
    protected final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		ButterKnife.inject(this);
		ButterKnife.setDebug(true);
        getDelegate().setContentView(R.layout.activity_base);

        mCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorlayout);
        mAppBarLayout = (AppBarLayout) findViewById(R.id.appbarlayout);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mContentLayout = (FrameLayout) findViewById(R.id.content);

        setSupportActionBar(mToolbar);
        setBackBar(true);
        mWaitDialog = new WaitDialog(this);
        // 创建请求队列, 默认并发3个请求,传入你想要的数字可以改变默认并发数, 例如NoHttp.newRequestQueue(1)。
        requestQueue = NoHttp.newRequestQueue();
        onActivityCreate(savedInstanceState);
    }

    protected abstract void onActivityCreate(Bundle savedInstanceState);

    public <T extends View> T findView(int viewId) {
        return (T) mContentLayout.findViewById(viewId);
    }

    public CoordinatorLayout getCoordinatorLayout() {
        return mCoordinatorLayout;
    }

    public FrameLayout getContentLayout() {
        return mContentLayout;
    }

    public AppBarLayout getAppBarLayout() {
        return mAppBarLayout;
    }

    public Toolbar getmToolbar() {
        return mToolbar;
    }

    @Override
    public void setTitle(CharSequence title) {
        mToolbar.setTitle(title);
    }

    @Override
    public void setTitle(int titleId) {
        mToolbar.setTitle(titleId);
    }

    public void setSubtitle(CharSequence title) {
        mToolbar.setSubtitle(title);
    }

    public void setSubtitle(int titleId) {
        mToolbar.setSubtitle(titleId);
    }

    public void setSubtitleTextColor(int color) {
        mToolbar.setSubtitleTextColor(color);
    }

    public void setTitleTextColor(int color) {
        mToolbar.setTitleTextColor(color);
    }

    public void setBackBar(boolean isShow) {
        getSupportActionBar().setDisplayHomeAsUpEnabled(isShow);
    }

    public void setContentBackground(int color) {
        mContentLayout.setBackgroundResource(color);
    }

    @Override
    public void setContentView(int layoutResID) {
        mContentLayout.removeAllViews();
        getLayoutInflater().inflate(layoutResID, mContentLayout, true);
    }

    @Override
    public void setContentView(View view) {
        mContentLayout.removeAllViews();
        mContentLayout.addView(view);
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        mContentLayout.addView(view, params);
    }

    @Override
    public final boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return onOptionsItemSelectedCompat(item);
    }

    protected boolean onOptionsItemSelectedCompat(MenuItem item) {
        return false;
    }

    public ViewGroup getContentRoot() {
        return mContentLayout;
    }

    /**
     * Show message dialog.
     *
     * @param title   title.
     * @param message message.
     */
    public void showMessageDialog(int title, int message) {
        showMessageDialog(getText(title), getText(message));
    }

    /**
     * Show message dialog.
     *
     * @param title   title.
     * @param message message.
     */
    public void showMessageDialog(int title, CharSequence message) {
        showMessageDialog(getText(title), message);
    }

    /**
     * Show message dialog.
     *
     * @param title   title.
     * @param message message.
     */
    public void showMessageDialog(CharSequence title, int message) {
        showMessageDialog(title, getText(message));
    }

    /**
     * Show message dialog.
     *
     * @param title   title.
     * @param message message.
     */
    public void showMessageDialog(CharSequence title, CharSequence message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton(R.string.know, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    /**
     * 显示图片dialog。
     *
     * @param title  标题。
     * @param bitmap 图片。
     */
    public void showImageDialog(CharSequence title, Bitmap bitmap) {
        ImageDialog imageDialog = new ImageDialog(this);
        imageDialog.setTitle(title);
        imageDialog.setImage(bitmap);
        imageDialog.show();
    }

    /**
     * 显示一个WebDialog。
     *
     * @param response 响应。
     */
    public void showWebDialog(Response<?> response) {
        String result = StringRequest.parseResponseString(response.getHeaders(), response.getByteArray());
        WebDialog webDialog = new WebDialog(this);
        String contentType = response.getHeaders().getContentType();
        webDialog.loadUrl(result, contentType, HeaderParser.parseHeadValue(contentType, "charset", "utf-8"));
        webDialog.show();
    }

    public <T> void request(int what, Request<T> request, HttpListener<T> callback, boolean canCancel, boolean isLoading) {
        request.setCancelSign(this);
        CallServer.getRequestInstance().add(this, what, request, callback, canCancel, isLoading);
    }

    @Override
    protected void onDestroy() {
        CallServer.getRequestInstance().cancelBySign(this);
        super.onDestroy();
    }

    /**
     * 回调对象，接受请求结果.
     */
    private OnResponseListener<String> onResponseListener = new OnResponseListener<String>() {
       
        @Override
        public void onSucceed(int what, Response<String> response) {
//            if (what == NOHTTP_WHAT_TEST) {// 根据what判断是哪个请求的返回，这样就可以用一个OnResponseListener来接受多个请求的结果。
//                int responseCode = response.getHeaders().getResponseCode();// 服务器响应码。
//
//                if (responseCode == 200) {// 如果是是用NoHttp的默认的请求或者自己没有对NoHttp做封装，这里最好判断下Http状态码。
//                    String result = response.get();// 响应结果。
//
//                    ((TextView) findView(R.id.tv_result)).setText(result);
//
//                    Object tag = response.getTag();// 拿到请求时设置的tag。
//                    byte[] responseBody = response.getByteArray();// 如果需要byteArray自己解析的话。
//
//                    // 响应头
//                    Headers headers = response.getHeaders();
//                    String headResult = getString(R.string.request_original_result);
//                    headResult = String.format(Locale.getDefault(), headResult, headers.getResponseCode(), response.getNetworkMillis());
//                    ((TextView) findView(R.id.tv_head)).setText(headResult);
//                }
//            }
        }

        @Override
        public void onStart(int what) {
            // 请求开始，这里可以显示一个dialog
            if (mWaitDialog != null && !mWaitDialog.isShowing())
                mWaitDialog.show();
        }

        @Override
        public void onFinish(int what) {
            // 请求结束，这里关闭dialog
            if (mWaitDialog != null && mWaitDialog.isShowing())
                mWaitDialog.dismiss();
        }

        @Override
        public void onFailed(int what, String url, Object tag, Exception exception, int responseCode, long networkMillis) {
            // 请求失败
            //((TextView) findView(R.id.tv_result)).setText("请求失败: " + exception.getMessage());
        }
    };
	
}
