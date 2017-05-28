package xyz.swwarehouse.echoclient;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.Scanner;

/**
 * Created by WES on 2017-02-19.
 */
public class Client {
    private static final Logger logger = LogManager.getLogger(Client.class);
    private SocketAddress serverAddress = null;
    private Socket socket = new Socket();
    private BufferedReader in = null;
    private PrintWriter out = null;

    public Client(String serverIp, int serverPort) {
        serverAddress = new InetSocketAddress(serverIp, serverPort);
    }

    public void run() {
        try {
            connect();
            Scanner scanner = new Scanner(System.in);
            while (true) {
                String str = scanner.nextLine();
                if (isQuit(str)) {
                    logger.info("Client Connect Close");
                    break;
                }
                send(str);
                recv();
            }
        } catch (IOException e) {
            logger.error(e.getMessage());
        } finally {
        }
    }

    private boolean isQuit(String str) {
        return str.equals("q");
    }

    private void connect() throws IOException {
        socket.connect(serverAddress, Config.INSTANCE.getTimeout());
        logger.info("Connected Server IP(" + socket.getInetAddress() + ":" + socket.getPort() + ")");
        setInputOutput();
        socket.setSoTimeout(Config.INSTANCE.getTimeout());
    }

    private void setInputOutput() {
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            logger.error(e.toString());
        }
    }

    private String recv() throws IOException {
        String str = in.readLine();
        logger.info("Client <- Server, output(" + str + ")");
        return str;
    }

    private void send(String str) throws IOException {
        out.println(str);
        logger.info("Client -> Server, Input(" + str + ")");
    }

    private void close() {
        try {
            socket.close();
        } catch (IOException e) {
            logger.error(e.toString());
        }
    }
}
