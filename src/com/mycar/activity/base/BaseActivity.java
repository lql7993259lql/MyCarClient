package com.mycar.activity.base;


import com.mycar.R;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;


public abstract class BaseActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		
	}
	public abstract void setContentView();
	public abstract void setContentView(View view);
	
}
