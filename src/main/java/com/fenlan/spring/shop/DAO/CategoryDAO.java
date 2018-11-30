package com.fenlan.spring.shop.DAO;

import com.fenlan.spring.shop.bean.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryDAO extends JpaRepository<Category, Long> {
    Category findByName(String name);
}
