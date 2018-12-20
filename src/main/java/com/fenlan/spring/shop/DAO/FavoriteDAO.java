package com.fenlan.spring.shop.DAO;

import com.fenlan.spring.shop.bean.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FavoriteDAO extends JpaRepository<Favorite, Long> {
    List<Favorite> findByUserIdAndType(Long id, int type);
    Favorite findByUserIdAndTypeAndEntityid(Long userId, Integer type, Long entityId);
}
