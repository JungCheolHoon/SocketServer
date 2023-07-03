package socketserver.property;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class QueryLoader {

    private Properties properties;

    public QueryLoader() {
        String propertiesFilePath = System.getProperty("user.dir") + "/resources/properties/queries.properties";
        try (InputStream inputStream = new FileInputStream(propertiesFilePath)) {
            properties = new Properties();
            properties.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getQuery(String key) {
        return properties.getProperty(key);
    }
}
