package com.fenlan.spring.shop.DAO;

import com.fenlan.spring.shop.bean.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FavoriteDAO extends JpaRepository<Favorite, Long> {
    Favorite findByUserId(Long id);
}
