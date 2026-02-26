package airhacks;

import java.io.IOException;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpServer;

import airhacks.addresses.boundary.AddressesResource;
import airhacks.customers.boundary.CustomersResource;

interface App {

    static void main(String... args) throws IOException {
        var server = HttpServer.create(new InetSocketAddress(8080), 0);
        new CustomersResource().register(server);
        new AddressesResource().register(server);
        server.start();
        System.out.println("Server started on http://localhost:8080");
    }
}
