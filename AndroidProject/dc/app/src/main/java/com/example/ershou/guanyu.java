package com.example.ershou;


import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

public class guanyu extends Activity{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.guanyu);
	}

}
