package xyz.swwarehouse.echoserver;

import org.apache.commons.configuration2.XMLConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by WES on 2017-02-18.
 */
public enum Config {
    INSTANCE;
    private int port;
    private int timeout;
    private static final Logger logger = LogManager.getLogger(Config.class);

    public void getConfig() {
        Parameters params = new Parameters();
        FileBasedConfigurationBuilder<XMLConfiguration> builder =
                new FileBasedConfigurationBuilder<XMLConfiguration>(XMLConfiguration.class)
                        .configure(params.xml()
                                .setFileName("config.xml"));
        try {
            XMLConfiguration config = builder.getConfiguration();
            port = config.getInt("server[@port]", 10000);
            timeout = config.getInt("server[@timeout]", 10);
            logger.debug(toString());
        } catch (ConfigurationException e) {
            logger.error(e.toString());
            e.printStackTrace();
        }
    }


    public int getPort() {
        return port;
    }

    public int getTimeout() {
        return timeout;
    }

    public String toString() {
        return ("Timeout(" + timeout + "), Port(" + port + ")");
    }
}
