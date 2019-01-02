package com.fenlan.spring.shop.DAO;

import com.fenlan.spring.shop.bean.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.List;

public interface OrderDAO extends JpaRepository<Order, Long> {
    List<Order> findAllByShopIdAndStatus(Pageable pageable, Long shopId, String status);
    List<Order> findAllByShopIdAndStatus(Long shopId, String status);
    List<Order> findAllByShopName(Pageable pageable, String shopName);
    List<Order> findAllByUserId(Pageable pageable, Long userId);
    int countAllByShopId(Long shopId);
    int countAllByShopIdAndStatus(Long shopId, String status);
    int countAllByUserId(Long userId);
    int countAllByUserIdAndStatus(Long userId, String status);
    List<Order> findAllByProductId(Pageable pageable, Long productId);
    int countAllByProductId(Long productId);
    List<Order> findAllByCreateTimeBetween(Date before, Date after);
    List<Order> findAllByCreateTimeBetween(Pageable pageable, Date before, Date after);
    List<Order> findAllByCreateTimeBetweenAndShopId(Pageable pageable, Date before, Date after, Long shopId);
    List<Order> findAllByCreateTimeBetweenAndShopId(Date before, Date after, Long shopId);
    List<Order> findAllByCreateTimeBetweenAndUserId(Pageable pageable, Date before, Date after, Long userID);
    List<Order> findAllByUserIdAndStatus(Pageable pageable, Long userId, String status);
}
