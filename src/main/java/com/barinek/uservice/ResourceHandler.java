package com.barinek.uservice;

import org.codehaus.jackson.map.ObjectMapper;
import org.eclipse.jetty.http.HttpMethod;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

public class ResourceHandler extends AbstractHandler {
    private final ResourceDAO dao;
    private final ObjectMapper mapper;

    public ResourceHandler(ResourceDAO dao) {
        this.dao = dao;
        this.mapper = new ObjectMapper();
    }

    @Override
    public void handle(String s, Request request, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException, ServletException {
        if (request.getMethod().equals(HttpMethod.POST.toString())) {
            if ("/resources".equals(request.getRequestURI())) {
                httpServletResponse.setContentType("application/json");
                try {
                    httpServletResponse.setStatus(HttpServletResponse.SC_CREATED);
                    mapper.writeValue(httpServletResponse.getOutputStream(),
                            dao.create(
                                    mapper.readValue(request.getReader(), Resource.class)
                            )
                    );
                } catch (SQLException e) {
                    httpServletResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                }
                request.setHandled(true);
            }
        }

        if (request.getMethod().equals(HttpMethod.GET.toString())) {
            if ("/resources".equals(request.getRequestURI())) {
                httpServletResponse.setContentType("application/json");
                try {
                    httpServletResponse.setStatus(HttpServletResponse.SC_OK);
                    mapper.writeValue(httpServletResponse.getOutputStream(), dao.list());
                } catch (SQLException e) {
                    httpServletResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                }
                request.setHandled(true);
            }
        }
    }
}
