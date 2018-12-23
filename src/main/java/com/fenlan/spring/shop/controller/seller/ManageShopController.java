/**
 * @author： fanzhonghao
 * @date: 18-12-10 14 58
 * @version: 1.0
 * @description:
 */
package com.fenlan.spring.shop.controller.seller;

import com.fenlan.spring.shop.bean.AdRequest;
import com.fenlan.spring.shop.bean.ResponseFormat;
import com.fenlan.spring.shop.bean.Shop;
import com.fenlan.spring.shop.service.AdService;
import com.fenlan.spring.shop.service.ProductService;
import com.fenlan.spring.shop.service.ShopService;
import com.fenlan.spring.shop.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Map;

@RestController
@RequestMapping("/seller")
public class ManageShopController {
    @Autowired
    ShopService shopService;
    @Autowired
    private HttpServletRequest request;
    @Autowired
    UserService userService;
    @Autowired
    AdService adService;
    @Autowired
    ProductService productService;

    /**
     * 由卖家名得到商店信息
     * @return
     */
    @GetMapping("/shop/info")
    public ResponseEntity<ResponseFormat> getShopInfo(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String sellerName = authentication.getName();
        try {
            return new ResponseEntity<>(new ResponseFormat.Builder(new Date(), HttpStatus.OK.value())
                    .error(null)
                    .message("find the shop info")
                    .path(request.getServletPath())
                    .data(shopService.findByUserName(sellerName))
                    .build(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseFormat.Builder(new Date(), HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .error("find error")
                    .message(e.getLocalizedMessage())
                    .path(request.getServletPath())
                    .data(null)
                    .build(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 获取卖家信息
     * @return
     */
    @GetMapping("/info")
    public ResponseEntity<ResponseFormat> getSellerInfo(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String sellerName = authentication.getName();
        try {
            return new ResponseEntity<>(new ResponseFormat.Builder(new Date(), HttpStatus.OK.value())
                    .error(null)
                    .message("find the seller info")
                    .path(request.getServletPath())
                    .data(userService.findByName(sellerName))
                    .build(), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(new ResponseFormat.Builder(new Date(), HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .error("find error")
                    .message(e.getLocalizedMessage())
                    .path(request.getServletPath())
                    .data(null)
                    .build(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/shop/advertisement")
    public ResponseEntity<ResponseFormat> addShopAd(@RequestBody Map param) {
        Double fee = Double.parseDouble(param.get("fee").toString());
        String image = param.get("image").toString();
        try {
            AdRequest adRequest = adService.addShopRequest(fee, image);
            return new ResponseEntity<>(new ResponseFormat.Builder(new Date(), HttpStatus.OK.value())
                    .error(null)
                    .message("add one shop advertisement request")
                    .path(request.getServletPath())
                    .data(adRequest)
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

    @PutMapping("/shop/update")
    public ResponseEntity<ResponseFormat> update(@RequestBody Map map) {
        try {
            String image = map.get("image").toString();
            String info = map.get("info").toString();
            String email = map.get("email").toString();
            String telephone = map.get("telephone").toString();
            String alipay = map.get("alipay").toString();

            return new ResponseEntity<>(new ResponseFormat.Builder(new Date(), HttpStatus.OK.value())
                    .error(null)
                    .message("update success")
                    .path(request.getServletPath())
                    .data(shopService.update(image, info, email, telephone, alipay))
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

    /**
     * 得到商店主页已应用的广告数量
     * @return
     */
    @GetMapping("/shop/advertisement/num")
    public ResponseEntity<ResponseFormat> getAdNum(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserName = authentication.getName();
        try {
            Shop shop = shopService.findByUserName(currentUserName);
            return new ResponseEntity<>(new ResponseFormat.Builder(new Date(), HttpStatus.OK.value())
                    .error(null)
                    .message("get products success")
                    .path(request.getServletPath())
                    .data(productService.amountByHomePageAndShopId(shop.getId(), true))
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

    /**
     * 查询商家某一商品是否应用于商城主页
     *
     */
    @GetMapping("/shop/advertisement/applytomail")
    public ResponseEntity<ResponseFormat> judgeWhetherAppliedToMail(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserName = authentication.getName();
        try {
            Shop shop = shopService.findByUserName(currentUserName);
            return new ResponseEntity<>(new ResponseFormat.Builder(new Date(), HttpStatus.OK.value())
                    .error(null)
                    .message("query success")
                    .path(request.getServletPath())
                    .data(shopService.judgeWhereAppliedToMail(shop.getId()))
                    .build(), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(new ResponseFormat.Builder(new Date(), HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .error("query failed")
                    .message(e.getLocalizedMessage())
                    .path(request.getServletPath())
                    .data(null)
                    .build(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
