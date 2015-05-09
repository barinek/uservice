package com.barinek.uservice;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class NoopHandler extends AbstractHandler {
    @Override
    public void handle(String s, Request request, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException, ServletException {
        httpServletResponse.setContentType("text/html; charset=UTF-8");
        httpServletResponse.getOutputStream().write("Noop!".getBytes());
        httpServletResponse.setStatus(HttpServletResponse.SC_OK);
        request.setHandled(true);
    }
}
