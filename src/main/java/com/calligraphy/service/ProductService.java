package com.calligraphy.service;

import com.calligraphy.entity.Product;

import java.util.List;

public interface ProductService {
    List<Product> list();
    Product detail(Long id);
    void add(Product product);
    void delete(Long id);
}