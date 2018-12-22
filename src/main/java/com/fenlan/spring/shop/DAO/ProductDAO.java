package com.fenlan.spring.shop.DAO;

import com.fenlan.spring.shop.bean.Product;
import com.fenlan.spring.shop.bean.Shop;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductDAO extends JpaRepository<Product, Long> {
    List<Product> findByName(Pageable pageable, String name);
    List<Product> findByNameAndCategoryId(Pageable pageable, String name, Long id);
    Product findByNameAndShop(String name, Shop shop);
    List<Product> findAllByShopId(Pageable pageable, Long id);
    List<Product> findAllByNameContaining(Pageable pageable, String name);
    List<Product> findAllByNameContainingAndCategoryId(Pageable pageable, String name, Long id);
    List<Product> findAllByCategoryId(Pageable pageable, Long id);
    Long countByNameContaining(String name);
    Long countByNameContainingAndCategoryId(String name, Long id);
    Long countAllByShopIdAndHomePage(Long shopId, boolean homePage);
    List<Product> findAllByShopIdAndHomePage(Long shopId, boolean homePage);
    Long countByCategoryId(Long id);
}
