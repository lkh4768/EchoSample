package xyz.swwarehouse.echoserver;

/**
 * Created by WES on 2017-02-18.
 */
public class Main {
    public static void main(String[] args){
        Config.INSTANCE.getConfig();
        Server server = new Server(Config.INSTANCE.getPort());
        server.run();
    }
}
