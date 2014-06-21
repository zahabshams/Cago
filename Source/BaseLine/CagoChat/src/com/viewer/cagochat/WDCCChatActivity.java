/**
 * 
 */
package com.viewer.cagochat;

import com.manager.cago.WDCCChatMgr;
import com.manager.cago.WDCCP2PManager;

import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.os.Handler;
import android.os.Message;


/**
 * @author zahab
 *
 */
public class WDCCChatActivity extends ActionBarActivity implements Handler.Callback/*, MessageTarget*/ {
	 public static final int MESSAGE_READ = 0x400 + 1;
	 public static final int MY_HANDLE = 0x400 + 2;
	 protected static final String TAG = WDCCChatActivity.class.getSimpleName();
	 private WiFiChatFragment chatFragment;
	 private Handler m_Msg_handler = new Handler(this);
	 private WDCCP2PManager mManager = WDCCP2PManager.getWDCCP2PManager();

	/**
	 * 
	 */
	public WDCCChatActivity() {
		// TODO Auto-generated constructor stub
	}
	 public Handler getChatMsgHandler() {
	        return m_Msg_handler;
	    }
	 
	 public void setChatMsgHandler() {
		 mManager.setChatMsgHandler(getChatMsgHandler());
		 
	 }
	/* (non-Javadoc)
	 * @see android.os.Handler.Callback#handleMessage(android.os.Message)
	 */
	@Override
	public boolean handleMessage(Message msg) {
		switch (msg.what) {
         case MESSAGE_READ:
             byte[] readBuf = (byte[]) msg.obj;
             // construct a string from the valid bytes in the buffer
             String readMessage = new String(readBuf, 0, msg.arg1);
             Log.d(TAG, readMessage);
             (chatFragment).pushMessage("Buddy: " + readMessage);
             break;

         case MY_HANDLE:
             Object obj = msg.obj;
             (chatFragment).setChatManager((WDCCChatMgr) obj);

     }
     return true;
	}

}
