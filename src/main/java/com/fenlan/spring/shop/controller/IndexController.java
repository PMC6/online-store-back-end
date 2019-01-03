package com.fenlan.spring.shop.controller;

import com.fenlan.spring.shop.bean.*;
import com.fenlan.spring.shop.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.Map;

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
    @Autowired
    UserService userService;
    @Autowired
    AdService adService;
    @Autowired
    ShopService shopService;

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
                                               @RequestParam("size") Integer size,
                                               @RequestParam(value = "category", required = false) Long categoryId) {
        try {
            List<Product> list = productService.listAll(page, size, categoryId);
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

    @GetMapping("amount")
    public ResponseEntity<ResponseFormat> amount(@RequestParam(value = "category", required = false) Long categoryId) {
        return new ResponseEntity<>(new ResponseFormat.Builder(new Date(), HttpStatus.OK.value())
                .error(null)
                .message("get product success")
                .path(request.getServletPath())
                .data(productService.amount(categoryId))
                .build(), HttpStatus.OK);
    }

    @GetMapping("product/search")
    public ResponseEntity<Object> findByName(@RequestParam("name") String name,
                                             @RequestParam("page") Integer page,
                                             @RequestParam("size") Integer size,
                                             @RequestParam(value = "category", required = false) Long categoryId) {
        try {
            return new ResponseEntity<>(new ResponseFormat.Builder(new Date(), HttpStatus.OK.value())
                    .error(null)
                    .message("find product")
                    .path(request.getServletPath())
                    .data(productService.findByNameContain(name, page, size, categoryId))
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

    @GetMapping("product/search/amount")
    public ResponseEntity<ResponseFormat> amountByName(@RequestParam("name") String name,
                                                       @RequestParam(value = "category", required = false) Long categoryId) {
        return new ResponseEntity<>(new ResponseFormat.Builder(new Date(), HttpStatus.OK.value())
                .error(null)
                .message("get product success")
                .path(request.getServletPath())
                .data(productService.amountByName(name, categoryId))
                .build(), HttpStatus.OK);
    }

    @PostMapping("/change/password")
    public ResponseEntity<ResponseFormat> changePasswd(@RequestBody Map map) {
        try {
            String before = map.get("before").toString();
            String after = map.get("after").toString();
            userService.changePasswd(before, after);
            return new ResponseEntity<>(new ResponseFormat.Builder(new Date(), HttpStatus.OK.value())
                    .error(null)
                    .message("change password success")
                    .path(request.getServletPath())
                    .data(null)
                    .build(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseFormat.Builder(new Date(), HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .error("Change failed")
                    .message(e.getLocalizedMessage())
                    .path(request.getServletPath())
                    .data(null)
                    .build(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/advertisement/product/list")
    public ResponseEntity<ResponseFormat> listProductAd() {
        try {
            List<Advertisement> list = adService.listProductTop();
            return new ResponseEntity<>(new ResponseFormat.Builder(new Date(), HttpStatus.OK.value())
                    .error(null)
                    .message("get product advertisement list")
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

    @GetMapping("/advertisement/shop/list")
    public ResponseEntity<ResponseFormat> listShopAd() {
        try {
            List<Advertisement> list = adService.listShopTop();
            return new ResponseEntity<>(new ResponseFormat.Builder(new Date(), HttpStatus.OK.value())
                    .error(null)
                    .message("get shop advertisement list")
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

    @GetMapping("/user/auth")
    public ResponseEntity<ResponseFormat> auth(Authentication auth, @RequestParam("username") String name) {
        try {
            if (null == auth || !auth.getName().equals(name))
                throw new Exception("Login expired");
            return new ResponseEntity<>(new ResponseFormat.Builder(new Date(), HttpStatus.OK.value())
                    .error(null)
                    .message("You are logged in to the system")
                    .path(request.getServletPath())
                    .data(auth)
                    .build(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseFormat.Builder(new Date(), HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .error("Login expired")
                    .message(e.getLocalizedMessage())
                    .path(request.getServletPath())
                    .data(null)
                    .build(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/user/shop")
    public ResponseEntity<ResponseFormat> shopDetail(@RequestParam("id") Long id) {
        try {
            return new ResponseEntity<>(new ResponseFormat.Builder(new Date(), HttpStatus.OK.value())
                    .error(null)
                    .message("query success")
                    .path(request.getServletPath())
                    .data(shopService.finById(id))
                    .build(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseFormat.Builder(new Date(), HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .error("Not found")
                    .message(e.getLocalizedMessage())
                    .path(request.getServletPath())
                    .data(null)
                    .build(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/user/shop/list")
    public ResponseEntity<ResponseFormat> listProductOfShop(@RequestParam("id") Long shopId,
                                                            @RequestParam("page") Integer page,
                                                            @RequestParam("size") Integer size,
                                                            @RequestParam(value = "name",required = false) String name) {
        try {
            return new ResponseEntity<>(new ResponseFormat.Builder(new Date(), HttpStatus.OK.value())
                    .error(null)
                    .message("query success")
                    .path(request.getServletPath())
                    .data(productService.listShopProduct(shopId, page, size, name))
                    .build(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseFormat.Builder(new Date(), HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .error("Not found")
                    .message(e.getLocalizedMessage())
                    .path(request.getServletPath())
                    .data(null)
                    .build(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/user/shop/amount")
    public ResponseEntity<ResponseFormat> productAmountOfShop(@RequestParam(value = "name", required = false) String name,
                                                              @RequestParam("id") Long shopId) {
        return new ResponseEntity<>(new ResponseFormat.Builder(new Date(), HttpStatus.OK.value())
                .error(null)
                .message("get product success")
                .path(request.getServletPath())
                .data(productService.amountProductOfShop(shopId, name))
                .build(), HttpStatus.OK);
    }

    // 暂定
    @PostMapping("/advertisement/upload")
    public ResponseEntity<ResponseFormat> uploads(@RequestParam("name") MultipartFile file) {
        String fileName = null;
        try {
            fileName = System.currentTimeMillis()+file.getOriginalFilename();
            byte[] bytes = file.getBytes();
            Path path = Paths.get("/root/Picture/image/static/image/"+fileName);
            Files.write(path, bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ResponseFormat.Builder(new Date(), HttpStatus.OK.value())
                .error(null)
                .message("get shop advertisement list")
                .path(request.getServletPath())
                .data("http://39.98.165.19:8084/static/image/"+fileName)
                .build(), HttpStatus.OK);
    }
}
