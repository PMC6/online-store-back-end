package com.fenlan.spring.shop.service;

import com.fenlan.spring.shop.DAO.SysRoleDAO;
import com.fenlan.spring.shop.DAO.UserDAO;
import com.fenlan.spring.shop.bean.SysRole;
import com.fenlan.spring.shop.bean.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    UserDAO userDAO;
    @Autowired
    SysRoleDAO sysRoleDAO;

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

    public User findByName(String username) {
        return userDAO.findByUsername(username);
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
}
