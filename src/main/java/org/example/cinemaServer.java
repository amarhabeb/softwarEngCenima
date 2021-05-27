package org.example;

import java.io.IOException;

public class cinemaServer extends AbstractServer{


    public cinemaServer(int port) {
        super(port);
    }



                    /********** implement************/
    @Override
    protected void handleMessageFromClient(Object var1, ConnectionToClient var2) {

    }


    @Override
    protected synchronized void clientDisconnected(ConnectionToClient client) {
        // TODO Auto-generated method stub

        System.out.println("Client Disconnected.");
        super.clientDisconnected(client);
    }



              /************CHANGE***********/
    @Override
    protected void clientConnected(ConnectionToClient client) {
        super.clientConnected(client);
        System.out.println("Client connected: " + client.getInetAddress());
    }


    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            System.out.println("Required argument: <port>");
        } else {
            cinemaServer server = new cinemaServer(Integer.parseInt(args[0]));
            server.listen();
        }
    }





}
