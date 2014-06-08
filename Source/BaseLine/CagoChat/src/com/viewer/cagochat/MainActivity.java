package com.viewer.cagochat;

import java.util.HashMap;
import java.util.Map;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.ActionListener;
import android.net.wifi.p2p.WifiP2pManager.Channel;
import android.net.wifi.p2p.WifiP2pManager.ChannelListener;
import android.net.wifi.p2p.WifiP2pManager.ConnectionInfoListener;
import android.net.wifi.p2p.WifiP2pManager.DnsSdServiceResponseListener;
import android.net.wifi.p2p.WifiP2pManager.DnsSdTxtRecordListener;
import android.net.wifi.p2p.nsd.WifiP2pDnsSdServiceInfo;
import android.net.wifi.p2p.nsd.WifiP2pDnsSdServiceRequest;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.manager.cago.ConnectionManager;

public class MainActivity extends ActionBarActivity implements
		Handler.Callback, ConnectionInfoListener, ChannelListener {
	static final int SERVER_PORT = 4545;
	protected static final String TAG = MainActivity.class.getSimpleName();
	public static final String TXTRECORD_PROP_AVAILABLE = "available";
	public static final String SERVICE_INSTANCE = "_wifidemotest";
	public static final String SERVICE_REG_TYPE = "_presence._tcp";

	public static final int MESSAGE_READ = 0x400 + 1;
	public static final int MY_HANDLE = 0x400 + 2;
	private WifiP2pManager manager;
	public WDCHPeerlistener mWDPeerlistener;

	private final IntentFilter intentFilter = new IntentFilter();
	private Channel channel;
	private BroadcastReceiver receiver = null;
	private WifiP2pDnsSdServiceRequest serviceRequest;
	private Handler handler = new Handler(this);
	/*
	 * private WiFiChatFragment chatFragment; private WiFiDirectServicesList
	 * servicesList;
	 */
	private TextView statusTxtView;
	private WiFiDirectBroadcastReceiver mReceiver;

	public Handler getHandler() {
		return handler;
	}

	public void setHandler(Handler handler) {
		this.handler = handler;
	}

	public static ConnectionManager mCManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
		mWDPeerlistener = new WDCHPeerlistener();
		intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
		intentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
		intentFilter
				.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
		intentFilter
				.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);
		// intentFilter.getAction(WifiP2pManager.WIFI_P2P_DISCOVERY_STARTED);

		manager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
		channel = manager.initialize(this, getMainLooper(), this);
		mReceiver = new WiFiDirectBroadcastReceiver(manager, channel, this,
				mWDPeerlistener);

		startRegistrationAndDiscovery();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onResume() {
		super.onResume();
		Log.d(TAG, "onResume");
		registerReceiver(mReceiver, intentFilter);

	}

	@Override
	protected void onPause() {
		super.onPause();
		unregisterReceiver(mReceiver);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
		
			return rootView;
		}
	}

	@Override
	public boolean handleMessage(Message msg) {
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * Registers a local service and then initiates a service discovery
	 */
	private void startRegistrationAndDiscovery() {
		Map<String, String> record = new HashMap<String, String>();
		record.put(TXTRECORD_PROP_AVAILABLE, "visible");

		WifiP2pDnsSdServiceInfo service = WifiP2pDnsSdServiceInfo.newInstance(
				SERVICE_INSTANCE, SERVICE_REG_TYPE, record);
		manager.addLocalService(channel, service, new ActionListener() {

			@Override
			public void onSuccess() {
				appendStatus("Added Local Service");
			}

			@Override
			public void onFailure(int error) {
				appendStatus("Failed to add a service");
			}
		});

		discoverService();

	}

	/*
	 * Register listeners for DNS-SD services. These are callbacks invoked by
	 * the system when a service is actually discovered.
	 */
	private void discoverService() {
		
		manager.discoverPeers(channel, new WifiP2pManager.ActionListener() {

			@Override
			public void onSuccess() {
				Log.d(TAG, "discoverPeers success");
				// Code for when the discovery initiation is successful goes
				// here.
				// No services have actually been discovered yet, so this method
				// can often be left blank. Code for peer discovery goes in the
				// onReceive method, of the broadcast receiver.
			}

			@Override
			public void onFailure(int reasonCode) {
				Log.d(TAG, "discoverPeers fail reasonCode =" + reasonCode);
				// Code for when the discovery initiation fails goes here.
				// Alert the user that something went wrong.
				// zahab add error handling here
			}
		});
	}

	public void appendStatus(String status) {
		Log.d(TAG, "appendStatus = " + status);
		/*
		 * String current = statusTxtView.getText().toString();
		 * statusTxtView.setText(current + "\n" + status);
		 */
	}

	@Override
	public void onConnectionInfoAvailable(WifiP2pInfo info) {
		Log.d(TAG, "onConnectionInfoAvailable" + info.groupOwnerAddress);
		// TODO Auto-generated method stub

	}

	@Override
	public void onChannelDisconnected() {
		Log.d(TAG, "onChannelDisconnected");

		// TODO Auto-generated method stub

	}

}
