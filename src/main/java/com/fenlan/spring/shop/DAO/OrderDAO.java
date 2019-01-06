package com.fenlan.spring.shop.DAO;

import com.fenlan.spring.shop.bean.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.List;

public interface OrderDAO extends JpaRepository<Order, Long> {
    List<Order> findAllByShopIdAndStatus(Pageable pageable, Long shopId, String status);
    List<Order> findAllByShopIdAndStatus(Long shopId, String status);
    List<Order> findAllByShopNameAndStatusNot(Pageable pageable, String shopName, String status);
    List<Order> findAllByUserIdAndStatusNot(Pageable pageable, Long userId, String status);
    int countAllByShopIdAndStatusNot(Long shopId, String status);
    int countAllByShopIdAndStatus(Long shopId, String status);
    int countAllByUserIdAndStatusNot(Long userId, String status);
    int countAllByUserIdAndStatus(Long userId, String status);
    int countAllByUserIdAndStatusAndCreateTimeBetween(Long userId, String status, Date before, Date after);
    List<Order> findAllByProductIdAndStatusNot(Pageable pageable, Long productId, String status);
    int countAllByProductIdAndStatusNot(Long productId, String status);
    List<Order> findAllByCreateTimeBetweenAndStatus(Date before, Date after, String status);
    List<Order> findAllByCreateTimeBetweenAndStatusNot(Pageable pageable, Date before, Date after, String status);
    List<Order> findAllByCreateTimeBetweenAndStatusNot(Date before, Date after, String status);
    int countAllByCreateTimeBetweenAndShopIdAndStatusNot(Date before, Date after, Long shopId, String status);
    List<Order> findAllByCreateTimeBetweenAndShopIdAndStatusNot(Pageable pageable, Date before, Date after,
                                                                Long shopId, String status);
    List<Order> findAllByCreateTimeBetweenAndProductIdAndStatus(Date before, Date after,
                                                                Long productId, String status);
    List<Order> findAllByCreateTimeBetweenAndProductIdAndStatus(Pageable pageable, Date before, Date after,
                                                                Long productId, String status);
    List<Order> findAllByCreateTimeBetweenAndShopIdAndStatus(Date before, Date after, Long shopId, String status);
    List<Order> findAllByCreateTimeBetweenAndUserIdAndStatus(Pageable pageable, Date before, Date after,
                                                               Long userID, String status);
    List<Order> findAllByUserIdAndStatus(Pageable pageable, Long userId, String status);
    List<Order> findAllByStatus(String status);
    int countAllByCreateTimeBetweenAndProductIdAndStatus(Date before, Date after, Long productId, String status);

}
