/**
 * 
 */
package com.manager.cago;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

import com.manager.cago.listeners.SessionListenerImp;

import android.os.Handler;
import android.util.Log;

/**
 * @author zahab
 * 
 */
public class WDCCClientSocketMgr extends Thread  {
	protected static final String TAG = WDCCClientSocketMgr.class
			.getSimpleName();

	private WDCCChatMgr mChatMgr;
	private InetAddress mAddress;
	private WDCCP2PManager mManager = null;
	private Socket socket = null;

	private SessionListenerImp mSessionListener = null;

	/**
	 * @param InetAddress
	 *            groupOwnerAddress
	 * @param Handler
	 *            handler
	 */
	public WDCCClientSocketMgr(Handler handler, InetAddress groupOwnerAddress) {
		Log.d(TAG, "WDCCClientSocketMgr");
		this.mAddress = groupOwnerAddress;
		if (groupOwnerAddress == null) {
			Log.d(TAG, "groupOwnerAddress == null");
		}
		mManager = WDCCP2PManager.getWDCCP2PManager();
		mSessionListener = new SessionListenerImp() {

			/*
			 * (non-Javadoc)
			 * 
			 * @see
			 * com.manager.cago.listeners.SessionListenerImp#onRemoveGroupSuccess
			 * ()
			 */
			@Override
			public void onRemoveGroupSuccess() {
				try {
					socket.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				super.onRemoveGroupSuccess();
			}

			/*
			 * (non-Javadoc)
			 * 
			 * @see com.manager.cago.listeners.SessionListenerImp#onChatStart()
			 */
			@Override
			public void onChatStart() {
				// TODO Auto-generated method stub
				super.onChatStart();
			}

			/*
			 * (non-Javadoc)
			 * 
			 * @see com.manager.cago.listeners.SessionListenerImp#onChatFinish()
			 */
			@Override
			public void onChatFinish() {
				super.onChatFinish();
			}

		};
		mManager.registerSessionListener(mSessionListener);
	}
	
	
	 @Override
	    public void run() {
	        socket = new Socket();
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
