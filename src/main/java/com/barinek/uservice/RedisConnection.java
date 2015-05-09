package com.barinek.uservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class RedisConnection implements AutoCloseable {
    private static final Logger logger = LoggerFactory.getLogger(RedisConnection.class);

    private final RedisDataSource dataSource;
    private final String password;

    private final Socket socket;
    private final BufferedReader reader;
    private final OutputStreamWriter writer;

    public RedisConnection(RedisDataSource dataSource, String host, int port, String password) throws IOException {
        this.dataSource = dataSource;
        this.password = password;

        logger.info("Creating new socket host={}, port={}", host, port);

        socket = new Socket(host, port);
        socket.setKeepAlive(true);
        socket.setTcpNoDelay(true);

        this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.writer = new OutputStreamWriter(socket.getOutputStream());

        writer.write("*2\r\n");
        writer.write("$4\r\nauth\r\n");
        writer.write(String.format("$%s\r\n%s\r\n", this.password.length(), this.password));
        writer.flush();

        readResponse(reader);
    }

    public void set(String key, String value) throws IOException {
        writer.write("*3\r\n");
        writer.write("$3\r\nset\r\n");
        writer.write(String.format("$%s\r\n%s\r\n", key.length(), key));
        writer.write(String.format("$%s\r\n%s\r\n", value.length(), value));
        writer.flush();

        readResponse(reader);
    }

    public String list(String key) throws IOException {
        writer.write("*2\r\n");
        writer.write("$3\r\nget\r\n");
        writer.write(String.format("$%s\r\n%s\r\n", key.length(), key));
        writer.flush();

        return readResponse(reader);
    }

    public boolean connected() throws IOException {
        writer.write("*1\r\n");
        writer.write("$4\r\nping\r\n");
        writer.flush();

        return readResponse(reader).equalsIgnoreCase("pong");
    }

    @Override
    public void close() throws IOException {
        dataSource.returnConnection(this);
    }

    /// PRIVATE

    private String readResponse(BufferedReader reader) throws IOException {
        char[] response = new char[1];
        reader.read(response);

        if (response[0] == '+') {
            String line = reader.readLine();
            return String.valueOf(line);
        }

        if (response[0] == '$') {
            String line = reader.readLine();

            if (line.equals("-1")) {
                return null;
            }

            char[] body = new char[Integer.parseInt(line) + 2]; // needs '\r\n'
            reader.read(body);
            return String.valueOf(body);
        }

        if (response[0] == '-') {
            String line = reader.readLine();
            logger.error(line);
        }

        return null;
    }
}