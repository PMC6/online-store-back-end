/**
 * @author： fanzhonghao
 * @date: 18-12-2 16 03
 * @version: 1.0
 * @description:
 */
package com.fenlan.spring.shop.controller.seller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


//@Controller
//@RequestMapping("/seller")
//public class loginControl {
//    @RequestMapping("/login")
//    public String login(Model model){
//        return "login";
//    }
//}

@Controller
@RequestMapping("/")
class login{
    @RequestMapping("/login")
    public String login(){
        return "login";
    }
}