package com.fenlan.spring.shop.service;

import com.fenlan.spring.shop.DAO.CategoryDAO;
import com.fenlan.spring.shop.DAO.ProductDAO;
import com.fenlan.spring.shop.DAO.ShopDAO;
import com.fenlan.spring.shop.bean.Category;
import com.fenlan.spring.shop.bean.Product;
import com.fenlan.spring.shop.bean.Shop;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    @Autowired
    ProductDAO productDAO;
    @Autowired
    CategoryDAO categoryDAO;
    @Autowired
    ShopDAO shopDAO;

    public Product add(Product newProduct) throws Exception {
        try {
            if (null != productDAO.findByShopIdAndName(newProduct.getShopId(), newProduct.getName()))
                throw new Exception("product has been added in this shop");
            return productDAO.save(newProduct);
        } catch (Exception e) {
            throw e;
        }
    }

    public List<Product> findByName(String name) throws Exception {
        List<Product> list = productDAO.findByName(name);
        if (list.size() == 0)
            throw new Exception("null");
        return list;
    }

    /**
     * update at 18.12.1 by fan
     * 由商品类别查找商品
     * @param category
     * @return
     */
    public List<Product> findByCategory(String category) throws Exception{
        Category category1 = categoryDAO.findByName(category);
        if(null == category1) throw new Exception("null");
        List<Product> productList = productDAO.findByCategoryId(category1.getId());
        if (productList == null) throw new Exception("null");
        return productList;
    }

    /**
     * 由shopId,category,page,size共同得到商品页
     * @param shopId
     * @param category
     * @param page
     * @param size
     * @return
     * @throws Exception
     */
    public List<Product> findByShopIdAndCategory(Long shopId, String category, int page, int size) throws Exception {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createTime"));
        List<Product> list = productDAO.findByShopIdAndCategoryId(shopId,
                categoryDAO.findByName(category).getId(), pageable);
        if (list.size() == 0)
            throw new Exception("no result or page param is bigger than normal");
        else
            return list;
    }

    /**
     * 由shopId,category,page,size共同得到商品页，时间逆序
     * @param shopId
     * @param category
     * @param page
     * @param size
     * @return
     * @throws Exception
     */
    public List<Product> findByShopIdAndCategoryDES(Long shopId, String category, int page, int size) throws Exception {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "createTime"));
        List<Product> list = productDAO.findByShopIdAndCategoryId(shopId,
                categoryDAO.findByName(category).getId(), pageable);
        if (list.size() == 0)
            throw new Exception("no result or page param is bigger than normal");
        else
            return list;
    }


    public Product findById(long id) throws Exception{
        Product product = productDAO.findById(id);
        if(product == null) throw new Exception("no this product");
        return product;
    }

    /**
     * update at 18.12.1 by fan
     * 由商店名查找商品
     * @param shopName
     * @return
     */
    public List<Product> findByShopName(String shopName) throws Exception{
        Shop shop = shopDAO.findByName(shopName);
        if (shop == null) throw new Exception("not exist the shop");
        List<Product> productList = productDAO.findByShopId(shop.getId());
        if (productList == null) throw new Exception("not exist product");
        return productList;
    }

    /**
     * 由店铺id、页数和大小得到商品列表
     * @param shopId
     * @param page
     * @param size
     * @return
     * @throws Exception
     */
    public List<Product> findByShopId(Long shopId, int page, int size) throws Exception {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createTime"));
        List<Product> list = productDAO.findByShopId(shopId, pageable);
        if (list.size() == 0)
            throw new Exception("no result or page param is bigger than normal");
        else
            return list;
    }

    /**
     * 由店铺id、页数和大小得到商品列表，按时间逆序
     * @param shopId
     * @param page
     * @param size
     * @return
     * @throws Exception
     */
    public List<Product> findByShopIdDec(Long shopId, int page, int size) throws Exception {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "createTime"));
        List<Product> list = productDAO.findByShopId(shopId, pageable);
        if (list.size() == 0)
            throw new Exception("no result or page param is bigger than normal");
        else
            return list;
    }

    /**
     * update at 18.12.1 by fan
     * 由商品名查找商品
     * @param productName
     * @return
     */
    public List<Product> findByProductName(String productName) throws Exception{
        List<Product> productList = productDAO.findByName(productName);
        if (productList == null) throw new Exception("null");
        return productList;
    }

    /**
     * 由店铺id和商品名得到商品
     * @param shopId
     * @param productName
     * @return
     */
    public Product findByShopIdAndProductName(long shopId, String productName){
        Product product = productDAO.findByShopIdAndName(shopId, productName);
        return product;
    }

    /**
     * update at 18.12.1 by fan
     * 更新商品信息
     * @param targetProduct
     */
    public Product updateProduct(Product targetProduct) throws Exception{
        try {
            productDAO.save(targetProduct);
        }catch (Exception e){
            throw new Exception("can't update the product");
        }
        return targetProduct;
    }

    /**
     * update at 18.12.1 by fan
     * 删除商品
     * @param product 商品
     */
    public void deleteProduct(Product product){
        try {
            productDAO.delete(product);
        }catch (Exception e){
            System.out.println("delete product error at ProductService.java");
        }
    }

    public void deleteProductById(long id){
        productDAO.deleteById(id);
    }
    /**
     * update at 18.12.1 by fan
     * 删除每个商店的全部产品
     * @param shop
     */
    public void deleteProductWithShop(Shop shop){
        productDAO.deleteAllByShopId(shop.getId());
    }
}
