/**
 * @author： fanzhonghao
 * @date: 19-1-2 10 57
 * @version: 1.0
 * @description:
 */
package com.fenlan.spring.shop.controller.admin;

import com.fenlan.spring.shop.bean.ResponseFormat;
import com.fenlan.spring.shop.service.DBService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@RestController
@RequestMapping("/admin/db")
public class ManageDBController {
    @Autowired
    DBService dbService;
    @Autowired
    private HttpServletRequest request;

    @PostMapping("/backup")
    public ResponseEntity<ResponseFormat> backup(){
        try {
            return new ResponseEntity<>(new ResponseFormat.Builder(new Date(), HttpStatus.OK.value())
                    .error(null)
                    .message("back up success")
                    .path(request.getServletPath())
                    .data(dbService.backup())
                    .build(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseFormat.Builder(new Date(), HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .error("back up error")
                    .message(e.getLocalizedMessage())
                    .path(request.getServletPath())
                    .data(null)
                    .build(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
