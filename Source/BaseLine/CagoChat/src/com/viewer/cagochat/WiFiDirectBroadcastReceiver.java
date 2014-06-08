package com.viewer.cagochat;

/*

 */

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.Channel;
import android.net.wifi.p2p.WifiP2pManager.ConnectionInfoListener;
import android.util.Log;

/**
 * A BroadcastReceiver that notifies of important wifi p2p events.
 */
public class WiFiDirectBroadcastReceiver extends BroadcastReceiver {

	private WifiP2pManager manager;
	private Channel channel;
	private Activity activity;
	private WDCHPeerlistener mPeerListener;
	protected static final String TAG = WiFiDirectBroadcastReceiver.class
			.getSimpleName();

	/**
	 * @param manager
	 *            WifiP2pManager system service
	 * @param channel
	 *            Wifi p2p channel
	 * @param activity
	 *            activity associated with the receiver
	 */
	public WiFiDirectBroadcastReceiver(WifiP2pManager manager, Channel channel,
			Activity activity, WDCHPeerlistener peerlistener) {
		super();
		this.manager = manager;
		this.channel = channel;
		this.activity = activity;
		this.mPeerListener = peerlistener;
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
		Log.d(TAG, action);
		
		if (WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION.equals(action)) {
			Log.d(TAG, "WIFI_P2P_CONNECTION_CHANGED_ACTION");

		} else if (WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION
				.equals(action)) {
			manager.requestPeers(channel, mPeerListener);
			
		} else if (WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action)) {
			Log.d(TAG, "WIFI_P2P_PEERS_CHANGED_ACTION");

		} else if (WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION.equals(action)) {
			Log.d(TAG, "WIFI_P2P_STATE_CHANGED_ACTION");

		}

	}
}
