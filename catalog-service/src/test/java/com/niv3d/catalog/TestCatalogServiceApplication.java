package com.niv3d.catalog;

import org.springframework.boot.SpringApplication;

public class TestCatalogServiceApplication {

	public static void main(String[] args) {
		SpringApplication.from(CatalogServiceApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
