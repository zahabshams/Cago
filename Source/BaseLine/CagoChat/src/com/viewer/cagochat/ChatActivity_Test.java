package com.viewer.cagochat;

import com.manager.cago.WDCCChatMgr;
import com.manager.cago.WDCCP2PManager;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
/**
 * 
 * @author zahab
 *<blockquote>
 *This activity handles the chat messages coming from ChatManager
 */
public class ChatActivity_Test extends ActionBarActivity implements
		Handler.Callback {
	public static final int MESSAGE_READ = 0x400 + 1;
	public static final int MY_HANDLE = 0x400 + 2;
	protected static final String TAG = ChatActivity_Test.class.getSimpleName();
	private WiFiChatFragment chatFragment;
	private Handler m_Msg_handler = new Handler(this);
	private WDCCP2PManager mManager = WDCCP2PManager.getWDCCP2PManager();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chat);
		chatFragment = new WiFiChatFragment();
		if (savedInstanceState == null) {
			getFragmentManager()
					.beginTransaction()
					.add(R.id.container, chatFragment /* new WiFiChatFragment() */)
					.commit();
		}
		//Adds images to the action bar
		ActionBar actionBar = getSupportActionBar();
		
		actionBar.setDisplayShowCustomEnabled(true);			

		LayoutInflater inflator = (LayoutInflater) this .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = inflator.inflate(R.layout.actionbarlayout, null);

		actionBar.setCustomView(v);
		mManager.setChatMsgHandler(m_Msg_handler);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		getMenuInflater().inflate(R.menu.chat, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

/*	*//**
	 * A placeholder fragment containing a simple view.
	 *//*
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_chat_test,
					container, false);

			return rootView;
		}
	}
*/
	public Handler getChatMsgHandler() {
		return m_Msg_handler;
	}

	public void setChatMsgHandler() {
		mManager.setChatMsgHandler(getChatMsgHandler());

	}

	/*
	 * (non-Javadoc)
	 * 
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
	/* (non-Javadoc)
	 * @see android.app.Activity#onKeyUp(int, android.view.KeyEvent)
	 */
	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK ){
			mManager.closeDownChat(true);
			this.finish();
		/*	Intent intent = new Intent(this, WDCCScanningActivity.class);
			startActivity(intent);*/
		} else if (keyCode == KeyEvent.KEYCODE_HOME) {
			this.finish();
			mManager.closeDownChat(false);
		} 
		return super.onKeyUp(keyCode, event);
	}
	@Override
protected void onDestroy() {
		//mManager.closeDownChat(false);
		super.onDestroy();
}
}
