package com.fenlan.spring.shop.DAO;

import com.fenlan.spring.shop.bean.Request;
import com.fenlan.spring.shop.bean.RequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RequestDAO extends JpaRepository<Request, Long> {
    Request findByShopName(String name);
    List<Request> findAllByStatus(RequestStatus status);
}
