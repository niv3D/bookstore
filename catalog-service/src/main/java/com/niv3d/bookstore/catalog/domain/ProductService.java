package com.niv3d.bookstore.catalog.domain;

import com.niv3d.bookstore.catalog.ApplicationProperties;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class ProductService {

    private final ProductRepository productRepository;
    private final ApplicationProperties properties;

    ProductService(ProductRepository productRepository, ApplicationProperties properties) {
        this.productRepository = productRepository;
        this.properties = properties;
    }

    public PagedResult<Product> getProducts(int pageNo) {
        pageNo = Math.max(pageNo - 1, 0);
        Sort sortByName = Sort.by("name").ascending();
        PageRequest pageRequest = PageRequest.of(pageNo, properties.pageSize(), sortByName);
        Page<Product> productsPage =
                productRepository.findAll(pageRequest)
                        .map(ProductMapper::toProduct);
        return new PagedResult<>(
                productsPage.getContent(),
                productsPage.getTotalElements(),
                productsPage.getNumber() + 1,
                productsPage.getTotalPages(),
                productsPage.isFirst(),
                productsPage.isLast(),
                productsPage.hasNext(),
                productsPage.hasPrevious()
        );
    }

    public Optional<Product> getProductByCode(String code) {
        return productRepository.findByCode(code)
                .map(ProductMapper::toProduct);
    }
}
