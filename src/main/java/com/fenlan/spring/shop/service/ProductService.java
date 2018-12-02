package com.fenlan.spring.shop.service;

import com.fenlan.spring.shop.DAO.ProductDAO;
import com.fenlan.spring.shop.bean.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    @Autowired
    ProductDAO productDAO;

    public Product add(Product newProduct) throws Exception {
        try {
            if (null != productDAO.findByShopIdAndName(newProduct.getShopId(), newProduct.getName()))
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
}
