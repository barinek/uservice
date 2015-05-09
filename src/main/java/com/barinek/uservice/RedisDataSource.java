package com.barinek.uservice;

import com.codahale.metrics.CachedGauge;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.health.HealthCheck;
import com.codahale.metrics.health.HealthCheckRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;

public class RedisDataSource {
    private static final Logger logger = LoggerFactory.getLogger(RedisDataSource.class);

    private final ConcurrentLinkedQueue<RedisConnection> pool = new ConcurrentLinkedQueue<>();
    private final String host;
    private final int port;
    private final String password;

    private HealthCheckRegistry healthCheckRegistry;
    private MetricRegistry metricRegistry;

    public RedisDataSource(String host, int port, String password) {
        this.host = host;
        this.port = port;
        this.password = password;
    }

    public RedisConnection getConnection() throws IOException {
        RedisConnection connection = pool.poll();
        if (connection == null || !connection.connected()) {
            logger.info("Creating new redis connection.");
            connection = new RedisConnection(this, host, port, password);
        }
        return connection;
    }

    public void returnConnection(RedisConnection connection) throws IOException {
        if (connection == null) {
            return;
        }
        pool.offer(connection);
    }

    public void setMetricRegistry(MetricRegistry metricRegistry) {
        this.metricRegistry = metricRegistry;

        this.metricRegistry.register(MetricRegistry.name("Redis", "pool", "TotalConnections"), new CachedGauge(10L, TimeUnit.SECONDS) {
            @Override
            protected Object loadValue() {
                return pool.size();
            }
        });
    }

    public void setHealthCheckRegistry(HealthCheckRegistry healthCheckRegistry) {
        this.healthCheckRegistry = healthCheckRegistry;

        this.healthCheckRegistry.register(MetricRegistry.name("Redis", "pool", "ConnectivityCheck"), new HealthCheck() {
                    @Override
                    protected Result check() throws Exception {
                        try (RedisConnection connection = getConnection()) {
                            if (connection.connected()) {
                                return Result.healthy();
                            } else {
                                return Result.healthy("Unable to ping server.");
                            }
                        } catch (IOException e) {
                            return Result.unhealthy(e.getMessage());
                        }
                    }
                }
        );
    }
}