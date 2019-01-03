/**
 * @author： fanzhonghao
 * @date: 19-1-2 11 10
 * @version: 1.0
 * @description:
 */
package com.fenlan.spring.shop.controller.admin;

import com.fenlan.spring.shop.bean.ResponseFormat;
import com.fenlan.spring.shop.service.CommissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Map;

@RestController
@RequestMapping("/admin/commission")
public class ManageCommissionController {
    @Autowired
    CommissionService commissionService;
    @Autowired
    private HttpServletRequest request;


    @GetMapping("/findLast")
    public ResponseEntity<ResponseFormat> findLast(){
        try {
            return new ResponseEntity<>(new ResponseFormat.Builder(new Date(), HttpStatus.OK.value())
                    .error(null)
                    .message("query success")
                    .path(request.getServletPath())
                    .data(commissionService.findLastCommissionRate())
                    .build(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseFormat.Builder(new Date(), HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .error("query error")
                    .message(e.getLocalizedMessage())
                    .path(request.getServletPath())
                    .data(null)
                    .build(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/add")
    public ResponseEntity<ResponseFormat> add(@RequestBody Map map){
        double rate = Double.parseDouble(map.get("rate").toString());
        try {
            return new ResponseEntity<>(new ResponseFormat.Builder(new Date(), HttpStatus.OK.value())
                    .error(null)
                    .message("add success")
                    .path(request.getServletPath())
                    .data(commissionService.add(rate))
                    .build(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseFormat.Builder(new Date(), HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .error("add error")
                    .message(e.getLocalizedMessage())
                    .path(request.getServletPath())
                    .data(null)
                    .build(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
