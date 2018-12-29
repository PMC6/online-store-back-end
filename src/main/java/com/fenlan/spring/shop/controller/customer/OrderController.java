/**
 * @author： fanzhonghao
 * @date: 18-12-28 11 31
 * @version: 1.0
 * @description:
 */
package com.fenlan.spring.shop.controller.customer;

import com.fenlan.spring.shop.bean.Order;
import com.fenlan.spring.shop.bean.ResponseFormat;
import com.fenlan.spring.shop.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@RestController
@RequestMapping("/customer/order")
public class OrderController {
    @Autowired
    OrderService orderService;
    @Autowired
    private HttpServletRequest request;

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
}
