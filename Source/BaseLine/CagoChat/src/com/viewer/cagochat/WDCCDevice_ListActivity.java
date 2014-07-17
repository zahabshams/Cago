package com.viewer.cagochat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;

import com.manager.cago.WDCCP2PManager;

/**
 * @author zahab
 * 
 */
public class WDCCDevice_ListActivity extends ActionBarActivity {
	public WDCCList_Fragment mDevice_List_Fragment = null;
	public WDCCP2PManager mManager;
	public Context mContext = null;
	protected static final String TAG = WDCCDevice_ListActivity.class
			.getSimpleName();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(TAG, "onCreate");

		setContentView(R.layout.activity_device_list);
		mManager = WDCCP2PManager.getWDCCP2PManager();

		mContext = mManager.getappContext();// getApplicationContext();
		mDevice_List_Fragment = new WDCCList_Fragment();
		mManager.registerDevListListener(mDevice_List_Fragment);
		if (savedInstanceState == null) {

			getFragmentManager().beginTransaction()
					.add(R.id.container, mDevice_List_Fragment).commit();
		}
		//Adds images to action bar
		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayShowCustomEnabled(true);

		LayoutInflater inflator = (LayoutInflater) this .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = inflator.inflate(R.layout.actionbarlayout, null);
		actionBar.setCustomView(v);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK /*|| keyCode == KeyEvent.KEYCODE_HOME*/){
			//mManager.closeDownChat();
			mManager.removeAndStopServiceDisc();
			this.finish();
			/*Intent intent = new Intent(this, WDCCScanningActivity.class);
			startActivity(intent);*/
		}
		return super.onKeyUp(keyCode, event);
	}
/*	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}*/

	/*
	 * @Override public void onSaveInstanceState(Bundle bundle) {
	 * super.onSaveInstanceState(bundle); Log.d(TAG,"onSaveInstanceState");
	 * 
	 * }
	 */
}
