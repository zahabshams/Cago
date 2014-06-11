/**
 * 
 */
package com.manager.cago;

import java.util.ArrayList;

import android.content.Context;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.Channel;
import android.util.Log;

import com.viewer.cagochat.Device_ListActivity;
import com.viewer.cagochat.WDCHPeerlistener;
import com.viewer.cagochat.WiFiDirectBroadcastReceiver;

/**
 * @author zahab
 * Singleton class to handle discovery, connection and data to UI layer. 
 * This class will act as a bridge UI and P2P operations related registration discover,connection
 * 
 */

public class WDCCP2PManager {

	public static WDCCP2PManager mManager = null;
	public WDCHPeerlistener mWDPeerlistener = null;
	public WiFiDirectBroadcastReceiver mP2PBroadcastReceiver = null;
	private WifiP2pManager mAndroidP2Pmanager = null;
	public Context mContext = null;
	private Channel mChannel = null;
	private WDCCServiceManager mServiceManager = null;
	protected static final String TAG = WDCCP2PManager.class.getSimpleName();
	private Device_ListActivity mDevListActivity;
	/**
	 * @return
	 * 
	 */
	private void initialise() {
		Log.d(TAG,"-------------------initialise---------------------------");
		mWDPeerlistener = new WDCHPeerlistener();
		mAndroidP2Pmanager = (WifiP2pManager) mContext
				.getSystemService(Context.WIFI_P2P_SERVICE);
		// zahab need to work on third argument of initialize which is null now.
		mChannel = mAndroidP2Pmanager.initialize(mContext,mContext.getMainLooper(), null);
		mP2PBroadcastReceiver = new WiFiDirectBroadcastReceiver(mAndroidP2Pmanager, mChannel, null, mWDPeerlistener);
		mServiceManager = new WDCCServiceManager(mChannel, mAndroidP2Pmanager, this);
		mServiceManager.startRegistrationAndDiscovery();
		
	}

	protected WDCCP2PManager(Context context) {
		Log.d(TAG, "WDCCP2PManager created");
		mContext = context;
		initialise();
	}

	/**
	 * @param context
	 * @return WDCCP2PManager
	 */
	public synchronized static WDCCP2PManager getWDCCP2PManager(Context context) {
		Log.d(TAG, "IN getWDCCP2PManager");

		if (mManager != null) {
			Log.d(TAG,
					"--------------------WDCCP2PManager is NOT null------------------------------ ");

		} else {
			Log.d(TAG,
					"--------------------WDCCP2PManager is null------------------------------ ");
			mManager = new WDCCP2PManager(context);
		}

		mManager.mContext = context;
		return mManager;

	}

	/**
	 * 
	 */
	public void cMStartConnection() {

	}

	public void registerBroadCastReceiver(Context context) {
		Log.d(TAG, "IN registerBroadCastReceiver");

		context.registerReceiver(mP2PBroadcastReceiver,
				mP2PBroadcastReceiver.intentFilter);

	}

	public void deregisterBroadCastReceiver(Context context) {
		Log.d(TAG, "IN degisterBroadCastReceiver");
		context.unregisterReceiver(mP2PBroadcastReceiver);
	}
	
	public boolean notifyServicesChanged(ArrayList<WifiP2pDevice> devicelist){
		return false;	
	}
	
	public boolean notifyDeviceDisconnected(WifiP2pDevice device) {
		return false;
	}
	
	
}
