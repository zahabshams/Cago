package com.viewer.cagochat;

import com.manager.cago.WDCCP2PManager;
import com.manager.cago.WDCCP2PService;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;

public class Device_ListActivity extends Activity implements WDCCViewerManager.DevList{
	public WDCCList_Fragment mDevice_List_Fragment = null;
	public WDCCP2PManager mManager;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_device__list);
		mDevice_List_Fragment = new WDCCList_Fragment();
		if (savedInstanceState == null) {
			
			getFragmentManager().beginTransaction()
					.add(R.id.container, mDevice_List_Fragment/*new WDCCList_Fragment()*/).commit();
		}
		mManager = WDCCP2PManager.getWDCCP2PManager(getApplicationContext());
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.device__list, menu);
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

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_device__list,
					container, false);
			return rootView;
		}
	}

	/* (non-Javadoc)
	 * @see com.viewer.cagochat.WDCCViewerManager.DevList#notifyDeviceList()
	 */
	@Override
	public void notifyDeviceList(WDCCP2PService service, boolean add) {
		
		// TODO Auto-generated method stub
		
	}

}
