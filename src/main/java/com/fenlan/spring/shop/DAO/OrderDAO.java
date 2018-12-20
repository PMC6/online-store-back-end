package com.fenlan.spring.shop.DAO;

import com.fenlan.spring.shop.bean.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.domain.Pageable;
import java.util.List;

public interface OrderDAO extends JpaRepository<Order, Long> {
    Order findByUserId(Long id);
    List<Order> findAllByShopIdAndStatus(Pageable pageable, Long productId, String statu);
}
