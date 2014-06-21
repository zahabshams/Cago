package com.viewer.cagochat;

import com.manager.cago.WDCCP2PManager;
import com.manager.cago.WDCCP2PService;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.text.style.LineHeightSpan.WithDensity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

public class WDCCScanningActivity extends ActionBarActivity {
	protected static final String TAG = WDCCScanningActivity.class
			.getSimpleName();

	public static final int MESSAGE_READ = 0x400 + 1;
	public static final int MY_HANDLE = 0x400 + 2;
	public Button mbtnStartAnotherActivity;
	public Context mContext = null;
	private WDCCP2PManager mManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = getApplicationContext();

		WDCCP2PManager.iInstantiateManager(mContext);

		setContentView(R.layout.activity_main);

		if (savedInstanceState == null) {

			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new WDCCScanningFragment()).commit();
		}

	}

	@Override
	public void onResume() {
		super.onResume();
		mManager = WDCCP2PManager.getWDCCP2PManager();
		mManager.registerBroadCastReceiver();

	}

	@Override
	public void onPause() {
		super.onPause();
		//mManager.deregisterBroadCastReceiver();

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
	public static class WDCCScanningFragment extends Fragment implements
			WDCCViewerManager.DevList {
		protected static final String TAG = WDCCScanningFragment.class
				.getSimpleName();
		private WDCCP2PManager mManager = null;

		@Override
		public void onPause() {
			Log.d(TAG, "onPause(");
			// TODO Auto-generated method stub
			super.onStop();
		}

		@Override
		public void onStop() {
			Log.d(TAG, "onStop(");
			// TODO Auto-generated method stub
			super.onStop();
		}

		public WDCCScanningFragment() {
		}

		OnClickListener browse = new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(),
						WDCCDevice_ListActivity.class);
				startActivity(intent);

			}
		};

		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			mManager = WDCCP2PManager.getWDCCP2PManager();
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			Button button = (Button) rootView
					.findViewById(R.id.btnStartAnotherActivity);
			button.setOnClickListener(browse);
			mManager.registerDevListListener(this);
			return rootView;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * com.viewer.cagochat.WDCCViewerManager.DevList#notifyDeviceList(com
		 * .manager.cago.WDCCP2PService, boolean)
		 */
		@Override
		public void notifyServicesChanged(WDCCP2PService service, boolean add) {
			Log.d(TAG, "notifyServicesChanged");
			mManager.deregisterDevListListener();
			Intent intent = new Intent(getActivity(),
					WDCCDevice_ListActivity.class);
			startActivity(intent);

		}
	}
}
