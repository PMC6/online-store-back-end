/**
 * @author： fanzhonghao
 * @date: 18-12-2 15 36
 * @version: 1.0
 * @description:
 */
package com.fenlan.spring.shop.controller.seller;

import com.fenlan.spring.shop.DAO.ShopDAO;
import com.fenlan.spring.shop.bean.ResponseFormat;
import com.fenlan.spring.shop.bean.Shop;
import com.fenlan.spring.shop.service.ProductService;
import com.fenlan.spring.shop.service.ShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
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
    @Autowired
    ShopDAO shopDAO;


    /**
     * 由商家名得到商店信息
     * @param ownerName
     * @return
     */
    @GetMapping("/shop/info")
    public ResponseEntity<ResponseFormat> findShopByOwnerName(@RequestParam("userName") String ownerName){

        try {
            return new ResponseEntity<>(new ResponseFormat.Builder(new Date(), HttpStatus.OK.value())
                    .error(null)
                    .message("find success")
                    .path(request.getServletPath())
                    .data(shopService.findByOwnerName(ownerName))
                    .build(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseFormat.Builder(new Date(), HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .error(null)
                    .message(e.getLocalizedMessage())
                    .path(request.getServletPath())
                    .data(null)
                    .build(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 更新商店信息
     * @param updatedShop
     * @return
     */
    @PutMapping("/shop/update")
    public ResponseEntity<ResponseFormat> updateShopInfo(@RequestBody Shop updatedShop){
        try {
            return new ResponseEntity<>(new ResponseFormat.Builder(new Date(), HttpStatus.OK.value())
                    .error(null)
                    .message("updated success")
                    .path(request.getServletPath())
                    .data(shopService.update(updatedShop))
                    .build(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseFormat.Builder(new Date(), HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .error("can't update the shop info")
                    .message(e.getLocalizedMessage())
                    .path(request.getServletPath())
                    .data(null)
                    .build(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/shop/delete")
    public ResponseEntity<ResponseFormat> deleteShop(@RequestParam("shopId") Long shopId){
        try {
            shopService.delete(shopId);
            return new ResponseEntity<>(new ResponseFormat.Builder(new Date(), HttpStatus.OK.value())
                    .error(null)
                    .message("delete success")
                    .path(request.getServletPath())
                    .data(null)
                    .build(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseFormat.Builder(new Date(), HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .error("can't delete shop")
                    .message(e.getLocalizedMessage())
                    .path(request.getServletPath())
                    .data(null)
                    .build(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/shop/add")
    public ResponseEntity<ResponseFormat> createShop(@RequestBody Shop newShop){
        try {
            shopService.add(newShop);
            return new ResponseEntity<>(new ResponseFormat.Builder(new Date(), HttpStatus.OK.value())
                    .error(null)
                    .message("add success")
                    .path(request.getServletPath())
                    .data(newShop)
                    .build(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseFormat.Builder(new Date(), HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .error("can't add the shop")
                    .message(e.getLocalizedMessage())
                    .path(request.getServletPath())
                    .data(null)
                    .build(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
