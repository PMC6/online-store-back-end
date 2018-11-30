package com.fenlan.spring.shop.DAO;

import com.fenlan.spring.shop.bean.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductDAO extends JpaRepository<Product, Long> {
    List<Product> findByName(String name);
    Product findByShopIdAndName(Long shopId, String name);
}
