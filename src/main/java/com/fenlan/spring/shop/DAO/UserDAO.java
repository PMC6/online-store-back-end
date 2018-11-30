package com.fenlan.spring.shop.DAO;

import com.fenlan.spring.shop.bean.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDAO extends JpaRepository<User, Long> {
    User findByUsername(String name);
}
