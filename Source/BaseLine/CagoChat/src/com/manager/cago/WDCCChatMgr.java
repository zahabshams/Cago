/**
 * 
 */
package com.manager.cago;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import android.os.Handler;
import android.util.Log;

import com.manager.cago.listeners.SessionListenerImp;
import com.viewer.cagochat.ChatActivity_Test;

/**
 * @author zahab
 * 
 */
public class WDCCChatMgr implements Runnable {

	protected static final String TAG = WDCCChatMgr.class.getSimpleName();
	private Socket socket = null;
	private Handler handler = null;
	private InputStream iStream;
	private OutputStream oStream;
	private WDCCP2PManager mManager;

	private SessionListenerImp mSessionListener = new SessionListenerImp(){
		public void onChatFinish() {
			Log.d(TAG,"onChatFinish");
			setHandler(null);
		};
	};
	/**
	 * @return the socket
	 */
	private Socket getSocket() {
		synchronized (socket) {
		return socket;
		}
	}

	/**
	 * @param socket the socket to set
	 */
	private void setSocket(Socket socket) {
		synchronized (socket) {
			this.socket = socket;
		}
	}


	/**
	 * @param socket
	 * 
	 */
	public WDCCChatMgr(Socket socket) {
		setSocket(socket);
		mManager = WDCCP2PManager.getWDCCP2PManager();
		mManager.stopConnectionInfoListener();	//After starting chat I am not interested in the connectioninfo.
		mManager.registerSessionListener(mSessionListener);
		}

	/*
	 * public interface MessageTarget { public Handler getHandler(); }
	 */

	/**
	 * @param handler
	 *            the handler to set
	 */
	public void setHandler(Handler handler) {
		synchronized (handler) {
			this.handler = handler;
	}
		
	
	}

	public Handler getHandler() {
		synchronized (handler) {
			return handler;
		}
	}

	@Override
	public void run() {
		try {

			iStream = getSocket().getInputStream();
			oStream = getSocket().getOutputStream();
			byte[] buffer = new byte[1024];
			int bytes;
			while (getSocket().isConnected()/*true*/) {
				try {
					Log.d(TAG, "isConnected is true");
					bytes = iStream.read(buffer);
					if (bytes == -1) {
						break;
					}

					// Send the obtained bytes to the UI Activity
					Log.d(TAG, "Rec:" + String.valueOf(buffer));
					Log.d(TAG, "data -----" + buffer.toString());
					if (getHandler() != null) {
						getHandler().obtainMessage(ChatActivity_Test.MESSAGE_READ,
								bytes, -1, buffer).sendToTarget();
					}

				} catch (IOException e) {
					Log.e(TAG, "disconnected", e);
					getSocket().close();
					return;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				getSocket().close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	public void write(byte[] buffer) {
		try {
			oStream.write(buffer);
		} catch (IOException e) {
			Log.e(TAG, "Exception during write", e);
		}
	}

	public void closeSocketConnection() {
		Log.d(TAG, "closeSocketConnection");
		try {
			getSocket().close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
