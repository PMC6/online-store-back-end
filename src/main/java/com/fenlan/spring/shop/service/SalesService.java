/**
 * @author： fanzhonghao
 * @date: 19-1-4 13 35
 * @version: 1.0
 * @description:
 */
package com.fenlan.spring.shop.service;

import com.fenlan.spring.shop.DAO.OrderDAO;
import com.fenlan.spring.shop.DAO.ProductDAO;
import com.fenlan.spring.shop.DAO.ShopDAO;
import com.fenlan.spring.shop.DAO.UserDAO;
import com.fenlan.spring.shop.bean.Order;
import com.fenlan.spring.shop.bean.Product;
import com.fenlan.spring.shop.bean.Shop;
import com.fenlan.spring.shop.bean.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@Service
public class SalesService {
    @Autowired
    UserDAO userDAO;
    @Autowired
    OrderDAO orderDAO;
    @Autowired
    ProductDAO productDAO;
    @Autowired
    ShopDAO shopDAO;

    private User authUser() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userDAO.findById(user.getId()).get();
    }
    public List<Order> listByTimes(Date before, Date after, int page, int size) throws Exception{
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createTime"));
        Shop shop = shopDAO.findByUser(authUser());
        if (shop == null) throw new Exception("you are not sellers");
        List<Product> productList = productDAO.findAllByShopId(pageable, shop.getId());
        if (productList.size() == 0) throw new Exception("this shop doesn't have any product");
        List<Order> orderList = new LinkedList<>();
        for (Product product : productList){
            Order order = null;
            List<Order> orders = orderDAO.findAllByCreateTimeBetweenAndProductIdAndStatus(before, after,
                    product.getId(), "Complete");
            if (orders.size() != 0){
                order = orders.get(0);
                int totalNum = 0;
                double totalSales = 0.0;
                for (Order order1 : orders){
                    totalNum += order1.getNumber();
                    totalSales += order1.getTotalPrice() - order1.getCommission();
                }
                order.setNumber(totalNum);
                order.setTotalPrice(totalSales);
            }else {
                order = new Order();
                order.setNumber(0);
                order.setTotalPrice(0);
                order.setStatus("Nothing");
                order.setProductName(product.getName());
                order.setPrice(product.getPrice());
                order.setProductImg(product.getImage());
            }
            ((LinkedList<Order>) orderList).addLast(order);
        }
        return orderList;
    }

    public List<Order> listOneProduct(Date before, Date after, int page, int size, String productName) throws Exception{
        User user = authUser();
        Shop shop = shopDAO.findByUser(user);
        if (shop == null) throw new Exception("you are not sellers");
        Product product = productDAO.findByNameAndShop(productName, shop);
        if (product == null) throw new Exception("no this product");
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createTime"));
        List<Order> orderList = orderDAO.findAllByCreateTimeBetweenAndProductIdAndStatus(pageable, before,
                after, product.getId(), "Complete");
        if (orderList.size() == 0) throw new Exception("no order about this product");
        return orderList;
    }

    public int amountOneProduct(Date before, Date after, String productName) throws Exception{
        User user = authUser();
        Shop shop = shopDAO.findByUser(user);
        if (shop == null) throw new Exception("you are not sellers");
        Product product = productDAO.findByNameAndShop(productName, shop);
        if (product == null) throw new Exception("no this product");
        int num = 0;
        num = orderDAO.countAllByCreateTimeBetweenAndProductIdAndStatus(before, after,
                product.getId(), "Complete");
        return num;
    }

    public Order searchOneProduct(Date before, Date after, String productName) throws Exception{
        User user = authUser();
        Shop shop = shopDAO.findByUser(user);
        if (shop == null) throw new Exception("you are not sellers");
        Product product = productDAO.findByNameAndShop(productName, shop);
        List<Order> orderList = orderDAO.findAllByCreateTimeBetweenAndProductIdAndStatus(before, after,
                product.getId(), "Complete");
        Order order = new Order();
        if (orderList.size() != 0){
            int totalNum = 0;
            double totalSales = 0.0;
            for (Order order1 : orderList){
                totalNum += order1.getNumber();
                totalSales += order1.getTotalPrice() - order1.getCommission();
            }
            order = orderList.get(0);
            order.setNumber(totalNum);
            order.setTotalPrice(totalSales);
        }else {
            order = new Order();
            order.setNumber(0);
            order.setTotalPrice(0);
            order.setStatus("Nothing");
            order.setProductName(product.getName());
            order.setPrice(product.getPrice());
            order.setProductImg(product.getImage());
            order.setUserName(user.getUsername());
        }
        return order;
    }
}
