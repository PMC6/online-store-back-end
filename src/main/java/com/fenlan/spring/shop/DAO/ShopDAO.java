package com.fenlan.spring.shop.DAO;

import com.fenlan.spring.shop.bean.Shop;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShopDAO extends JpaRepository<Shop, Long> {
    Shop findByName(String name);
    Shop findByUserId(Long id);
}
