/**
 * @author： fanzhonghao
 * @date: 18-12-31 15 03
 * @version: 1.0
 * @description:
 */
package com.fenlan.spring.shop.DAO;


import com.fenlan.spring.shop.bean.Comment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentDAO extends JpaRepository<Comment, Long> {
    List<Comment> findAllByProductId(Pageable pageable, Long productId);
}
