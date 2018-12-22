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

    public Cart add(Long productId, Integer number) throws Exception {
        if (null == productId || null == number)
            throw new Exception("please set 'productid' or 'number' param");
        Cart cart = cartDAO.findByUserIdAndProductId(authUser().getId(), productId);
        try {
            if (null != cart) {
                if (cart.getProduct().getNumber() < cart.getNumber()+number)
                    throw new Exception("this product only "+cart.getProduct().getNumber()+" left in stock");
                cart.setNumber(cart.getNumber() + number);
            } else {
                int left = productDAO.getOne(productId).getNumber();
                if (left < number)
                    throw new Exception("this product only "+left+" left in stock");
                cart = new Cart();
                cart.setUser(authUser());
                cart.setNumber(number);
                cart.setProduct(productDAO.getOne(productId));
            }
            return cartDAO.save(cart);
        } catch (Exception e) {
            throw e;
        }
    }

    public List<Cart> list() throws Exception {
        List<Cart> list = cartDAO.findByUser(authUser());
        if (list.size() == 0)
            throw new Exception("no result");
        return list;
    }

    public Cart update(Long id, Integer number) throws Exception {
        Cart cart = cartDAO.findById(id).get();
        boolean flag = authUser().getId().equals(cart.getUser().getId());
        if (!flag)
            throw new Exception("don't have permission");
        cart.setNumber(number);
        return cartDAO.save(cart);
    }

    public void delete(Long id) throws Exception {
        Cart cart = cartDAO.findById(id).get();
        if (null == cart)
            throw new Exception("not found this cart to delete");
        boolean flag = authUser().getId().equals(cart.getUser().getId());
        if (!flag)
            throw new Exception("don't have permission");
        cartDAO.deleteById(id);
    }

    private User authUser() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userDAO.findById(user.getId()).get();
    }
}
