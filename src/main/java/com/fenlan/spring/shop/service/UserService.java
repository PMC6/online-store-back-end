package com.fenlan.spring.shop.service;

import com.fenlan.spring.shop.DAO.ShopDAO;
import com.fenlan.spring.shop.DAO.SysRoleDAO;
import com.fenlan.spring.shop.DAO.UserDAO;
import com.fenlan.spring.shop.bean.Shop;
import com.fenlan.spring.shop.bean.SysRole;
import com.fenlan.spring.shop.bean.Type;
import com.fenlan.spring.shop.bean.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    UserDAO userDAO;
    @Autowired
    SysRoleDAO sysRoleDAO;
    @Autowired
    ShopDAO shopDAO;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        User user = userDAO.findByUsername(s);
        if (user == null) {
            throw new UsernameNotFoundException("don't have this username");
        }
        System.out.println("s:"+s);
        System.out.println("username:"+user.getUsername()+";password:"+user.getPassword());
        return user;
    }

    public User register(String username, String password, String telephone, String email, String address) throws Exception {
        if (null != userDAO.findByUsername(username))
            throw new Exception("username exist");
        else {
            User newUser = new User();
            SysRole role = sysRoleDAO.findByName("ROLE_USER");
            newUser.setUsername(username);
            newUser.setPassword(new BCryptPasswordEncoder().encode(password));
            newUser.setRoles(Arrays.asList(role));
            newUser.setAddress(address);
            newUser.setTelephone(telephone);
            newUser.setEmail(email);
            userDAO.save(newUser);
            return newUser;
        }
    }

    // shop 与 seller是一对一关系，因此直接查询shop 中的seller属性
    // 并不建议这么查询，非常依赖shop 与seller的一对一关系
    public List<User> list(int page, int size) throws Exception {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createTime"));
        List<Shop> shopList = shopDAO.findAll(pageable).getContent();
        List<User> list = new ArrayList<>(size);
        for (Shop shop : shopList)
            list.add(shop.getUser());
        if (list.size() == 0)
            throw new Exception("no result or page param is bigger than normal");
        else
            return list;
    }

    public User findByName(String username) {
        return userDAO.findByUsername(username);
    }

    public User findById(Long id) {
        return userDAO.findById(id).get();
    }

    public User findByNameAndRole(String username, String role) throws Exception {
        if (sysRoleDAO.existsByName(role))
            return checkRole(findByName(username), sysRoleDAO.findByName(role));
        else
            throw new Exception("system role don't contain " + role);
    }

    public User checkRole(User user, SysRole role) throws Exception {
        if (user.getRoles().contains(role))
            return user;
        else
            throw new Exception(user.getUsername() + " " + "is not " + role.getName());
    }

    public void delete(Long id) {
        User user = userDAO.findById(id).get();
        if (user.getRoles().contains(sysRoleDAO.findByName("ROLE_SELLER"))) {
            Shop shop = shopDAO.findByUser(user);
            shopDAO.deleteById(shop.getId());
        }
        user.setRoles(Arrays.asList(sysRoleDAO.findByName("ROLE_USER")));
        userDAO.save(user);
    }
}
