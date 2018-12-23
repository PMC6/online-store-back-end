package com.fenlan.spring.shop.service;

import com.fenlan.spring.shop.DAO.*;
import com.fenlan.spring.shop.bean.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.text.ParseException;
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
        return userDAO.findById(user.getId()).get();
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

    public AdRequest addShopRequest(Double fee, String image) throws Exception {
        Shop shop = shopDAO.findByUser(authUser());
        if (null == shop)
            throw new Exception("not found this shop");
        AdRequest request = new AdRequest();
        request.setShop(shop);
        request.setFee(fee);
        request.setImage(image);
        request.setStatus(RequestStatus.PROCESS);
        return adRequestDAO.save(request);
    }

    public Advertisement process(Long id, Integer status) throws Exception {
        AdRequest request = adRequestDAO.findById(id).get();
        if (null == request)
            throw new Exception("not found this request");
        SysRole admin = sysRoleDAO.findByName("ROLE_ADMIN");
        if (!authUser().getRoles().contains(admin))
            throw new Exception("don't have permission");
        if (request.getStatus().equals(RequestStatus.PROCESS)) {
            switch (RequestStatus.getByCode(status)) {
                case REJECT: reject(request); return null;
                case APPROVE: return approve(request);
                default: throw new Exception("invalid param 'status");
            }
        } else
            throw new Exception("request is not in process");

    }

    public List<AdRequest> listOneDayRequest(Integer status, Integer page, Integer size) throws Exception {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createTime"));
        RequestStatus rs = RequestStatus.getByCode(status);
        if (null == rs)
            throw new Exception("param 'status' must be in ["
                    + RequestStatus.PROCESS.getCode() + ", "
                    + RequestStatus.APPROVE.getCode() + ", "
                    + RequestStatus.REJECT.getCode() + "]");
        SysRole admin = sysRoleDAO.findByName("ROLE_ADMIN");
        if (!authUser().getRoles().contains(admin))
            throw new Exception("don't have permission");
        return adRequestDAO
                .findByCreateTimeGreaterThanEqualAndStatusOrderByFeeDesc(pageable, today(), rs);
    }

    public Long amountOneDay() throws ParseException {
        return adRequestDAO.countByCreateTimeGreaterThanEqual(today());
    }

    public List<Advertisement> listProductTop() throws ParseException {
        return advertisementDAO
                .findByCreateTimeGreaterThanEqualAndProductNotNullOrderByFeeDesc(yesterday());
    }

    public List<Advertisement> listShopTop() throws ParseException {
        return advertisementDAO
                .findByCreateTimeGreaterThanEqualAndShopNotNullOrderByFeeDesc(yesterday());
    }

    private Advertisement approve(AdRequest request) throws Exception {
        Long amountOfProduct = advertisementDAO.countByCreateTimeGreaterThanEqualAndProductNotNull(today());
        Long amountOfShop = advertisementDAO.countByCreateTimeGreaterThanEqualAndShopNotNull(today());
        if (null != request.getProduct() && amountOfProduct >= 10)
            throw new Exception("product advertisement is filled");
        if (null != request.getShop() && amountOfShop >= 5)
            throw new Exception("shop advertisement is filled");
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

    private Date today() throws ParseException {
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String day = format.format(date).substring(0,10);
        Date today = format.parse(day + " 00:00:00");
        return today;
    }

    private Date yesterday() throws ParseException {
        Long timestamp = new Date().getTime() - 24*60*60*1000;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String yesterday = format.format(new Date(timestamp)).substring(0,10);
        Date date = format.parse(yesterday + " 00:00:00");
        return date;
    }
}
