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

import com.viewer.cagochat.WDCCChatActivity;

/**
 * @author zahab
 * 
 */
public class WDCCChatMgr implements Runnable {
	/**
	 * @param handler
	 *            the handler to set
	 */
	synchronized public void setHandler(Handler handler) {
		this.handler = handler;
	}

	protected static final String TAG = WDCCChatMgr.class.getSimpleName();
	private Socket socket = null;
	private Handler handler;
	private WDCCP2PManager mManager = null;
	/**
	 * @param handler
	 * @param socket
	 * 
	 */
	public WDCCChatMgr(Socket socket/*, Handler handler*/) {
		this.socket = socket;
		mManager= WDCCP2PManager.getWDCCP2PManager();
/*		this.handler = handler;
*/
	}

	public interface MessageTarget {
		public Handler getHandler();
	}

	private InputStream iStream;
	private OutputStream oStream;

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {

		try {
			iStream = socket.getInputStream();
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			oStream = socket.getOutputStream();
		} catch (IOException e) {
			e.printStackTrace();
		}

		byte[] buffer = new byte[1024];
		int bytes;
		// handler.obtainMessage(WDCCChatActivity.MY_HANDLE,
		// this).sendToTarget();

		while (true) {
			try {
				/**
				 * <blockquote> Read from the InputStream
				 */
				bytes = iStream.read(buffer);
				if (bytes == -1) {
					break;
				}

				// Send the obtained bytes to the UI Activity
				Log.d(TAG, "Rec:" + String.valueOf(buffer));
				if (handler != null) {
					handler.obtainMessage(WDCCChatActivity.MESSAGE_READ, bytes,
							-1, buffer).sendToTarget();
				}

			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
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

}
