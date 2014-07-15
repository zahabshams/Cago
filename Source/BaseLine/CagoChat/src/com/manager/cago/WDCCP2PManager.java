/**
 * @author zahab <blockquote> Singleton class to handle discovery, connection
 *         and data to UI layer. This class will act as a bridge between UI and
 *         P2P operations related to registration, discovery,connection
 * 
 */
package com.manager.cago;

import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.Channel;
import android.os.Handler;
import android.util.Log;

import com.manager.cago.WDCCP2PService.serviceOperation;
import com.manager.cago.listeners.SessionListenerImp;
import com.manager.cago.listeners.SessionListeners;
import com.viewer.cagochat.ChatActivity_Test;
import com.viewer.cagochat.WDCCBroadcastReceiver;
import com.viewer.cagochat.WDCCDevice_ListActivity;
import com.viewer.cagochat.WDCCPeerlistener;
import com.viewer.cagochat.WDCCViewerManager;

/**
 * @author zahab <blockquote> Singleton class to handle discovery, connection
 *         and data to UI layer. This class will act as a bridge between UI and
 *         P2P operations related to registration, discovery,connection
 * 
 */
public class WDCCP2PManager {
	public WDCCPeerlistener mWDPeerlistener = null;
	public WDCCBroadcastReceiver mP2PBroadcastReceiver = null;
	private WifiP2pManager mAndroidP2Pmanager = null;
	public Context mContext = null;
	private LinkedList<SessionListenerImp> mSessionlistenerList= new LinkedList<SessionListenerImp>();
	/**
	 * <blockquote>
	 * the synchronised to hold the P2P devices with specified service.
	 */
	private List<WDCCP2PService> mServiceList = null;
	private Channel mChannel = null;
	private WDCCConnectionMgr mConnectionMgr = null;
	private WDCCServiceManager mServiceManager = null;
	private WDCCViewerManager.DevList mDevListListener;
	private Runnable run = null;
	protected static final String TAG = WDCCP2PManager.class.getSimpleName();
	private WDCCChatMgr mChatMgr = null;

	protected WDCCP2PManager(Context context) {
		Log.d(TAG, "WDCCP2PManager created");
		mContext = context;
	}

	public void setupP2P(){
		run = new Runnable() {

			@Override
			public void run() {
				initialise();
			}
		};
		Thread thread = new Thread(run);
		thread.start();		
	}
	/**
	 * @return the mConnectionMgr
	 */
	public WDCCConnectionMgr getConnectionMgr() {
		return mConnectionMgr;
	}

	private static WDCCP2PManager mManager = null;

