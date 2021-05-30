package org.example;

import java.io.IOException;
import java.util.logging.Logger;



public class cinemaClient extends AbstractClient {



    private static final Logger LOGGER =
            Logger.getLogger(cinemaClient.class.getName());

    cinemaClientCLI cinemaCLI;
    public cinemaClient(String host, int port) {
        super(host, port);
        this.cinemaCLI = new cinemaClientCLI(this);
    }

    @Override
    protected void connectionEstablished() {
        // TODO Auto-generated method stub
        super.connectionEstablished();
        LOGGER.info("Connected to server.");

        cinemaClientCLI.loop();
    }

    @Override
    protected void connectionClosed() {
        // TODO Auto-generated method stub
        super.connectionClosed();
        cinemaCLI.closeConnection();
    }



    @Override
    protected void handleMessageFromServer(Object msg) {



    }

    public static void main(String[] args) throws IOException {
        if (args.length != 2) {
            System.out.println("Required arguments: <host> <port>");
        } else {
            String host = args[0];
            int port = Integer.parseInt(args[1]);

            cinemaClient CinemaClient = new cinemaClient(host, port);
            CinemaClient.openConnection();
            App.main(args);
        }
    }
}

