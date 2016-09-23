package com.mycar.dialog;

import android.content.Context;
import android.view.Window;

public class ProgressDialog extends android.app.ProgressDialog {

    public ProgressDialog(Context context) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setCanceledOnTouchOutside(false);
        setProgressStyle(STYLE_HORIZONTAL);
    }

}
