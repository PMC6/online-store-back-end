package com.fenlan.spring.shop.service;

import com.fenlan.spring.shop.DAO.RequestDAO;
import com.fenlan.spring.shop.DAO.ShopDAO;
import com.fenlan.spring.shop.DAO.SysRoleDAO;
import com.fenlan.spring.shop.DAO.UserDAO;
import com.fenlan.spring.shop.bean.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

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
    @Autowired
    ShopService shopService;

    public long numOfStatus(int status) throws Exception {
        switch (status) {
            case 0 : return requestDAO.countByStatus(RequestStatus.PROCESS);
            case 1 : return requestDAO.countByStatus(RequestStatus.APPROVE);
            case 2 : return requestDAO.countByStatus(RequestStatus.REJECT);
            default: throw new Exception("wrong status param");
        }
    }

    public List<Request> list(Integer status, int page, int size) throws Exception {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createTime"));
        List<Request> list;
        if (null == status)
            list = requestDAO.findAll(pageable).getContent();
        else
            list = requestDAO.findAllByStatus(RequestStatus.getByCode(status), pageable);
        if (list.size() == 0)
            throw new Exception("no result or page param is bigger than normal");
        else
            return list;
    }

    public Request add(Request request) throws Exception {
        try {
            if (null == request.getName())
                throw new Exception("missing 'name'");
            else if (null != shopDAO.findByName(request.getName()))
                throw new Exception("shop name is exist");
            User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            request.setStatus(RequestStatus.PROCESS);
            request.setUser(user);
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
            if (status == 1 && null == shopService.findByUserId(request.getUser().getId())) {
                Shop newShop = new Shop();
                newShop.setName(request.getName());
                newShop.setEmail(request.getEmail());
                newShop.setImage(request.getImage());
                newShop.setInfo(request.getInfo());
                newShop.setTelephone(request.getTelephone());
                newShop.setUser(request.getUser());
                newShop.setAlipay(request.getAlipay());
                shopService.add(newShop);
                return requestDAO.save(request);
            } else if (null != shopService.findByUserId(request.getUser().getId())) {
                throw new Exception("this person has a shop");
            } else
                return requestDAO.save(request);
        } else {
            if (id == null) {
                throw new Exception("missing id");
            } else {
                throw new Exception("missing status");
            }
        }

    }

    public void delete(Long id) throws Exception {
        Request request = requestDAO.findById(id).get();
        if (null != request)
            requestDAO.deleteById(id);
        else
            throw new Exception("don't have this request");
    }
}
