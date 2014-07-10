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
	 * @return the mDeviceList
	 */
	public Activity getmDeviceList() {
		return mDeviceList;
	}

	/**
	 * @param mDeviceList
	 *            the mDeviceList to set
	 */
	public void setmDeviceList(Activity mDeviceList) {
		this.mDeviceList = mDeviceList;
	}

	/**
	 * @return the mScanning
	 */
	public Activity getmScanning() {
		return mScanning;
	}

	/**
	 * @param mScanning
	 *            the mScanning to set
	 */
	public void setmScanning(Activity mScanning) {
		this.mScanning = mScanning;
	}

	/**
	 * @return the mChat
	 */
	public Activity getmChat() {
		return mChat;
	}

	/**
	 * @param mChat
	 *            the mChat to set
	 */
	public void setmChat(Activity mChat) {
		this.mChat = mChat;
	}

	/**
	 * 
	 */
	public WDCCViewerManager() {
		// TODO Auto-generated constructor stub
	}

	public void getChatMgr(){
		
	}
	public interface DevList {
		public void notifyServicesChanged(WDCCP2PService service, int operation);
	}

	public interface ChatActivity {
		public void notifyChatActivity();

	}

}
