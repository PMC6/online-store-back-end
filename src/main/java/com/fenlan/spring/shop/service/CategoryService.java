package com.fenlan.spring.shop.service;

import com.fenlan.spring.shop.DAO.CategoryDAO;
import com.fenlan.spring.shop.bean.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {
    @Autowired
    CategoryDAO categoryDAO;

    public List<Category> list() throws Exception {
        List<Category> list = categoryDAO.findAll();
        if (list.size() == 0)
            throw new Exception("no result");
        return list;
    }
}
