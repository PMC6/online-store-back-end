package com.fenlan.spring.shop.controller.admin;

import com.fenlan.spring.shop.bean.*;
import com.fenlan.spring.shop.service.BlackListService;
import com.fenlan.spring.shop.service.RequestService;
import com.fenlan.spring.shop.service.ShopService;
import com.fenlan.spring.shop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin/seller")
public class ManageSellerController {
    @Autowired
    RequestService requestService;
    @Autowired
    private HttpServletRequest request;
    @Autowired
    UserService userService;
    @Autowired
    ShopService shopService;
    @Autowired
    BlackListService blackListService;

    @GetMapping("/list")
    public ResponseEntity<Object> list(@RequestParam("page") Integer page,
                                       @RequestParam("size") Integer size) {
        try {
            List<User> list = userService.list(page, size);
            return new ResponseEntity<>(new ResponseFormat.Builder(new Date(), HttpStatus.OK.value())
                    .error(null)
                    .message("list user success")
                    .path(request.getServletPath())
                    .data(list)
                    .build(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseFormat.Builder(new Date(), HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .error("Not found")
                    .message(e.getLocalizedMessage())
                    .path(request.getServletPath())
                    .data(null)
                    .build(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/search/sellername")
    public ResponseEntity<Object> searchBySellerame(@RequestParam("username") String sellerName) {
        try {
            User seller = userService.findByNameAndRole(sellerName, "ROLE_SELLER");
            // Shop shop = shopService.findByUserId(seller.getId());
            return new ResponseEntity<>(new ResponseFormat.Builder(new Date(), HttpStatus.OK.value())
                    .error(null)
                    .message("search success")
                    .path(request.getServletPath())
                    .data(seller)
                    .build(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseFormat.Builder(new Date(), HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .error("Not found")
                    .message(e.getLocalizedMessage())
                    .path(request.getServletPath())
                    .data(null)
                    .build(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/search/shopname")
    public ResponseEntity<Object> searchByShopName(@RequestParam("shopname") String shopName) {
        try {
            Shop shop = shopService.findByName(shopName);
            return new ResponseEntity<>(new ResponseFormat.Builder(new Date(), HttpStatus.OK.value())
                    .error(null)
                    .message("search success")
                    .path(request.getServletPath())
                    .data(shop)
                    .build(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseFormat.Builder(new Date(), HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .error("Not found")
                    .message(e.getLocalizedMessage())
                    .path(request.getServletPath())
                    .data(null)
                    .build(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/request/amount")
    public ResponseEntity<Object> numOfStatus(@RequestParam("status") Integer status) {
        try {
            long amount = requestService.numOfStatus(status);
            return new ResponseEntity<>(new ResponseFormat.Builder(new Date(), HttpStatus.OK.value())
                    .error(null)
                    .message("list request")
                    .path(request.getServletPath())
                    .data(amount)
                    .build(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseFormat.Builder(new Date(), HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .error("Param Error")
                    .message(e.getLocalizedMessage())
                    .path(request.getServletPath())
                    .data(null)
                    .build(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/request/list")
    public ResponseEntity<Object> listRequest(@RequestParam(value = "status", required = false) Integer status,
                                              @RequestParam("page") Integer page,
                                              @RequestParam("size") Integer size) {
        try {
            return new ResponseEntity<>(new ResponseFormat.Builder(new Date(), HttpStatus.OK.value())
                    .error(null)
                    .message("list request")
                    .path(request.getServletPath())
                    .data(requestService.list(status, page, size))
                    .build(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseFormat.Builder(new Date(), HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .error("List error")
                    .message(e.getLocalizedMessage())
                    .path(request.getServletPath())
                    .data(null)
                    .build(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/shop/amount")
    public ResponseEntity<Object> numOfShop() {
        return new ResponseEntity<>(new ResponseFormat.Builder(new Date(), HttpStatus.OK.value())
                .error(null)
                .message("list shop")
                .path(request.getServletPath())
                .data(shopService.amount())
                .build(), HttpStatus.OK);
    }

    @GetMapping("/shop/list")
    public ResponseEntity<Object> listShop(@RequestParam("page") Integer page,
                                           @RequestParam("size") Integer size) {
        try {
            return new ResponseEntity<>(new ResponseFormat.Builder(new Date(), HttpStatus.OK.value())
                    .error(null)
                    .message("list shop")
                    .path(request.getServletPath())
                    .data(shopService.list(page, size))
                    .build(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseFormat.Builder(new Date(), HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .error("List error")
                    .message(e.getLocalizedMessage())
                    .path(request.getServletPath())
                    .data(null)
                    .build(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/shop/delete")
    public ResponseEntity<Object> deleteShop(@RequestParam("id") Long id) {
        try {
            shopService.delete(id);
            return new ResponseEntity<>(new ResponseFormat.Builder(new Date(), HttpStatus.OK.value())
                    .error(null)
                    .message("delete shop success")
                    .path(request.getServletPath())
                    .data(null)
                    .build(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseFormat.Builder(new Date(), HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .error("Delete failed")
                    .message(e.getLocalizedMessage())
                    .path(request.getServletPath())
                    .data(null)
                    .build(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @DeleteMapping("/delete")
    public ResponseEntity<Object> deleteSeller(@RequestParam("id") Long id) {
        userService.delete(id);
        return new ResponseEntity<>(new ResponseFormat.Builder(new Date(), HttpStatus.OK.value())
                .error(null)
                .message("delete seller success")
                .path(request.getServletPath())
                .data(null)
                .build(), HttpStatus.OK);
    }


    @PutMapping(value = "/request/deal")
    public ResponseEntity<Object> dealRequest(@RequestBody Map<String, Object> map) {
        Long requestId = Long.parseLong(map.get("id").toString());
        Integer status = Integer.parseInt(map.get("status").toString());
        try {
            return new ResponseEntity<>(new ResponseFormat.Builder(new Date(), HttpStatus.OK.value())
                    .error(null)
                    .message("registration dealed")
                    .path(request.getServletPath())
                    .data(requestService.update(requestId, status))
                    .build(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseFormat.Builder(new Date(), HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .error("Parameter error")
                    .message(e.getLocalizedMessage())
                    .path(request.getServletPath())
                    .data(null)
                    .build(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/blacklist/add")
    public ResponseEntity<Object> addEntity(@RequestBody Map map) {
        Integer type = Integer.parseInt(map.get("type").toString());
        Long id = Long.parseLong(map.get("id").toString());
        try {
            blackListService.add(type, id);
            return new ResponseEntity<>(new ResponseFormat.Builder(new Date(), HttpStatus.OK.value())
                    .error(null)
                    .message("add one entity in blacklist")
                    .path(request.getServletPath())
                    .data(null)
                    .build(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseFormat.Builder(new Date(), HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .error("Parameter error")
                    .message(e.getLocalizedMessage())
                    .path(request.getServletPath())
                    .data(null)
                    .build(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/blacklist/list")
    public ResponseEntity<Object> listBlackList(@RequestParam(value = "type", required = false) Integer type,
                                                @RequestParam("page") Integer page,
                                                @RequestParam("size") Integer size) {
        try {
            List<BlackList> items = blackListService.list(type, page, size);
            List<Object> list = new ArrayList<>();
            if (null != Type.getByCode(type)) {
                switch (Type.getByCode(type)) {
                    case SHOP: {
                        for (BlackList item : items)
                            list.add(shopService.finById(item.getEntityid()));
                    }
                    break;
                    case SELLER:
                    case CUSTOMER: {
                        for (BlackList item : items)
                            list.add(userService.findById(item.getEntityid()));
                    }
                    break;
                }
            }
            return new ResponseEntity<>(new ResponseFormat.Builder(new Date(), HttpStatus.OK.value())
                    .error(null)
                    .message("list blacklist")
                    .path(request.getServletPath())
                    .data(list)
                    .build(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseFormat.Builder(new Date(), HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .error("Parameter error")
                    .message(e.getLocalizedMessage())
                    .path(request.getServletPath())
                    .data(null)
                    .build(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/blacklist/amount")
    public ResponseEntity<Object> amountBlackList(@RequestParam(value = "type", required = false) Integer type) {
        try {
            Long amount = blackListService.amountOfType(type);
            return new ResponseEntity<>(new ResponseFormat.Builder(new Date(), HttpStatus.OK.value())
                    .error(null)
                    .message("list blacklist")
                    .path(request.getServletPath())
                    .data(amount)
                    .build(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseFormat.Builder(new Date(), HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .error("Parameter error")
                    .message(e.getLocalizedMessage())
                    .path(request.getServletPath())
                    .data(null)
                    .build(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
