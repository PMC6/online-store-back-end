/**
 * @author： fanzhonghao
 * @date: 18-12-31 15 00
 * @version: 1.0
 * @description:
 */
package com.fenlan.spring.shop.controller.customer;

import com.fenlan.spring.shop.bean.ResponseFormat;
import com.fenlan.spring.shop.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Map;

@RestController
@RequestMapping("/customer/comment")
public class CommentController {
    @Autowired
    CommentService commentService;
    @Autowired
    private HttpServletRequest request;

    /**
     * @param map 包含orderId，grade(评分0-5)，comment
     * @return
     */
    @PostMapping("/add")
    public ResponseEntity<ResponseFormat> commentProduct(@RequestBody Map map){
        Long orderId = Long.parseLong(map.get("orderId").toString());
        int grade = (int) map.get("grade");
        String comment = map.get("comment").toString();
        try {
            return new ResponseEntity<>(new ResponseFormat.Builder(new Date(), HttpStatus.OK.value())
                    .error(null)
                    .message("add comment success")
                    .path(request.getServletPath())
                    .data(commentService.add(orderId, grade, comment))
                    .build(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseFormat.Builder(new Date(), HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .error("add comment failed")
                    .message(e.getLocalizedMessage())
                    .path(request.getServletPath())
                    .data(null)
                    .build(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 查看商品评论
     * @param productId
     * @param page
     * @param size
     * @return
     */
    @GetMapping("/list")
    public ResponseEntity<ResponseFormat> listByProductId(Long productId, int page, int size){
        try {
            return new ResponseEntity<>(new ResponseFormat.Builder(new Date(), HttpStatus.OK.value())
                    .error(null)
                    .message("query success")
                    .path(request.getServletPath())
                    .data(commentService.listByProductId(page, size, productId))
                    .build(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseFormat.Builder(new Date(), HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .error("query failed")
                    .message(e.getLocalizedMessage())
                    .path(request.getServletPath())
                    .data(null)
                    .build(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/del")
    public ResponseEntity<ResponseFormat> delComment(@RequestParam("commentId") Long commentId){
        try {
            return new ResponseEntity<>(new ResponseFormat.Builder(new Date(), HttpStatus.OK.value())
                    .error(null)
                    .message("del success")
                    .path(request.getServletPath())
                    .data(commentService.del(commentId))
                    .build(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseFormat.Builder(new Date(), HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .error("del failed")
                    .message(e.getLocalizedMessage())
                    .path(request.getServletPath())
                    .data(null)
                    .build(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
