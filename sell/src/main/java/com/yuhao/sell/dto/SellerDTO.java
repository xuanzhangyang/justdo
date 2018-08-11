package com.yuhao.sell.dto;

import com.yuhao.sell.model.SellerInfo;
import lombok.Data;

import java.math.BigDecimal;

/**
 * SellerDTO
 *
 * @author CYH
 * @date 2018/4/19
 */
@Data
public class SellerDTO{


    private String name;

    private BigDecimal turnover;

    private String idCard;

    private String phone;

    public SellerDTO(SellerInfo sellerInfo, BigDecimal turnover) {
        this.name = sellerInfo.getName();
        this.idCard = sellerInfo.getIdCard();
        this.phone = sellerInfo.getPhone();
        this.turnover = turnover;
    }



}
