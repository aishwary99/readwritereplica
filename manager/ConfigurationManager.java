package manager;
import model.*;
import exception.*;
import java.io.*;
import com.google.gson.*;

public class ConfigurationManager {
    private static volatile ConfigurationManager instance = null;
    private static final String CONF_FILE_PATH = "config/conf.json";
    private DatabaseConfiguration databaseConfiguration;

    private ConfigurationManager() {
        this.databaseConfiguration = mapConfiguration(loadDatabaseConfigurationFile());
    }

    private String loadDatabaseConfigurationFile() {
        File file = new File(CONF_FILE_PATH);
        if (!file.exists() || !file.isFile()) {
            throw new ConfigurationFileNotFoundException("Database Configuration File not found at path : " + CONF_FILE_PATH);
        }

        StringBuilder builder = new StringBuilder();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
        } catch (Exception e) {
            throw new ConfigurationFileContentNotValid("Database Configuration File content is not valid");
        }

        return builder.toString();
    }

    private DatabaseConfiguration mapConfiguration(String json) {
        Gson gson = new Gson();
        DatabaseConfiguration databaseConfiguration = gson.fromJson(json, DatabaseConfiguration.class);
        
        System.out.println("-------------Database Configuration---------------");
        System.out.println(">> JDBC Driver : " + databaseConfiguration.getJdbcDriver());
        System.out.println(">> JDBC URL : " + databaseConfiguration.getJdbcUrl());
        System.out.println(">> Port : " + databaseConfiguration.getPort());
        System.out.println(">> DB Name : " + databaseConfiguration.getDBName());
        System.out.println(">> Master : " + databaseConfiguration.getMaster());
        System.out.println(">> Slaves : ");
        for (Slave slave : databaseConfiguration.getSlaves()) {
            System.out.println(slave);
        }
        System.out.println(">> Load Balancing Algorithm : " + databaseConfiguration.getLoadBalancingAlgorithm());
        System.out.println("--------------------------------------------------");

        // TODO: code to validate conf.json contents
        return databaseConfiguration;
    }

    public static ConfigurationManager getInstance() {
        if (instance == null) {
            synchronized(ConfigurationManager.class) {
                if (instance == null) instance = new ConfigurationManager();
            }
        }
        return instance;
    }

    public DatabaseConfiguration getDatabaseConfiguration() {
        return this.databaseConfiguration;
    }

    // driver : for testing
    public static void main(String args[]) {
        ConfigurationManager configurationManager = ConfigurationManager.getInstance();        
    }
}