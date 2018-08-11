package com.yuhao.sell.dao;

import com.yuhao.sell.model.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.util.List;

/**
 * ProductCategoryDAO
 *
 * @author CYH
 * @date 2018/3/26
 */
public interface ProductCategoryDAO extends JpaRepository<ProductCategory,Integer>{


    /**
     * 通过类目查询
     * @param categoryTypeList
     * @return
     */
    public List<ProductCategory> findByCategoryTypeIn(List<Integer> categoryTypeList);


    /**
     * 通过sellerId查找
     * @param sellerId
     * @return
     */
    public List<ProductCategory> findBySellerId(String sellerId);

    @Transactional
    public Integer deleteByCategoryId(Integer id);

}
