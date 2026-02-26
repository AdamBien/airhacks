package oliver;

import com.sun.net.httpserver.HttpServer;
import java.net.InetSocketAddress;

interface App {

    static void main(String... args) throws Exception {
        var server = HttpServer.create(new InetSocketAddress(8080), 0);
        server.createContext("/customers", new CustomersResource());
        var productsResource = new ProductsResource();
        var cartResource = new CartResource(productsResource);
        var ordersResource = new OrdersResource(cartResource);
        server.createContext("/products", productsResource);
        server.createContext("/cart", cartResource);
        server.createContext("/orders", ordersResource);
        server.start();
        System.out.println("Server started on http://localhost:8080");
    }
}
