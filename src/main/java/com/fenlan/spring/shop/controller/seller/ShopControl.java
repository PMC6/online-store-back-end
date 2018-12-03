/**
 * @author： fanzhonghao
 * @date: 18-12-2 15 36
 * @version: 1.0
 * @description:
 */
package com.fenlan.spring.shop.controller.seller;

import com.fenlan.spring.shop.bean.Product;
import com.fenlan.spring.shop.bean.Shop;
import com.fenlan.spring.shop.service.ShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

//@RestController
@Controller
@RequestMapping("/")
public class ShopControl {
    @Autowired
    ShopService shopService;


    @RequestMapping("/login")
    public String allShop(Model model){
        List<Shop> shopList = null;
        try {
            shopList = shopService.findAllShop();
        } catch (Exception e) {
            e.printStackTrace();
        }
        model.addAttribute("shops", shopList);
        return "shopInfo";
    }
}
