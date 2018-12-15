package com.fenlan.spring.shop.service;

import com.fenlan.spring.shop.DAO.*;
import com.fenlan.spring.shop.bean.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class AdService {
    @Autowired
    AdRequestDAO adRequestDAO;
    @Autowired
    AdvertisementDAO advertisementDAO;
    @Autowired
    ProductDAO productDAO;
    @Autowired
    UserDAO userDAO;
    @Autowired
    ShopDAO shopDAO;
    @Autowired
    SysRoleDAO sysRoleDAO;

    private User authUser() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return user = userDAO.findById(user.getId()).get();
    }

    public AdRequest addProductRequest(Long productId, Double fee, String image) throws Exception {
        Product product = productDAO.findById(productId).get();
        if (null == product)
            throw new Exception("not found this product");
        boolean flag = product.getShop().getUser().getId().equals(authUser().getId());
        if (!flag)
            throw new Exception("don't have permission");
        AdRequest request = new AdRequest();
        request.setProduct(product);
        request.setFee(fee);
        request.setImage(image);
        request.setStatus(RequestStatus.PROCESS);
        return adRequestDAO.save(request);
    }

    public AdRequest addShopRequest(Long shopId, Double fee, String image) throws Exception {
        Shop shop = shopDAO.findById(shopId).get();
        if (null == shop)
            throw new Exception("not found this shop");
        boolean flag = shop.getUser().getId().equals(authUser().getId());
        if (!flag)
            throw new Exception("don't have permission");
        AdRequest request = new AdRequest();
        request.setShop(shop);
        request.setFee(fee);
        request.setImage(image);
        request.setStatus(RequestStatus.PROCESS);
        return adRequestDAO.save(request);
    }

    public Advertisement approval(Long id, Integer type) throws Exception {
        AdRequest request = adRequestDAO.findById(id).get();
        if (null == request)
            throw new Exception("not found this request");
        SysRole admin = sysRoleDAO.findByName("ROLE_ADMIN");
        if (!authUser().getRoles().contains(admin))
            throw new Exception("don't have permission");
        switch (type) {
            case 0: reject(request); return null;
            case 1: return approve(request);
            default: throw new Exception("invalid param 'type");
        }

    }

    public List<Advertisement> listOneDay() {
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String day = format.format(date).substring(0,10);
        return advertisementDAO.findByCreateTimeContaining(day);
    }

    private Advertisement approve(AdRequest request) {
        Advertisement ad = new Advertisement();
        ad.setProduct(request.getProduct());
        ad.setShop(request.getShop());
        ad.setImage(request.getImage());
        ad.setFee(request.getFee());
        request.setStatus(RequestStatus.APPROVE);
        adRequestDAO.save(request);
        return advertisementDAO.save(ad);
    }

    private void reject(AdRequest request) {
        request.setStatus(RequestStatus.REJECT);
        adRequestDAO.save(request);
    }
}
