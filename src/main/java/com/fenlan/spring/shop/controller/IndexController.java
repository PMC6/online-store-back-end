package com.fenlan.spring.shop.controller;

import com.fenlan.spring.shop.bean.Category;
import com.fenlan.spring.shop.bean.Product;
import com.fenlan.spring.shop.bean.ResponseFormat;
import com.fenlan.spring.shop.bean.User;
import com.fenlan.spring.shop.service.CategoryService;
import com.fenlan.spring.shop.service.ProductService;
import com.fenlan.spring.shop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/")
public class IndexController {
    @Autowired
    UserService service;
    @Autowired
    private HttpServletRequest request;
    @Autowired
    AuthenticationManager manager;
    @Autowired
    CategoryService categoryService;
    @Autowired
    ProductService productService;

    @GetMapping("")
    public ResponseEntity<ResponseFormat> index(Authentication auth) {
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

    @GetMapping("category")
    public ResponseEntity<ResponseFormat> getCategory() {
        try {
            List<Category> list = categoryService.list();
            return new ResponseEntity<>(new ResponseFormat.Builder(new Date(), HttpStatus.OK.value())
                    .error(null)
                    .message("get category success")
                    .path(request.getServletPath())
                    .data(list)
                    .build(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseFormat.Builder(new Date(), HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .error("Query failed")
                    .message(e.getLocalizedMessage())
                    .path(request.getServletPath())
                    .data(null)
                    .build(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("list")
    public ResponseEntity<ResponseFormat> list(@RequestParam("page") Integer page,
                                               @RequestParam("size") Integer size) {
        try {
            List<Product> list = productService.listAll(page, size);
            return new ResponseEntity<>(new ResponseFormat.Builder(new Date(), HttpStatus.OK.value())
                    .error(null)
                    .message("get product success")
                    .path(request.getServletPath())
                    .data(list)
                    .build(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseFormat.Builder(new Date(), HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .error("Query failed")
                    .message(e.getLocalizedMessage())
                    .path(request.getServletPath())
                    .data(null)
                    .build(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("product/search")
    public ResponseEntity<Object> findByName(@RequestParam("name") String name,
                                             @RequestParam("page") Integer page,
                                             @RequestParam("size") Integer size) {
        try {
            return new ResponseEntity<>(new ResponseFormat.Builder(new Date(), HttpStatus.OK.value())
                    .error(null)
                    .message("find product")
                    .path(request.getServletPath())
                    .data(productService.findByName(name, page, size))
                    .build(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseFormat.Builder(new Date(), HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .error("Query error")
                    .message(e.getLocalizedMessage())
                    .path(request.getServletPath())
                    .data(null)
                    .build(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
