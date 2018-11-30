package com.fenlan.spring.shop.DAO;

import com.fenlan.spring.shop.bean.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderDAO extends JpaRepository<Order, Long> {
    Order findByUserId(Long id);
}
