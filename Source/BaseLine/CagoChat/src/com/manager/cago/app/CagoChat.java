/**
 * 
 */
package com.manager.cago.app;

import android.app.Application;
import android.content.ComponentCallbacks;
import android.content.res.Configuration;
import android.util.Log;

import com.manager.cago.WDCCP2PManager;

/**
 * @author zahab
 *<blockquote> 
 *This the base of application where the WDCCP2PManager is instantiated
 */

public class CagoChat extends Application {
	
	WDCCP2PManager mManager = null;
	private String TAG = "WDCCCagoChat";

	/**
	 * 
	 */
	public CagoChat() {
		Log.d(TAG, "CagoChat");
	}
	
	/* (non-Javadoc)
	 * @see android.app.Application#onCreate()
	 */
	@Override
	public void onCreate() {
		Log.d(TAG, "onCreate");
		Log.d("MyTest","in cago chat");
		mManager = WDCCP2PManager.iInstantiateManager(this);
		mManager.setupP2P();
		super.onCreate();
	}

	/* (non-Javadoc)
	 * @see android.app.Application#onTerminate()
	 */
	@Override
	public void onTerminate() {
		super.onTerminate();
	}

	/* (non-Javadoc)
	 * @see android.app.Application#onConfigurationChanged(android.content.res.Configuration)
	 */
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}

	/* (non-Javadoc)
	 * @see android.app.Application#registerComponentCallbacks(android.content.ComponentCallbacks)
	 */
	@Override
	public void registerComponentCallbacks(ComponentCallbacks callback) {
		super.registerComponentCallbacks(callback);
	}

	/* (non-Javadoc)
	 * @see android.app.Application#registerActivityLifecycleCallbacks(android.app.Application.ActivityLifecycleCallbacks)
	 */
	@Override
	public void registerActivityLifecycleCallbacks(
			ActivityLifecycleCallbacks callback) {
		super.registerActivityLifecycleCallbacks(callback);
	}

}
