package com.fenlan.spring.shop.controller.seller;

import com.fenlan.spring.shop.bean.*;
import com.fenlan.spring.shop.service.AdService;
import com.fenlan.spring.shop.service.ShopService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import com.fenlan.spring.shop.service.CategoryService;
import com.fenlan.spring.shop.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/seller")
public class ManageProductController {
    @Autowired
    ProductService productService;
    @Autowired
    private HttpServletRequest request;
    @Autowired
    CategoryService categoryService;
    @Autowired
    ShopService shopService;
    @Autowired
    AdService adService;

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
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String sellerName = authentication.getName();
        try {
            return new ResponseEntity<>(new ResponseFormat.Builder(new Date(), HttpStatus.OK.value())
                    .error(null)
                    .message("update one product in your shop")
                    .path(request.getServletPath())
                    .data(productService.update(param, sellerName))
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

    @GetMapping("/product/amount")
    public ResponseEntity<ResponseFormat> amount() {
        return new ResponseEntity<>(new ResponseFormat.Builder(new Date(), HttpStatus.OK.value())
                .error(null)
                .message("amount success")
                .path(request.getServletPath())
                .data(productService.amount(null))
                .build(), HttpStatus.OK);
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

    @GetMapping("/product/search")
    public ResponseEntity<ResponseFormat> search(@RequestParam("name") String name) {
        try {
            Product product = productService.findByNamAndShop(name);
            return new ResponseEntity<>(new ResponseFormat.Builder(new Date(), HttpStatus.OK.value())
                    .error(null)
                    .message("search success")
                    .path(request.getServletPath())
                    .data(product)
                    .build(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseFormat.Builder(new Date(), HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .error("Search failed")
                    .message(e.getLocalizedMessage())
                    .path(request.getServletPath())
                    .data(null)
                    .build(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/product/search/id")
    public ResponseEntity<ResponseFormat> searchById(@RequestParam("id") Long id) {
        try {
            Product product = productService.findById(id);
            return new ResponseEntity<>(new ResponseFormat.Builder(new Date(), HttpStatus.OK.value())
                    .error(null)
                    .message("search success")
                    .path(request.getServletPath())
                    .data(product)
                    .build(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseFormat.Builder(new Date(), HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .error("Search failed")
                    .message(e.getLocalizedMessage())
                    .path(request.getServletPath())
                    .data(null)
                    .build(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/product/category")
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

    /**
     * 按照价格排序
     * @return
     */
    @GetMapping("/product/sortByPrice")
    public ResponseEntity<ResponseFormat> sortByPrice(@RequestParam("page") int page,
                                                      @RequestParam("size") int size,
                                                      @RequestParam("positive") boolean positive){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserName = authentication.getName();
        try {
            Shop shop = shopService.findByUserName(currentUserName);
            List<Product> list = productService.listByPrice(shop.getId(), page, size, positive);
            return new ResponseEntity<>(new ResponseFormat.Builder(new Date(), HttpStatus.OK.value())
                    .error(null)
                    .message("get products success")
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

    /**
     * 按库存量排序
     * @param page
     * @param size
     * @param positive
     * @return
     */
    @GetMapping("/product/sortByStock")
    public ResponseEntity<ResponseFormat> sortByStock(@RequestParam("page") int page,
                                                      @RequestParam("size") int size,
                                                      @RequestParam("positive") boolean positive){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserName = authentication.getName();
        try {
            Shop shop = shopService.findByUserName(currentUserName);
            List<Product> list = productService.listByStock(shop.getId(), page, size, positive);
            return new ResponseEntity<>(new ResponseFormat.Builder(new Date(), HttpStatus.OK.value())
                    .error(null)
                    .message("get products success")
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
  
    /**
     * 按是否应用于店铺主页排序，适用于manageProduct部分
     * @param page
     * @param size
     * @param positive true表示已应用的在前
     * @return
     */
    @GetMapping("/product/sortByAppliedToShop")
    public ResponseEntity<ResponseFormat> sortByAppliedToShop(@RequestParam("page") int page,
                                                      @RequestParam("size") int size,
                                                      @RequestParam("positive") boolean positive){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserName = authentication.getName();
        try {
            Shop shop = shopService.findByUserName(currentUserName);
            List<Product> list = productService.listByAppliedToShop(shop.getId(), page, size, positive);
            return new ResponseEntity<>(new ResponseFormat.Builder(new Date(), HttpStatus.OK.value())
                    .error(null)
                    .message("get products success")
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

    /**
     * 将商店商品按照销量排序
     * @param page
     * @param size
     * @param positive true表示销量大的在前
     * @return
     */
    @GetMapping("/product/sortBySales")
    public ResponseEntity<ResponseFormat> sortBySales(@RequestParam("page") int page,
                                                      @RequestParam("size") int size,
                                                      @RequestParam("positive") boolean positive,
                                                      @RequestParam("shopName") String shopName){
        try {
            Shop shop = shopService.findByName(shopName);
            return new ResponseEntity<>(new ResponseFormat.Builder(new Date(), HttpStatus.OK.value())
                    .error(null)
                    .message("get products success")
                    .path(request.getServletPath())
                    .data(productService.listBySales(shop.getId(), page, size, positive))
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
     * 得到商店所有要显示在商家主页的商品
     * @param shopName
     * @return
     */
    @GetMapping("/product/advertisement/all")
    public ResponseEntity<ResponseFormat> getAdverstiment(String shopName){
        try {
            return new ResponseEntity<>(new ResponseFormat.Builder(new Date(), HttpStatus.OK.value())
                    .error(null)
                    .message("get products success")
                    .path(request.getServletPath())
                    .data(productService.findByAdvertisement(shopService.findByName(shopName).getId()))
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
     * 提交一个展示在商城主页的商品广告申请
     * @param param
     * @return
     */
    @PostMapping("/product/advertisement")
    public ResponseEntity<ResponseFormat> addProductAd(@RequestBody Map param) {
        Long productId = Long.parseLong(param.get("id").toString());
        Double fee = Double.parseDouble(param.get("fee").toString());
        String image = param.get("image").toString();
        try {
            AdRequest adRequest = adService.addProductRequest(productId, fee, image);
            return new ResponseEntity<>(new ResponseFormat.Builder(new Date(), HttpStatus.OK.value())
                    .error(null)
                    .message("add one product advertisement request")
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

}