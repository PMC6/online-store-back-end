/**
 * @author： fanzhonghao
 * @date: 18-12-28 11 31
 * @version: 1.0
 * @description:
 */
package com.fenlan.spring.shop.controller.customer;

import com.fenlan.spring.shop.bean.Order;
import com.fenlan.spring.shop.bean.ResponseFormat;
import com.fenlan.spring.shop.controller.seller.ManageOrderController;
import com.fenlan.spring.shop.service.OrderService;
import com.fenlan.spring.shop.service.TimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Map;

@RestController
@RequestMapping("/customer/order")
public class OrderController {
    @Autowired
    OrderService orderService;
    @Autowired
    private HttpServletRequest request;
    @Autowired
    TimeService timeService;

    /**
     * 添加订单，需提供Order的数据productName,shopName,number
     * @param order
     * @return
     */
    @PostMapping("/add")
    public ResponseEntity<ResponseFormat> addOrder(@RequestBody Order order){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        try {
            return new ResponseEntity<>(new ResponseFormat.Builder(new Date(), HttpStatus.OK.value())
                    .error(null)
                    .message("add an order")
                    .path(request.getServletPath())
                    .data(orderService.addOrder(order, userName))
                    .build(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseFormat.Builder(new Date(), HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .error("add failed")
                    .message(e.getLocalizedMessage())
                    .path(request.getServletPath())
                    .data(null)
                    .build(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 查询买家已完成订单数量
     * @return
     */
    @GetMapping("/amount")
    public ResponseEntity<ResponseFormat> amountCompletedOrder(){
        try {
            return new ResponseEntity<>(new ResponseFormat.Builder(new Date(), HttpStatus.OK.value())
                    .error(null)
                    .message("query success")
                    .path(request.getServletPath())
                    .data(orderService.amountUserOrder("complete"))
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
     * 查询买家全部订单数量,除取消了的
     * @return
     */
    @GetMapping("/amount/total")
    public ResponseEntity<ResponseFormat> amountTotalOrder(){
        try {
            return new ResponseEntity<>(new ResponseFormat.Builder(new Date(), HttpStatus.OK.value())
                    .error(null)
                    .message("query success")
                    .path(request.getServletPath())
                    .data(orderService.amountUserOrder(null))
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
     * 按时间区间查看购买历史,只有已完成的
     * @param beforeNum
     * @param type (daily,weekly,monthly,yearly)
     * @param page
     * @param size
     * @return
     */
    @GetMapping("/history")
    public ResponseEntity<ResponseFormat> viewHistory(@RequestParam("beforeNum") int beforeNum,
                                                      @RequestParam("type") String type,
                                                      @RequestParam("page") int page,
                                                      @RequestParam("size") int size){
        Date[] dates = timeService.timeSelector(beforeNum, type);
        try {
            return new ResponseEntity<>(new ResponseFormat.Builder(new Date(), HttpStatus.OK.value())
                    .error(null)
                    .message("query success")
                    .path(request.getServletPath())
                    .data(orderService.customerFindOrderBetweenTimes(dates[0], dates[1], page, size))
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

    @PutMapping("/update")
    public ResponseEntity<ResponseFormat> completeOrder(@RequestParam("orderId") Long orderId){
        try {
            return new ResponseEntity<>(new ResponseFormat.Builder(new Date(), HttpStatus.OK.value())
                    .error(null)
                    .message("complete success")
                    .path(request.getServletPath())
                    .data(orderService.completeOrder(orderId))
                    .build(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseFormat.Builder(new Date(), HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .error("complete failed")
                    .message(e.getLocalizedMessage())
                    .path(request.getServletPath())
                    .data(null)
                    .build(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/list")
    public ResponseEntity<ResponseFormat> list(@RequestParam("page") int page,
                                               @RequestParam("size") int size){
        try {
            return new ResponseEntity<>(new ResponseFormat.Builder(new Date(), HttpStatus.OK.value())
                    .error(null)
                    .message("query success")
                    .path(request.getServletPath())
                    .data(orderService.listByUser(page, size))
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
     * 通过订单状态查看订单
     * @param status
     * @param page
     * @param size
     * @return
     */
    @GetMapping("/list/status")
    public ResponseEntity<ResponseFormat> listByStatus(@RequestParam("status") String status,
                                                       @RequestParam("page") int page,
                                                       @RequestParam("size") int size){
        try {
            return new ResponseEntity<>(new ResponseFormat.Builder(new Date(), HttpStatus.OK.value())
                    .error(null)
                    .message("query success")
                    .path(request.getServletPath())
                    .data(orderService.listByOrderStatus(status, page, size))
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
     * 取消订单
     * @param map
     * @return
     */
    @DeleteMapping("/cancel")
    public ResponseEntity<ResponseFormat> cancel(@RequestBody Map map){
        Long id =Long.parseLong(map.get("orderId").toString());
        try {
            return new ResponseEntity<>(new ResponseFormat.Builder(new Date(), HttpStatus.OK.value())
                    .error(null)
                    .message("cancel success")
                    .path(request.getServletPath())
                    .data(orderService.cancelOrder(id, null))
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
     * 取消订单
     * @param map
     * @return
     */
    @DeleteMapping("/delete")
    public ResponseEntity<ResponseFormat> delete(@RequestBody Map map){
        Long id =Long.parseLong(map.get("orderId").toString());
        try {
            return new ResponseEntity<>(new ResponseFormat.Builder(new Date(), HttpStatus.OK.value())
                    .error(null)
                    .message("delete success")
                    .path(request.getServletPath())
                    .data(orderService.deleteOrder(id, null))
                    .build(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseFormat.Builder(new Date(), HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .error("delete failed")
                    .message(e.getLocalizedMessage())
                    .path(request.getServletPath())
                    .data(null)
                    .build(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



}
