package org.example;

import java.util.LinkedList;
import java.util.Queue;
import java.io.IOException;

public class CinemaClientCLI {
	
	static Queue<LinkedList<Object>> ClientInput;
	private CinemaClient client;
    private boolean isRunning;
   // private static final String SHELL_STRING = "Enter message (or exit to quit)> ";
    private Thread loopThread;
    
    public CinemaClientCLI(CinemaClient client) {
        this.client = client;
        this.isRunning = false;
    	ClientInput = new LinkedList<>();


    }
            /********IMPLEMENT*********/
    public void loop() throws IOException {
    	loopThread = new Thread(new Runnable() {

			@Override
			public void run() {
				//BufferedReader reader = new BufferedReader(new InputStreamReader(ClientInput));
				LinkedList<Object> message;	// a message is a list of objects
				
				while (client.isConnected()) {
					//System.out.print(SHELL_STRING);
				
					try {
						message = ClientInput.poll();
						if (message==null)
							continue;
						
						else {
							client.sendToServer(message);
						}
						
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
		});
    	
		loopThread.start();
		this.isRunning = true;

    }

    public void closeConnection() {
        System.out.println("Connection closed.");
        System.exit(0);
    }
    
    // add message to queue for client
    public static void sendMessage(LinkedList<Object> message) {
    	CinemaClientCLI.ClientInput.add(message);
    }
}
