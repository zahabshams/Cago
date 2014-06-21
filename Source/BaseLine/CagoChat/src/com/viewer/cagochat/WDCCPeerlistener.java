/**
 * 
 */
package com.viewer.cagochat;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pManager;
import android.util.Log;

/**
 * @author zahab Class used for listening the available wi-fi direct peers.
 *         Implements WifiP2pManager.PeerListListener which gives a callback
 *         onPeersAvailable with the WifiP2pDeviceList
 * 
 */
public class WDCCPeerlistener implements WifiP2pManager.PeerListListener {

	/**
	 * 
	 */
	static int a = 0;
	private List peers = new ArrayList();
	protected static final String TAG = WDCCPeerlistener.class.getSimpleName();
	private List<WifiP2pDevice> items;

	public WDCCPeerlistener() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onPeersAvailable(WifiP2pDeviceList peerList) {
		Log.d(TAG, "----------onPeersAvailable--------------" + a++);
		peers.clear();
		peers.addAll(peerList.getDeviceList());
		// items.addAll(peerList.getDeviceList());
		items = peers;
		if (peers.size() == 0) {
			Log.d(TAG, "No devices found");
			return;
		} else {
			Log.d(TAG, "peers.size() = " + peers.size());
			for (int i = 0; i < items.size(); i++) {
				WifiP2pDevice device = items.get(i);
				Log.d(TAG, "Device name " + device.deviceName
						+ "Device Address " + device.deviceAddress);
			}
		}

		// TODO Auto-generated method stub

	}

}
