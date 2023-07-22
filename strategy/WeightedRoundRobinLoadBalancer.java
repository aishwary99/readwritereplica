package strategy;

import java.net.InetAddress;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WeightedRoundRobinLoadBalancer extends LoadBalancer {
    private static Integer currentIndex = -1;
    private static Integer currentWeight = 0;
    private static Integer maximumWeight = 10;
    private static Random random = new Random();
    private List<Pair> weightedSlaves = new ArrayList<>();

    class Pair {
        Connection connection;
        Integer weight;

        public Pair(Connection connection, Integer weight) {
            this.connection = connection;
            this.weight = weight;
        }
    }

    public WeightedRoundRobinLoadBalancer(List<Connection> slaves) {
        super(slaves);
        System.out.println(">> Configuring slave(s) for - Weighted Round Robin Load Balancer");
        for (Connection slave : slaves) {
            int weight = random.nextInt((maximumWeight) + 1);
            System.out.println(">> Node - " + getIP(slave) + " : Weight : " + weight);
            weightedSlaves.add(new Pair(slave, weight));
        }
    }

    @Override
    public Connection getSlave() {
        Integer totalWeight = getTotalWeight();
        while (true) {
            currentIndex = (currentIndex + 1) % slaves.size();
            if (currentIndex == 0) {
                currentWeight--;
                if (currentWeight <= 0) {
                    currentWeight = totalWeight;
                    if (currentWeight == 0) return null;
                }
            }

            if (weightedSlaves.get(currentIndex).weight >= currentWeight) {
                return weightedSlaves.get(currentIndex).connection;
            }
        }
    }

    private Integer getTotalWeight() {
        Integer totalWeight = 0;
        for(Pair slave : weightedSlaves) {
            totalWeight += slave.weight;
        }
        return totalWeight;
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
