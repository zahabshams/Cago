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
 * <blockquote>
 * This holds remains active throughout the lifecycle of Application.
 * @version ver.0.1
 */
public class CagoChat extends Application {

	WDCCP2PManager mManager = null;
	private String TAG = "WDCCCagoChat";

	/**
	 * 
	 */
	public CagoChat() {
		Log.d(TAG, "CagoChat");
		// mManager =
		// WDCCP2PManager.iInstantiateManager(getApplicationContext());
		// TODO Auto-generated constructor stub
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Application#onCreate()
	 */
	@Override
	public void onCreate() {
		Log.d(TAG, "onCreate");

		mManager = WDCCP2PManager.iInstantiateManager(this);
		super.onCreate();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Application#onTerminate()
	 */
	@Override
	public void onTerminate() {
		super.onTerminate();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Application#onConfigurationChanged(android.content.res.
	 * Configuration)
	 */
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		super.onConfigurationChanged(newConfig);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Application#registerComponentCallbacks(android.content.
	 * ComponentCallbacks)
	 */
	@Override
	public void registerComponentCallbacks(ComponentCallbacks callback) {
		// TODO Auto-generated method stub
		super.registerComponentCallbacks(callback);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.app.Application#registerActivityLifecycleCallbacks(android.app
	 * .Application.ActivityLifecycleCallbacks)
	 */
	@Override
	public void registerActivityLifecycleCallbacks(
			ActivityLifecycleCallbacks callback) {
		// TODO Auto-generated method stub
		super.registerActivityLifecycleCallbacks(callback);
	}

}
