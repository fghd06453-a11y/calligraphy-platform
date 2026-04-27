package com.calligraphy.service.impl;

import com.calligraphy.entity.Product;
import com.calligraphy.mapper.ProductMapper;
import com.calligraphy.service.ProductService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductMapper productMapper;

    public ProductServiceImpl(ProductMapper productMapper) {
        this.productMapper = productMapper;
    }

    @Override
    public List<Product> list() {
        return productMapper.selectList(null);
    }

    @Override
    public Product detail(Long id) {
        return productMapper.selectById(id);
    }

    @Override
    public void add(Product product) {
        productMapper.insert(product);
    }

    @Override
    public void delete(Long id) {
        productMapper.deleteById(id);
    }
}
