package com.barinek.uservice;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.health.HealthCheckRegistry;
import com.zaxxer.hikari.HikariDataSource;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Properties;

public class App {
    private static final Logger logger = LoggerFactory.getLogger(App.class);
    private static final MetricRegistry metrics = new MetricRegistry();
    private static final HealthCheckRegistry healthChecks = new HealthCheckRegistry();

    private final Server server;

    public static void main(String[] args) throws Exception {
        new App().start();
    }

    public App() throws IOException, ClassNotFoundException, SQLException {
        Properties properties = new Properties();
        properties.load(getClass().getResourceAsStream("/default.properties"));
        String defaultJson = properties.getProperty("container.json");

        HikariDataSource hikariDataSource = getHikariDataSource(defaultJson);
        RedisDataSource redisDataSource = getRedisDataSource(defaultJson);

        MigrationsMySQL migrations = new MigrationsMySQL(hikariDataSource);
        migrations.tryMigrations();

        HandlerList list = new HandlerList(); // ordered
        list.addHandler(new ResultHandler(new ResultDAO(redisDataSource)));
        list.addHandler(new ResourceHandler(new ResourceDAO(hikariDataSource)));
        list.addHandler(new HealthCheckHandler(healthChecks));
        list.addHandler(new MetricHandler(metrics));
        list.addHandler(new NoopHandler());

        server = new Server(tryPort(Integer.parseInt(properties.getProperty("server.port"))));
        server.setHandler(list);

        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                try {
                    if (server.isRunning()) {
                        server.stop();
                    }
                    logger.info("App shutdown.");
                } catch (Exception e) {
                    logger.info("Error shutting down app.", e);
                }
            }
        });
    }

    public void start() throws Exception {
        logger.info("App started.");
        server.start();
    }

    public void stop() throws Exception {
        metrics.getMetrics().keySet().forEach(metrics::remove);

        healthChecks.getNames().forEach(healthChecks::unregister);

        logger.info("App stopped.");
        server.stop();
    }

    /// PRIVATE HELPERS

    private int tryPort(int defaultPort) {
        return System.getenv("PORT") != null ? Integer.parseInt(System.getenv("PORT")) : defaultPort;
    }

    private RedisDataSource getRedisDataSource(String defaultJson) throws IOException {
        CredentialsRedis credentials = CredentialsRedis.from(System.getenv("VCAP_SERVICES") != null ? System.getenv("VCAP_SERVICES") : defaultJson);

        RedisDataSource dataSource = new RedisDataSource(credentials.getHostname(), credentials.getPort(), credentials.getPassword());
        dataSource.setMetricRegistry(metrics);
        dataSource.setHealthCheckRegistry(healthChecks);
        return dataSource;
    }

    private HikariDataSource getHikariDataSource(String defaultJson) throws IOException {
        CredentialsMySQL credentials = CredentialsMySQL.from(System.getenv("VCAP_SERVICES") != null ? System.getenv("VCAP_SERVICES") : defaultJson);

        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(credentials.getUrl());
        dataSource.setMetricRegistry(metrics);
        dataSource.setHealthCheckRegistry(healthChecks);
        return dataSource;
    }
}