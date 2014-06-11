package com.viewer.cagochat;

import com.manager.cago.WDCCP2PManager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

public class WDCCScanningActivity extends ActionBarActivity{
	protected static final String TAG = WDCCScanningActivity.class
			.getSimpleName();

	public static final int MESSAGE_READ = 0x400 + 1;
	public static final int MY_HANDLE = 0x400 + 2;
	public Button mbtnStartAnotherActivity;
	public Context mContext = getApplicationContext();
	private WDCCP2PManager mManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);

		if (savedInstanceState == null) {

			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
		mManager = WDCCP2PManager.getWDCCP2PManager(mContext);

	}
	@Override
	 public void onResume () {
		mManager.registerBroadCastReceiver(mContext);
		
	}
	@Override
	public void onPause(){
		mManager.deregisterBroadCastReceiver(mContext);

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

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {
		
		public PlaceholderFragment() {
		}


		OnClickListener browse = new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(), Device_ListActivity.class);
				startActivity(intent);
				
			}
		};

		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			Button button = (Button) rootView
					.findViewById(R.id.btnStartAnotherActivity);
			button.setOnClickListener(browse);
			
			return rootView;
		}
	}
}
