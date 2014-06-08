package com.viewer.cagochat;

import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager.ChannelListener;
import android.net.wifi.p2p.WifiP2pManager.ConnectionInfoListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.manager.cago.WDCCP2PManager;

public class WDCCScanningActivity extends ActionBarActivity implements
		Handler.Callback, ConnectionInfoListener, ChannelListener {
	protected static final String TAG = WDCCScanningActivity.class.getSimpleName();

	public static final int MESSAGE_READ = 0x400 + 1;
	public static final int MY_HANDLE = 0x400 + 2;
	
	private Handler handler = new Handler(this);
	/*
	 * private WiFiChatFragment chatFragment; private WiFiDirectServicesList
	 * servicesList;
	 */

	public Handler getHandler() {
		return handler;
	}

	public void setHandler(Handler handler) {
		this.handler = handler;
	}

	public static WDCCP2PManager mCManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
		mCManager = WDCCP2PManager.getWDCCP2PManager(this);
	
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
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

	@Override
	public void onResume() {
		super.onResume();
		Log.d(TAG, "onResume");
		if(mCManager != null){
			Log.d(TAG, "mCManager" + mCManager);
			mCManager.registerBroadCastReceiver(this);
			return;
		}
			
		else {
			mCManager = WDCCP2PManager.getWDCCP2PManager(this);
			mCManager.registerBroadCastReceiver(this);
			return;

		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		if(mCManager != null){
			Log.d(TAG, "mCManager" + mCManager);
			//mCManager.degisterBroadCastReceiver(this);
			return;
		}
			
		else {
			mCManager = WDCCP2PManager.getWDCCP2PManager(this);
			//mCManager.degisterBroadCastReceiver(this);
			return;
		}
	
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
		
			return rootView;
		}
	}

	@Override
	public boolean handleMessage(Message msg) {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public void onConnectionInfoAvailable(WifiP2pInfo info) {
		Log.d(TAG, "onConnectionInfoAvailable" + info.groupOwnerAddress);
		// TODO Auto-generated method stub

	}

	@Override
	public void onChannelDisconnected() {
		Log.d(TAG, "onChannelDisconnected");

		// TODO Auto-generated method stub

	}

}
