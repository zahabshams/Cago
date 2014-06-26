package com.viewer.cagochat;

import java.util.ArrayList;
import java.util.List;

import android.app.ListFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.manager.cago.WDCCP2PManager;
import com.manager.cago.WDCCP2PService;

/**
 * A ListFragment that displays available mServiceDeviceList on discovery and
 * requests the parent activity to handle user interaction events
 */
public class WDCCList_Fragment extends ListFragment implements
		WDCCViewerManager.DevList {

	private List<WDCCP2PService> mServiceDeviceList = new ArrayList<WDCCP2PService>();
	ProgressDialog progressDialog = null;
	View mContentView = null;
	private WifiP2pDevice device;
	public WiFiPeerListAdapter mPeerListAdapter;
	private WDCCP2PManager mManager;
	public Context mContext = null;
	protected static final String TAG = WDCCList_Fragment.class.getSimpleName();

	public WDCCList_Fragment() {
		Log.d(TAG, "WDCCList_Fragment created");

		mManager = WDCCP2PManager.getWDCCP2PManager();
		mContext = mManager.getappContext();

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(TAG, "onCreate registerDevListListener");
		mManager.registerDevListListener(this);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.d(TAG, "onCreateView");
		mPeerListAdapter = new WiFiPeerListAdapter(getActivity(),
				R.layout./*peerlist*/row_devices, mServiceDeviceList);
		 /* android.R.layout.simple_list_item_2, android.R.id.text1*/
		 /* mPeerListAdapter = new WiFiPeerListAdapter(getActivity(),
				  android.R.layout.simple_list_item_2, mServiceDeviceList);*/
		this.setListAdapter(mPeerListAdapter);

		if (savedInstanceState == null) {
			Log.d(TAG, "savedInstanceState == null");

			mContentView = inflater.inflate(R.layout.device_list, null);
		}

		return mContentView;
	}

	@Override
	public void onSaveInstanceState(Bundle bundle) {
		super.onSaveInstanceState(bundle);
		Log.d(TAG, "onSaveInstanceState");

	}

	@Override
	public void onPause() {
		Log.d(TAG, "onPause");
		super.onPause();
	}

	@Override
	public void onResume() {
		Log.d(TAG, "onResume");
		mManager.registerDevListListener(this);
		mServiceDeviceList.clear();
		mServiceDeviceList.addAll(mManager.getServiceList());
		mPeerListAdapter.notifyDataSetChanged();
		super.onStop();
	}

	@Override
	public void onStop() {
		Log.d(TAG, "onStop");

		super.onStop();
	}

	/**
	 * @return this device
	 */
	public WifiP2pDevice getDevice() {
		return device;
	}

	private static String getDeviceStatus(int deviceStatus) {
		Log.d("TAG", "Peer status :" + deviceStatus);
		switch (deviceStatus) {
		case WifiP2pDevice.AVAILABLE:
			return "Available";
		case WifiP2pDevice.INVITED:
			return "Invited";
		case WifiP2pDevice.CONNECTED:
			return "Connected";
		case WifiP2pDevice.FAILED:
			return "Failed";
		case WifiP2pDevice.UNAVAILABLE:
			return "Unavailable";
		default:
			return "Unknown";

		}
	}

	/**
	 * Initiate a connection with the peer.
	 */
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		Log.d(TAG,"----------onListItemClick----------------");
		WDCCP2PService service = (WDCCP2PService) getListAdapter().getItem(position);
		mManager.connectP2p(service);
		/*WifiP2pDevice device = (WifiP2pDevice) getListAdapter().getItem(
				position);
		((DeviceActionListener) getActivity()).showDetails(device);*/
	}

	/**
	 * Array adapter for ListFragment that maintains WifiP2pDevice list.
	 */
	private class WiFiPeerListAdapter extends ArrayAdapter<WDCCP2PService> {

		// private List<WDCCP2PService> items;

		/**
		 * @param context
		 * @param textViewResourceId
		 * @param objects
		 */
		public WiFiPeerListAdapter(Context context, int textViewResourceId,
				List<WDCCP2PService> objects) {
			super(context, textViewResourceId, objects);
			// items = mServiceDeviceList;
			Log.d(TAG, "WiFiPeerListAdapter");

		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			Log.d(TAG, "getView");
			View v = convertView;
			if (v == null) {
				LayoutInflater vi = (LayoutInflater) getActivity()
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				//v = vi.inflate(R.layout.peerlist, null);
				v = vi.inflate(R.layout.row_devices, null);
			}
			WDCCP2PService service = mServiceDeviceList.get(position);// items.get(position);
			WifiP2pDevice device = service.device;
			if (device != null) {
				TextView top = (TextView) v.findViewById(R.id.device_name/*txt*/);
				if (top != null) {
					top.setText(device.deviceName);
				}

			}

			return v;

		}
	}

	/**
	 * Update UI for this device.
	 * 
	 * @param device
	 *            WifiP2pDevice object
	 */
	public void updateThisDevice(WifiP2pDevice device) {
		this.device = device;
		TextView view = (TextView) mContentView.findViewById(R.id.my_name);
		view.setText(device.deviceName);
		view = (TextView) mContentView.findViewById(R.id.my_status);
		view.setText(getDeviceStatus(device.status));
	}

	public void clearPeers() {
		mServiceDeviceList.clear();
		((WiFiPeerListAdapter) getListAdapter()).notifyDataSetChanged();
	}

	/**
     * 
     */
	public void onInitiateDiscovery() {
		if (progressDialog != null && progressDialog.isShowing()) {
			progressDialog.dismiss();
		}
		progressDialog = ProgressDialog.show(getActivity(),
				"Press back to cancel", "finding mServiceDeviceList", true,
				true, new DialogInterface.OnCancelListener() {

					@Override
					public void onCancel(DialogInterface dialog) {

					}
				});
	}

	/**
	 * An interface-callback for the activity to listen to fragment interaction
	 * events.
	 */
	public interface DeviceActionListener {

		void showDetails(WifiP2pDevice device);

		void cancelDisconnect();

		void connect(WifiP2pConfig config);

		void disconnect();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.viewer.cagochat.WDCCViewerManager.DevList#notifyDeviceList(com.manager
	 * .cago.WDCCP2PService, boolean)
	 */
	@Override
	public void notifyServicesChanged(WDCCP2PService service, boolean add) {
		Log.d(TAG, "notifyDeviceList");
		if (mPeerListAdapter == null) {
			Log.d(TAG, "mPeerListAdapter IS NULL");
			return;

		} else {
			if (add)
				mPeerListAdapter.add(service);
			else
				mPeerListAdapter.remove(service);

			mPeerListAdapter.notifyDataSetChanged();

		}

	}

}
