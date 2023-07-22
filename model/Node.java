package model;
import java.sql.*;

public class Node {
    // JDBC connection reference
    private Connection connection;
    // weight is used for weighted RR algorithm
    private Integer weight;
    // activeConnections is used for least connections algorithm
    private Integer activeConnections;
    // more properties / setters / getters

    public Node() {}

    public Node(Connection connection) {
        this.connection = connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public Connection getConnection() {
        return this.connection;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public Integer getWeight() {
        return this.weight;
    }

    public void setActiveConnections(Integer activeConnections) {
        this.activeConnections = activeConnections;
    }

    public Integer getActiveConnections() {
        return this.activeConnections;
    }
}