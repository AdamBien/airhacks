package com.airhacks;

import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author airhacks.com
 */
@WebServlet(name = "HelloWorld", urlPatterns = {"/HelloWorld"})
public class HelloWOrld extends HttpServlet {

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        System.out.println("hey joe");
        response.getWriter().print("hello world " + System.currentTimeMillis());
    }

}
