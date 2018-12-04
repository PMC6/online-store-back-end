package com.fenlan.spring.shop.controller.seller;

import com.fenlan.spring.shop.bean.Product;
import com.fenlan.spring.shop.bean.ResponseFormat;
import com.fenlan.spring.shop.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/seller")
public class ManageProductController {
    @Autowired
    ProductService productService;
    @Autowired
    private HttpServletRequest request;

    @PostMapping("/product/add")
    public ResponseEntity<ResponseFormat> addProduct(@RequestBody Product param) {
        try {
            Product product = productService.add(param);
            return new ResponseEntity<>(new ResponseFormat.Builder(new Date(), HttpStatus.OK.value())
                    .error(null)
                    .message("add one product in your shop")
                    .path(request.getServletPath())
                    .data(product)
                    .build(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseFormat.Builder(new Date(), HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .error("Add failed")
                    .message(e.getLocalizedMessage())
                    .path(request.getServletPath())
                    .data(null)
                    .build(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/product/modify")
    public ResponseEntity<ResponseFormat> update(@RequestBody Product param) {
        try {
            return new ResponseEntity<>(new ResponseFormat.Builder(new Date(), HttpStatus.OK.value())
                    .error(null)
                    .message("update one product in your shop")
                    .path(request.getServletPath())
                    .data(productService.update(param))
                    .build(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseFormat.Builder(new Date(), HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .error("Update failed")
                    .message(e.getLocalizedMessage())
                    .path(request.getServletPath())
                    .data(null)
                    .build(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/product/delete")
    public ResponseEntity<ResponseFormat> delete(@RequestParam("id") Long id) {
        try {
            productService.delete(id);
            return new ResponseEntity<>(new ResponseFormat.Builder(new Date(), HttpStatus.OK.value())
                    .error(null)
                    .message("delete one product in your shop")
                    .path(request.getServletPath())
                    .data(null)
                    .build(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseFormat.Builder(new Date(), HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .error("Delete failed")
                    .message(e.getLocalizedMessage())
                    .path(request.getServletPath())
                    .data(null)
                    .build(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/product/list")
    public ResponseEntity<ResponseFormat> list(@RequestParam("page") Integer page,
                                               @RequestParam("size") Integer size) {
        try {
            List<Product> list = productService.list(page, size);
            return new ResponseEntity<>(new ResponseFormat.Builder(new Date(), HttpStatus.OK.value())
                    .error(null)
                    .message("list success")
                    .path(request.getServletPath())
                    .data(list)
                    .build(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseFormat.Builder(new Date(), HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .error("List failed")
                    .message(e.getLocalizedMessage())
                    .path(request.getServletPath())
                    .data(null)
                    .build(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
