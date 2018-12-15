package com.fenlan.spring.shop.service;

import com.fenlan.spring.shop.DAO.BlackListDAO;
import com.fenlan.spring.shop.DAO.ShopDAO;
import com.fenlan.spring.shop.DAO.SysRoleDAO;
import com.fenlan.spring.shop.DAO.UserDAO;
import com.fenlan.spring.shop.bean.BlackList;
import com.fenlan.spring.shop.bean.Shop;
import com.fenlan.spring.shop.bean.Type;
import com.fenlan.spring.shop.bean.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BlackListService {
    @Autowired
    BlackListDAO blackListDAO;
    @Autowired
    UserDAO userDAO;
    @Autowired
    SysRoleDAO sysRoleDAO;
    @Autowired
    ShopDAO shopDAO;

    public void add(int type, long id) throws Exception {
        switch (type) {
            case 0 : addSeller(id);break;
            case 1 : addShop(id);break;
            case 2 : addCustomer(id);break;
            default: throw new Exception("type just in [0, 1, 3]");
        }
    }

    private void addSeller(long id) throws Exception {
        User user = userDAO.findById(id).get();
        if (user.getRoles().contains(sysRoleDAO.findByName("ROLE_SELLER"))) {
            BlackList one = new BlackList();
            one.setType(Type.SELLER.getCode());
            one.setEntityid(user.getId());
            blackListDAO.save(one);
        } else
            throw new Exception(id + " user is not ROLE_SELLER");
    }

    private void addShop(long id) throws Exception {
        Shop shop = shopDAO.findById(id).get();
        if (null != shop) {
            BlackList one = new BlackList();
            one.setType(Type.SHOP.getCode());
            one.setEntityid(shop.getId());
            blackListDAO.save(one);
        } else
            throw new Exception("shop " + id + " not found");
    }

    private void addCustomer(long id) throws Exception {
        User user = userDAO.findById(id).get();
        if (null != user) {
            BlackList one = new BlackList();
            one.setType(Type.CUSTOMER.getCode());
            one.setEntityid(user.getId());
            blackListDAO.save(one);
        } else
            throw new Exception("user " + id + " not found");
    }

    public long amountOfType(Integer type) throws Exception {
        if (null == type)
            return blackListDAO.count();
        Type amountType = Type.getByCode(type);
        if (null == amountType)
            throw new Exception("type must in [0, 1, 2]");
        else
            return blackListDAO.countByType(amountType.getCode());
    }

    public List<BlackList> list(Integer type, int page, int size) throws Exception {
        List<BlackList> lists;
        if (null == type)
            lists = blackListDAO.findAll();
        else {
            Type listType = Type.getByCode(type);
            if (null == listType)
                throw new Exception("type must in [0, 1, 2]");
            Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createTime"));
            lists = blackListDAO.findAllByType(pageable, listType.getCode());
        }
        return lists;
    }

    public void delete(Long id) throws Exception {
        BlackList one = blackListDAO.findById(id).get();
        if (null == one)
            throw new Exception("Not Fount this one in blackList");
        else
            blackListDAO.deleteById(id);
    }
}
