/**
 * 
 */
package com.manager.cago;

/**
 * @author zahab
 *
 */
public class ConnectionManager {
	
	static ConnectionManager mCManager;
	/**
	 * 
	 */
	
	protected ConnectionManager() {
		// TODO Auto-generated constructor stub
	}
	/**
	 * @return ConnectionManager
	 */
	public static ConnectionManager getConnectionManager() {
		if (mCManager != null) {
			mCManager = new ConnectionManager();
		}
		return mCManager;

	}
	/**
	 * 
	 */
	public void cMStartConnection(){
		
	}
}
