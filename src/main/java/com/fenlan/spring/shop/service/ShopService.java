package com.fenlan.spring.shop.service;

import com.fenlan.spring.shop.DAO.ShopDAO;
import com.fenlan.spring.shop.DAO.SysRoleDAO;
import com.fenlan.spring.shop.DAO.UserDAO;
import com.fenlan.spring.shop.bean.Shop;
import com.fenlan.spring.shop.bean.SysRole;
import com.fenlan.spring.shop.bean.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ShopService {
    @Autowired
    ShopDAO shopDAO;
    @Autowired
    UserDAO userDAO;
    @Autowired
    SysRoleDAO sysRoleDAO;

    public Shop add(Shop shop) throws Exception {
        if (null != shopDAO.findByName(shop.getName()))
            throw new Exception("shop name is exist");
        else {
            try {
                User user = shop.getUser();
                List<SysRole> roles = new ArrayList<>();
                roles.add(sysRoleDAO.findByName("ROLE_USER"));
                roles.add(sysRoleDAO.findByName("ROLE_SELLER"));
                user.setRoles(roles);
                userDAO.save(user);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return shopDAO.save(shop);
        }
    }

    // 需要改进筛选条件
    public Long amount() {
        return shopDAO.count();
    }

    public Shop findByName(String shopName) throws Exception {
        Shop shop = shopDAO.findByName(shopName);
        if (null == shop)
            throw new Exception("don't have one shop named " + shopName);
        else
            return shop;
    }

    // 需要权衡异常处理
    public Shop findByUserId(Long id) {
        return shopDAO.findByUserId(id);
    }

    public List<Shop> list(int page, int size) throws Exception {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createTime"));
        List<Shop> list = shopDAO.findAll(pageable).getContent();
        if (list.size() == 0)
            throw new Exception("no result or page param is bigger than normal");
        else
            return list;
    }
}
