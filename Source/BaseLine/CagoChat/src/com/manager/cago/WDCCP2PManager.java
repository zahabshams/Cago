/**
 * 
 */
package com.manager.cago;

/**
 * @author zahab
 *
 */
public class WDCCP2PManager {
	
	static WDCCP2PManager mCManager;
	/**
	 * 
	 */
	
	protected WDCCP2PManager() {
		// TODO Auto-generated constructor stub
	}
	/**
	 * @return WDCCP2PManager
	 */
	public static WDCCP2PManager getConnectionManager() {
		if (mCManager != null) {
			mCManager = new WDCCP2PManager();
		}
		return mCManager;

	}
	/**
	 * 
	 */
	public void cMStartConnection(){
		
	}
}
