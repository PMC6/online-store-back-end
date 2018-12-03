package com.fenlan.spring.shop.service;

import com.fenlan.spring.shop.DAO.CategoryDAO;
import com.fenlan.spring.shop.DAO.ProductDAO;
import com.fenlan.spring.shop.DAO.ShopDAO;
import com.fenlan.spring.shop.bean.Category;
import com.fenlan.spring.shop.bean.Product;
import com.fenlan.spring.shop.bean.Shop;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.Entity;
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
     * update at 18.12.1 by fan
     * 由商店名查找商品
     * @param shopName
     * @return
     */
    public List<Product> findByShopName(String shopName) throws Exception{
        Shop shop = shopDAO.findByName(shopName);
        if (shop == null) throw new Exception("null");
        List<Product> productList = productDAO.findByShopId(shop.getId());
        if (productList == null) throw new Exception("null");
        return productList;
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
     * update at 18.12.1 by fan
     * 更新商品信息
     * @param originalProduct
     * @param targetProduct
     */
    public void updateProduct(Product originalProduct, Product targetProduct){
        productDAO.delete(originalProduct);
        productDAO.save(targetProduct);
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

    /**
     * update at 18.12.1 by fan
     * 删除每个商店的全部产品
     * @param shop
     */
    public void deleteProductWithShop(Shop shop){
        productDAO.deleteAllByShopId(shop.getId());
    }
}
