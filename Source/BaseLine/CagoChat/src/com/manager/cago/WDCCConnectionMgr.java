/**
 * 
 */
package com.manager.cago;

import java.io.IOException;

import android.net.wifi.WpsInfo;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.ActionListener;
import android.net.wifi.p2p.WifiP2pManager.Channel;
import android.net.wifi.p2p.WifiP2pManager.ConnectionInfoListener;
import android.os.Handler;
import android.util.Log;

/**
 * @author zahab <blockquote>This class will handle the P2P connection
 *         initiation and the connection information received from
 *         ConnectionInfoListener. It will interact with {@link WDCCP2PManager}
 *         which will pass service related request to {@link WDCCServiceManager}
 *         .
 */
public class WDCCConnectionMgr implements ConnectionInfoListener {
	WDCCP2PManager mManager = null;
	private Handler handler;
	protected static final String TAG = WDCCConnectionMgr.class.getSimpleName();

	/**
	 * 
	 */
	public WDCCConnectionMgr() {
		mManager = WDCCP2PManager.getWDCCP2PManager();
	}

	public void connectP2p(WDCCP2PService service) {
		WifiP2pConfig config = new WifiP2pConfig();
		config.deviceAddress = service.device.deviceAddress;
		config.wps.setup = WpsInfo.PBC;
		WifiP2pManager mAndroidP2Pmanager = mManager.getAndroidP2Pmanager();
		Channel channel = mManager.getP2PChannel();
		Log.d(TAG, "connectP2p calling stopServiceDiscovery");
		mManager.stopServiceDiscovery();
		
		mAndroidP2Pmanager.connect(channel, config, new ActionListener() {

			@Override
			public void onSuccess() {
				Log.d(TAG, "connect onSuccess");
			}

			@Override
			public void onFailure(int errorCode) {
				Log.d(TAG, "connect onFailure errorCode = " + errorCode);
			}
		});
	}

	/**
	 * @see android.net.wifi.p2p.WifiP2pManager.ConnectionInfoListener#
	 * onConnectionInfoAvailable(android.net.wifi.p2p.WifiP2pInfo)
	 */
	@Override
	public void onConnectionInfoAvailable(WifiP2pInfo p2pInfo) {
		Thread handler = null;
		Log.d(TAG, "onConnectionInfoAvailable");
		if(p2pInfo== null){
			Log.d(TAG, "p2pInfo== null");
		}
		/*
		 * The group owner accepts connections using a server socket and then
		 * spawns a client socket for every client. This is handled by {@code
		 * GroupOwnerSocketHandler}
		 */

		if (p2pInfo.isGroupOwner) {
			
			Log.d(TAG, "Connected as group owner");
			try {
				handler = new WDCCGOSocketMgr(this.handler);
				handler.start();
			} catch (IOException e) {
				Log.d(TAG,
						"Failed to create a server thread - " + e.getMessage());
				return;
			}
		} else {
			if(p2pInfo.groupOwnerAddress== null){
				Log.d(TAG, "p2pInfo.groupOwnerAddress== null");
				return;
			}
			Log.d(TAG, "Connected as peer");
			handler = new WDCCClientSocketMgr(this.handler,
					p2pInfo.groupOwnerAddress);
			
			handler.start();
		}

	}

}
