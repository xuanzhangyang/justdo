package com.yuhao.sell.controller;

import ch.qos.logback.core.net.SyslogOutputStream;
import com.lly835.bestpay.rest.type.Get;
import com.yuhao.sell.config.ProjectUrlConfig;
import com.yuhao.sell.dto.OrderDTO;
import com.yuhao.sell.enums.OrderStatusEnum;
import com.yuhao.sell.enums.PayStatusEnum;
import com.yuhao.sell.enums.ResultEnum;
import com.yuhao.sell.exception.SellException;
import com.yuhao.sell.model.OrderMaster;
import com.yuhao.sell.model.SellerInfo;
import com.yuhao.sell.model.TransData;
import com.yuhao.sell.service.OrderMasterService;
import com.yuhao.sell.service.SellerService;
import com.yuhao.sell.utils.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.querydsl.QPageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;

/**
 * SellOrderController
 *
 * @author CYH
 * @date 2018/3/30
 */
@Controller
@RequestMapping("/seller/order")
@Slf4j
public class SellerOrderController {

    @Autowired
    private OrderMasterService orderService;
    @Autowired
    private ProjectUrlConfig projectUrlConfig;

    @Autowired
    private SecurityUtils securityUtils;

    @Autowired
    private SellerService sellerService;

    @GetMapping("/list")
    public ModelAndView list(@RequestParam(value = "page",defaultValue = "1")Integer page,
                             @RequestParam(value = "size",defaultValue = "10")Integer size,
                             @RequestParam(value = "OrderId",defaultValue = "0")String OrderId,
                             Map<String,Object> map){
        System.out.println( OrderId );
        SellerInfo sellerInfo=securityUtils.getCurrentSeller();
        if (sellerInfo==null) {
            map.put("msg", "请重新登陆");
            map.put("url", "/sell/seller/order/list");
            return new ModelAndView("common/error", map);
        }
        Sort sort = new Sort(Sort.Direction.DESC,"createTime");

        PageRequest request=new PageRequest(page-1,size,sort);

        List<OrderMaster> orders = orderService.findAll(sort);
         int width = (orders.size()/size)+1;
        List<Integer> widths = new ArrayList<>();
        for (int i=1;i<=width;i++){
            widths.add(i);
        }

        for (OrderMaster order:orders) {
            OrderDTO orderDTO = orderService.findOne(order.getOrderId());
            if(orderDTO.getOrderStatus()==0)
            orderService.finish(orderDTO);
        }
/**
 * 判断是否打印
 */
        if (!OrderId.equals("0")) {
            System.out.println( OrderId );
             OrderDTO orderDTO = orderService.findOne(OrderId);
             orderDTO.setIsprint(1);
             orderService.finish(orderDTO);
         }

        Page<OrderDTO> orderDTOPage=orderService.findList(request,PayStatusEnum.SUCCESS.getCode(),sellerInfo.getSellerId());

        map.put("orderDTOPage",orderDTOPage);
        map.put("widths",widths);
        map.put("currentPage", page);
        map.put("width",width);
        map.put("size", size);
        map.put("socket",projectUrlConfig.getSocket());

        return new ModelAndView("order/unreceivedOrder",map);
    }
//    @GetMapping("/flash")
//    pub
//    public Page<OrderDTO> flash(){
//
//    }
//    @GetMapping("/rlist")
//    public ModelAndView rlist(@RequestParam(value = "page",defaultValue = "1")Integer page,
//                             @RequestParam(value = "size",defaultValue = "10")Integer size,
//                             Map<String,Object> map){
//        SellerInfo sellerInfo=securityUtils.getCurrentSeller();
//        if (sellerInfo==null){
//            map.put("msg","请重新登陆");
//            map.put("url", "/sell/seller/order/list");
//            return new ModelAndView("common/error", map);
//        }
//
//        PageRequest request=new PageRequest(page-1,size);
//        Page<OrderDTO> orderDTOPage=orderService.findList(request, OrderStatusEnum.FINISHED.getCode(),sellerInfo.getSellerId());
//
//        map.put("orderDTOPage",orderDTOPage);
//        map.put("currentPage", page);
//        map.put("size", size);
//        map.put("socket",projectUrlConfig.getSocket());
//
//        return new ModelAndView("order/receivedOrder",map);
//    }

//    @GetMapping("/ulist")
//    public ModelAndView ulist(@RequestParam(value = "page",defaultValue = "1")Integer page,
//                              @RequestParam(value = "size",defaultValue = "10")Integer size,
//                              Map<String,Object> map){
//
//        SellerInfo sellerInfo=securityUtils.getCurrentSeller();
//        if (sellerInfo==null){
//            map.put("msg","请重新登陆");
//            map.put("url", "/sell/seller/onSellerLogin");
//            return new ModelAndView("common/error", map);
//        }
//
//        PageRequest request=new PageRequest(page-1,size);
//        Page<OrderDTO> orderDTOPage=orderService.findList(request, OrderStatusEnum.NEW.getCode(),sellerInfo.getSellerId());
//
//        map.put("orderDTOPage",orderDTOPage);
//        map.put("currentPage", page);
//        map.put("size", size);
//        map.put("socket",projectUrlConfig.getSocket());
//
//        return new ModelAndView("order/unreceivedOrder",map);
//    }


//    @GetMapping("/cancel")
//    public ModelAndView cancel(@RequestParam("orderId")String orderId,
//                               Map<String,Object> map){
//
//        try {
//            OrderDTO orderDTO = orderService.findOne(orderId);
//            orderService.cancel(orderDTO);
//        } catch (Exception e) {
//            log.error("【卖家端取消订单】发生异常{}", e);
//            map.put("msg", e.getMessage());
//            map.put("url", "/sell/seller/order/ulist");
//            return new ModelAndView("common/error", map);
//        }
//
//        map.put("msg", ResultEnum.ORDER_CANCEL_SUCCESS.getMessage());
//        map.put("url", "/sell/seller/order/ulist");
//        return new ModelAndView("common/success");
//    }



