package com.fenlan.spring.shop.DAO;

import com.fenlan.spring.shop.bean.Shop;
import com.fenlan.spring.shop.bean.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShopDAO extends JpaRepository<Shop, Long> {
    Shop findByName(String name);
    Shop findByUser(User user);
}
