package com.fenlan.spring.shop.service;

import com.fenlan.spring.shop.DAO.CartDAO;
import com.fenlan.spring.shop.DAO.ProductDAO;
import com.fenlan.spring.shop.DAO.UserDAO;
import com.fenlan.spring.shop.bean.Cart;
import com.fenlan.spring.shop.bean.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartService {
    @Autowired
    CartDAO cartDAO;
    @Autowired
    UserDAO userDAO;
    @Autowired
    ProductDAO productDAO;

    public Cart add(Long productid, Integer number) throws Exception {
        if (null == productid || null == number)
            throw new Exception("please set 'productid' or 'number' param");
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        user = userDAO.findById(user.getId()).get();
        Cart cart = cartDAO.findByUserIdAndProduct_Id(user.getId(), productid);
        try {
            if (null != cart) {
                cart.setNumber(cart.getNumber() + number);
            } else {
                cart = new Cart();
                cart.setUser(user);
                cart.setNumber(number);
                cart.setProduct(productDAO.getOne(productid));
            }
            return cartDAO.save(cart);
        } catch (Exception e) {
            throw e;
        }
    }

    public List<Cart> list() throws Exception {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        user = userDAO.findById(user.getId()).get();
        List<Cart> list = cartDAO.findByUser(user);
        if (list.size() == 0)
            throw new Exception("no result");
        return list;
    }
}
