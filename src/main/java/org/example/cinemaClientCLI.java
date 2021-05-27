package org.example;

public class cinemaClientCLI {

    private cinemaClient client;
    private boolean isRunning;
   // private static final String SHELL_STRING = "Enter message (or exit to quit)> ";
    private Thread loopThread;

    public cinemaClientCLI(cinemaClient client) {
        this.client = client;
        this.isRunning = false;
    }
            /********IMPLEMENT*********/
    public static void loop() {
    }

    public void closeConnection() {
        System.out.println("Connection closed.");
        System.exit(0);
    }
}
