/**
 * 
 */
package com.manager.cago.listeners;

/**
 * @author zahab
 *
 */
public class SessionListenerImp implements SessionListeners{

	/* (non-Javadoc)
	 * @see com.manager.cago.listeners.SessionListeners#onChatStart()
	 */
	@Override
	public void onChatStart() {}

	@Override
	public void onChatFinish() {}

	
	@Override
	public void onDeviceFound() {}
	public void onRemoveGroupSuccess(){}
	public void onRemoveGroupFail(){}
}
