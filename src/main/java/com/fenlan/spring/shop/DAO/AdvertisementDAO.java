package com.fenlan.spring.shop.DAO;

import com.fenlan.spring.shop.bean.Advertisement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AdvertisementDAO extends JpaRepository<Advertisement, Long> {
    List<Advertisement> findByCreateTimeContaining(String day);
}
