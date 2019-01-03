package com.fenlan.spring.shop.DAO;

import com.fenlan.spring.shop.bean.Advertisement;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface AdvertisementDAO extends JpaRepository<Advertisement, Long> {
    List<Advertisement> findByCreateTimeGreaterThanEqualAndProductNotNullOrderByFeeDesc(Date day);
    List<Advertisement> findByCreateTimeGreaterThanEqualAndShopNotNullOrderByFeeDesc(Date day);
    Long countByCreateTimeGreaterThanEqualAndProductNotNull(Date day);
    Long countByCreateTimeGreaterThanEqualAndShopNotNull(Date day);
    List<Advertisement> findByProductId(Long productId);
    List<Advertisement> findByShopId(Long shopId);
    List<Advertisement> findAllByCreateTimeBetween(Date before, Date after);
    List<Advertisement> findAllByCreateTimeBetweenAndShopId(Date before, Date after, Long shopId);
    List<Advertisement> findAllByCreateTimeBetweenAndProductId(Date before, Date after, Long productId);
}
