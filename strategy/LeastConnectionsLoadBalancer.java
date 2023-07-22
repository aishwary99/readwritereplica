package strategy;

import java.net.InetAddress;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Random;

public class LeastConnectionsLoadBalancer extends LoadBalancer {
    private Integer maximumActiveConnections = 10;
    private Random random = new Random();

    class Pair {
        Connection connection;
        Integer activeConnections;

        Pair(Connection connection, Integer activeConnections) {
            this.connection = connection;
            this.activeConnections = activeConnections;
        }
    }

    private PriorityQueue<Pair> priorityQueue = new PriorityQueue<>((a, b) -> a.activeConnections - b.activeConnections);
    
    public LeastConnectionsLoadBalancer(List<Connection> slaves) {
        super(slaves);
        System.out.println(">> Configuring slave(s) for - Least Connections Load Balancer");
        for (Connection slave : slaves) {
            int activeConnectionsCount = random.nextInt((maximumActiveConnections) + 1);
            System.out.println(">> Node - " + getIP(slave) + " : active connections count : " + activeConnectionsCount);
            priorityQueue.add(new Pair(slave, activeConnectionsCount));
        }
    }

    @Override
    public Connection getSlave() {
        // TODO : checks for slave(s) in priority queue
        Pair pair = priorityQueue.poll();
        pair.activeConnections++;
        priorityQueue.offer(pair);
        return pair.connection;
    }

    private String getIP(Connection node) {
        try {
            DatabaseMetaData databaseMetaData = node.getMetaData();
            String url = databaseMetaData.getURL();
            String hostName = url.substring(url.indexOf("//") + 2, url.lastIndexOf(":"));
            String ipAddress = InetAddress.getByName(hostName).getHostAddress();
            return ipAddress;            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}
