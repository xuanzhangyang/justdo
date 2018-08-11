package com.yuhao.sell.service.impl;


import com.yuhao.sell.dao.AdminDAO;
import com.yuhao.sell.model.Admin;
import com.yuhao.sell.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * SellerServiceImpl
 *
 * @author CYH
 * @date 2018/3/26
 */
@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private AdminDAO adminDAO;
    @Override
    public Admin findByUsernameAndPassword(String username, String password) {
//        System.out.println("username:"+username+"password:"+password);
        return adminDAO.findByUsernameAndPassword(username,password);
    }

    @Override
    public List<Admin> findAll(){
        return adminDAO.findAll();
    }

    @Override
    public Admin findByUsername(String username){
        return adminDAO.findByUsername(username);
    }
    @Override
    public Admin save(Admin admin){
        return  adminDAO.save(admin);
    }
}
