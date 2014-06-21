package com.viewer.cagochat;

/*

 */

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.NetworkInfo;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.Channel;
import android.util.Log;

import com.manager.cago.WDCCP2PManager;

/**
 * A BroadcastReceiver that notifies of important wifi p2p events.
 */
public class WDCCBroadcastReceiver extends BroadcastReceiver {

	private WifiP2pManager mAndroidP2PManager = null;
	private Channel mchannel = null;
	private Activity activity;
	private WDCCPeerlistener mPeerListener;
	public final IntentFilter intentFilter = new IntentFilter();
	private WDCCP2PManager mManager = null;
	protected static final String TAG = WDCCBroadcastReceiver.class
			.getSimpleName();

	/**
	 * @param manager
	 *            WifiP2pManager system service
	 * @param mchannel
	 *            Wifi p2p mchannel
	 * @param activity
	 *            activity associated with the receiver
	 */
	public WDCCBroadcastReceiver(WifiP2pManager manager, Channel channel,
			Activity activity, WDCCPeerlistener peerlistener) {
		super();
		Log.d(TAG, "creating WDCCBroadcastReceiver");
		this.mAndroidP2PManager = manager;
		mchannel = channel;
		if(mchannel == null){
			Log.d(TAG,"mchannel is null");
		}
		this.activity = activity;
		this.mPeerListener = peerlistener;
		mManager = WDCCP2PManager.getWDCCP2PManager();
		intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
		intentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
		intentFilter
				.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
		intentFilter
				.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.content.BroadcastReceiver#onReceive(android.content.Context,
	 * android.content.Intent)
	 */
	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();
		Log.d(TAG, "onReceive" + action);

		if (WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION.equals(action)) {
			Log.d(TAG, "WIFI_P2P_CONNECTION_CHANGED_ACTION");
			
			
			
			NetworkInfo networkInfo = (NetworkInfo) intent
					.getParcelableExtra(WifiP2pManager.EXTRA_NETWORK_INFO);
			Log.d(TAG, "networkInfo.isConnected()" + networkInfo.isConnected());
			if (networkInfo.isConnected()) {
				Log.d(TAG, "networkInfo.isConnected()-----------------1" );
				// we are connected with the other device, request connection
				// info to find group owner IP
				if(mAndroidP2PManager == null){
					Log.d(TAG, "----------------2" );

				}
				if(mManager == null){
					Log.d(TAG, "----------------4" );

				}
				if(mManager.getP2PChannel() == null){
					Log.d(TAG, "mchannel IS NULL----------------3" );

				}
			

				//Log.d(TAG,"Connected to p2p network. Requesting network details" + mAndroidP2PManager + mchannel + mManager.getmConnectionMgr());
				
				
				//mAndroidP2PManager.requestConnectionInfo(mchannel,mManager.getmConnectionMgr());
			} else {
				// It's a disconnect
			}

		} else if (WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION
				.equals(action)) {
			Log.d(TAG, "WIFI_P2P_THIS_DEVICE_CHANGED_ACTION");

			mAndroidP2PManager.requestPeers(mchannel, mPeerListener);
			WifiP2pDevice device = (WifiP2pDevice) intent
					.getParcelableExtra(WifiP2pManager.EXTRA_WIFI_P2P_DEVICE);
			Log.d(TAG, "Device status -" + device.status);

		} else if (WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action)) {
			Log.d(TAG, "WIFI_P2P_PEERS_CHANGED_ACTION");

		} else if (WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION.equals(action)) {
			Log.d(TAG, "WIFI_P2P_STATE_CHANGED_ACTION");

		}

	}
}
