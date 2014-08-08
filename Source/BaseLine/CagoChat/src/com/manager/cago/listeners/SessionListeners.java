/**
 * 
 */
package com.manager.cago.listeners;

/**
 * @author zahab
 *
 */
public interface SessionListeners {
	public void onChatStart();
	public void onChatFinish();
	public void onDeviceFound();
	public void onRemoveGroupSuccess();
	public void onRemoveGroupFail();


}
