/**
 * 
 */
package com.manager.cago;

import android.net.wifi.p2p.WifiP2pDevice;

/**
 * @author zahab
 * 
 *         A structure to hold service information.
 */

public class WDCCP2PService {
	public WifiP2pDevice device;
	public String instanceName = null;
	public String serviceRegistrationType = null;

	public static class serviceOperation {
		public static final int ADD_SERVICE = 0;
		public static final int REMOVE_SERIVICE = 1;
		public static final int UPDATE_SERIVICE = 2;
	}
}
