package manager;
import java.net.InetAddress;
import java.sql.*;
import java.util.*;
import java.util.concurrent.*;

import factory.LoadBalancerFactory;
import strategy.*;
import model.*;

public class ConnectionManager {
    private static volatile ConnectionManager instance = null;
    private static ConfigurationManager configurationManager = null;
    private static DatabaseConfiguration databaseConfiguration = null;
    private static List<Connection> slaves = new ArrayList<>();
    private static Connection master;
    private static LoadBalancer loadBalancer = null;
    
    // for testing purpose
    private static final Integer THREADS_COUNT = 5;
    
    private ConnectionManager() {
        configurationManager = ConfigurationManager.getInstance();
        databaseConfiguration = configurationManager.getDatabaseConfiguration();
        initialize();
    }

    public static ConnectionManager getInstance() {
        // double-checked singleton
        if (instance == null) {
            synchronized (ConnectionManager.class) {
                if (instance == null) {
                    instance = new ConnectionManager();
                }
            }
        }
        return instance;
    }

    private static void initialize() {
        // load the jdbc driver
        try {   
            Class.forName(databaseConfiguration.getJdbcDriver());
            System.out.println("JDBC Driver loaded");
        } catch (ClassNotFoundException e) {
            System.out.println("Failed to load JDBC Driver");
            e.printStackTrace();
        }

        // establish connection with master
        addMaster(databaseConfiguration.getMaster().getHost(),
        databaseConfiguration.getMaster().getUsername(),
        databaseConfiguration.getMaster().getPassword());

        // establish connection with slave(s)
        List<Slave> slaveNodes = databaseConfiguration.getSlaves();
        for (Slave slave : slaveNodes) {
            addSlave(slave.getHost(), slave.getUsername(), slave.getPassword());
        }

        // initialize load balancing algorithm
        String loadBalancingAlgorithm = databaseConfiguration.getLoadBalancingAlgorithm();
        loadBalancer = LoadBalancerFactory.getLoadBalancer(loadBalancingAlgorithm, slaves);
    }

    /*
     * method to add master node -
     *  1. Establish connection using specified host, username, password
     *  2. Store the reference of connection for write operations
     */
    public static void addMaster(String host, String username, String password) {
        // TODO : necessary validations for host,username,password
        synchronized(ConnectionManager.class) {
            String connectionURL = databaseConfiguration.getJdbcUrl()
             + databaseConfiguration.getMaster().getHost() + ":" 
             + databaseConfiguration.getPort()
             + "/" + databaseConfiguration.getDBName();

            try {
                master = establishConnection(connectionURL, username, password);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /*
     * method to add slave node -
     *  1. Establish connection using specified host,username & password
     *  2. Add the connection reference in slaves list for read operations
     */
    public static synchronized void addSlave(String host, String username, String password) {
        String connectionURL = databaseConfiguration.getJdbcUrl() + 
        host + ":" + databaseConfiguration.getPort() + "/" + 
        databaseConfiguration.getDBName();

        try {
            Connection connection = establishConnection(connectionURL, username, password);
            slaves.add(connection);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
     * Utility method to that gets connection for specified url,username & password
     */
    private static Connection establishConnection(String url, String username, String password) {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    /*
     * Utility method that returns connection reference of master node
     * Used for write operations
     */
    public synchronized Connection getConnectionForWriting() {
        printSlave(master);
        return master;
    }

    /*
     * Utility method that returns connection reference of slave node
     * Selects specified load balancing algorithm
     * Used for read operations
     */
    public synchronized Connection getConnectionForReading() {
        Connection slaveNode = loadBalancer.getSlave();
        printSlave(slaveNode);
        return slaveNode;
    }

    /*
     * Utility method to display node info
     */
    private void printSlave(Connection node) {
        try {
            DatabaseMetaData databaseMetaData = node.getMetaData();
            String url = databaseMetaData.getURL();
            String hostName = url.substring(url.indexOf("//") + 2, url.lastIndexOf(":"));
            String ipAddress = InetAddress.getByName(hostName).getHostAddress();
            if (ipAddress.endsWith("62")) {
                System.out.println(">> Write on - Node (IP) : " + ipAddress);
            } else {
                System.out.println(">> Read on - Node (IP) : " + ipAddress);   
            }            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // driver - for testing purpose
    public static void main(String args[]) {
        try {
            ConnectionManager connectionManager = ConnectionManager.getInstance();
            
            Connection masterNode = connectionManager.getConnectionForWriting();
            for (int index = 0; index < 5; index++) {
                Connection slaveNode = connectionManager.getConnectionForReading();
            }

            /*
            ExecutorService executorService = Executors.newFixedThreadPool(THREADS_COUNT);
            List<Callable<Void>> threads = new ArrayList<>();

            for (int index = 0; index < THREADS_COUNT; index++) {
                threads.add(() -> {
                    Connection sConnection = connectionManager.getConnectionForReading();
                    System.out.println(">> Got slave connection :: " + sConnection.getMetaData().getURL());
                    return null;
                });
            }
            

            List<Future<Void>> threadsFutures = executorService.invokeAll(threads);
            executorService.shutdown();

            for (Future<Void> future : threadsFutures) {
                future.get();
            }
            */

            /*
            slaveConnection = connectionManager.getConnectionForReading();
            System.out.println("Got slave connection - " + slaveConnection.getMetaData().getURL());
            
            slaveConnection = connectionManager.getConnectionForReading();
            System.out.println("Got slave connection - " + slaveConnection.getMetaData().getURL());

            slaveConnection = connectionManager.getConnectionForReading();
            System.out.println("Got slave connection - " + slaveConnection.getMetaData().getURL());

            */
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}