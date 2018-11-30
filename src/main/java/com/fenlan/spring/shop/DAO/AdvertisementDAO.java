package com.fenlan.spring.shop.DAO;

import com.fenlan.spring.shop.bean.Advertisement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdvertisementDAO extends JpaRepository<Advertisement, Long> {
    Advertisement findByShopId(Long id);
}
