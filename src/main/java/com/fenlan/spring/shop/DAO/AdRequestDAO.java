package com.fenlan.spring.shop.DAO;

import com.fenlan.spring.shop.bean.AdRequest;
import com.fenlan.spring.shop.bean.RequestStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface AdRequestDAO extends JpaRepository<AdRequest, Long> {
    List<AdRequest> findByCreateTimeGreaterThanEqualAndStatusOrderByFeeDesc(Pageable pageable,Date date, RequestStatus status);
    Long countByCreateTimeGreaterThanEqual(Date date);
}
