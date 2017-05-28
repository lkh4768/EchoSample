package xyz.swwarehouse.echoserver;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.ServerSocket;

/**
 * Created by WES on 2017-02-19.
 */
public class Server {
    private static final Logger logger = LogManager.getLogger(Server.class);
    private int port = 9000;
    private ServerSocket listener = null;
    private Session session = null;

    public Server() {
        init();
    }

    public Server(final int port) {
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
        while (true) {
            try {
                accept();
                while (true) {
                    send(recv());
                }
            } catch (IOException e) {
                logger.error("Session(" + session.getAddr() + "), " + e.getMessage());
            } finally {
                session.close();
            }
        }
    }

    private void accept() throws IOException {
        session = new Session(listener.accept());
        logger.info("Accpeted Client(" + session.getAddr() + ")");
    }

    private String recv() throws IOException {
        String str = session.read();
        logger.info("Server <- Client(" + session.getAddr() + "), Input(" + str + ")");
        return str;
    }

    private void send(final String str) {
        session.write(str);
        logger.info("Server -> Client(" + session.getAddr() + "), output(" + str + ")");
    }

    private void closeAll() {
        closeClient();
        closeServer();
    }

    private void closeClient() {
        if (session != null) {
            session.close();
            session = null;
        }
    }

    private void closeServer() {
        try {
            if (listener != null) {
                listener.close();
                listener = null;
            }
        } catch (IOException e) {
            logger.error(e.toString());
        }
    }

}
