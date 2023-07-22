package model;
import java.util.*;

public class DatabaseConfiguration {
    private String jdbcDriver;
    private String jdbcUrl;
    private Integer port;
    private String dbName;
    private Master master;
    private List<Slave> slaves;
    private String loadBalancingAlgorithm;

    public DatabaseConfiguration() {

    }

    public void setJdbcDriver(String jdbcDriver) {
        this.jdbcDriver = jdbcDriver;
    }

    public String getJdbcDriver() {
        return this.jdbcDriver;
    }

    public void setJdbcUrl(String jdbcUrl) {
        this.jdbcUrl = jdbcUrl;
    }

    public String getJdbcUrl() {
        return this.jdbcUrl;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public Integer getPort() {
        return this.port;
    }

    public void setDBName(String dbName) {
        this.dbName = dbName;
    }

    public String getDBName() {
        return this.dbName;
    }

    public void setMaster(Master master) {
        this.master = master;
    }

    public Master getMaster() {
        return this.master;
    }

    public void setSlaves(List<Slave> slaves) {
        this.slaves = slaves;
    }

    public List<Slave> getSlaves() {
        return this.slaves;
    }

    public void setLoadBalancingAlgorithm(String loadBalancingAlgorithm) {
        this.loadBalancingAlgorithm = loadBalancingAlgorithm;
    }

    public String getLoadBalancingAlgorithm() {
        return this.loadBalancingAlgorithm;
    }
}