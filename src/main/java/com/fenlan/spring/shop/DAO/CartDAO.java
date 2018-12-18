package com.fenlan.spring.shop.DAO;

import com.fenlan.spring.shop.bean.Cart;
import com.fenlan.spring.shop.bean.Product;
import com.fenlan.spring.shop.bean.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartDAO extends JpaRepository<Cart, Long> {
    List<Cart> findByUser(User user);
    Cart findByUserIdAndProductId(Long userId, Long productId);
}
