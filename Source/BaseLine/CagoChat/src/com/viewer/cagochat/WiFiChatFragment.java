package com.viewer.cagochat;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import com.manager.cago.WDCCChatMgr;
import com.manager.cago.WDCCP2PManager;
import com.manager.cago.listeners.SessionListenerImp;

/**
 * This fragment handles chat related UI which includes a list view for messages
 * and a message entry field with send button.
 */
public class WiFiChatFragment extends Fragment {

	private static final String TAG = "WDCCChatFragment";
	private View view;
	private WDCCChatMgr chatManager;
	private TextView chatLine;
	private ListView listView;
	ChatMessageAdapter adapter = null;
	private WDCCP2PManager mManager = WDCCP2PManager.getWDCCP2PManager();
	private List<String> items = new ArrayList<String>();
	SessionListenerImp mSessionListener = null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.d(TAG, "onCreateView");
		mSessionListener = new SessionListenerImp(){

			/* (non-Javadoc)
			 * @see com.manager.cago.listeners.SessionListenerImp#onRemoveGroupSuccess()
			 */
			@Override
			public void onRemoveGroupSuccess() {
				Log.d(TAG, "onRemoveGroupSuccess");
				if(getActivity()!=null){
					getActivity().finish();
				} else {
					Log.d(TAG, "getActivity returned null");
				}
				
				super.onRemoveGroupSuccess();
			}

			/* (non-Javadoc)
			 * @see com.manager.cago.listeners.SessionListenerImp#onRemoveGroupFail()
			 */
			@Override
			public void onRemoveGroupFail() {
				Log.d(TAG, "onRemoveGroupFail");
				if(getActivity()!=null){
					getActivity().finish();
				} else {
					Log.d(TAG, "getActivity returned null");
				}
				super.onRemoveGroupFail();
			}
			
		};
		view = inflater.inflate(R.layout.fragment_chat, container, false);
		chatLine = (TextView) view.findViewById(R.id.txtChatLine);
		listView = (ListView) view.findViewById(android.R.id.list);
	//	listView = (ListView) view.findViewById(R.id.list);
		adapter = new ChatMessageAdapter(getActivity(), android.R.id.text1,
				items);
		listView.setAdapter(adapter);
		view.findViewById(R.id.button1).setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View arg0) {
						if (chatManager != null) {
							chatManager.write(chatLine.getText().toString()
									.getBytes());
							pushMessage("Me: " + chatLine.getText().toString());
							chatLine.setText("");
							chatLine.clearFocus();
						}
					}
				});
		view.findViewById(R.id.btnend).setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						Log.d("TAG","END button Clicked");
						//mManager.removeGroup();
						mManager.closeDownChat(true);
			/*			getActivity().finish();*/
						}
					
				});
		chatManager = mManager.getChatMgr();
		Log.d(TAG, "onCreateView calling stopServiceDiscovery");
		mManager.stopServiceDiscovery();

		return view;
	}
	@Override
	public void onResume(){
		mManager.registerSessionListener(mSessionListener);
		super.onResume();

	}

	@Override
	public void onPause(){
		//mManager.deregisterSessionListener(mSessionListener);
		super.onPause();

	}
	public interface MessageTarget {
		public Handler getHandler();
	}

	public void setChatManager(WDCCChatMgr obj) {
		chatManager = obj;
	}

	public void pushMessage(String readMessage) {
		adapter.add(readMessage);
		adapter.notifyDataSetChanged();
	}

	/**
	 * ArrayAdapter to manage chat messages.
	 */
	public class ChatMessageAdapter extends ArrayAdapter<String> {

		List<String> messages = null;

		public ChatMessageAdapter(Context context, int textViewResourceId,
				List<String> items) {
			super(context, textViewResourceId, items);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View v = convertView;
			if (v == null) {
				LayoutInflater vi = (LayoutInflater) getActivity()
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = vi.inflate(android.R.layout.simple_list_item_1, null);
			}
			String message = items.get(position);
			if (message != null && !message.isEmpty()) {
				TextView nameText = (TextView) v
						.findViewById(android.R.id.text1);

				if (nameText != null) {
					nameText.setText(message);
					if (message.startsWith("Me: ")) {
						nameText.setTextAppearance(getActivity(),
								R.style.normalText);
					} else {
						nameText.setTextAppearance(getActivity(),
								R.style.boldText);
					}
				}
			}
			return v;
		}
	}
}
