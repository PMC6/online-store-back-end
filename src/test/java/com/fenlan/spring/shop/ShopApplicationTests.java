package com.fenlan.spring.shop;

import com.fenlan.spring.shop.DAO.RequestDAO;
import com.fenlan.spring.shop.DAO.SysRoleDAO;
import com.fenlan.spring.shop.DAO.UserDAO;
import com.fenlan.spring.shop.bean.Request;
import com.fenlan.spring.shop.bean.SysRole;
import com.fenlan.spring.shop.bean.User;
import com.fenlan.spring.shop.service.RequestService;
import com.fenlan.spring.shop.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ShopApplicationTests {
    @Autowired
    UserDAO userDAO;
    @Autowired
    SysRoleDAO sysRoleDAO;
    @Autowired
    RequestDAO requestDAO;
    @Autowired
    RequestService requestService;

    @Test
    public void contextLoads() {
    }

    @Test
    public void registerAdmin() {
        if (null != userDAO.findByUsername("root"))
            try {
                throw new Exception("管理员已存在");
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        else {
            User admin = new User();
            SysRole role = sysRoleDAO.findByName("ROLE_ADMIN");
            admin.setUsername("root");
            admin.setPassword(new BCryptPasswordEncoder().encode("root"));
            admin.setRoles(Arrays.asList(role));
            admin.setAddress("xian");
            admin.setTelephone("151");
            admin.setEmail("1475307818@qq.com");
            userDAO.save(admin);
        }
    }

    @Test
    public void addRoles() {
        SysRole admin = new SysRole();
        admin.setName("ROLE_ADMIN");
        sysRoleDAO.save(admin);
        SysRole user = new SysRole();
        user.setName("ROLE_USER");
        sysRoleDAO.save(user);
        SysRole customer = new SysRole();
        customer.setName("ROLE_SELLER");
        sysRoleDAO.save(customer);
    }

    @Test
    public void init() {
        addRoles();
        registerAdmin();
    }

}
