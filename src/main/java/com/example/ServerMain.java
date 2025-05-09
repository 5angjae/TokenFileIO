package main.java.com.example;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

public class ServerMain {
    public static void main(String[] args) throws Exception {
        Server server = new Server(8080);
        ServletContextHandler handler = new ServletContextHandler();
        handler.addServlet(new ServletHolder(new TokenHandler()), "/tokenize");
        server.setHandler(handler);

        server.start();
        System.out.println("서버 실행됨: http://localhost:8080/tokenize");
        server.join();
    }
}