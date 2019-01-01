/**
 * @author： fanzhonghao
 * @date: 18-12-31 15 08
 * @version: 1.0
 * @description:
 */
package com.fenlan.spring.shop.service;

import com.fenlan.spring.shop.DAO.*;
import com.fenlan.spring.shop.bean.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {
    @Autowired
    CommentDAO commentDAO;
    @Autowired
    OrderDAO orderDAO;
    @Autowired
    UserDAO userDAO;
    @Autowired
    ShopDAO shopDAO;
    @Autowired
    ProductDAO productDAO;

    private User authUser() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userDAO.findById(user.getId()).get();
    }

    /**
     * 通过productId得到商品相关评论
     * @param productId
     * @param page
     * @param size
     * @return
     */
    public List<Comment> listByProductId(int page, int size, Long productId) throws Exception{
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createTime"));
        List<Comment> comments = commentDAO.findAllByProductId(pageable, productId);
        if (comments.size() == 0) throw new Exception("no result with this page");
        return comments;
    }

    /**
     * 添加评论
     * @param orderId
     * @param grade 评分
     * @param comment 评论
     * @return
     * @throws Exception
     */
    public boolean add(Long orderId, int grade, String comment) throws Exception{
        Order order = orderDAO.findById(orderId).get();
        User user = authUser();
        if (!order.getUserId().equals(user.getId())) throw new Exception("this order is not yours");
        if (!order.getStatus().equals("complete")) throw new Exception("this order has not been completed");
        Shop shop = shopDAO.findById(order.getShopId()).get();
        Product product = productDAO.findById(order.getProductId()).get();
        try {
            Comment comment1 = new Comment();
            comment1.setShopId(shop.getId());
            comment1.setShopName(shop.getName());
            comment1.setShopImage(shop.getImage());
            comment1.setProductId(product.getId());
            comment1.setProductComment(comment);
            comment1.setProductImage(product.getImage());
            comment1.setProductName(product.getName());
            comment1.setUserId(user.getId());
            comment1.setUserName(user.getUsername());
            comment1.setGrade(grade);
            commentDAO.save(comment1);
            return true;
        }catch (Exception e){
            throw new Exception(e.getLocalizedMessage());
        }
    }

    /**
     * 删除评论
     * @return
     */
    public boolean del(Long commentId) throws Exception{
        try {
            Comment comment = commentDAO.findById(commentId).get();
            commentDAO.delete(comment);
            return true;
        }catch (Exception e){
            throw new Exception("can't delete this comment");
        }
    }

}
