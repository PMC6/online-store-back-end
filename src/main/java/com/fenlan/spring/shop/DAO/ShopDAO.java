package com.fenlan.spring.shop.DAO;

import com.fenlan.spring.shop.bean.Shop;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShopDAO extends JpaRepository<Shop, Long> {
    Shop findByName(String name);

    /**
     * update at 18.12.1 by fan
     */
    Shop findByUserId(long id);
}
