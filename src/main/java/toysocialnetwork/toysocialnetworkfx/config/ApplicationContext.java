package toysocialnetwork.toysocialnetworkfx.config;

import java.util.Properties;

/**
 * Represents the current context of the application
 */
public class ApplicationContext {
    private static final Properties configProperties = Config.getProperties();

    /**
     * Gets the configuration properties
     *
     * @return the configuration properties
     */
    public static Properties getConfigProperties() {
        return configProperties;
    }
}
