package com.fenlan.spring.shop.service;

import com.fenlan.spring.shop.DAO.FavoriteDAO;
import com.fenlan.spring.shop.DAO.ProductDAO;
import com.fenlan.spring.shop.DAO.ShopDAO;
import com.fenlan.spring.shop.DAO.UserDAO;
import com.fenlan.spring.shop.bean.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FavoriteService {
    @Autowired
    FavoriteDAO favoriteDAO;
    @Autowired
    UserDAO userDAO;
    @Autowired
    ShopDAO shopDAO;
    @Autowired
    ProductDAO productDAO;

    private User authUser() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userDAO.findById(user.getId()).get();
    }

    public Favorite add(Integer type, Long id) throws Exception {
        Type type1 = Type.getByCode(type);
        if (null == type1)
            throw new Exception("param 'type' must be one of ["
                    +Type.SHOP.getCode()+", "+Type.PRODUCT.getCode());
        switch (type1) {
            case CUSTOMER: case SELLER: case ADMIN:
                throw new Exception("param 'type' must be one of ["
                        +Type.SHOP.getCode()+", "+Type.PRODUCT.getCode());
            case SHOP: return addShop(id);
            case PRODUCT: return addProduct(id);
            default: return null;
        }
    }

    public List<FavoriteResponseFormat> listShop() {
        List<Favorite> list = favoriteDAO.findByUserIdAndType(authUser().getId(), Type.SHOP.getCode());
        List<FavoriteResponseFormat> shopList = new ArrayList<>();
        for (Favorite item : list) {
            Shop shop = shopDAO.findById(item.getEntityid()).get();
            if (null == shop)
                favoriteDAO.deleteById(item.getId());
            else {
                FavoriteResponseFormat format = new FavoriteResponseFormat();
                format.setId(item.getId());
                format.setEntity(shop);
                format.setCreateTime(item.getCreateTime());
                shopList.add(format);
            }
        }
        return shopList;
    }

    public List<FavoriteResponseFormat> listProduct() {
        List<Favorite> list = favoriteDAO.findByUserIdAndType(authUser().getId(), Type.PRODUCT.getCode());
        List<FavoriteResponseFormat> productList = new ArrayList<>();
        for (Favorite item : list) {
            Product product = productDAO.findById(item.getEntityid()).get();
            if (null == product)
                favoriteDAO.deleteById(item.getId());
            else {
                FavoriteResponseFormat format = new FavoriteResponseFormat();
                format.setId(item.getId());
                format.setEntity(product);
                format.setCreateTime(item.getCreateTime());
                productList.add(format);
            }
        }
        return productList;
    }

    public void delete(Long id) throws Exception {
        Favorite favorite = favoriteDAO.findById(id).get();
        if (null == favorite)
            throw new Exception("don't find this favorite record");
        if (!favorite.getUserId().equals(authUser().getId()))
            throw new Exception("don't have permission");
        favoriteDAO.deleteById(id);
    }

    private Favorite addShop(Long id) throws Exception {
        Favorite favorite = favoriteDAO.findByUserIdAndTypeAndEntityid(
                authUser().getId(), Type.SHOP.getCode(), id);
        if (null != favorite)
            return favorite;
        favorite = new Favorite();
        favorite.setUserId(authUser().getId());
        favorite.setType(Type.SHOP.getCode());
        Shop shop = shopDAO.findById(id).get();
        if (null == shop)
            throw new Exception("Not Found this shop");
        favorite.setEntityid(shop.getId());
        return favoriteDAO.save(favorite);
    }

    private Favorite addProduct(Long id) throws Exception {
        Favorite favorite = favoriteDAO.findByUserIdAndTypeAndEntityid(
                authUser().getId(), Type.PRODUCT.getCode(), id);
        if (null != favorite)
            return favorite;
        favorite = new Favorite();
        favorite.setUserId(authUser().getId());
        favorite.setType(Type.PRODUCT.getCode());
        Product product = productDAO.findById(id).get();
        if (null == product)
            throw new Exception("Not Found this product");
        favorite.setEntityid(product.getId());
        return favoriteDAO.save(favorite);
    }
}
