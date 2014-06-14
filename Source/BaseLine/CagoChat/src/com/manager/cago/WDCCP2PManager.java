/**
 * 
 */
package com.manager.cago;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.Channel;
import android.util.Log;

import com.viewer.cagochat.WDCCDevice_ListActivity;
import com.viewer.cagochat.WDCCViewerManager;
import com.viewer.cagochat.WDCCPeerlistener;
import com.viewer.cagochat.WDCCBroadcastReceiver;

/**
 * @author zahab Singleton class to handle discovery, connection and data to UI
 *         layer. This class will act as a bridge UI and P2P operations related
 *         registration discover,connection
 * 
 */

public class WDCCP2PManager {

	private static WDCCP2PManager mManager = null;
	public WDCCPeerlistener mWDPeerlistener = null;
	public WDCCBroadcastReceiver mP2PBroadcastReceiver = null;
	private WifiP2pManager mAndroidP2Pmanager = null;
	public Context mContext = null;
	private List<WDCCP2PService> mServiceList = new ArrayList<WDCCP2PService>();

	/**
	 * @return the mContext
	 */
	public Context getmContext() {
		return mContext;
	}

	private Channel mChannel = null;
	private WDCCServiceManager mServiceManager = null;
	private WDCCViewerManager.DevList mDevListListener;

	protected static final String TAG = WDCCP2PManager.class.getSimpleName();
	private WDCCDevice_ListActivity mDevListActivity;

	/**
	 * @return
	 * 
	 */
	private void initialise() {
		Log.d(TAG, "-------------------initialise---------------------------");
		mWDPeerlistener = new WDCCPeerlistener();
		mAndroidP2Pmanager = (WifiP2pManager) mContext
				.getSystemService(Context.WIFI_P2P_SERVICE);
		// zahab need to work on third argument of initialize which is null now.
		mChannel = mAndroidP2Pmanager.initialize(mContext,
				mContext.getMainLooper(), null);
		mP2PBroadcastReceiver = new WDCCBroadcastReceiver(mAndroidP2Pmanager,
				mChannel, null, mWDPeerlistener);
		mServiceManager = new WDCCServiceManager(mChannel, mAndroidP2Pmanager,
				this);
		mServiceManager.startRegistrationAndDiscovery();

	}

	protected WDCCP2PManager(Context context) {
		Log.d(TAG, "WDCCP2PManager created");
		mContext = context;
		initialise();
	}

	/**
	 * @return the mManager
	 */
	private static WDCCP2PManager getmManager() {
		return mManager;
	}

	/**
	 * @param mManager
	 *            the mManager to set
	 */
	public static WDCCP2PManager iInstantiateManager(Context context) {

		if (getmManager() != null) {
			Log.d(TAG,
					"--------------------WDCCP2PManager is NOT null------------------------------ ");

		} else {
			Log.d(TAG,
					"--------------------WDCCP2PManager is null------------------------------ ");
			mManager = new WDCCP2PManager(context);
		}

		return mManager;
	}

	/**
	 * @return WDCCP2PManager
	 */
	public synchronized static WDCCP2PManager getWDCCP2PManager() {
		Log.d(TAG, "IN getWDCCP2PManager");

		return getmManager();
	}

	/**
	 * 
	 */
	public void cMStartConnection() {

	}

	public void registerBroadCastReceiver() {
		Log.d(TAG, "IN registerBroadCastReceiver");

		mContext.registerReceiver(mP2PBroadcastReceiver,
				mP2PBroadcastReceiver.intentFilter);

	}

	public void deregisterBroadCastReceiver() {
		Log.d(TAG, "IN deregisterBroadCastReceiver");
		mContext.unregisterReceiver(mP2PBroadcastReceiver);
	}

	public boolean notifyServicesChanged(WDCCP2PService service, boolean add) {
		Log.d(TAG, "notifyServicesChanged");
		if (add) {
			Log.d(TAG, "notifyServicesChanged adding service to list");
			mServiceList.add(service);
		}
		if (mDevListListener == null) {
			Log.d(TAG, "mDevListListener == null");
			return false;

		} else {
			mDevListListener.notifyServicesChanged(service, add);
			return false;

		}

	}

	public boolean notifyDeviceDisconnected(WifiP2pDevice device) {
		return false;
	}


	/**
	 * @param devListListener
	 *            the devListListener to set
	 */
	public void registerDevListListener(
			WDCCViewerManager.DevList devListListener) {
		Log.d(TAG, "registerDevListListener");
		mDevListListener = null;
		mDevListListener = devListListener;
	}

	public void deregisterDevListListener() {
		mDevListListener = null;
	}

	/**
	 * 
	 */
	public List<WDCCP2PService> getServiceList() {
		Log.d(TAG, "getServiceList");
		return mServiceList;
		// TODO Auto-generated method stub

	}

}
