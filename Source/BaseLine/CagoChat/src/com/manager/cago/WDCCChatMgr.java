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

import com.viewer.cagochat.ChatActivity_Test;

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

	/**
	 * @param handler
	 * @param socket
	 * 
	 */
	public WDCCChatMgr(Socket socket) {
		this.socket = socket;
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
			oStream = socket.getOutputStream();
			byte[] buffer = new byte[1024];
			int bytes;
			while (true) {
				try {
					// Read from the InputStream
					bytes = iStream.read(buffer);
					if (bytes == -1) {
						break;
					}

					// Send the obtained bytes to the UI Activity
					Log.d(TAG, "Rec:" + String.valueOf(buffer));
					Log.d(TAG, "data -----" + buffer.toString());
					if (handler != null) {
						handler.obtainMessage(ChatActivity_Test.MESSAGE_READ,
								bytes, -1, buffer).sendToTarget();
					}

				} catch (IOException e) {
					Log.e(TAG, "disconnected", e);
				}
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

	public void write(byte[] buffer) {
		try {
			oStream.write(buffer);
		} catch (IOException e) {
			Log.e(TAG, "Exception during write", e);
		}
	}

}
