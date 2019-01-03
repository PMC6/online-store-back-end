/**
 * @author： fanzhonghao
 * @date: 18-12-27 08 48
 * @version: 1.0
 * @description:
 */
package com.fenlan.spring.shop.controller.seller;

import com.fenlan.spring.shop.bean.Product;
import com.fenlan.spring.shop.bean.ResponseFormat;
import com.fenlan.spring.shop.bean.Shop;
import com.fenlan.spring.shop.bean.User;
import com.fenlan.spring.shop.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.xml.crypto.Data;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;

@RestController
@RequestMapping("/seller")
public class ManageOrderController {
    @Autowired
    OrderService orderService;
    @Autowired
    private HttpServletRequest request;
    @Autowired
    UserService userService;
    @Autowired
    ProductService productService;
    @Autowired
    ShopService shopService;
    @Autowired
    TimeService timeService;
    @Autowired
    AdService adService;

    /**
     * 商家更新订单状态
     * @param orderId
     * @return
     */
    @PutMapping("/order/update")
    public ResponseEntity<ResponseFormat> updateOrderStatus(@RequestParam("orderId") Long orderId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        User user = userService.findByName(userName);
        try {
            return new ResponseEntity<>(new ResponseFormat.Builder(new Date(), HttpStatus.OK.value())
                    .error(null)
                    .message("update success")
                    .path(request.getServletPath())
                    .data(orderService.updateOrderStatus(orderId, user))
                    .build(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseFormat.Builder(new Date(), HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .error("update failed")
                    .message(e.getLocalizedMessage())
                    .path(request.getServletPath())
                    .data(null)
                    .build(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 查看订单,没有被取消的订单
     * @param page
     * @param size
     * @param positive
     * @return
     */
    @GetMapping("/order/listByShopName")
    public ResponseEntity<ResponseFormat> list(@RequestParam("page") int page,
                                               @RequestParam("size") int size,
                                               @RequestParam("positive") boolean positive){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        User user = userService.findByName(userName);
        try {
            return new ResponseEntity<>(new ResponseFormat.Builder(new Date(), HttpStatus.OK.value())
                    .error(null)
                    .message("query success")
                    .path(request.getServletPath())
                    .data(orderService.list(page, size, positive, user))
                    .build(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseFormat.Builder(new Date(), HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .error("query failed")
                    .message(e.getLocalizedMessage())
                    .path(request.getServletPath())
                    .data(null)
                    .build(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 根据订单状态查看订单
     * @param status
     * @param page
     * @param size
     * @return
     */
    @GetMapping("/order/listByOrderStatus")
    public ResponseEntity<ResponseFormat> listByOrderStatus(@RequestParam("status") String status,
                                                            @RequestParam("page") int page,
                                                            @RequestParam("size") int size){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        User user = userService.findByName(userName);
        try {
            return new ResponseEntity<>(new ResponseFormat.Builder(new Date(), HttpStatus.OK.value())
                    .error(null)
                    .message("query success")
                    .path(request.getServletPath())
                    .data(orderService.listByOrderStatus(status, page, size, user))
                    .build(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseFormat.Builder(new Date(), HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .error("query failed")
                    .message(e.getLocalizedMessage())
                    .path(request.getServletPath())
                    .data(null)
                    .build(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 取消订单(卖家or买家)
     * @param orderId
     * @return
     */
    @PutMapping("/order/cancelOrder")
    public ResponseEntity<ResponseFormat> cancelOrder(@RequestParam("orderId") Long orderId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        User user = userService.findByName(userName);
        try {
            return new ResponseEntity<>(new ResponseFormat.Builder(new Date(), HttpStatus.OK.value())
                    .error(null)
                    .message("cancel success")
                    .path(request.getServletPath())
                    .data(orderService.cancelOrder(orderId, user))
                    .build(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseFormat.Builder(new Date(), HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .error("cancel failed")
                    .message(e.getLocalizedMessage())
                    .path(request.getServletPath())
                    .data(null)
                    .build(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 根据订单状态查看订单数量，
     * @param status status为null时，查看所有订单数量
     * @return
     */
    @GetMapping("/order/amount")
    public ResponseEntity<ResponseFormat> amountOrder(@RequestParam("status") String status){
        if (status.equals("null")){
            status = null;
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        User user = userService.findByName(userName);
        try {
            return new ResponseEntity<>(new ResponseFormat.Builder(new Date(), HttpStatus.OK.value())
                    .error(null)
                    .message("query success")
                    .path(request.getServletPath())
                    .data(orderService.amountShopOrder(status, user))
                    .build(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseFormat.Builder(new Date(), HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .error("query failed")
                    .message(e.getLocalizedMessage())
                    .path(request.getServletPath())
                    .data(null)
                    .build(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    /**
     * 按时间区间查看订单
     * @param beforeNum 几天(月、年)前，0表示当天(月、年)
     * @param type 类型(daily,monthly,yearly)
     * @return
     */
    @GetMapping("/order/findOrders")
    public ResponseEntity<ResponseFormat> findOrders(@RequestParam("beforeNum") int beforeNum,
                                                     @RequestParam("type") String type,
                                                     @RequestParam("page") int page,
                                                     @RequestParam("size") int size){
        Date[] dates = null;
        dates = timeService.timeSelector(beforeNum, type);
        try {
            return new ResponseEntity<>(new ResponseFormat.Builder(new Date(), HttpStatus.OK.value())
                    .error(null)
                    .message("query success")
                    .path(request.getServletPath())
                    .data(orderService.findOrderBetweenTimes(dates[0], dates[1], page, size))
                    .build(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseFormat.Builder(new Date(), HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .error("query failed")
                    .message(e.getLocalizedMessage())
                    .path(request.getServletPath())
                    .data(null)
                    .build(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 按时间区间查看销售额
     * @param beforeNum 几天(月、年)前，0表示当天(月、年)
     * @param type 类型(daily,monthly,yearly)
     * @return
     */
    @GetMapping("/order/findSale")
    public ResponseEntity<ResponseFormat> findSale(@RequestParam("beforeNum") int beforeNum,
                                                           @RequestParam("type") String type){
        Date[] dates = timeService.timeSelector(beforeNum, type);
        try {
            return new ResponseEntity<>(new ResponseFormat.Builder(new Date(), HttpStatus.OK.value())
                    .error(null)
                    .message("query success")
                    .path(request.getServletPath())
                    .data(orderService.findSaleBetweenTimes(dates[0], dates[1]))
                    .build(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseFormat.Builder(new Date(), HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .error("query failed")
                    .message(e.getLocalizedMessage())
                    .path(request.getServletPath())
                    .data(0)
                    .build(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 按时间区间查看广告花费
     * @param beforeNum 几天(月、年)前，0表示当天(月、年)
     * @param type 类型(daily,monthly,yearly)
     * @return
     */
    @GetMapping("/order/findPayment")
    public ResponseEntity<ResponseFormat> findPayment(@RequestParam("beforeNum") int beforeNum,
                                                   @RequestParam("type") String type){
        Date[] dates = timeService.timeSelector(beforeNum, type);
        try {
            return new ResponseEntity<>(new ResponseFormat.Builder(new Date(), HttpStatus.OK.value())
                    .error(null)
                    .message("query success")
                    .path(request.getServletPath())
                    .data(adService.findPayment(dates[0], dates[1]))
                    .build(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseFormat.Builder(new Date(), HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .error("query failed")
                    .message(e.getLocalizedMessage())
                    .path(request.getServletPath())
                    .data(0)
                    .build(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 查看所有广告花费
     * @return
     */
    @GetMapping("/order/findAllPayment")
    public ResponseEntity<ResponseFormat> findAllPayment(){
        try {
            return new ResponseEntity<>(new ResponseFormat.Builder(new Date(), HttpStatus.OK.value())
                    .error(null)
                    .message("query success")
                    .path(request.getServletPath())
                    .data(adService.findPayment(null, null))
                    .build(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseFormat.Builder(new Date(), HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .error("query failed")
                    .message(e.getLocalizedMessage())
                    .path(request.getServletPath())
                    .data(0)
                    .build(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    /**
     * 查看某件商品的订单信息
     * @param productName
     * @param page
     * @param size
     * @return
     */
    @GetMapping("/order/product/list")
    public ResponseEntity<ResponseFormat> listProductOrder(@RequestParam("productName") String productName,
                                                           @RequestParam("page") int page,
                                                           @RequestParam("size") int size){
        try {
            Product product = productService.findByNamAndShop(productName);
            return new ResponseEntity<>(new ResponseFormat.Builder(new Date(), HttpStatus.OK.value())
                    .error(null)
                    .message("query success")
                    .path(request.getServletPath())
                    .data(orderService.productSales(product.getId(), page, size))
                    .build(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseFormat.Builder(new Date(), HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .error("query failed")
                    .message(e.getLocalizedMessage())
                    .path(request.getServletPath())
                    .data(null)
                    .build(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 查看店铺总销售额
     * @return
     */
    @GetMapping("/order/totalSale")
    public ResponseEntity<ResponseFormat> totalSale(){
        try {
            return new ResponseEntity<>(new ResponseFormat.Builder(new Date(), HttpStatus.OK.value())
                    .error(null)
                    .message("query success")
                    .path(request.getServletPath())
                    .data(orderService.sellerSale())
                    .build(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseFormat.Builder(new Date(), HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .error("query failed")
                    .message(e.getLocalizedMessage())
                    .path(request.getServletPath())
                    .data(null)
                    .build(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 按时间区间查看销售额
     * @param beforeNum 几天(月、年)前，0表示当天(月、年)
     * @return
     */
    @GetMapping("/order/findDailySale")
    public ResponseEntity<ResponseFormat> findWeeklySale(@RequestParam("beforeNum") int beforeNum){
        Date[] dates = timeService.timeSelector(beforeNum, "weekly");
        try {
            Map map = orderService.listDailySales(dates[0], dates[1]);
            return new ResponseEntity<>(new ResponseFormat.Builder(new Date(), HttpStatus.OK.value())
                    .error(null)
                    .message("query success")
                    .path(request.getServletPath())
                    .data(map)
                    .build(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseFormat.Builder(new Date(), HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .error("query failed")
                    .message(e.getLocalizedMessage())
                    .path(request.getServletPath())
                    .data(0)
                    .build(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
