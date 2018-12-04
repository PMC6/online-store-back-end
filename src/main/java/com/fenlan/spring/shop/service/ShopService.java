package com.fenlan.spring.shop.service;

/**
 * @author： fanzhonghao
 * @date: 18-12-1 19 26
 * @version: 1.0
 * @description:
 *   提供商店信息查看与修改功能
 */
import com.fenlan.spring.shop.DAO.ProductDAO;
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
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
public class ShopService {
    @Autowired
    ShopDAO shopDAO;
    @Autowired
    UserDAO userDAO;
    @Autowired
    SysRoleDAO sysRoleDAO;
    @Autowired
    ProductDAO productDAO;

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
        return shopDAO.findByUserId(id);
    }

    public Shop findByOwnerName(String ownerName) throws Exception{
        Shop shop = shopDAO.findByUserId(userDAO.findByUsername(ownerName).getId());
        if (shop == null) throw new Exception("can't find the owner");
        return shop;
    }

    /**
     * 得到分页商铺信息
     * @param page
     * @param size
     * @return
     * @throws Exception
     */
    public List<Shop> list(int page, int size) throws Exception {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createTime"));
        List<Shop> list = shopDAO.findAll(pageable).getContent();
        if (list.size() == 0)
            throw new Exception("no result or page param is bigger than normal");
        else
            return list;
    }

    /**
     * 删除店铺信息同时改变卖家状态,同时删除商店所有产品
     * @param id
     * @throws Exception
     */
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
     * 更新shop数据
     * @param shop
     * @return
     */
    public Shop update(Shop shop) throws Exception{
            try {
                Shop shop1 = shopDAO.findById(shop.getId()).get();
                shop1.setTelephone(shop.getTelephone());
                shop1.setInfo(shop.getInfo());
                shop1.setName(shop.getName());
                shop1.setImage(shop.getImage());
                shop1.setEmail(shop.getEmail());
                shop1.setUpdateTime(new Date());
                shop1.setUser(shop.getUser());
                shopDAO.save(shop1);
                return shop1;
            }catch (Exception e){
                throw new Exception("can't update the shop info");
            }
    }
}
