package com.niv3d.bookstore.catalog.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest(properties = {
        "spring.test.database.replace=none",
        "spring.datasource.url=jdbc:tc:postgresql:16-alpine:///db"
})
@Sql("/test-data.sql")
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Test
    void shouldFindProductByCode() {
        ProductEntity product = productRepository.findByCode("P100").orElseThrow();
        assertThat(product.getCode()).isEqualTo("P100");
        assertThat(product.getName()).isEqualTo("Echoes of Tomorrow");
        assertThat(product.getDescription()).isEqualTo("A thrilling sci-fi adventure set in a world where time travel blurs the line between past and destiny.");
        assertThat(product.getImageUrl()).isEqualTo("https://example.com/images/echoes-of-tomorrow.jpg");
        assertThat(product.getPrice()).isEqualTo(new BigDecimal("29.99"));
    }

    @Test
    void shouldReturnEmptyWhenProductCodeNotFound() {
        var productOpt = productRepository.findByCode("NON_EXISTENT_CODE");
        assertThat(productOpt).isEmpty();
    }
}