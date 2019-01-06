/**
 * @author： fanzhonghao
 * @date: 19-1-3 15 20
 * @version: 1.0
 * @description:
 */
package com.fenlan.spring.shop.controller.seller;

import com.fenlan.spring.shop.bean.ResponseFormat;
import com.fenlan.spring.shop.service.OrderService;
import com.fenlan.spring.shop.service.SalesService;
import com.fenlan.spring.shop.service.TimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@RestController
@RequestMapping("/seller/sales")
public class ManageSaleController {
    @Autowired
    TimeService timeService;
    @Autowired
    private HttpServletRequest request;
    @Autowired
    OrderService orderService;
    @Autowired
    SalesService salesService;

    /**
     * 查看一段时间内某商品的销售情况
     * @return
     */
    @GetMapping("/listByTime")
    public ResponseEntity<ResponseFormat> listByTime(@RequestParam("type") String type,
                                                     @RequestParam("beforeNum") int beforeNum,
                                                     @RequestParam("productName") String productName,
                                                     @RequestParam("page") int page,
                                                     @RequestParam("size") int size){
        Date[] dates = timeService.timeSelector(beforeNum, type);
        try {
            return new ResponseEntity<>(new ResponseFormat.Builder(new Date(), HttpStatus.OK.value())
                    .error(null)
                    .message("query success")
                    .path(request.getServletPath())
                    .data(salesService.listOneProduct(dates[0], dates[1], page, size, productName))
                    .build(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseFormat.Builder(new Date(), HttpStatus.OK.value())
                    .error(null)
                    .message("query error")
                    .path(request.getServletPath())
                    .data(null)
                    .build(), HttpStatus.OK);
        }
    }

    /**
     * 查看一段时间内某商品的销售数量
     * @return
     */
    @GetMapping("/amountByTime")
    public ResponseEntity<ResponseFormat> amountByTime(@RequestParam("type") String type,
                                                     @RequestParam("beforeNum") int beforeNum,
                                                     @RequestParam("productName") String productName){
        Date[] dates = timeService.timeSelector(beforeNum, type);
        try {
            return new ResponseEntity<>(new ResponseFormat.Builder(new Date(), HttpStatus.OK.value())
                    .error(null)
                    .message("query success")
                    .path(request.getServletPath())
                    .data(salesService.amountOneProduct(dates[0], dates[1], productName))
                    .build(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseFormat.Builder(new Date(), HttpStatus.OK.value())
                    .error(null)
                    .message("query error")
                    .path(request.getServletPath())
                    .data(null)
                    .build(), HttpStatus.OK);
        }
    }

    /**
     * 查看一段时间内的商品的销售情况
     * @param beforeNum
     * @param type
     * @return
     */
    @GetMapping("/list")
    public ResponseEntity<ResponseFormat> list(@RequestParam("beforeNum") int beforeNum,
                                               @RequestParam("type") String type,
                                               @RequestParam("page") int page,
                                               @RequestParam("size") int size){
        Date[] dates = timeService.timeSelector(beforeNum, type);
        try {
            return new ResponseEntity<>(new ResponseFormat.Builder(new Date(), HttpStatus.OK.value())
                    .error(null)
                    .message("query success")
                    .path(request.getServletPath())
                    .data(salesService.listByTimes(dates[0], dates[1], page, size))
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
     * 得到一段时间内某个产品的总销售信息
     * @param beforeNum
     * @param type
     * @param productName
     * @return
     */
    @GetMapping("/search")
    public ResponseEntity<ResponseFormat> search(@RequestParam("beforeNum") int beforeNum,
                                                 @RequestParam("type") String type,
                                                 @RequestParam("productName") String productName){
        Date[] dates = timeService.timeSelector(beforeNum, type);
        try {
            return new ResponseEntity<>(new ResponseFormat.Builder(new Date(), HttpStatus.OK.value())
                    .error(null)
                    .message("query success")
                    .path(request.getServletPath())
                    .data(salesService.searchOneProduct(dates[0], dates[1], productName))
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

    @GetMapping("/calByTime")
    public ResponseEntity<ResponseFormat> calByTime(@RequestParam("date") String date,
                                                    @RequestParam("type") String type){
        try {
            Date date1 = timeService.getDate(date, type);
            return new ResponseEntity<>(new ResponseFormat.Builder(new Date(), HttpStatus.OK.value())
                    .error(null)
                    .message("query success")
                    .path(request.getServletPath())
                    .data(salesService.salesSelector(date1, type))
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
