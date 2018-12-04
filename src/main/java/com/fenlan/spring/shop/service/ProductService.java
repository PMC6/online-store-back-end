package com.fenlan.spring.shop.service;

import com.fenlan.spring.shop.DAO.ProductDAO;
import com.fenlan.spring.shop.DAO.ShopDAO;
import com.fenlan.spring.shop.DAO.UserDAO;
import com.fenlan.spring.shop.bean.Product;
import com.fenlan.spring.shop.bean.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    @Autowired
    ProductDAO productDAO;
    @Autowired
    ShopDAO shopDAO;
    @Autowired
    UserDAO userDAO;

    public Product add(Product newProduct) throws Exception {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        user = userDAO.findById(user.getId()).get();
        newProduct.setShop(shopDAO.findByUser(user));
        try {
            if (null == newProduct.getName())
                throw new Exception("missing 'name' of product");
            if (null == (Double)newProduct.getPrice())
                throw new Exception("missing 'price'");
            if (null == (Integer)newProduct.getNumber())
                newProduct.setNumber(0);
            if (null != productDAO.findByNameAndShop(newProduct.getName(), newProduct.getShop()))
                throw new Exception("product has been added in this shop");
            return productDAO.save(newProduct);
        } catch (Exception e) {
            throw e;
        }
    }

    public List<Product> findByName(String name) throws Exception {
        List<Product> list = productDAO.findByName(name);
        if (list.size() == 0)
            throw new Exception("null");
        return list;
    }

    // 未异常处理
    public Product update(Product product) throws Exception {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        user = userDAO.findById(user.getId()).get();
        try {
            if (!product.getShop().getUser().equals(user))
                throw new Exception("you don't have permission to operate");
            if (null == product.getName())
                throw new Exception("missing 'shopName'");
            if (null == (Double)product.getPrice())
                throw new Exception("missing 'price'");
            if (null == (Integer)product.getNumber())
                product.setNumber(0);
            return productDAO.save(product);
        } catch (Exception e) {
            throw e;
        }
    }

    public void delete(Long id) throws Exception {
        Product product = productDAO.findById(id).get();
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        user = userDAO.findById(user.getId()).get();
        if (!product.getShop().getUser().equals(user))
            throw new Exception("you don't have permission to operate");
        if (null == product)
            throw new Exception("this product is not in DB");
        productDAO.deleteById(id);
    }

    public List<Product> list(Integer page, Integer size) throws Exception {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        user = userDAO.findById(user.getId()).get();
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createTime"));
        List<Product> list = productDAO.findAllByShopId(pageable, shopDAO.findByUser(user).getId());
        if (list.size() == 0)
            throw new Exception("no result or page param is bigger than normal");
        return list;
    }
}
