package strategy;

import java.sql.Connection;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

public class RoundRobinLoadBalancer extends LoadBalancer {
    private Integer counter = 0;
    private final ReentrantLock reentrantLock;

    public RoundRobinLoadBalancer(List<Connection> slaves) {
        super(slaves);
        reentrantLock = new ReentrantLock();
    }

    @Override
    public Connection getSlave() {
        reentrantLock.lock();
        try {
            Connection slave = slaves.get(counter);
            counter = (++counter) % slaves.size();
            return slave;
        } finally {
            reentrantLock.unlock();
        }
    }
}
