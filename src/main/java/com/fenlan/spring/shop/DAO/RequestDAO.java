package com.fenlan.spring.shop.DAO;

import com.fenlan.spring.shop.bean.Request;
import com.fenlan.spring.shop.bean.RequestStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RequestDAO extends JpaRepository<Request, Long> {
    Request findByName(String name);
    List<Request> findAllByStatus(RequestStatus status);
    List<Request> findAllByStatus(RequestStatus status, Pageable pageable);
    Long countByStatus(RequestStatus status);
}
