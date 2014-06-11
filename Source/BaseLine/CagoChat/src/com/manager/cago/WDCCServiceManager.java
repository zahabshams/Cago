/**
 * 
 */
package com.manager.cago;

import java.util.HashMap;
import java.util.Map;

import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.ActionListener;
import android.net.wifi.p2p.WifiP2pManager.Channel;
import android.net.wifi.p2p.WifiP2pManager.DnsSdServiceResponseListener;
import android.net.wifi.p2p.WifiP2pManager.DnsSdTxtRecordListener;
import android.net.wifi.p2p.nsd.WifiP2pDnsSdServiceInfo;
import android.net.wifi.p2p.nsd.WifiP2pDnsSdServiceRequest;
import android.util.Log;

/**
 * @author zahab
 * For registering DNS service and listening to the available service same signature
 */
public class WDCCServiceManager {

	/**
	 * 
	 */
	protected static final String TAG = WDCCServiceManager.class
			.getSimpleName();
	public static final String TXTRECORD_PROP_AVAILABLE = "available";
	public static final String SERVICE_INSTANCE = "_wifidemotest";
	public static final String SERVICE_REG_TYPE = "_presence._tcp";
	private WifiP2pManager mAndroidP2Pmanager;
	WDCCP2PManager mManager;
	static final int SERVER_PORT = 4545;
	private Channel mChannel;
	private WifiP2pDnsSdServiceRequest serviceRequest;

	public WDCCServiceManager(Channel channel, WifiP2pManager androidP2PManager, WDCCP2PManager manager) {
		
		mAndroidP2Pmanager = androidP2PManager;
		mChannel = channel;
		mManager = manager;
		startRegistrationAndDiscovery();
		// TODO Auto-generated constructor stub
	}

	public void startRegistrationAndDiscovery() {
		Log.d(TAG,"startRegistrationAndDiscovery");
		Map<String, String> record = new HashMap<String, String>();
		record.put(TXTRECORD_PROP_AVAILABLE, "visible");

		WifiP2pDnsSdServiceInfo service = WifiP2pDnsSdServiceInfo.newInstance(
				SERVICE_INSTANCE, SERVICE_REG_TYPE, record);
		Log.d(TAG,"adding LocalService");

		mAndroidP2Pmanager.addLocalService(mChannel, service, new ActionListener() {

			@Override
			public void onSuccess() {
				Log.d(TAG,"addLocalService onSuccess");
				// appendStatus("Added Local Service");
			}

			@Override
			public void onFailure(int error) {
				Log.d(TAG,"addLocalService onFailure");
				// appendStatus("Failed to add a service");
			}
		});

		discoverService();

	}

	private void discoverService() {

		/*
		 * Register listeners for DNS-SD services. These are callbacks invoked
		 * by the system when a service is actually discovered.
		 */
		Log.d(TAG,"In discoverService ");

		mAndroidP2Pmanager.setDnsSdResponseListeners(mChannel,new DnsSdServiceResponseListener() {

					@Override
					public void onDnsSdServiceAvailable(String instanceName,
							String registrationType, WifiP2pDevice srcDevice) {
						Log.d(TAG, "onDnsSdServiceAvailable");
						// A service has been discovered. Is this our app?
						if (instanceName.equalsIgnoreCase(SERVICE_INSTANCE)) {

							WDCCP2PService service = new WDCCP2PService();
							service.device = srcDevice;
							service.instanceName = instanceName;
							service.serviceRegistrationType = registrationType;
							Log.d(TAG, "onBonjourServiceAvailable "
									+ instanceName);
							
							
						}
					}

				}, new DnsSdTxtRecordListener() {

					/**
					 * A new TXT record is available. Pick up the advertised
					 * buddy name.
					 */
					@Override
					public void onDnsSdTxtRecordAvailable(
							String fullDomainName, Map<String, String> record,
							WifiP2pDevice device) {
						Log.d(TAG,
								device.deviceName + " is "
										+ record.get(TXTRECORD_PROP_AVAILABLE));
					
					}
				});

		// After attaching listeners, create a service request and initiate
		// discovery.
		serviceRequest = WifiP2pDnsSdServiceRequest.newInstance();
		mAndroidP2Pmanager.addServiceRequest(mChannel, serviceRequest,
				new ActionListener() {

					@Override
					public void onSuccess() {
						Log.d(TAG, "addServiceRequest onSuccess");
						// appendStatus("Added service discovery request");
					}

					@Override
					public void onFailure(int error) {
						Log.d(TAG, "addServiceRequest onFailure error = " + error);
						// appendStatus("Failed adding service discovery request");
					}
				});
		mAndroidP2Pmanager.discoverServices(mChannel, new ActionListener() {

			@Override
			public void onSuccess() {
				Log.d(TAG, "discoverServices onSuccess");
				// appendStatus("Service discovery initiated");
			}

			@Override
			public void onFailure(int error) {
				Log.d(TAG, "discoverServices onFailure error = " + error);
				// appendStatus("Service discovery failed");

			}
		});
	}

}
