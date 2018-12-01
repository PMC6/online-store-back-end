package com.fenlan.spring.shop.controller;

import com.fenlan.spring.shop.bean.ResponseFormat;
import com.fenlan.spring.shop.bean.User;
import com.fenlan.spring.shop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@RestController
@RequestMapping("/")
public class IndexController {
    @Autowired
    UserService service;
    @Autowired
    private HttpServletRequest request;
    @Autowired
    AuthenticationManager manager;

    @GetMapping("")
    public ResponseEntity<ResponseFormat> index(Authentication auth) {
        System.out.println("------------------------------------");
        return new ResponseEntity<>(new ResponseFormat.Builder(new Date(), HttpStatus.OK.value())
                .error(null)
                .message("login success")
                .path(request.getServletPath())
                .data(auth)
                .build(), HttpStatus.OK);
    }

    @PostMapping("user/login")
    public ResponseEntity<ResponseFormat> login(@RequestBody User param) {
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(param.getUsername(), param.getPassword());
        try {
            Authentication auth = manager.authenticate(token);
            SecurityContextHolder.getContext().setAuthentication(auth);
            System.out.println("fuck");
            return new ResponseEntity<>(new ResponseFormat.Builder(new Date(), HttpStatus.OK.value())
                    .error(null)
                    .message("login success")
                    .path(request.getServletPath())
                    .data(auth)
                    .build(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseFormat.Builder(new Date(), HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .error("login fail")
                    .message(e.getLocalizedMessage())
                    .path(request.getServletPath())
                    .data(null)
                    .build(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("test")
    public ResponseEntity<ResponseFormat> test(@RequestParam("username") String username) {
        return new ResponseEntity<>(new ResponseFormat.Builder(new Date(), HttpStatus.OK.value())
                .error(null)
                .message("login success")
                .path(request.getServletPath())
                .data(username)
                .build(), HttpStatus.OK);
    }

    @PostMapping(value = "register")
    public ResponseEntity<ResponseFormat> register(@RequestBody User param) {
        try {
            service.register(param.getUsername(), param.getPassword(), param.getTelephone(), param.getEmail(), param.getAddress());
            return new ResponseEntity<>(new ResponseFormat.Builder(new Date(), HttpStatus.OK.value())
                    .error(null)
                    .message("registration success")
                    .path(request.getServletPath())
                    .data(null)
                    .build(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseFormat.Builder(new Date(), HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .error("Registration fail")
                    .message(e.getLocalizedMessage())
                    .path(request.getServletPath())
                    .data(null)
                    .build(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("user/role")
    public ResponseEntity<ResponseFormat> getUserRoleByName(@RequestParam("username") String username) {
        User user = service.findByName(username);
        return new ResponseEntity<>(new ResponseFormat.Builder(new Date(), HttpStatus.OK.value())
                .error(null)
                .message("query success")
                .path(request.getServletPath())
                .data(user.getRoles())
                .build(), HttpStatus.OK);
    }
}
