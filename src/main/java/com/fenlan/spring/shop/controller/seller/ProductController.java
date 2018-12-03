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
public class ProductController {
    @Autowired
    private HttpServletRequest request;
    @Autowired
    ProductService productService;

    /**
     * 由店铺名得到该店铺的所有产品
     * @param shopName
     * @return
     */
    @GetMapping("/allProduct")
    public ResponseEntity<ResponseFormat> allProduct(@RequestParam("shopName") String shopName){
        try {
            return new ResponseEntity<>(new ResponseFormat.Builder(new Date(), HttpStatus.OK.value())
                    .error(null)
                    .message("find success")
                    .path(request.getServletPath())
                    .data(productService.findByShopName(shopName))
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
    @GetMapping("/productDetailedInfo")
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
     * @param map
     * @return
     */
    @PutMapping("/product/update")
    public ResponseEntity<ResponseFormat> updateProductInfo(@RequestBody Map<String, Object> map){
//        Product product = new Product();
//        product.setId((long) map.get("id"));
//        product.setName((String) map.get("name"));
//        product.setCategoryId((long) map.get("categoryId"));
//        product.setImage((String) map.get("img"));
//        product.setInfo((String) map.get("info"));
//        product.setUserId((long) map.get("userId"));
//        product.setPrice((double) map.get("price"));
//        product.setNumber((int) map.get("number"));
//        product.setShopId((long) map.get("shopId"));
//        product.setHomePage((boolean) map.get("homePage"));
//        product.setCreateTime((Date) map.get("createTime"));
//        product.setUpdateTime((new Date().getTime()));
//        return new ResponseEntity<>(new ResponseFormat.Builder(new Date(), HttpStatus.OK.value())
//                .error(null)
//                .message("update success")
//                .path(request.getServletPath())
//                .data(productService.updateProduct(product.getId(),product))
//                .build(), HttpStatus.OK);
        Product product = (Product) map.get("updatedProduct");
        return new ResponseEntity<>(new ResponseFormat.Builder(new Date(), HttpStatus.OK.value())
                .error(null)
                .message("update success")
                .path(request.getServletPath())
                .data(productService.updateProduct(product.getId(),product))
                .build(), HttpStatus.OK);
    }

    /**
     * 由产品id删除产品
     * @param id
     * @return
     */
    @PutMapping("/productInfo/delete")
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
     * @param map
     * @return
     */
    @PutMapping("/addProduct")
    public ResponseEntity<ResponseFormat> addProduct(@RequestBody Map<String, Object> map){
        Product product =(Product) map.get("newProduct");
        try{
            return new ResponseEntity<>(new ResponseFormat.Builder(new Date(), HttpStatus.OK.value())
                    .error(null)
                    .message("add success")
                    .path(request.getServletPath())
                    .data(productService.add(product))
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
     * 由店铺id和商品名删除商品
     * @param shopId
     * @param productName
     * @return
     */
    @GetMapping("/search?{shopId}&{productName}")
    public ResponseEntity<ResponseFormat> findProductByName(@PathVariable long shopId,
                                                            @PathVariable String productName){

        try {
            return new ResponseEntity<>(new ResponseFormat.Builder(new Date(), HttpStatus.OK.value())
                    .error(null)
                    .message("find success")
                    .path(request.getServletPath())
                    .data(productService.findByShopIdAndProductName(shopId,productName))
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
     * 由店铺id和商品类别得到商品列表
     * @param shopId
     * @param categoryName
     * @return
     */
    @GetMapping("/search?{shopId}&{categoryName}")
    public ResponseEntity<ResponseFormat> findProductByCategoryName(@PathVariable long shopId,
                                                                    @PathVariable String categoryName){
        try {
            return new ResponseEntity<>(new ResponseFormat.Builder(new Date(), HttpStatus.OK.value())
                    .error(null)
                    .message("find success")
                    .path(request.getServletPath())
                    .data(productService.findByShopIdAndProductCategory(shopId, categoryName))
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
}
