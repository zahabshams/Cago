/**
 * 
 */
package com.viewer.cagochat;

import android.app.Activity;

import com.manager.cago.WDCCP2PService;

/**
 * @author zahab
 *
 */
public abstract class WDCCViewerManager {
	public Activity mDeviceList;
	public Activity mScanning;
	public Activity mChat;

	

	/**
	 * 
	 */
	public WDCCViewerManager() {
		// TODO Auto-generated constructor stub
	}
	interface DevList{
		public void notifyDeviceList(WDCCP2PService service, boolean add);
	}
	interface ChatActivity{
		public void notifyChatActivity();

	}

	
}
