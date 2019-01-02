/**
 * @author： fanzhonghao
 * @date: 18-12-27 08 53
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

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class OrderService {
    @Autowired
    OrderDAO orderDAO;
    @Autowired
    ShopDAO shopDAO;
    @Autowired
    ProductDAO productDAO;
    @Autowired
    UserDAO userDAO;

    //admin
    public Order findById(Long orderId) throws Exception{
        try {
            Order order = orderDAO.findById(orderId).get();
            return order;
        }catch (Exception e){
            throw new Exception("no this order");
        }
    }

    /**
     * 商城某一段时间的收入
     * @param before
     * @param after
     * @return
     */
    public double findMailIncome(Date before, Date after){
        List<Order> list = null;
        if (before == null && after== null){
            list = orderDAO.findAll();

        }else list = orderDAO.findAllByCreateTimeBetween(before, after);
        double sale = 0.0;
        for (Order order : list){
            sale += order.getCommission();
        }
        return sale;
    }

    private User authUser() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userDAO.findById(user.getId()).get();
    }

    //seller
    /**
     * 由店铺名字按照createTime排序查找店铺相关订单
     * @param user
     * @param page
     * @param size
     * @param positive
     * @return
     */
    public List<Order> list(int page, int size, boolean positive, User user) throws Exception{
        Shop shop = shopDAO.findByUser(user);
        if (shop == null) throw new Exception("you are not sellers");
        Pageable pageable = null;
        if (positive) {
            pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createTime"));
        }else pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "createTime"));
        List<Order> orderList = orderDAO.findAllByShopName(pageable, shop.getName());
        if (orderList.size() == 0) throw new Exception("no result with this page");
        return orderList;
    }

    /**
     * 商家通过订单状态查看订单
     * @param status
     * @param page
     * @param size
     * @return
     */
    public List<Order> listByOrderStatus(String status, int page, int size, User user) throws Exception{
        Shop shop = shopDAO.findByUser(user);
        if (shop == null) throw new Exception("you are not sellers");
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createTime"));
        List<Order> orderList = orderDAO.findAllByShopIdAndStatus(pageable, shop.getId(), status);
        if (orderList.size() == 0) throw new Exception("no result with this page");
        return orderList;
    }

    /**
     * 商家更改订单状态
     * @param orderId
     * @param status
     * @return
     * @throws Exception
     */
    public boolean updateOrderStatus(Long orderId, String status, User user) throws Exception{

        try {
            Order order = orderDAO.findById(orderId).get();
            Shop shop = shopDAO.findByUser(user);
            if (shop == null || !shop.getId().equals(order.getShopId()))
                throw new Exception("you are not authorized to update this order");
            order.setStatus(status);
            orderDAO.save(order);
            return true;
        }catch (Exception e){
            throw new Exception(e.getLocalizedMessage());
        }
    }

    /**
     * 取消订单
     * @return
     */
    public boolean cancelOrder(Long orderId, User user) throws Exception{
        if (user == null) user = authUser();
        Order order = orderDAO.findById(orderId).get();
        boolean authoity = false;
        if (user.getId().equals(order.getUserId())){
            //取消订单的是买家
            authoity = true;
        }else {
            Shop shop = shopDAO.findByUser(user);
            if (shop.getId().equals(order.getShopId())){
                //取消订单的是卖家
                authoity = true;
            }
        }
        if (!authoity) throw new Exception("you are not authorized to cancel this order");
        order.setStatus("canceled");
        orderDAO.save(order);
        return true;
    }

    /**
     * 查看某个商品的订单情况
     * @param productId
     * @return
     */
    public List<Order> productSales(Long productId, int page, int size) throws Exception{
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createTime"));
        List<Order> orderList = orderDAO.findAllByProductId(pageable, productId);
        if (orderList.size() == 0) throw new Exception("no result");
        return orderList;
    }

    /**
     * 查看商品销量
     * @param productId
     * @return
     */
    public int amountProductSales(Long productId){
        return orderDAO.countAllByProductId(productId);
    }

    /**
     * 查看店铺销售额
     * @param before
     * @param after
     * @return
     * @throws Exception
     */
    public double findSaleBetweenTimes(Date before, Date after) throws Exception{
        Shop shop = shopDAO.findByUser(authUser());
        if (shop == null) throw new Exception("you are not sellers");
        List<Order> orderList = orderDAO.findAllByCreateTimeBetweenAndShopId(before, after, shop.getId());
        double sale = 0.0;
        for (Order order : orderList){
            sale += order.getPrice() - order.getCommission();
        }
        return sale;
    }

    /**
     * 查看店铺总销售额
     * @return
     * @throws Exception
     */
    public double sellerSale() throws Exception{
        User user = authUser();
        Shop shop = shopDAO.findByUser(user);
        if (shop == null) throw new Exception("you are not sellers");
        List<Order> orderList = orderDAO.findAllByShopIdAndStatus(shop.getId(), "complete");
        double sale = 0.0;
        for (Order order : orderList){
            sale += order.getPrice() - order.getCommission();
        }
        return sale;
    }

    /**
     * 商家得到某一段时间的订单信息
     * @param before
     * @param after
     * @return
     * @throws Exception
     */
    public List<Order> findOrderBetweenTimes(Date before, Date after, int page, int size) throws Exception{
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createTime"));
        Shop shop = shopDAO.findByUser(authUser());
        List<Order> orderList = null;
        if (shop == null) {
            orderList = orderDAO.findAllByCreateTimeBetween(pageable, before, after);
        }else {
            orderList = orderDAO.findAllByCreateTimeBetweenAndShopId(pageable, before, after, shop.getId());
        }
        if (orderList.size() == 0) throw new Exception("no order at this time");
        return orderList;
    }

    /**
     * 根据订单状态查看商家有多少该类订单
     * @param user
     * @return
     */
    public int amountShopOrder(String status, User user) throws Exception{
        Shop shop = shopDAO.findByUser(user);
        if (shop == null) throw new Exception("you are not sellers");
        if (status == null){
            //所有订单数量
            return orderDAO.countAllByShopId(shop.getId());
        }
        return orderDAO.countAllByShopIdAndStatus(shop.getId(), status);
    }


    //买家
    /**
     * 由用户信息得到他的订单信息
     * @param page
     * @param size
     * @return
     * @throws Exception
     */
    public List<Order> listByUser(int page, int size) throws Exception{
        User user = authUser();
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createTime"));
        List<Order> orderList = orderDAO.findAllByUserId(pageable, user.getId());
        if (orderList.size() == 0) throw new Exception("no order being found");
        return orderList;
    }

    /**
     * 根据订单状态查看买家有多少该类订单
     * @param status
     * @return
     * @throws Exception
     */
    public int amountUserOrder(String status) throws Exception{
        User user = authUser();
        if (status == null){
            return orderDAO.countAllByUserId(user.getId());
        }
        return orderDAO.countAllByUserIdAndStatus(user.getId(), status);
    }

    /**
     * 买家查看一段时间内已经完成的订单
     * @param before
     * @param after
     * @param page
     * @param size
     * @return
     * @throws Exception
     */
    public List<Order> customerFindOrderBetweenTimes(Date before, Date after, int page, int size) throws Exception{
        User user = authUser();
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createTime"));
        List<Order> orderList = orderDAO.findAllByCreateTimeBetweenAndUserId(pageable, before, after, user.getId());
        if (orderList.size() == 0) throw new Exception("no order at this time");
        return orderList;
    }


    /**
     * 根据订单状态列出订单
     * @param status
     * @param page
     * @param size
     * @return
     * @throws Exception
     */
    public List<Order> listByOrderStatus(String status, int page, int size) throws Exception{
        User user = authUser();
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createTime"));
        List<Order> orderList = orderDAO.findAllByUserIdAndStatus(pageable, user.getId(), status);
        if (orderList.size() == 0) throw new Exception("no order at this status");
        return orderList;
    }

    /**
     * 添加订单,提供productName,shopName,userName,number，存疑怎么获取commision
     * @param order
     * @return
     */
    public boolean addOrder(Order order, String userName) throws Exception{
        Shop shop = shopDAO.findByName(order.getShopName());
        if (shop == null) throw new Exception("there doesn't have this shop");
        order.setShopName(shop.getName());
        order.setShopId(shop.getId());
        Product product = productDAO.findByNameAndShop(order.getProductName(), shop);
        if (product == null) throw new Exception("this shop doesn't have this product");
        String productName = product.getName();
        if (productName == null) throw new Exception("there doesn't have this product");
        order.setProductName(product.getName());
        order.setProductImg(product.getImage());
        order.setProductId(product.getId());
        User user = userDAO.findByUsername(userName);
        if (user == null) throw new Exception("the user isn't login");
        order.setUserId(user.getId());
        order.setUserName(userName);
        order.setPrice(product.getPrice());
        if (order.getNumber() == 0) throw new Exception("the num of product can't be zero");
        order.setTotalPrice(product.getPrice() * order.getNumber());
        order.setCommissionRate(0.02);//怎么获得？
        order.setCommission(order.getTotalPrice() * order.getCommissionRate());
        order.setStatus("place");//买家下订单
        orderDAO.save(order);
        return true;
    }
}
