package strategy;

import java.sql.Connection;
import java.util.List;

public abstract class LoadBalancer {
    final List<Connection> slaves;

    public LoadBalancer(List<Connection> slaves) {
        this.slaves = slaves;
    }

    public abstract Connection getSlave();
}
