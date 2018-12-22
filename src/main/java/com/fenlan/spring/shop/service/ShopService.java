package com.fenlan.spring.shop.service;

/**
 * @author： fanzhonghao
 * @date: 18-12-1 19 26
 * @version: 1.0
 * @description:
 *   提供商店信息查看与修改功能
 */
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
import org.springframework.security.core.context.SecurityContextHolder;
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

    private User authUser() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userDAO.findById(user.getId()).get();
    }

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

    /**
     * 由shopname查找shop信息
     * @param shopName
     * @return
     * @throws Exception
     */
    public Shop findByName(String shopName) throws Exception {
        Shop shop = shopDAO.findByName(shopName);
        if (null == shop)
            throw new Exception("don't have one shop named " + shopName);
        else
            return shop;
    }

    public Shop finById(Long id) {
        return shopDAO.findById(id).get();
    }

    // 需要权衡异常处理
    public Shop findByUserId(Long id) {
        return shopDAO.findByUser(userDAO.findById(id).get());
    }

    public List<Shop> list(int page, int size) throws Exception {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createTime"));
        List<Shop> list = shopDAO.findAll(pageable).getContent();
        if (list.size() == 0)
            throw new Exception("no result or page param is bigger than normal");
        else
            return list;
    }

    public void delete(Long id) throws Exception {
        try {
            User seller = shopDAO.findById(id).get().getUser();
            shopDAO.deleteById(id);
            List<SysRole> list = new ArrayList<>();
            list.add(sysRoleDAO.findByName("ROLE_USER"));
            seller.setRoles(list);
            userDAO.save(seller);
        } catch (Exception e) {
            throw new Exception("don't have this shop OR disconnect db");
        }
    }

    /**
     * 通过用户名查找店铺
     * @param name
     * @return
     * @throws Exception
     */
    public Shop findByUserName(String name) throws Exception{
        Shop shop = shopDAO.findByUser(userDAO.findByUsername(name));
        if (shop == null) throw new Exception("can't find " + name + "'s shop");
        return shop;
    }

    public Shop update(Shop shop) throws Exception {
        SysRole admin = sysRoleDAO.findByName("ROLE_SELLER");
        if (!authUser().getRoles().contains(admin))
            throw new Exception("don't have permission");
        Shop shop1 = shopDAO.findById(shop.getId()).get();
        if (null == shop1)
            throw new Exception("not found this shop");
        if (null == shop.getName() || shop.getName().equals(""))
            throw new Exception("must contain 'name' param");
        shop.setUser(authUser());
        return shopDAO.save(shop);
    }
}
