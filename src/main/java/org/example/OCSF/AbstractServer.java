package org.example.OCSF;


import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.ServerSocket;
import java.net.Socket;

public abstract class AbstractServer implements Runnable {
    private ServerSocket serverSocket = null;
    private Thread connectionListener = null;
    private int port;
    private int timeout = 500;
    private int backlog = 10;
    private ThreadGroup clientThreadGroup;
    private boolean readyToStop = true;
    private AbstractConnectionFactory connectionFactory = null;

    public AbstractServer(int port) {
        this.port = port;
        this.clientThreadGroup = new ThreadGroup("ConnectionToClient threads") {
            public void uncaughtException(Thread thread, Throwable exception) {
                AbstractServer.this.clientException((ConnectionToClient)thread, exception);
            }
        };
    }

    public final void listen() throws IOException {
    	System.out.println("Server is now listening, port = " + Integer.toString(this.getPort()));
        if (!this.isListening()) {
            if (this.serverSocket == null) {
                this.serverSocket = new ServerSocket(this.getPort(), this.backlog);
            }

            this.serverSocket.setSoTimeout(this.timeout);
            this.connectionListener = new Thread(this);
            this.connectionListener.start();
        }

    }

    public final void stopListening() {
        this.readyToStop = true;
    }

    public final void close() throws IOException {
        if (this.serverSocket != null) {
            this.stopListening();
            boolean var21 = false;

            try {
                var21 = true;
                this.serverSocket.close();
                var21 = false;
            } finally {
                if (var21) {
                    synchronized(this) {
                        Thread[] clientThreadList = this.getClientConnections();
                        int i = 0;

                        while(true) {
                            if (i >= clientThreadList.length) {
                                this.serverSocket = null;
                                break;
                            }

                            try {
                                ((ConnectionToClient)clientThreadList[i]).close();
                            } catch (Exception var24) {
                            }

                            ++i;
                        }
                    }

                    try {
                        this.connectionListener.join();
                    } catch (InterruptedException var22) {
                    } catch (NullPointerException var23) {
                    }

                    this.serverClosed();
                }
            }

            synchronized(this) {
                Thread[] clientThreadList = this.getClientConnections();
                int i = 0;

                while(true) {
                    if (i >= clientThreadList.length) {
                        this.serverSocket = null;
                        break;
                    }

                    try {
                        ((ConnectionToClient)clientThreadList[i]).close();
                    } catch (Exception var27) {
                    }

                    ++i;
                }
            }

            try {
                this.connectionListener.join();
            } catch (InterruptedException var25) {
            } catch (NullPointerException var26) {
            }

            this.serverClosed();
        }
    }

    public void sendToAllClients(Object msg) {
        Thread[] clientThreadList = this.getClientConnections();

        for(int i = 0; i < clientThreadList.length; ++i) {
            try {
                ((ConnectionToClient)clientThreadList[i]).sendToClient(msg);
            } catch (Exception var5) {
            }
        }

    }

    public final boolean isListening() {
        return this.connectionListener != null && this.connectionListener.isAlive();
    }

    public final boolean isClosed() {
        return this.serverSocket == null;
    }

    public final synchronized Thread[] getClientConnections() {
        Thread[] clientThreadList = new Thread[this.clientThreadGroup.activeCount()];
        this.clientThreadGroup.enumerate(clientThreadList);
        return clientThreadList;
    }

    public final int getNumberOfClients() {
        return this.clientThreadGroup.activeCount();
    }

    public final int getPort() {
        return this.port;
    }

    public final void setPort(int port) {
        this.port = port;
    }

    public final void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public final void setBacklog(int backlog) {
        this.backlog = backlog;
    }

    public final void setConnectionFactory(AbstractConnectionFactory factory) {
        this.connectionFactory = factory;
    }

    public final void run() {
        this.readyToStop = false;
        this.serverStarted();

        try {
            while(!this.readyToStop) {
                try {
                    Socket clientSocket = this.serverSocket.accept();
                    synchronized(this) {
                        if (!this.readyToStop) {
                            if (this.connectionFactory == null) {
                                new ConnectionToClient(this.clientThreadGroup, clientSocket, this);
                            } else {
                                this.connectionFactory.createConnection(this.clientThreadGroup, clientSocket, this);
                            }
                        }
                    }
                } catch (InterruptedIOException var10) {
                }
            }
        } catch (IOException var11) {
            if (!this.readyToStop) {
                this.listeningException(var11);
            }
        } finally {
            this.readyToStop = true;
            this.connectionListener = null;
            this.serverStopped();
        }

    }

    protected void clientConnected(ConnectionToClient client) {
    }

    protected synchronized void clientDisconnected(ConnectionToClient client) {
    }

    protected synchronized void clientException(ConnectionToClient client, Throwable exception) {
    }

    protected void listeningException(Throwable exception) {
    }

    protected void serverStarted() {
    }

    protected void serverStopped() {
    }

    protected void serverClosed() {
    }

    protected abstract void handleMessageFromClient(Object var1, ConnectionToClient var2);

    final synchronized void receiveMessageFromClient(Object msg, ConnectionToClient client) {
        this.handleMessageFromClient(msg, client);
    }
}