    @ResponseBody
    @GetMapping("/GetData")
    public TransData GetData() {
        TransData Data = new TransData();
        SellerInfo sellerInfo=securityUtils.getCurrentSeller();
        List<OrderMaster> orderMasters = orderService.findNotIsPrint( sellerInfo.getSellerId(),0);
        System.out.println( orderMasters.size() );
        //有未打印的订单  返回 false
        if (orderMasters.size()!=0){
            Data.setFlag( false );
            Data.setList( orderMasters );
            System.out.println( Data );
            return Data;
            //无未打印的订单 返回true
        }else {
            Data.setFlag( true );
            Data.setList( orderMasters );
            System.out.println( Data );
            return Data;
        }
        //返回订单是否有未打印   与所有订单list。


    }
    /**
     * 订单详情
     * @param orderId
     * @return
     */

    @ResponseBody
    @GetMapping("/detail")
    public OrderDTO detail(@RequestParam("orderId") String orderId) {
        OrderDTO orderDTO = new OrderDTO();
        try {
            orderDTO = orderService.findOne(orderId);
        }catch (SellException e) {
            log.error("【卖家端查询订单详情】发生异常{}", e);
            return orderDTO;
        }
        return orderDTO;
    }
    @GetMapping("/sellerDetail")
    public ModelAndView sellerDetail(Map<String, Object> map) {
        SellerInfo sellerInfo=securityUtils.getCurrentSeller();
        if (sellerInfo==null){
            map.put("msg","请重新登陆");
            map.put("url", "/sell/seller/order/list");
            return new ModelAndView("common/error", map);
        }
        map.put("sellerInfo", sellerInfo);
        return new ModelAndView("order/seller_detail", map);
    }
    /**
     * 完结订单
     * @param orderId
     * @param map
     * @return
     */
//    @GetMapping("/finish")
//    public ModelAndView finished(@RequestParam("orderId") String orderId,
//                                 Map<String, Object> map) {
//        try {
//            OrderDTO orderDTO = orderService.findOne(orderId);
//            orderService.finish(orderDTO);
//        } catch (SellException e) {
//            log.error("【卖家端完结订单】发生异常{}", e);
//            map.put("msg", e.getMessage());
//            map.put("url", "/sell/seller/order/ulist");
//            return new ModelAndView("common/error", map);
//        }
//
//        map.put("msg", ResultEnum.ORDER_FINISH_SUCCESS.getMessage());
//        map.put("url", "/sell/seller/order/ulist");
//        return new ModelAndView("common/success");
//    }




}
