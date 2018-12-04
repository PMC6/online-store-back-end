/**
 * @author： fanzhonghao
 * @date: 18-12-2 15 36
 * @version: 1.0
 * @description:
 */
package com.fenlan.spring.shop.controller.seller;

import com.fenlan.spring.shop.bean.Product;
import com.fenlan.spring.shop.bean.ResponseFormat;
import com.fenlan.spring.shop.bean.Shop;
import com.fenlan.spring.shop.service.ProductService;
import com.fenlan.spring.shop.service.RequestService;
import com.fenlan.spring.shop.service.ShopService;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/seller")
public class ShopControl {
    @Autowired
    ShopService shopService;
    @Autowired
    private HttpServletRequest request;
    @Autowired
    ProductService productService;

    /**
     * 由商家名得到商店信息
     * @param ownerName
     * @return
     */
    @GetMapping("/shopInfo")
    public ResponseEntity<ResponseFormat> findShopByOwnerName(@RequestParam("ownerName") String ownerName){

        return new ResponseEntity<>(new ResponseFormat.Builder(new Date(), HttpStatus.OK.value())
                .error(null)
                .message("find success")
                .path(request.getServletPath())
                .data(shopService.findByOwnerName(ownerName))
                .build(), HttpStatus.OK);
    }

    /**
     * 由shopName得到商品信息
     * @param shopName
     * @return
     */
    @GetMapping("/allProductInfo")
    public ResponseEntity<ResponseFormat> findProductsByShopName(@RequestParam("shopName") String shopName){
        try {
            return new ResponseEntity<>(new ResponseFormat.Builder(new Date(), HttpStatus.OK.value())
                    .error(null)
                    .message("find success")
                    .path(request.getServletPath())
                    .data(productService.findByShopName(shopName))
                    .build(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseFormat.Builder(new Date(), HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .error("doesn't exist the shop or doesn't exist product")
                    .message(e.getLocalizedMessage())
                    .path(request.getServletPath())
                    .data(null)
                    .build(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 更新商店信息
     * @param map
     * @return
     */
    @PutMapping("/updateShopInfo")
    public ResponseEntity<ResponseFormat> updateShopInfo(@RequestBody Map<String, Object> map){
        Shop shop = (Shop) map.get("targetShop");
        return new ResponseEntity<>(new ResponseFormat.Builder(new Date(), HttpStatus.OK.value())
                .error(null)
                .message("find success")
                .path(request.getServletPath())
                .data(shopService.update(shop.getId(), shop))
                .build(), HttpStatus.OK);
    }
}
