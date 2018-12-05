/**
 * @author： fanzhonghao
 * @date: 18-12-3 15 48
 * @version: 1.0
 * @description:
 */
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
import java.util.Map;

@RestController
@RequestMapping("/seller")
public class SellerProductController {
    @Autowired
    private HttpServletRequest request;
    @Autowired
    ProductService productService;

    /**
     * 由shopName得到商品信息
     * @param shopName
     * @return
     */
    @GetMapping("/product/allInfo")
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
     * 由shopId得到商品页，已由createtime排序
     * @param shopId
     * @param page
     * @param size
     * @return
     */
    @GetMapping("/product/partly")
    public ResponseEntity<ResponseFormat> findProduct(@RequestParam("shopId") Long shopId,
                                                      @RequestParam("page") int page,
                                                      @RequestParam("size") int size){
        try {
            return new ResponseEntity<>(new ResponseFormat.Builder(new Date(), HttpStatus.OK.value())
                    .error(null)
                    .message("find success")
                    .path(request.getServletPath())
                    .data(productService.findByShopId(shopId, page, size))
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
     * 根据创建时间排序
     * @param shopId
     * @param page
     * @param size
     * @return
     */
    @GetMapping("/product/sortByCreateTime")
    public ResponseEntity<ResponseFormat> findProductByCreateTime(@RequestParam("shopId") Long shopId,
                                                      @RequestParam("page") int page,
                                                      @RequestParam("size") int size){
        return findProduct(shopId, page, size);
    }

    /**
     * 根据创建时间排序
     * @param shopId
     * @param page
     * @param size
     * @return
     */
    @GetMapping("/product/sortByCreateTimeDec")
    public ResponseEntity<ResponseFormat> findProductByCreateTimeDec(@RequestParam("shopId") Long shopId,
                                                                     @RequestParam("page") int page,
                                                                     @RequestParam("size") int size){
        try {
            return new ResponseEntity<>(new ResponseFormat.Builder(new Date(), HttpStatus.OK.value())
                    .error(null)
                    .message("find success")
                    .path(request.getServletPath())
                    .data(productService.findByShopIdDec(shopId, page, size))
                    .build(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseFormat.Builder(new Date(), HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .error(e.getLocalizedMessage())
                    .message(e.getLocalizedMessage())
                    .path(request.getServletPath())
                    .data(null)
                    .build(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 由店铺id和商品类别得到商品页
     * @param shopId
     * @param categoryName
     * @return
     */
    @GetMapping("/product/search")
    public ResponseEntity<ResponseFormat> findProductByCategoryName(@RequestParam("shopId") long shopId,
                                                                    @RequestParam("category") String categoryName,
                                                                    @RequestParam("page") int page,
                                                                    @RequestParam("size") int size){
        try {
            return new ResponseEntity<>(new ResponseFormat.Builder(new Date(), HttpStatus.OK.value())
                    .error(null)
                    .message("find success")
                    .path(request.getServletPath())
                    .data(productService.findByShopIdAndCategory(shopId, categoryName, page, size))
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
     * 由商品id得到商品具体信息
     * @param productId
     * @return
     */
    @GetMapping("/product/detailedInfo")
    public ResponseEntity<ResponseFormat> productDetailedInfo(@RequestParam("productId") long productId){
        try {
            return new ResponseEntity<>(new ResponseFormat.Builder(new Date(), HttpStatus.OK.value())
                    .error(null)
                    .message("find success")
                    .path(request.getServletPath())
                    .data(productService.findById(productId))
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
     * 更新产品信息
     * @param updatedProduct
     * @return
     */
    @PutMapping("/product/update")
    public ResponseEntity<ResponseFormat> updateProductInfo(@RequestBody Product updatedProduct){
        try {
            return new ResponseEntity<>(new ResponseFormat.Builder(new Date(), HttpStatus.OK.value())
                    .error(null)
                    .message("update success")
                    .path(request.getServletPath())
                    .data(productService.updateProduct(updatedProduct))
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
     * 由产品id删除产品
     * @param id
     * @return
     */
    @DeleteMapping("/product/delete")
    public ResponseEntity<ResponseFormat> deleteProduct(@RequestParam("productId") long id){
        try {
            productService.deleteProductById(id);
            return new ResponseEntity<>(new ResponseFormat.Builder(new Date(), HttpStatus.OK.value())
                    .error(null)
                    .message("delete success")
                    .path(request.getServletPath())
                    .data(null)
                    .build(), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(new ResponseFormat.Builder(new Date(), HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .error(null)
                    .message(e.getLocalizedMessage())
                    .path(request.getServletPath())
                    .data(null)
                    .build(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 增加商品
     * @param newProduct
     * @return
     */
    @PutMapping("/product/add")
    public ResponseEntity<ResponseFormat> addProduct(@RequestBody Product newProduct){
        try{
            return new ResponseEntity<>(new ResponseFormat.Builder(new Date(), HttpStatus.OK.value())
                    .error(null)
                    .message("add success")
                    .path(request.getServletPath())
                    .data(productService.add(newProduct))
                    .build(), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(new ResponseFormat.Builder(new Date(), HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .error(null)
                    .message(e.getLocalizedMessage())
                    .path(request.getServletPath())
                    .data(null)
                    .build(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
