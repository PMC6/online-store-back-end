package com.fenlan.spring.shop.DAO;

import com.fenlan.spring.shop.bean.BlackList;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlackListDAO extends JpaRepository<BlackList, Long> {
    BlackList findByTypeAndEntityId(String type, Long entityId);
}