	/**
	 * @return the mManager
	 */
	private static WDCCP2PManager getmManager() {
		Log.d(TAG,"Test="+mManager);
	//	mManager = WDCCP2PManager.iInstantiateManager(this);
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
	 * @return the mChatMgr
	 */
	public WDCCChatMgr getChatMgr() {
		return mChatMgr;
	}

	/**
	 * 
	 */
	public WDCCChatMgr startChat(Socket socket) {
		mChatMgr = new WDCCChatMgr(socket);
		Intent intent = new Intent(mContext, ChatActivity_Test.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		mContext.startActivity(intent);
		return mChatMgr;

	}

	public void setChatMsgHandler(Handler handler) {
		if (mChatMgr != null)
			mChatMgr.setHandler(handler);

	}

	public void deregisterBroadCastReceiver() {
		Log.d(TAG, "IN deregisterBroadCastReceiver");
		mContext.unregisterReceiver(mP2PBroadcastReceiver);
	}

	public void deregisterDevListListener() {
		mDevListListener = null;
	}

	/**
	 * @return the mChannel
	 */
	public Channel getP2PChannel() {
		return mChannel;
	}

	/**
	 * @return the mAndroidP2Pmanager
	 */
	public WifiP2pManager getAndroidP2Pmanager() {
		return mAndroidP2Pmanager;
	}

	/**
	 * @return the mContext
	 */
	public Context getappContext() {
		return mContext;
	}

	/**
	 * 
	 */
	public List<WDCCP2PService> getServiceList() {
		Log.d(TAG, "getServiceList");
		return mServiceList;

	}

	/**
	 * @return
	 * 
	 */
	private void initialise() {
		Log.d(TAG, "-------------------initialise---------------------------");
		mServiceList = Collections
				.synchronizedList(new ArrayList<WDCCP2PService>());
		mWDPeerlistener = new WDCCPeerlistener();
		mAndroidP2Pmanager = (WifiP2pManager) mContext
				.getSystemService(Context.WIFI_P2P_SERVICE);
		// zahab need to work on third argument of initialize which is null now.
		
		mChannel = mAndroidP2Pmanager.initialize(mContext,
				mContext.getMainLooper(), null);
		mConnectionMgr = new WDCCConnectionMgr();

		mP2PBroadcastReceiver = new WDCCBroadcastReceiver(mAndroidP2Pmanager,
				mChannel, mWDPeerlistener);
		mServiceManager = new WDCCServiceManager(mChannel, mAndroidP2Pmanager,
				this);
		startRegistrationAndDiscovery();
		
	}

	public boolean notifyDeviceDisconnected(WifiP2pDevice device) {
		return false;
	}

	public boolean notifyServicesChanged(WDCCP2PService service, int operation) {
		Log.d(TAG, "notifyServicesChanged");
		int opt = -1;
		String instancename = service.instanceName;
		String macaddr = service.device.deviceAddress;
		ListIterator<WDCCP2PService> litr = mServiceList.listIterator();
		while (litr.hasNext()) {
			Log.d(TAG, "while-----------1");
			WDCCP2PService element = (WDCCP2PService) litr.next();

			if (element.instanceName.equalsIgnoreCase(instancename)
					&& element.device.deviceAddress.equals(macaddr)) {

				/**
				 * <tbody> When the device has already been added to the list
				 */
				element = service;

				// updateDevInfoInList(element, service);
				opt = serviceOperation.UPDATE_SERIVICE;

			}
			if (mDevListListener == null) {
				Log.d(TAG, "mDevListListener == null");
				return false;

			} else {
				mDevListListener.notifyServicesChanged(service, opt);
				return true;

			}
		}
		if (operation == serviceOperation.ADD_SERVICE) {
			Log.d(TAG, "notifyServicesChanged adding service to list");
			mServiceList.add(service);
			opt = serviceOperation.ADD_SERVICE;
		}
		if (mDevListListener == null) {
			Log.d(TAG, "mDevListListener == null");
			return false;

		} else {
			mDevListListener.notifyServicesChanged(service, opt);
			return true;

		}

	}

	/**
	 * @param element
	 */
	@SuppressWarnings("unused")
	private void updateDevInfoInList(WDCCP2PService element,
			WDCCP2PService service) {
		element.device = service.device;
		notifyServicesChanged(null, serviceOperation.UPDATE_SERIVICE);

	}

	public void registerBroadCastReceiver() {
		Log.d(TAG, "IN registerBroadCastReceiver");

		mContext.registerReceiver(mP2PBroadcastReceiver,
				mP2PBroadcastReceiver.intentFilter);
	}

	/**
	 * @param devListListener
	 *            the devListListener to set
	 */
	public void registerDevListListener(
			WDCCViewerManager.DevList devListListener) {
		Log.d(TAG, "registerDevListListener");
	//	mDevListListener = null;
		mDevListListener = devListListener;
	}

	public boolean removeAndStopServiceDisc() {
		mServiceList.clear();
		return mServiceManager.removeAndStopServiceDisc();
	}

	public boolean startRegistrationAndDiscovery() {
		mServiceList.clear();
		mServiceManager.startRegistrationAndDiscovery();
		return false;

	}

	public void closeDownChat() {
		removeAndStopServiceDisc();
		stopConnectionInfoListener();
		mAndroidP2Pmanager.removeGroup(mChannel, null);	
	}
	public void stopConnectionInfoListener(){
		mAndroidP2Pmanager.requestConnectionInfo(mChannel, null);

	}
	public void removeGroup(){
		mAndroidP2Pmanager.removeGroup(mChannel, null);	
	}
	public boolean stopServiceDiscovery() {
		return mServiceManager.stopServiceDiscovery();
	}

	public void startServiceDiscovery() {
		mServiceList.clear();
		mServiceManager.startServiceDiscovery();
	}

	public void connectP2p(WDCCP2PService service) {
		mConnectionMgr.connectP2p(service);
	}
	
	public void registerSessionListener(SessionListenerImp listener){

        Log.i(TAG, "registerMirrorLinkSessionListner("+(listener==null?"null":"")+")");
        if(listener == null) {
            return;
        }
        
        synchronized (mSessionlistenerList) {
			if (mSessionlistenerList.contains(listener)) {
				Log.w(TAG, "MirrorLinkSessionListner already known");
				return;
			}
			mSessionlistenerList.add(listener);
        }
    
		mSessionlistenerList.add(listener);
	}
	public void deregisterSessionListener(SessionListenerImp listener){

        Log.i(TAG, "deregisterSessionListener("+(listener==null?"null":"")+")");
        if(listener == null) {
            return;
        }

		synchronized (mSessionlistenerList) {
			if (!mSessionlistenerList.remove(listener)) {
				Log.w(TAG, "SessionListner not known");
			}
		}
    
	}
}