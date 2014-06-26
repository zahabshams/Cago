/**
 * 
 */
package com.manager.cago;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import android.os.Handler;
import android.util.Log;



/**
 * @author zahab
 *<blockquote>	this class will handle the connection for Group owner. 
 *				In this case the group owner will create a server socket
 *				and on receiving the connection request it will just start
 *				a new socket to complete the connection
 */
public class WDCCGOSocketMgr extends Thread{
	 	ServerSocket socket = null;
	    private final int THREAD_COUNT = 10;
/*	    private Handler handler;
*/		protected static final String TAG = WDCCGOSocketMgr.class.getSimpleName();
		private WDCCP2PManager mManger = null;

	/**
	 * 
	 */
	
	public WDCCGOSocketMgr(Handler handler) throws IOException {
			mManger = WDCCP2PManager.getWDCCP2PManager();
		 try {
			 Log.d(TAG, "creating Connected as group owner");
	            socket = new ServerSocket(4545);
/*	            this.handler = handler;
*/	            Log.d(TAG, "Socket Started");
	        } catch (IOException e) {
	            Log.d(TAG, "Socket catch");
	            e.printStackTrace();
	            pool.shutdownNow();
	            throw e;
	        }
	}
	
	

   
    /**
     * A ThreadPool for client sockets.
     */
    private final ThreadPoolExecutor pool = new ThreadPoolExecutor(
            THREAD_COUNT, THREAD_COUNT, 10, TimeUnit.SECONDS,
            new LinkedBlockingQueue<Runnable>());

    @Override
    public void run() {
        while (true) {
            try {
                // A blocking operation. Initiate a ChatManager instance when
                // there is a new connection
            	//Socket socket.accept();
            	Socket server_socket = socket.accept();
               // pool.execute(new WDCCChatMgr(socket.accept(), handler));
            	pool.execute(mManger.startChat(server_socket));
                Log.d(TAG, "Launching the I/O handler");

            } catch (IOException e) {
                try {
                    if (socket != null && !socket.isClosed())
                        socket.close();
                } catch (IOException ioe) {

                }
                e.printStackTrace();
                pool.shutdownNow();
                break;
            }
        }
    }


	
}
