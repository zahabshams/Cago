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
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import com.manager.cago.WDCCP2PManager;
import com.manager.cago.WDCCP2PService;
import com.manager.cago.listeners.SessionListenerImp;

public class WDCCScanningActivity extends ActionBarActivity {
	


	protected static final String TAG = WDCCScanningActivity.class
			.getSimpleName();
	public Context mContext = null;
	private WDCCP2PManager mManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d("MyTest", "in scanning activity");
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
		//mManager.
		mManager.addLocalService();
	}

	@Override
	public void onResume() {
		super.onResume();
		mManager = WDCCP2PManager.getWDCCP2PManager();
		//mManager.registerBroadCastReceiver();
	}

	@Override
	public void onPause() {
		Log.d(TAG, "onActivityPause(");
		super.onPause();

	}

	/*
	 * @Override public boolean onKeyUp(int keycode, KeyEvent event){ if(keycode
	 * == KeyEvent.KEYCODE_BACK ||keycode == KeyEvent.KEYCODE_HOME){ //
	 * mManager.closeDownChat(); //mManager.removeAndStopServiceDisc();
	 * finish();
	 * 
	 * } return super.onKeyUp(keycode, event);
	 * 
	 * }
	 */

	@Override
	public void onDestroy() {
		Log.d(TAG, "--------onActivityDestroy");
		Log.d(TAG, "calling removeAndStopServiceDisc");
		mManager.removeAndStopServiceDisc();
		super.onDestroy();
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
		private int mScanTimeout = 30 * 1000;
		private AlertDialog mdialog = null;
		private WDCCScanningFragment mScanningFrag;
		private ProgressBar progressbar1;
		private ProgressBar progressbar2;
		private Button scanButton;
		private SessionListenerImp mSessionlistener = new SessionListenerImp() {
			@Override
			public void onChatFinish() {

			};

			@Override
			public void onChatStart() {
				getActivity().finish();
			};
		};

		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			Log.d(TAG, "WDCCScanningFragment  onCreateView");
			mManager = WDCCP2PManager.getWDCCP2PManager();
	        setRetainInstance(true);	//need to work on
			mManager.registerDevListListener(this);
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			mdialog = new AlertDialog.Builder(getActivity()).create();
			progressbar1 = (ProgressBar) rootView
					.findViewById(R.id.progressbarmoving);
			progressbar2 = (ProgressBar) rootView
					.findViewById(R.id.progressbarstop);
			scanButton = (Button) rootView.findViewById(R.id.btnScanning);
			scanButton.setEnabled(false);
			Button btnBrowse = (Button) rootView
					.findViewById(R.id.btnStartAnotherActivity);
			btnBrowse.setVisibility(View.GONE);
			btnBrowse.setOnClickListener(browse);

			scanButton.setOnClickListener(scanning);
			//mManager.registerSessionListener(mSessionlistener);
			TimerControl();
			//mCTimer.start();
			return rootView;
		}

		@Override
		public void onPause() {
			Log.d(TAG, "onPause(");
			super.onPause();
		}

		@Override
		public void onStop() {
			Log.d(TAG, "onStop");
			 mCTimer.cancel();
			super.onStop();
		}

		@Override
		public void onResume() {
			Log.d(TAG, "onResume");
			mScanningFrag = this;
			mManager.registerDevListListener(this);
			if(!mManager.isLocalServRegd()){
				Log.d(TAG, "isLocalServRegd()" + mManager.isLocalServRegd());
				mManager.addLocalService();
			}
			else if(mManager.isDiscoveryActive()){
				Log.d(TAG, "isDiscoveryActive()" + mManager.isDiscoveryActive());
				mManager.stopServiceDiscovery();
			}		
			mManager.startServiceDiscovery();
			mCTimer.start();
			super.onResume();
		}

		@Override
		public void onDestroy() {
			Log.d(TAG, "onDestroy(");
			mCTimer.cancel();
			//mManager.deregisterSessionListener(mSessionlistener);
			super.onDestroy();
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
				progressbar2.setVisibility(8);// removes stop progress bar
				progressbar1.setVisibility(0);// display moving progress bar
				scanButton.setEnabled(false);
				//mCTimer.start();
				mManager.registerDevListListener(mScanningFrag);
				mManager.startServiceDiscovery();

			}
		};

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
			mManager.deregisterDevListListener();
			mCTimer.cancel();
			Intent intent = new Intent(getActivity(),
					WDCCDevice_ListActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
			startActivity(intent);
			// getActivity().finish();

		}

		void TimerControl() {
			mCTimer = new CountDownTimer(mScanTimeout, 1000) {

				public void onTick(long millisUntilFinished) {
				}

				public void onFinish() {
					Log.d(TAG,
							"---------------timer onFinish()--------------------------");
					progressbar1.setVisibility(8);// removes moving progress bar
					progressbar2.setVisibility(0);// display stop progress bar
					mdialog.setCancelable(false);
					mdialog.setMessage("Do you want to restart scanning?");
					mManager.deregisterDevListListener();
					Log.d(TAG, "onFinish calling stopServiceDiscovery");
					mManager.stopServiceDiscovery();

					mdialog.setButton(DialogInterface.BUTTON_NEGATIVE, "NO",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Add your code for the button here.
									scanButton.setEnabled(true);
									/*
									 * mManager.deregisterDevListListener();
									 * mManager.stopServiceDiscovery();
									 */
								}
							});
					mdialog.setButton(DialogInterface.BUTTON_POSITIVE, "YES",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									mManager.registerDevListListener(mScanningFrag);
									//mManager.startRegistrationAndDiscovery();
									mManager.startServiceDiscovery();
									progressbar2.setVisibility(8); // removes
																	// stop
																	// progress
																	// bar
									progressbar1.setVisibility(0); // display
																	// moving
																	// progress
																	// bar
									mCTimer.start();
								}
							});
					mdialog.show();

				}
			};

		}
	}

}
