package com.viewer.cagochat;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.manager.cago.WDCCP2PManager;
import com.manager.cago.WDCCP2PService;

/**
 * @author zahab
 * 
 */
public class WDCCDevice_ListActivity extends Activity {
	public WDCCList_Fragment mDevice_List_Fragment = null;
	public WDCCP2PManager mManager;
	public Context mContext = null;
	protected static final String TAG = WDCCDevice_ListActivity.class
			.getSimpleName();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(TAG, "onCreate");

		setContentView(R.layout.activity_device__list);
		mManager = WDCCP2PManager.getWDCCP2PManager();

		mContext = mManager.getappContext();// getApplicationContext();
		mDevice_List_Fragment = new WDCCList_Fragment();
		mManager.registerDevListListener(mDevice_List_Fragment);
		if (savedInstanceState == null) {

			getFragmentManager().beginTransaction()
					.add(R.id.container, mDevice_List_Fragment).commit();
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.device__list, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/*
	 * @Override public void onSaveInstanceState(Bundle bundle) {
	 * super.onSaveInstanceState(bundle); Log.d(TAG,"onSaveInstanceState");
	 * 
	 * }
	 */
}
