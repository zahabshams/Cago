/**
 * 
 */
package com.manager.cago;

import java.util.HashMap;
import java.util.Map;

import com.manager.cago.WDCCP2PService.serviceOperation;

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
 * @author zahab For registering DNS service and listening to the available
 *         service same signature
 */
public class WDCCServiceManager {

	/**
	 * @return the isLocalServeRegd
	 */
	public boolean isLocalServRegd() {
		return isLocalServeRegd;
	}

	/**
	 * @return the isDiscoveryActive
	 */
	public boolean isDiscoveryActive() {
		return isDiscoveryActive;
	}
	protected static final String TAG = WDCCServiceManager.class
			.getSimpleName();
	public static final String TXTRECORD_PROP_AVAILABLE = "available";
	public static final String SERVICE_INSTANCE = "_wifidemotest";
	public static final String SERVICE_REG_TYPE = "_presence._tcp";
	private WifiP2pManager mAndroidP2Pmanager = null;;
	private WDCCP2PManager mManager = null;;
	static final int SERVER_PORT = 4545;
	private Channel mChannel = null;;
	private WifiP2pDnsSdServiceRequest mServiceRequest = null;
	private WifiP2pDnsSdServiceInfo mServiceInfo = null;
	private boolean ret = false;
	protected boolean isDiscoveryActive = false;
	protected boolean isLocalServeRegd = false;
	static int numberServiceAdded =0;
	static int numberdiscoverer =0;



	/**
	 * @return the serviceRequest
	 */
	public WifiP2pDnsSdServiceRequest getServiceRequest() {
		return mServiceRequest;
	}

	public WDCCServiceManager(Channel channel,
			WifiP2pManager androidP2PManager, WDCCP2PManager manager) {

		mAndroidP2Pmanager = androidP2PManager;
		mChannel = channel;
		mManager = manager;
		addLocalService();
	}

	public void addLocalService() {
		Log.d(TAG, "addLocalService");
		Map<String, String> record = new HashMap<String, String>();
		record.put(TXTRECORD_PROP_AVAILABLE, "visible");

		/*WifiP2pDnsSdServiceInfo*/ mServiceInfo = WifiP2pDnsSdServiceInfo.newInstance(
				SERVICE_INSTANCE, SERVICE_REG_TYPE, record);
		++numberServiceAdded;
		Log.d(TAG, "adding LocalService " + numberServiceAdded);

		mAndroidP2Pmanager.addLocalService(mChannel, mServiceInfo,
				new ActionListener() {

					@Override
					public void onSuccess() {
						Log.d(TAG, "addLocalService onSuccess");
						isLocalServeRegd = true;
						// appendStatus("Added Local Service");
					}

					@Override
					public void onFailure(int error) {
						Log.d(TAG, "addLocalService onFailure");
						// appendStatus("Failed to add a service");
					}
				});

		//discoverService();

	}

	public void discoverService() {
		++numberdiscoverer;
		/**
		 * <blockquote>>Register listeners for DNS-SD services. These are callbacks invoked
		 * by the system when a service is actually discovered.
		 */
		Log.d(TAG, "In discoverService numberdiscoverer =" + numberdiscoverer);
		mAndroidP2Pmanager.setDnsSdResponseListeners(mChannel,
				new DnsSdServiceResponseListener() {

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
							Log.d(TAG, "onDnsSdServiceAvailable---1");

							mManager.notifyServicesChanged(service, serviceOperation.ADD_SERVICE);

							Log.d(TAG, "onDnsSdServiceAvailable --2");

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
		mServiceRequest = WifiP2pDnsSdServiceRequest.newInstance();
		mAndroidP2Pmanager.addServiceRequest(mChannel, mServiceRequest,
				new ActionListener() {

					@Override
					public void onSuccess() {
						isDiscoveryActive = true;
						Log.d(TAG, "addServiceRequest onSuccess");
					}

					@Override
					public void onFailure(int error) {
						Log.d(TAG, "addServiceRequest onFailure error = "
								+ error);
					}
				});
		mAndroidP2Pmanager.discoverServices(mChannel, new ActionListener() {

			@Override
			public void onSuccess() {
				Log.d(TAG, "Service discovery initiated sucessfully");
			}

			@Override
			public void onFailure(int error) {
				Log.d(TAG, "Service discovery initiation failed error = " + error);

			}
		});
	}
	/**
	 */
	public void startServiceDiscovery() {
		discoverService();
	}

	boolean stopServiceDiscovery() {
		Log.d(TAG, "stopServiceDiscovery");
		//mAndroidP2Pmanager.clearServiceRequests(mChannel, new ActionListener() {
		mAndroidP2Pmanager.removeServiceRequest(mChannel, mServiceRequest,new ActionListener() {

			@Override
			public void onSuccess() {
				Log.d(TAG, "removeServiceRequest onSuccess");
				ret = true;
				isDiscoveryActive  = false;
			}

			@Override
			public void onFailure(int arg0) {
				Log.d(TAG, "removeServiceRequest onFailure");
				ret = false;
			}
		});

		return ret;
	}
	 boolean removeService() {
		Log.d(TAG, "Removing the advertised service");
		mAndroidP2Pmanager.removeLocalService(mChannel, mServiceInfo,
				new ActionListener() {

					@Override
					public void onSuccess() {
						Log.d(TAG, "removeLocalService onSuccess");
						isLocalServeRegd = false;
						ret = true;
					}

					@Override
					public void onFailure(int arg0) {
						Log.d(TAG, "removeLocalService onFailure");
						ret = false;
					}
				});
		return ret;
	}
	 public boolean removeAndStopServiceDisc(){
		 boolean ret =stopServiceDiscovery();
		 ret = ret && removeService();
		 return ret;
		 
	 }

	
}
