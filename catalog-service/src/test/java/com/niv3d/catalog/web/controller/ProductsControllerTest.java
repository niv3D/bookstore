package com.niv3d.catalog.web.controller;

import com.niv3d.catalog.AbstractIntegrationTest;
import com.niv3d.catalog.domain.Product;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.jdbc.Sql;

import java.math.BigDecimal;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

@Sql("/test-data.sql")
class ProductsControllerTest extends AbstractIntegrationTest {

    @Test
    void shouldReturnProducts() {
        given().contentType(ContentType.JSON)
                .when()
                .get("api/products")
                .then()
                .statusCode(200)
                .body("data", hasSize(10))
                .body("totalElements", is(15))
                .body("pageNumber", is(1))
                .body("totalElements", is(15))
                .body("pageNumber", is(1))
                .body("totalPages", is(2))
                .body("isFirst", is(true))
                .body("isLast", is(false))
                .body("hasNext", is(true))
                .body("hasPrevious", is(false));
    }

    @Test
    void shouldReturnProductByCode() {
        Product product = given().contentType(ContentType.JSON)
                .when()
                .get("api/products/{code}", "P100")
                .then()
                .statusCode(200)
                .assertThat()
                .extract()
                .body()
                .as(Product.class);

        assertThat(product.code()).isEqualTo("P100");
        assertThat(product.name()).isEqualTo("Echoes of Tomorrow");
        assertThat(product.description()).isEqualTo("A thrilling sci-fi adventure set in a world where time travel blurs the line between past and destiny.");
        assertThat(product.imageUrl()).isEqualTo("https://example.com/images/echoes-of-tomorrow.jpg");
        assertThat(product.price()).isEqualTo(new BigDecimal("29.99"));
    }

    @Test
    void shouldReturnNotFoundForNonExistentProductCode() {
        String code = "NON_EXISTENT_CODE";
        given().contentType(ContentType.JSON)
                .when()
                .get("api/products/{code}", code)
                .then()
                .statusCode(404)
                .body("title", is("Product Not Found"))
                .body("type", is("https://api.bookstore.com/errors/not-found"))
                .body("service", is("Catalog Service"))
                .body("error_category", is("Generic"))
                .body("detail", is("Product with code " + code + " not found"));
    }
}