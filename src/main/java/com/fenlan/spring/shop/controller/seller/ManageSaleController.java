/**
 * @author： fanzhonghao
 * @date: 19-1-3 15 20
 * @version: 1.0
 * @description:
 */
package com.fenlan.spring.shop.controller.seller;

import com.fenlan.spring.shop.bean.ResponseFormat;
import com.fenlan.spring.shop.service.OrderService;
import com.fenlan.spring.shop.service.TimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@RestController
@RequestMapping("/seller/sales/")
public class ManageSaleController {
    @Autowired
    TimeService timeService;
    @Autowired
    private HttpServletRequest request;
    @Autowired
    OrderService orderService;

    /**
     * 查看一段时间内商品的销售情况
     * @return
     */
    @GetMapping("/listByTime")
    public ResponseEntity<ResponseFormat> listByTime(@RequestBody Map map){
        String type = map.get("type").toString();
        int beforeNum = Integer.parseInt(map.get("beforeNumber").toString());
        LinkedHashMap<String, ArrayList<Integer>> linkedHashMap = (LinkedHashMap<String, ArrayList<Integer>>) map.get("productIds");
        List<Long> longList = new LinkedList<>();
        ArrayList<Integer> products = linkedHashMap.get("list");
        for (Integer id : products){
            longList.add((long) id);
        }
        Date[] dates = timeService.timeSelector(beforeNum, type);
        try {
            return new ResponseEntity<>(new ResponseFormat.Builder(new Date(), HttpStatus.OK.value())
                    .error(null)
                    .message("query success")
                    .path(request.getServletPath())
                    .data(orderService.findSalesBetweenTimes(dates[0], dates[1], longList))
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
}
