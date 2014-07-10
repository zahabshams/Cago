package com.viewer.cagochat;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import com.manager.cago.WDCCP2PManager;
import com.manager.cago.WDCCP2PService;

public class WDCCScanningActivity extends ActionBarActivity {
	protected static final String TAG = WDCCScanningActivity.class
			.getSimpleName();
	public Context mContext = null;
	private WDCCP2PManager mManager;
	private static ProgressBar progressBar;
	private static Button scanButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mManager = WDCCP2PManager.getWDCCP2PManager();
		mContext = mManager.getappContext();
		setContentView(R.layout.activity_main);
		if (savedInstanceState == null) {

			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new WDCCScanningFragment()).commit();
		}
		// Adds images to action bar
		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayShowCustomEnabled(true);
		LayoutInflater inflator = (LayoutInflater) this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = inflator.inflate(R.layout.actionbarlayout, null);
		actionBar.setCustomView(v);
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

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	/*
	 * @Override public boolean onOptionsItemSelected(MenuItem item) { // Handle
	 * action bar item clicks here. The action bar will // automatically handle
	 * clicks on the Home/Up button, so long // as you specify a parent activity
	 * in AndroidManifest.xml. int id = item.getItemId(); if (id ==
	 * R.id.action_settings) { return true; } return
	 * super.onOptionsItemSelected(item); }
	 */

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class WDCCScanningFragment extends Fragment implements
			WDCCViewerManager.DevList {
		protected static final String TAG = WDCCScanningFragment.class
				.getSimpleName();
		private WDCCP2PManager mManager = null;
		private CountDownTimer mCTimer;
		private int mScanTimeout = 60 * 1000;
		private AlertDialog mdialog = null;
		private WDCCScanningFragment mScanningFrag;

		@Override
		public void onPause() {
			Log.d(TAG, "onPause(");
			super.onPause();
		}

		@Override
		public void onStop() {
			Log.d(TAG, "onStop(");
			mCTimer.cancel();
			super.onStop();
		}

		@Override
		public void onResume() {
			Log.d(TAG, "onResume(");
			mScanningFrag = this;
			mManager.registerDevListListener(mScanningFrag);
			super.onResume();
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
		OnClickListener scanning = new OnClickListener() {

			@Override
			public void onClick(View v) {
				progressBar.setVisibility(0);// display progress bar
				scanButton.setEnabled(false);
				mCTimer.start();
				mManager.registerDevListListener(mScanningFrag);
				mManager.startServiceDiscovery();

			}
		};

		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			mManager = WDCCP2PManager.getWDCCP2PManager();
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			mdialog = new AlertDialog.Builder(getActivity()).create();
			progressBar = (ProgressBar) rootView.findViewById(R.id.progressbar);
			scanButton = (Button) rootView.findViewById(R.id.btnScanning);
			scanButton.setEnabled(false);
			Button btnBrowse = (Button) rootView
					.findViewById(R.id.btnStartAnotherActivity);
			btnBrowse.setVisibility(8);
			btnBrowse.setOnClickListener(browse);

			scanButton.setOnClickListener(scanning);
			mManager.registerDevListListener(this);
			Log.d(TAG, "Starting ----------------TimerControl------------------1");
			TimerControl();
			Log.d(TAG, "Starting ----------------TimerControl------------------2");
			mCTimer.start();

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
		public void notifyServicesChanged(WDCCP2PService service, int operation) {
			Log.d(TAG, "notifyServicesChanged");
			// singleTask.cancel();
			mManager.deregisterDevListListener();
			Intent intent = new Intent(getActivity(),
					WDCCDevice_ListActivity.class);
			startActivity(intent);
			mCTimer.cancel();
			mManager.stopServiceDiscovery();

		}

		void TimerControl() {
			mCTimer = new CountDownTimer(mScanTimeout, 1000) {

				public void onTick(long millisUntilFinished) {
				}

				public void onFinish() {
					Log.d(TAG, "---------------timer onFinish()--------------------------");
					progressBar.setVisibility(8);

					mdialog.setCancelable(false);
					mdialog.setMessage("Do you want to restart scanning?");
					mManager.deregisterDevListListener();
					mManager.removeAndStopServiceDisc();
					// identifier is negative
					mdialog.setButton(DialogInterface.BUTTON_NEGATIVE, "NO",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Add your code for the button here.
									scanButton.setEnabled(true);
									progressBar.setVisibility(8);// removes
																	// progress
																	// bar
									mManager.deregisterDevListListener();
									mManager.stopServiceDiscovery();
								}
							});
					mdialog.setButton(DialogInterface.BUTTON_POSITIVE, "YES",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									progressBar.setVisibility(0); // display
																	// progress
																	// bar
									scanButton.setEnabled(false);
									mCTimer.start();
									mManager.registerDevListListener(mScanningFrag);
									mManager.startServiceDiscovery();
								}
							});
					mdialog.show();

				}
			};/* .start(); */

		}
	}

}
