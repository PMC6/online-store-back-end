package com.fenlan.spring.shop.controller.customer;

import com.fenlan.spring.shop.bean.Request;
import com.fenlan.spring.shop.bean.ResponseFormat;
import com.fenlan.spring.shop.service.RequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@RestController
@RequestMapping("/customer/request")
public class RequestController {
    @Autowired
    RequestService requestService;
    @Autowired
    private HttpServletRequest request;

    @PostMapping("")
    public ResponseEntity<ResponseFormat> add(@RequestBody Request request) {
        try {
            return new ResponseEntity<>(new ResponseFormat.Builder(new Date(), HttpStatus.OK.value())
                    .error(null)
                    .message("request added")
                    .path(this.request.getServletPath())
                    .data(requestService.add(request))
                    .build(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseFormat.Builder(new Date(), HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .error("Request added failed")
                    .message(e.getLocalizedMessage())
                    .path(this.request.getServletPath())
                    .data(null)
                    .build(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
