package com.yuhao.sell.service;

import com.yuhao.sell.dto.OrderDTO;
import com.yuhao.sell.model.OrderMaster;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * OrderMasterService
 *
 * @author CYH
 * @date 2018/3/26
 */
public interface OrderMasterService {

    /**
     * 结束订单
     * @param orderMaster
     * @return
     */
    public OrderMaster endOrder(OrderMaster orderMaster);


    /**
     * 创建订单
     * @param orderDTO
     * @return
     */
    public OrderDTO create(OrderDTO orderDTO);

    /**
     * 查询单个订单
     * @param orderId
     * @return
     */
    public OrderDTO findOne(String orderId);

    /**
     * 查询订单列表
     * @param buyerOpenid
     * @param pageable
     * @return
     */
    public Page<OrderDTO> findList(String buyerOpenid, Pageable pageable);
    /**
     *添加备注
     */
//    public OrderDTO addDesc(OrderDTO orderDTO,String);

    /**
     * 查询订单列表
     * @param buyerOpenid
     * @param pageable
     * @param sellerId
     * @return
     */
    public Page<OrderDTO> findList(String buyerOpenid, Pageable pageable,String sellerId);


    /**
     * 取消订单
     * @param orderDTO
     * @return
     */
    public OrderDTO cancel(OrderDTO orderDTO);

    /**
     * 完结订单
     * @param orderDTO
     * @return
     */
    public OrderDTO finish(OrderDTO orderDTO);

    /**
     * 支付订单
     * @param orderDTO
     * @return
     */
    public OrderDTO paid(OrderDTO orderDTO);

    /**
     * 查询订单列表
     * @param pageable
     * @return
     */
    public Page<OrderDTO> findList(Pageable pageable);

    /**
     * 查询订单列表
     * @param pageable
     * @param sellerId
     * @return
     */
    public Page<OrderDTO> findList(Pageable pageable,String sellerId);

    /**
     * 通过状态查询
     * @param pageable
     * @param orderStatus
     * @param sellerId
     * @return
     */
    public Page<OrderDTO> findList(Pageable pageable,Integer orderStatus, String sellerId);

    /**
     * 通过时间和状态查看订单
     * @param sellerId
     * @param orderStatus
     * @param createTime
     * @param pageable
     * @return
     */
    public Page<OrderDTO> findBySellerIdAndOrderStatusAndCreateTimeAfter(String sellerId, Integer orderStatus, Date createTime, Pageable pageable);


    public BigDecimal sum(String sellerId, Integer orderStatus,Date createTime);


    /**
     * 查找需要结账的订单
     * @param sellerId
     * @param orderStatus
     * @param payStatus
     * @param isEnd
     * @return
     */
    public List<OrderMaster> findBySellerIdAndOrderStatusAndPayStatusAndIsEnd(String sellerId,Integer orderStatus,Integer payStatus,Integer isEnd);


//    public  List<OrderMaster> findByIsPrint(Integer status);

    public List<OrderMaster> findNotIsPrint(String sellerId, Integer isprint);

    public List<OrderMaster> findAll();

    List<OrderMaster> findAll(Sort sort);
}
