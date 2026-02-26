package airhacks.qmp.products.boundary;

import java.math.BigDecimal;
import java.util.List;

import airhacks.qmp.auth.boundary.AuthResourceClient;
import airhacks.qmp.auth.entity.RegistrationRequest;
import airhacks.qmp.auth.entity.Role;
import airhacks.qmp.auth.entity.TokenResponse;
import airhacks.qmp.products.entity.Category;
import airhacks.qmp.products.entity.CreateProductRequest;
import airhacks.qmp.products.entity.Product;
import airhacks.qmp.products.entity.ProductStatus;
import airhacks.qmp.products.entity.UpdateProductRequest;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import static org.assertj.core.api.Assertions.assertThat;

@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ProductResourceIT {

    @Inject
    @RestClient
    AuthResourceClient authClient;

    @Inject
    @RestClient
    ProductResourceClient productClient;

    static String sellerToken;
    static String seller2Token;
    static String customerToken;
    static String productId;

    @Test
    @Order(1)
    void registerSeller() {
        var request = new RegistrationRequest("seller-products@test.com", "secret123", "Product Seller", Role.SELLER);
        var response = this.authClient.register(request);
        assertThat(response.getStatus()).isEqualTo(201);
        var tokenResponse = response.readEntity(TokenResponse.class);
        sellerToken = tokenResponse.token();
    }

    @Test
    @Order(2)
    void registerSecondSeller() {
        var request = new RegistrationRequest("seller2-products@test.com", "secret123", "Second Seller", Role.SELLER);
        var response = this.authClient.register(request);
        assertThat(response.getStatus()).isEqualTo(201);
        var tokenResponse = response.readEntity(TokenResponse.class);
        seller2Token = tokenResponse.token();
    }

    @Test
    @Order(3)
    void registerCustomer() {
        var request = new RegistrationRequest("customer-products@test.com", "secret123", "Product Customer",
                Role.CUSTOMER);
        var response = this.authClient.register(request);
        assertThat(response.getStatus()).isEqualTo(201);
        var tokenResponse = response.readEntity(TokenResponse.class);
        customerToken = tokenResponse.token();
    }

    @Test
    @Order(4)
    void createProductAsSeller() {
        var request = new CreateProductRequest("Test Product", "A test product description",
                new BigDecimal("29.99"), Category.ELECTRONICS);
        var response = this.productClient.create("Bearer " + sellerToken, request);
        assertThat(response.getStatus()).isEqualTo(201);
        var product = response.readEntity(Product.class);
        assertThat(product.name()).isEqualTo("Test Product");
        assertThat(product.status()).isEqualTo(ProductStatus.DRAFT);
        assertThat(product.id()).isNotNull();
        productId = product.id();
    }

    @Test
    @Order(5)
    void createProductAsCustomer() {
        var request = new CreateProductRequest("Customer Product", "Should fail",
                new BigDecimal("19.99"), Category.BOOKS);
        var response = this.productClient.create("Bearer " + customerToken, request);
        assertThat(response.getStatus()).isEqualTo(403);
    }

    @Test
    @Order(6)
    void createProductWithoutAuth() {
        var request = new CreateProductRequest("No Auth Product", "Should fail",
                new BigDecimal("9.99"), Category.CLOTHING);
        var response = this.productClient.create(null, request);
        assertThat(response.getStatus()).isEqualTo(401);
    }

    @Test
    @Order(7)
    void listActiveProductsEmpty() {
        var response = this.productClient.listActive();
        assertThat(response.getStatus()).isEqualTo(200);
        var products = response.readEntity(List.class);
        assertThat(products).isEmpty();
    }

    @Test
    @Order(8)
    void updateProductToActive() {
        var request = new UpdateProductRequest("Test Product", "A test product description",
                new BigDecimal("29.99"), Category.ELECTRONICS, ProductStatus.ACTIVE);
        var response = this.productClient.update("Bearer " + sellerToken, productId, request);
        assertThat(response.getStatus()).isEqualTo(200);
        var product = response.readEntity(Product.class);
        assertThat(product.status()).isEqualTo(ProductStatus.ACTIVE);
    }

    @Test
    @Order(9)
    void listActiveProductsOne() {
        var response = this.productClient.listActive();
        assertThat(response.getStatus()).isEqualTo(200);
        var products = response.readEntity(List.class);
        assertThat(products).hasSize(1);
    }

    @Test
    @Order(10)
    void getProductById() {
        var response = this.productClient.getById(productId);
        assertThat(response.getStatus()).isEqualTo(200);
        var product = response.readEntity(Product.class);
        assertThat(product.id()).isEqualTo(productId);
        assertThat(product.name()).isEqualTo("Test Product");
    }

    @Test
    @Order(11)
    void getNonexistentProduct() {
        var response = this.productClient.getById("nonexistent-id");
        assertThat(response.getStatus()).isEqualTo(404);
    }

    @Test
    @Order(12)
    void listSellerProducts() {
        var response = this.productClient.listBySeller("Bearer " + sellerToken);
        assertThat(response.getStatus()).isEqualTo(200);
        var products = response.readEntity(List.class);
        assertThat(products).hasSize(1);
    }

    @Test
    @Order(13)
    void updateProductByDifferentSeller() {
        var request = new UpdateProductRequest("Hijacked", "Should fail",
                new BigDecimal("0.01"), Category.OTHER, ProductStatus.INACTIVE);
        var response = this.productClient.update("Bearer " + seller2Token, productId, request);
        assertThat(response.getStatus()).isEqualTo(403);
    }

    @Test
    @Order(14)
    void deleteProductByDifferentSeller() {
        var response = this.productClient.delete("Bearer " + seller2Token, productId);
        assertThat(response.getStatus()).isEqualTo(403);
    }

    @Test
    @Order(15)
    void deleteProductByOwner() {
        var response = this.productClient.delete("Bearer " + sellerToken, productId);
        assertThat(response.getStatus()).isEqualTo(204);
    }

    @Test
    @Order(16)
    void getDeletedProduct() {
        var response = this.productClient.getById(productId);
        assertThat(response.getStatus()).isEqualTo(404);
    }

    @Test
    @Order(17)
    void listActiveAfterDelete() {
        var response = this.productClient.listActive();
        assertThat(response.getStatus()).isEqualTo(200);
        var products = response.readEntity(List.class);
        assertThat(products).isEmpty();
    }
}
