package com.fenlan.spring.shop.DAO;

import com.fenlan.spring.shop.bean.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartDAO extends JpaRepository<Cart, Long> {
    Cart findByUserId(Long id);
}
