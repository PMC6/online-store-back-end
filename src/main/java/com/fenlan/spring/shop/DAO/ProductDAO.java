package com.fenlan.spring.shop.DAO;

import com.fenlan.spring.shop.bean.Product;
import com.fenlan.spring.shop.bean.Shop;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductDAO extends JpaRepository<Product, Long> {
    List<Product> findByName(String name);
    Product findByNameAndShop(String name, Shop shop);
    List<Product> findAllByShopId(Pageable pageable, Long id);
}
