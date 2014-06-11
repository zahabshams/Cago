
/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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

import com.manager.cago.WDCCP2PService;
/**
 * A ListFragment that displays available mServiceDeviceList on discovery and requests the
 * parent activity to handle user interaction events
 */
public class WDCCList_Fragment extends ListFragment  implements WDCCViewerManager.DevList{

   // private List<WifiP2pDevice> mServiceDeviceList = new ArrayList<WifiP2pDevice>();
	private List<WDCCP2PService> mServiceDeviceList = new ArrayList<WDCCP2PService>();
    ProgressDialog progressDialog = null;
    View mContentView = null;
    private WifiP2pDevice device;
    public WiFiPeerListAdapter mPeerListAdapter;
    public WDCCList_Fragment(){
    	
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mPeerListAdapter = new WiFiPeerListAdapter(getActivity(), R.layout.row_devices, mServiceDeviceList);
        this.setListAdapter(mPeerListAdapter/*new WiFiPeerListAdapter(getActivity(), R.layout.row_devices, mServiceDeviceList)*/);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContentView = inflater.inflate(R.layout.device_list, null);
        return mContentView;
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
        WifiP2pDevice device = (WifiP2pDevice) getListAdapter().getItem(position);
        ((DeviceActionListener) getActivity()).showDetails(device);
    }

    /**
     * Array adapter for ListFragment that maintains WifiP2pDevice list.
     */
    private class WiFiPeerListAdapter extends ArrayAdapter<WDCCP2PService> {

        private List<WifiP2pDevice> items;

        /**
         * @param context
         * @param textViewResourceId
         * @param objects
         */
        public WiFiPeerListAdapter(Context context, int textViewResourceId,
                List<WDCCP2PService> objects) {
            super(context, textViewResourceId, objects);
            /* WDCCP2PService d1,d2,d3,d4;
            d1 = new WDCCP2PService();
            d2 = new WDCCP2PService();
            d3 = new WDCCP2PService();
            d4 = new WDCCP2PService();
            d1.deviceName = "zahab";
            d2.deviceName = "shams";
            d3.deviceName = "Mohammad";
            d4.deviceName = "abc";
            items = objects;
            items.add(d4);
            items.add(d2);
            items.add(d3);
            items.add(d1);*/

            

        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;
            if (v == null) {
                LayoutInflater vi = (LayoutInflater) getActivity().getSystemService(
                        Context.LAYOUT_INFLATER_SERVICE);
                //v = vi.inflate(R.layout.row_devices, null);
                v = vi.inflate(R.layout.peerlist, null);
            }
            WifiP2pDevice device = items.get(position);
            if (device != null) {
                TextView top = (TextView) v.findViewById(R.id.txt);
                //TextView bottom = (TextView) v.findViewById(R.id.device_details);
                if (top != null) {
                    top.setText(device.deviceName);
                }
               /* if (bottom != null) {
                    bottom.setText(getDeviceStatus(device.status));
                }*/
            }

            return v;

        }
    }

    /**
     * Update UI for this device.
     * 
     * @param device WifiP2pDevice object
     */
    public void updateThisDevice(WifiP2pDevice device) {
        this.device = device;
        TextView view = (TextView) mContentView.findViewById(R.id.my_name);
        view.setText(device.deviceName);
        view = (TextView) mContentView.findViewById(R.id.my_status);
        view.setText(getDeviceStatus(device.status));
    }

    /*@Override
    public void onPeersAvailable(WifiP2pDeviceList peerList) {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        mServiceDeviceList.clear();
        mServiceDeviceList.addAll(peerList.getDeviceList());
        ((WiFiPeerListAdapter) getListAdapter()).notifyDataSetChanged();
        if (mServiceDeviceList.size() == 0) {
            Log.d("TAG", "No devices found");
            return;
        }

    }*/

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
        progressDialog = ProgressDialog.show(getActivity(), "Press back to cancel", "finding mServiceDeviceList", true,
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

	/* (non-Javadoc)
	 * @see com.viewer.cagochat.WDCCViewerManager.DevList#notifyDeviceList(com.manager.cago.WDCCP2PService, boolean)
	 */
	@Override
	public void notifyDeviceList(WDCCP2PService service, boolean add) {
		// TODO Auto-generated method stub
		
	}

}
