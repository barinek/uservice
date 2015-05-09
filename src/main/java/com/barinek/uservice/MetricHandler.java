package com.barinek.uservice;

import com.codahale.metrics.MetricRegistry;
import org.codehaus.jackson.map.ObjectMapper;
import org.eclipse.jetty.http.HttpMethod;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class MetricHandler extends AbstractHandler {
    private final MetricRegistry metrics;
    private final ObjectMapper mapper;

    public MetricHandler(MetricRegistry metrics) {
        this.metrics = metrics;
        this.mapper = new ObjectMapper();
    }

    @Override
    public void handle(String s, Request request, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException, ServletException {
        if (request.getMethod().equals(HttpMethod.GET.toString())) {
            if ("/metrics".equals(request.getRequestURI())) {
                httpServletResponse.setContentType("application/json");
                httpServletResponse.setStatus(HttpServletResponse.SC_OK);
                mapper.writerWithDefaultPrettyPrinter().writeValue(httpServletResponse.getOutputStream(), metrics.getMetrics());
                request.setHandled(true);
            }
        }
    }
}