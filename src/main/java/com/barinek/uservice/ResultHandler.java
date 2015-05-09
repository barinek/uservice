package com.barinek.uservice;

import org.eclipse.jetty.http.HttpMethod;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ResultHandler extends AbstractHandler {
    private final ResultDAO dao;

    public ResultHandler(ResultDAO dao) {
        this.dao = dao;
    }

    @Override
    public void handle(String s, Request request, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException, ServletException {
        String uri = request.getRequestURI();

        if (request.getMethod().equals(HttpMethod.POST.toString())) {
            if (uri.matches("/resources/(\\d+)/results")) {
                httpServletResponse.setContentType("application/json");
                try {
                    httpServletResponse.setStatus(HttpServletResponse.SC_CREATED);
                    dao.create(uri, request.getReader().readLine());
                    httpServletResponse.getWriter().print("success");
                } catch (Exception e) {
                    httpServletResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                }
                request.setHandled(true);
            }
        }

        if (request.getMethod().equals(HttpMethod.GET.toString())) {
            if (uri.matches("/resources/(\\d+)/results")) {
                httpServletResponse.setContentType("application/json");
                try {
                    httpServletResponse.setStatus(HttpServletResponse.SC_OK);
                    String list = dao.list(uri);
                    if (list != null)
                        httpServletResponse.getWriter().print(list);
                } catch (Exception e) {
                    httpServletResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                }
                request.setHandled(true);
            }
        }
    }
}