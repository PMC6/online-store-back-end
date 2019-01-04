/**
 * @author： fanzhonghao
 * @date: 18-12-27 08 53
 * @version: 1.0
 * @description:
 */
package com.fenlan.spring.shop.service;

import com.fenlan.spring.shop.DAO.*;
import com.fenlan.spring.shop.bean.*;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;

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
    @Autowired
    CommissionService commissionService;
    @Autowired
    CartDAO cartDAO;

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
            list = orderDAO.findAllByStatus("Complete");
        }else list = orderDAO.findAllByCreateTimeBetweenAndStatus(before, after, "Complete");
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

    /**
     * 查看非取消的订单的数量
     * @param before
     * @param after
     * @return
     */
    public int amountOrderBetweenTimes(Date before, Date after){
        List<Order> orderList = orderDAO.findAllByCreateTimeBetweenAndStatusNot(before, after, "Canceled");
        return orderList.size();
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
        List<Order> orderList = orderDAO.findAllByShopNameAndStatusNot(pageable, shop.getName(), "Canceled");
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
     * @return
     * @throws Exception
     */
    public boolean updateOrderStatus(Long orderId, User user) throws Exception{

        String status = null;
        try {
            Order order = orderDAO.findById(orderId).get();
            Shop shop = shopDAO.findByUser(user);
            if (shop == null || !shop.getId().equals(order.getShopId()))
                throw new Exception("you are not authorized to update this order");
            if (order.getStatus().equals("Processing Order")){
                status = "Preparing for Shipment";
            }else if (order.getStatus().equals("Preparing for Shipment")){
                status = "Shipped";
            }else if (order.getStatus().equals("Shipped")){
                status = "Complete";
            }else throw new Exception("can't update current status");
            order.setStatus(status);
            orderDAO.save(order);
            return true;
        }catch (Exception e){
            throw new Exception(e.getLocalizedMessage());
        }
    }

    /**
     * 买家完成订单
     * @param orderId
     * @return
     * @throws Exception
     */
    public boolean completeOrder(Long orderId) throws Exception{
        User user = authUser();
        Order order = orderDAO.findById(orderId).get();
        if (order.getUserId() != user.getId()){
            throw new Exception("this order is not belong to you");
        }
        if (order.getStatus().equals("Shipped")){
            order.setStatus("Complete");
        }else throw new Exception("you can't update it's status");
        orderDAO.save(order);
        return true;
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
        if (!order.getStatus().equals("Processing Order")) throw new Exception("can't be canceled");
        order.setStatus("Canceled");
        orderDAO.save(order);
        return true;
    }

    /**
     * 取消订单
     * @return
     */
    public boolean deleteOrder(Long orderId, User user) throws Exception{
        if (user == null) user = authUser();
        Order order = orderDAO.findById(orderId).get();
        boolean authoity = false;
        if (user.getId().equals(order.getUserId())){
            //删除订单的是买家
            authoity = true;
        }else throw new Exception("can't delete it");
        if (!authoity) throw new Exception("you are not authorized to cancel this order");
        orderDAO.delete(order);
        return true;
    }

    /**
     * 查看某个商品的订单情况
     * @param productId
     * @return
     */
    public List<Order> productSales(Long productId, int page, int size) throws Exception{
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createTime"));
        List<Order> orderList = orderDAO.findAllByProductIdAndStatusNot(pageable, productId, "Canceled");
        if (orderList.size() == 0) throw new Exception("no result");
        return orderList;
    }

    /**
     * 查看商品订单数量
     * @param productId
     * @return
     */
    public int amountProductSales(Long productId){
        return orderDAO.countAllByProductIdAndStatusNot(productId, "Canceled");
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
        List<Order> orderList = orderDAO.findAllByCreateTimeBetweenAndShopIdAndStatus(before, after, shop.getId(),
                "Complete");
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
        List<Order> orderList = orderDAO.findAllByShopIdAndStatus(shop.getId(), "Complete");
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
            orderList = orderDAO.findAllByCreateTimeBetweenAndStatusNot(pageable, before, after, "Canceled");
        }else {
            orderList = orderDAO.findAllByCreateTimeBetweenAndShopIdAndStatusNot(pageable, before, after,
                    shop.getId(), "Canceled");
        }
        if (orderList.size() == 0) throw new Exception("no order at this time");
        return orderList;
    }

    /**
     * 商家得到某一段时间的订单数量
     * @param before
     * @param after
     * @return
     * @throws Exception
     */
    public int sellerAmountOrderBetweenTimes(Date before, Date after) throws Exception{
        Shop shop = shopDAO.findByUser(authUser());
        if (shop == null) throw new Exception("you are not sellers");
        int num = 0;
        num = orderDAO.countAllByCreateTimeBetweenAndShopIdAndStatusNot(before,
                after, shop.getId(), "Canceled");
        return num;
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
            return orderDAO.countAllByShopIdAndStatusNot(shop.getId(), "Canceled");
        }
        return orderDAO.countAllByShopIdAndStatus(shop.getId(), status);
    }

    /**
     * 得到before和after间每天的销售额
     * @param before
     * @param after
     * @return
     */
    public Map<Integer, Double> listDailySales(Date before, Date after) throws Exception{
        User user = authUser();
        Shop shop = shopDAO.findByUser(user);
        if (shop == null) throw new Exception("you are not sellers");
        Map map = new TreeMap();
        List<Order> orderList = null;
        Calendar calendar = Calendar.getInstance();
        for (int i = 0;i < 7;i++){
            calendar.setTime(before);
            calendar.add(Calendar.DATE, 1);
            Date tmp = calendar.getTime();
            double sales = 0.0;
            if (tmp.getTime() < after.getTime()){
                orderList = orderDAO.findAllByCreateTimeBetweenAndShopIdAndStatus(before, tmp,
                        shop.getId(), "Complete");
                for (Order order : orderList){
                    sales += order.getPrice() - order.getCommission();
                }
            }
            before = tmp;
            map.put(i, sales);
        }
        return map;
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
        List<Order> orderList = orderDAO.findAllByUserIdAndStatusNot(pageable, user.getId(), "Canceled");
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
            return orderDAO.countAllByUserIdAndStatusNot(user.getId(), "Canceled");
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
        List<Order> orderList = orderDAO.findAllByCreateTimeBetweenAndUserIdAndStatus(pageable, before,
                after, user.getId(), "Complete");
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
        order.setCommissionRate(commissionService.findLastCommissionRate());
        order.setCommission(order.getTotalPrice() * order.getCommissionRate());
        order.setStatus("Processing Order");//买家下订单
        try {
            Cart cart = cartDAO.findByUserIdAndProductId(user.getId(), product.getId());
            cartDAO.delete(cart);
        }catch (Exception e){

        }
        int left = product.getNumber()-1;
        if (left < 0) throw new Exception("no product left");
        product.setNumber(left);
        productDAO.save(product);
        orderDAO.save(order);
        return true;
    }

}
