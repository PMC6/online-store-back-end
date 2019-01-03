/**
 * @author： fanzhonghao
 * @date: 19-1-3 10 15
 * @version: 1.0
 * @description:
 */
package com.fenlan.spring.shop.DAO;

import com.fenlan.spring.shop.bean.CommissionRate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;

public interface CommissionDAO extends JpaRepository<CommissionRate, Long> {

}
