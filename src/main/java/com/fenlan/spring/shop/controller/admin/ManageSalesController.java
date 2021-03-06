/**
 * @author： fanzhonghao
 * @date: 18-12-31 20 08
 * @version: 1.0
 * @description:
 */
package com.fenlan.spring.shop.controller.admin;

import com.fenlan.spring.shop.bean.ResponseFormat;
import com.fenlan.spring.shop.service.AdService;
import com.fenlan.spring.shop.service.OrderService;
import com.fenlan.spring.shop.service.TimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

@RestController
@RequestMapping("/admin/sales")
public class ManageSalesController {
    @Autowired
    private HttpServletRequest request;
    @Autowired
    OrderService orderService;
    @Autowired
    AdService adService;
    @Autowired
    TimeService timeService;

    /**
     * 查看商城总收入
     * @return
     */
    @GetMapping("/total")
    public ResponseEntity<ResponseFormat> findTotalSales(){
        double orderCommission = 0.0;
        double adSales = 0.0;
        orderCommission += orderService.findMailIncome(null, null);
        adSales += adService.findSales(null, null);
        Map map = new HashMap<String, Double>();
        map.put("adSales", adSales);
        map.put("commission", orderCommission);
        map.put("totalSales", adSales+orderCommission);
        try {
            return new ResponseEntity<>(new ResponseFormat.Builder(new Date(), HttpStatus.OK.value())
                    .error(null)
                    .message("query success")
                    .path(request.getServletPath())
                    .data(map)
                    .build(), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(new ResponseFormat.Builder(new Date(), HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .error("query error")
                    .message(e.getLocalizedMessage())
                    .path(request.getServletPath())
                    .data(0)
                    .build(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 查看某一段时间内的收入
     * @param beforeNum
     * @param type
     * @return
     */
    @GetMapping("/partly")
    public ResponseEntity<ResponseFormat> findPartlySales(@RequestParam("beforeNum") int beforeNum,
                                                          @RequestParam("type") String type){
        Map map = new HashMap<String, Double>();
        try {
            Date[] dates = timeService.timeSelector(beforeNum, type);
            double adSales = adService.findSales(dates[0], dates[1]);
            double commission = orderService.findMailIncome(dates[0], dates[1]);
            map.put("adSales", adSales);
            map.put("commission", commission);
            map.put("totalSales", adSales+commission);
            return new ResponseEntity<>(new ResponseFormat.Builder(new Date(), HttpStatus.OK.value())
                    .error(null)
                    .message("query success")
                    .path(request.getServletPath())
                    .data(map)
                    .build(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseFormat.Builder(new Date(), HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .error("query error")
                    .message(e.getLocalizedMessage())
                    .path(request.getServletPath())
                    .data(0)
                    .build(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
