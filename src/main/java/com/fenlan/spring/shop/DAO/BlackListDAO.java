package com.fenlan.spring.shop.DAO;

import com.fenlan.spring.shop.bean.BlackList;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BlackListDAO extends JpaRepository<BlackList, Long> {
    List<BlackList> findAllByType(Pageable pageable, int type);
    long countByType(int type);
}
