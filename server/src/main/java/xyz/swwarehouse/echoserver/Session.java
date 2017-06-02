package xyz.swwarehouse.echoserver;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;

/**
 * Created by WES on 2017-05-28.
 */
public class Session {
    private static final Logger logger = LogManager.getLogger(Session.class);
    private String host;
    private int port = -1;
    private Socket socket = null;
    private BufferedReader in = null;
    private PrintWriter out = null;
    private int timeout = 0;

    public Session(Socket socket) {
        this.socket = socket;
        setAddr();
        setInputOutput();
    }

    private void setAddr() {
        host = socket.getInetAddress().toString();
        port = socket.getPort();
    }

    private void setInputOutput() {
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            logger.error(e.toString());
            close();
        }
    }

    public void setTimeout(int time)  throws SocketException
    {
        this.timeout = time;
        socket.setSoTimeout(time);
    }

    public String getAddr() {
        return host + ":" + port;
    }

    public String read() throws IOException{
        return in.readLine();
    }

    public void write(String str){
        out.println(str);
    }

    public void close() {
        try {
            if (socket != null) {
                socket.close();
                socket = null;
            }
        } catch (IOException e) {
            logger.error(e.toString());
        }
    }

}
