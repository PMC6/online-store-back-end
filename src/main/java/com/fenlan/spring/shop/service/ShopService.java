/**
 * @author： fanzhonghao
 * @date: 18-12-1 19 26
 * @version: 1.0
 * @description:
 *   提供商店信息查看与修改功能
 */
package com.fenlan.spring.shop.service;

import com.fenlan.spring.shop.DAO.ShopDAO;
import com.fenlan.spring.shop.DAO.UserDAO;
import com.fenlan.spring.shop.bean.Shop;
import com.fenlan.spring.shop.bean.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class ShopService {
    @Autowired
    ShopDAO shopDAO;
    @Autowired
    UserDAO userDAO;
    /**
     * 保存商店信息
     * @param shop
     */
    public void saveShop(Shop shop){
        shopDAO.save(shop);
    }

    /**
     * 更新商店信息
     * @param originalShop 更改之前的商店信息
     * @param targetShop 更改之后的商店信息
     */
    public void update(Shop originalShop, Shop targetShop){
        shopDAO.delete(originalShop);
        shopDAO.save(targetShop);
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
    public void deleteShopByOwnerName(String sellerName){
        User user = userDAO.findByUsername(sellerName);
        if (user == null) return;
        Shop shop = shopDAO.findByUserId(user.getId());
        new ProductService().deleteProductWithShop(shop);
        shopDAO.delete(shop);
    }
}
