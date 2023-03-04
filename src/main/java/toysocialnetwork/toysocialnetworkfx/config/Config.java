package toysocialnetwork.toysocialnetworkfx.config;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

/**
 * Class that stores the configuration properties
 */
public class Config {
    /**
     * The path to the configuration file
     */
    public static final String CONFIG_PATH = Config.class.getClassLoader()
            .getResource("toysocialnetwork/toysocialnetworkfx/config.properties").getFile();

    /**
     * Gets the properties object from the configuration file
     *
     * @return the properties object
     */
    public static Properties getProperties() {
        Properties properties = new Properties();

        try {
            properties.load(new FileReader(CONFIG_PATH));

            return properties;
        } catch (IOException e) {
            throw new RuntimeException("Cannot load config properties");
        }
    }
}
