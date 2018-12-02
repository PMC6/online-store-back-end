package com.fenlan.spring.shop.DAO;

import com.fenlan.spring.shop.bean.SysRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SysRoleDAO extends JpaRepository<SysRole, Long> {

    SysRole findByName(String name);
    boolean existsByName(String name);
}
