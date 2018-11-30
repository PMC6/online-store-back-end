package com.fenlan.spring.shop.service;

import com.fenlan.spring.shop.DAO.RequestDAO;
import com.fenlan.spring.shop.DAO.ShopDAO;
import com.fenlan.spring.shop.DAO.SysRoleDAO;
import com.fenlan.spring.shop.DAO.UserDAO;
import com.fenlan.spring.shop.bean.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RequestService {
    @Autowired
    RequestDAO requestDAO;
    @Autowired
    ShopDAO shopDAO;
    @Autowired
    UserDAO userDAO;
    @Autowired
    SysRoleDAO sysRoleDAO;

    public List<Request> list() throws Exception {
        List<Request> notDeal = requestDAO.findAllByStatus(RequestStatus.PROCESS);
        if (notDeal.size() == 0)
            throw new Exception("没有未处理的申请");
        else
            return notDeal;
    }

    public Request add(Request request) throws Exception {
        try {
            if (null == request.getShopName())
                throw new Exception("missing 'shopName'");
            else if (null != shopDAO.findByName(request.getShopName()))
                throw new Exception("店铺已存在");
            User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            request.setStatus(RequestStatus.PROCESS);
            request.setUserId(user.getId());
            return requestDAO.save(request);
        } catch (Exception e) {
            throw e;
        }
    }

    public Request update(Long id, Integer status) throws Exception {
        if (id != null && status != null) {
            Request request = requestDAO.getOne(id);
            request.setStatus(RequestStatus.values()[status]);
            // 批准申请
            if (status == 1 && null == shopDAO.findByName(request.getShopName())) {
                Shop newShop = new Shop();
                newShop.setName(request.getShopName());
                newShop.setEmail(request.getEmail());
                newShop.setImage(request.getImage());
                newShop.setInfo(request.getInfo());
                newShop.setTelephone(request.getTelephone());
                newShop.setUserId(request.getUserId());
                shopDAO.save(newShop);

                try {
                    User user = userDAO.findById(request.getUserId()).get();
                    List<SysRole> roles = new ArrayList<>();
                    roles.add(sysRoleDAO.findByName("ROLE_USER"));
                    roles.add(sysRoleDAO.findByName("ROLE_SELLER"));
                    user.setRoles(roles);
                    userDAO.save(user);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                throw new Exception("店铺已存在");
            }
            return requestDAO.save(request);
        } else {
            if (id == null) {
                throw new Exception("missing id");
            } else {
                throw new Exception("missing status");
            }
        }

    }
}
