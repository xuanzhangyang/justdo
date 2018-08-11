package com.yuhao.sell.service;


import com.yuhao.sell.model.Admin;

import java.util.List;

/**
 * AdminService
 *
 * @author CYH
 * @date 2018/3/26
 */
public interface AdminService {

    /**
     * 查询用户
     * @param username
     * @param password
     * @return
     */
    public Admin findByUsernameAndPassword(String username, String password);

    public Admin findByUsername(String username);

    public Admin save(Admin admin);

    public List<Admin> findAll();

}
