package factory;

import java.sql.Connection;
import java.util.List;

import strategy.LeastConnectionsLoadBalancer;
import strategy.LoadBalancer;
import strategy.RoundRobinLoadBalancer;
import strategy.WeightedRoundRobinLoadBalancer;

public class LoadBalancerFactory {
    /*
     * Load Balance Factory : Gets the load balancer based on the specified type
     * by default - round robin algorithm will be considered
     */
    public static LoadBalancer getLoadBalancer(String type, List<Connection> slaves) {
        System.out.println("LB Type : " + type.toUpperCase().trim());
        switch(type.toUpperCase().trim()) {
            case "ROUND ROBIN":
                return new RoundRobinLoadBalancer(slaves);
            case "WEIGHTED ROUND ROBIN":
                return new WeightedRoundRobinLoadBalancer(slaves);
            case "LEAST CONNECTIONS" : 
                return new LeastConnectionsLoadBalancer(slaves);
            default:
                return new RoundRobinLoadBalancer(slaves);
        }
    }
}
