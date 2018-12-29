/**
 * @author： fanzhonghao
 * @date: 18-12-27 08 48
 * @version: 1.0
 * @description:
 */
package com.fenlan.spring.shop.controller.seller;

import com.fenlan.spring.shop.bean.ResponseFormat;
import com.fenlan.spring.shop.bean.User;
import com.fenlan.spring.shop.service.OrderService;
import com.fenlan.spring.shop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

import java.util.Calendar;
import java.util.Date;

@RestController
@RequestMapping("/seller")
public class ManageOrder {
    @Autowired
    OrderService orderService;
    @Autowired
    private HttpServletRequest request;
    @Autowired
    UserService userService;

    /**
     * 商家更新订单状态
     * @param orderId
     * @param status
     * @return
     */
    @PutMapping("/order/update")
    public ResponseEntity<ResponseFormat> updateOrderStatus(@RequestParam("orderId") Long orderId,
                                                            @RequestParam("status") String status){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        User user = userService.findByName(userName);
        try {
            return new ResponseEntity<>(new ResponseFormat.Builder(new Date(), HttpStatus.OK.value())
                    .error(null)
                    .message("update success")
                    .path(request.getServletPath())
                    .data(orderService.updateOrderStatus(orderId, status, user))
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
     * 查看订单
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
                                                           @RequestParam("type") String type){
        Date[] dates = null;
        if (type.equals("monthly"))
        {
            dates = getMonthTime(beforeNum);
        }else if (type.equals("yearly")){
            dates = getYearTime(beforeNum);
        }else if (type.equals("daily")){
            dates = getDayTime(beforeNum);
        }
        try {
            return new ResponseEntity<>(new ResponseFormat.Builder(new Date(), HttpStatus.OK.value())
                    .error(null)
                    .message("query success")
                    .path(request.getServletPath())
                    .data(orderService.findOrderBetweenTimes(dates[0], dates[1]))
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
        Date[] dates = null;
        if (type.equals("monthly"))
        {
            dates = getMonthTime(beforeNum);
        }else if (type.equals("daily")){
            dates = getDayTime(beforeNum);
        }else if (type.equals("yearly")){
            dates = getYearTime(beforeNum);
        }
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
     * 获取beforeMonthNum月之前月份的第一天和最后一天的时间
     * @param beforeMonthNum
     * @return
     */
    private Date[] getMonthTime(int beforeMonthNum){
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, 0-beforeMonthNum);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        Date[] dates = new Date[2];
        dates[0] = cal.getTime();//要的月份的第一天
        cal.set(Calendar.DAY_OF_MONTH, Calendar.getInstance().getActualMaximum(Calendar.DAY_OF_MONTH));
        dates[1] = cal.getTime();//最后一天
        return dates;
    }


    private Date[] getYearTime(int yearNum){
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.YEAR, 0-yearNum);
        cal.set(Calendar.DAY_OF_YEAR, 1);
        Date[] dates = new Date[2];
        dates[0] = cal.getTime();//要的月份的第一天
        cal.set(Calendar.DAY_OF_YEAR, Calendar.getInstance().getActualMaximum(Calendar.DAY_OF_YEAR));
        dates[1] = cal.getTime();//最后一天
        return dates;
    }

    private Date[] getDayTime(int dayNum){
        Date[] dates = new Date[2];
        Date nowDate = new Date();
        Long time = nowDate.getTime();
        time -= 86400000 * (dayNum);//之前时间
        dates[1] = new Date(time);
        Long time1 = nowDate.getTime();
        time1 = time1 - 86400000 * (dayNum+1);
        dates[0] = new Date(time1);//之后时间
        return dates;
    }
}
