/**
 * @author： fanzhonghao
 * @date: 18-12-31 20 08
 * @version: 1.0
 * @description:
 */
package com.fenlan.spring.shop.controller.admin;

import com.fenlan.spring.shop.bean.ResponseFormat;
import com.fenlan.spring.shop.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@RestController
@RequestMapping("/admin/sales")
public class ManageSalesController {
    @Autowired
    private HttpServletRequest request;
    @Autowired
    OrderService orderService;

    /**
     * 查看商城总收入
     * @return
     */
    @GetMapping("/total")
    public ResponseEntity<ResponseFormat> findTotalSales(){
        try {
            return new ResponseEntity<>(new ResponseFormat.Builder(new Date(), HttpStatus.OK.value())
                    .error(null)
                    .message("query success")
                    .path(request.getServletPath())
                    .data(orderService.findMailIncome(null, null))
                    .build(), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(new ResponseFormat.Builder(new Date(), HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .error("query error")
                    .message(e.getLocalizedMessage())
                    .path(request.getServletPath())
                    .data(null)
                    .build(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

//    @GetMapping("/partly")
//    public ResponseEntity<ResponseFormat> findPartlySales(@RequestParam("beforeNum") int beforeNum,
//                                                          @RequestParam("type") String type){
//
//    }

}
