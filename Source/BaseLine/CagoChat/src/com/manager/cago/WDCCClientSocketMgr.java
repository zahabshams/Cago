/**
 * 
 */
package com.manager.cago;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;


import com.viewer.cagochat.WDCCBroadcastReceiver;

import android.os.Handler;
import android.util.Log;

/**
 * @author zahab
 * 
 */
public class WDCCClientSocketMgr extends Thread {
	protected static final String TAG = WDCCBroadcastReceiver.class
			.getSimpleName();

	//private Handler mHandler;
	private WDCCChatMgr mChatMgr;
	private InetAddress mAddress;
	private WDCCP2PManager mManager = null;

	/**
	 * @param InetAddress
	 *            groupOwnerAddress
	 * @param Handler
	 *            handler
	 */
	public WDCCClientSocketMgr(Handler handler, InetAddress groupOwnerAddress) {
		Log.d(TAG, "WDCCClientSocketMgr");
		//this.mHandler = handler;
		this.mAddress = groupOwnerAddress;
		mManager = WDCCP2PManager.getWDCCP2PManager();
	}
	 @Override
	    public void run() {
	        Socket socket = new Socket();
	        try {
	            socket.bind(null);
	            socket.connect(new InetSocketAddress(mAddress.getHostAddress(),
	            		4545), 5000);
	            Log.d(TAG, "Launching the I/O handler");
	            mChatMgr = mManager.startChat(socket);/*new WDCCChatMgr(socket, handler);*/
	            new Thread(mChatMgr).start();
	        } catch (IOException e) {
	            e.printStackTrace();
	            try {
	                socket.close();
	            } catch (IOException e1) {
	                e1.printStackTrace();
	            }
	            return;
	        }
	    }

	    public WDCCChatMgr getChat() {
	        return mChatMgr;
	    }

}
