package xyz.swwarehouse.echoserver;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by WES on 2017-02-19.
 */
public class Server {
    private static final Logger logger = LogManager.getLogger(Server.class);
    private int port = 9000;
    private BufferedReader in = null;
    private PrintWriter out = null;
    private ServerSocket listener = null;
    private Socket socket = null;

    public Server() {
        init();
    }

    public Server(int port) {
        this.port = port;
        init();
    }

    private void init() {
        try {
            listener = new ServerSocket(this.port);
        } catch (IOException e) {
            logger.error(e.toString());
        }
    }

    public void run() {
        try {
            accept();
            while (true) {
                String str = recv();
                if (str.isEmpty()) {
                    logger.warn("Recv Data Empty");
                    break;
                }
                send(str);
            }
        } catch (IOException e) {
            logger.error(e.toString());
            e.printStackTrace();
        } finally {
            closeAll();
        }
    }

    private void accept() throws IOException {
        socket = listener.accept();
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);
        logger.info("Accpet Client IP(" + socket.getInetAddress() + "), Port(" + socket.getPort() + ")");
    }

    private String recv() throws IOException {
        String str = in.readLine();
        logger.info("Server <- Client, Input(" + str + ")");
        return str;
    }

    private void send(String str) {
        out.println(str);
        logger.info("Server -> Client, output(" + str + ")");
    }

    private void closeAll() {
        closeClient();
        closeServer();
    }

    private void closeClient() {
        try {
            if (socket != null)
                socket.close();
        } catch (IOException e) {
            logger.error(e.toString());
        }
    }

    private void closeServer() {
        try {
            if (listener != null)
                listener.close();
        } catch (IOException e) {
            logger.error(e.toString());
        }
    }
}
