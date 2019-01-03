/**
 * @author： fanzhonghao
 * @date: 19-1-2 11 11
 * @version: 1.0
 * @description:
 */
package com.fenlan.spring.shop.service;

import com.fenlan.spring.shop.DAO.CommissionDAO;
import com.fenlan.spring.shop.bean.CommissionRate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommissionService {

    @Autowired
    CommissionDAO commissionDAO;

    public double findLastCommissionRate() throws Exception{
        Pageable pageable = PageRequest.of(0, 1, Sort.by(Sort.Direction.DESC, "id"));
        List<CommissionRate> list = commissionDAO.findAll(pageable).getContent();
        if (list.size() != 0) return list.get(0).getRate();
        throw new Exception("no commission rate");
    }

    public boolean add(double rate) throws Exception{
        try {
            CommissionRate commissionRate = new CommissionRate();
            commissionRate.setRate(rate);
            commissionDAO.save(commissionRate);
            return true;
        }catch (Exception e){
            throw new Exception(e.getLocalizedMessage());
        }
    }
}
