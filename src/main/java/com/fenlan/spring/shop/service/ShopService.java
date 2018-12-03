package com.fenlan.spring.shop.service;

/**
 * @author： fanzhonghao
 * @date: 18-12-1 19 26
 * @version: 1.0
 * @description:
 *   提供商店信息查看与修改功能
 */
import com.fenlan.spring.shop.DAO.ShopDAO;
import com.fenlan.spring.shop.DAO.UserDAO;
import com.fenlan.spring.shop.bean.Shop;
import com.fenlan.spring.shop.bean.User;
import com.fenlan.spring.shop.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    /**
     * 保存商店信息
     * @param shop
     */
    public void saveShop(Shop shop){
        shopDAO.save(shop);
    }

    /**
     * 更新商店信息
     * @param originalShopId 更改之前的商店ID
     * @param targetShop 更改之后的商店信息
     */
    public Shop update(long originalShopId, Shop targetShop){
        shopDAO.deleteById(originalShopId);
        shopDAO.save(targetShop);
        return shopDAO.findById((long) targetShop.getId());
    }

    /**
     * 由shop_id得到商店信息
     * @param id shop id
     * @return
     */
    public Shop findByShopId(long id){
        return shopDAO.findById(id);
    }

    /**
     * 由卖家名字得到其所有的商店的信息
     * @param sellerName: seller name
     * @return shop
     */
    public Shop findByOwnerName(String sellerName){
        if(sellerName == null){
            return null;
        }
        User user = userDAO.findByUsername(sellerName);
        if (user == null){
            return null;
        }
        return  shopDAO.findByUserId(user.getId());
    }

    /**
     * 由卖家名删除商店信息
     * @param sellerName
     */
    public void deleteShopByOwnerName(String sellerName) {
        User user = userDAO.findByUsername(sellerName);
        if (user == null) return;
        Shop shop = shopDAO.findByUserId(user.getId());
        new ProductService().deleteProductWithShop(shop);
        shopDAO.delete(shop);
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

    /**
     * 查找所有商店信息
     * @return
     */
    public List<Shop> findAllShop(){
        return shopDAO.findAll();
    }
}
