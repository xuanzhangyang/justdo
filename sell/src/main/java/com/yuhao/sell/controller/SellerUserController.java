package com.yuhao.sell.controller;

import com.yuhao.sell.config.ProjectUrlConfig;
import com.yuhao.sell.constant.CookieConstant;
import com.yuhao.sell.constant.RedisConstant;
import com.yuhao.sell.enums.ResultEnum;
import com.yuhao.sell.enums.SellerStatusEnum;
import com.yuhao.sell.model.SellerInfo;
import com.yuhao.sell.service.SellerService;
import com.yuhao.sell.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Date;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * SellerUserController
 *
 * @author CYH
 * @date 2018/3/30
 */
@Controller
@RequestMapping("/seller")
public class SellerUserController {

    @Autowired
    private SellerService sellerService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private ProjectUrlConfig projectUrlConfig;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private SecurityUtils securityUtils;


    @GetMapping("/onSellerRegister")
    public ModelAndView register(){

        return new ModelAndView("admin/register");
    }

    @GetMapping("/onSellerLogin")
    public ModelAndView onLogin(){

        return new ModelAndView("order/login_1");
    }



    @GetMapping("/banner")
    public ModelAndView banner(){
        return new ModelAndView("order/banner");
    }


    /**
     *用户名密码登陆
     * @return
     */
    @PostMapping("/sellerLogin")
    public ModelAndView adminLogin(@RequestParam("username")String username,
                                   @RequestParam("password")String password,
                                   HttpServletResponse response,
                                   Map<String, Object> map){

        if (StringUtils.isEmpty(username)){
            map.put("msg", ResultEnum.NULL_USERNAME.getMessage());
            map.put("url", "/sell/seller/order/list");
            return new ModelAndView("common/error");
        }
        if (StringUtils.isEmpty(password)){
            map.put("msg", ResultEnum.NULL_PASSWORD.getMessage());
            map.put("url", "/sell/seller/order/list");
            return new ModelAndView("common/error");
        }

        SellerInfo sellerInfo=sellerService.findSellerInfoByPasswordAndUsername(passwordEncoder.encodePassword(password,username),username);

        if (sellerInfo==null){
            map.put("msg", ResultEnum.USERNAME_PASSWORD_ERROR.getMessage());
            map.put("url", "/sell/seller/onSellerLogin");
            return new ModelAndView("common/error");
        }


        //进行用户使用权限判断
        if(sellerInfo.getAudit()==0){
            if (sellerInfo.getTrial()==null||sellerInfo.getTrialTime()==null){
                map.put("msg", "未开启试用,请联系管理员");
                map.put("url", "/sell/seller/onSellerLogin");
                return new ModelAndView("common/error");
            }else if(sellerInfo.getTrialTime().getTime()<System.currentTimeMillis()){
                map.put("msg", "试用到期,请联系管理员");
                map.put("url", "/sell/seller/onSellerLogin");
                return new ModelAndView("common/error");
            }
        }else {
            if (sellerInfo.getTrialTime().getTime()<System.currentTimeMillis()){
                map.put("msg", "使用到期,请联系管理员");
                map.put("url", "/sell/seller/onSellerLogin");
                return new ModelAndView("common/error");
            }

        }
        //2. 设置token至redis
        //String token = UUID.randomUUID().toString();
        Integer expire = RedisConstant.EXPIRE;

        redisTemplate.opsForValue().set(String.format(RedisConstant.TOKEN_PREFIX, sellerInfo.getKey()), sellerInfo.getKey(), expire, TimeUnit.SECONDS);

        //3. 设置token至cookie
        CookieUtil.set(response, CookieConstant.TOKEN, sellerInfo.getKey(), expire);


        return new ModelAndView("redirect:" + projectUrlConfig.getSell() + "/sell/seller/order/list");

    }

    /**
     * 微信登陆
     * @param openid
     * @param response
     * @param map
     * @return ModelAndView
     */
    @GetMapping("/login")
    public ModelAndView login(@RequestParam("openid") String openid,
                              HttpServletResponse response,
                              Map<String, Object> map) {

        //1. openid去和数据库里的数据匹配
        SellerInfo sellerInfo = sellerService.findSellerInfoByOpenid(openid);
        if (sellerInfo == null) {
            map.put("msg", ResultEnum.LOGIN_FAIL.getMessage());
            map.put("url", "/sell/seller/order/list");
            return new ModelAndView("common/error");
        }

        //2. 设置token至redis
        String token = UUID.randomUUID().toString();
        Integer expire = RedisConstant.EXPIRE;

        redisTemplate.opsForValue().set(String.format(RedisConstant.TOKEN_PREFIX, token), openid, expire, TimeUnit.SECONDS);

        //3. 设置token至cookie
        CookieUtil.set(response, CookieConstant.TOKEN, token, expire);

        return new ModelAndView("redirect:" + projectUrlConfig.getSell() + "/sell/seller/order/list");

    }

    @GetMapping("/logout")
    public ModelAndView logout(HttpServletRequest request,
                               HttpServletResponse response,
                               Map<String, Object> map) {
        //1. 从cookie里查询
        Cookie cookie = CookieUtil.get(request, CookieConstant.TOKEN);
        if (cookie != null) {
            //2. 清除redis
            redisTemplate.opsForValue().getOperations().delete(String.format(RedisConstant.TOKEN_PREFIX, cookie.getValue()));

            //3. 清除cookie
            CookieUtil.set(response, CookieConstant.TOKEN, null, 0);

        }

        map.put("msg", ResultEnum.LOGOUT_SUCCESS.getMessage());
        map.put("url", "/sell/seller/onSellerLogin");
        return new ModelAndView("common/success", map);
    }

    @GetMapping("/detail")
    public ModelAndView detail(Map<String, Object> map) {

        SellerInfo sellerInfo=securityUtils.getCurrentSeller();
        if (sellerInfo==null){
            map.put("msg","请重新登陆");
            map.put("url", "/sell/seller/onSellerLogin");
            return new ModelAndView("common/error", map);
        }

        map.put("sellerInfo", sellerInfo);
        return new ModelAndView("order/detail1", map);
    }
    @PostMapping("/save")
    public ModelAndView save(@RequestParam("sellerName")String sellerName,@RequestParam("sellerDname")String sellerDname,
                                @RequestParam("sellerIdcard")String sellerIdcard,
                             @RequestParam("sellerSecret")String sellerSecret,
                             @RequestParam("point")float point, @RequestParam("sellerid")String sellerid, Map<String, Object> map){
//        SellerInfo sellerInfo=securityUtils.getCurrentSeller();
        SellerInfo sellerInfo = sellerService.findOne(sellerid);
        if(sellerInfo==null){
//            map.put("url", "/sell/seller/onSellerRegister");
            map.put("msg", "修改失败");
            map.put("url", "/sell/admin/list");
            return new ModelAndView("common/error");
        }else {
            sellerInfo.setUsername(sellerName);
            sellerInfo.setName(sellerDname);
//            sellerInfo.setBusinessLicense(sellerLicense);
            sellerInfo.setIdCard(sellerIdcard);
            sellerInfo.setPoint(point);
            sellerInfo.setKey(sellerSecret);
            sellerService.save(sellerInfo);

            map.put("msg", "修改成功");
            map.put("url", "/sell/admin/list");
            return new ModelAndView("common/success",map);

        }
//        if (bindingResult.hasErrors()) {
//            map.put("msg", bindingResult.getFieldError().getDefaultMessage());
//            map.put("url", "/sell/seller/onSellerRegister");
//            return new ModelAndView("common/error", map);
//        }
//
//        if (sellerService.isRepeat(sellerInfo.getUsername(),sellerInfo.getPhone())){
//            map.put("msg", "手机号或者用户名重复");
//            map.put("url", "/sell/seller/onSellerRegister");
//            return new ModelAndView("common/error", map);
//        }
//        if (CardUtil.getInstance().getBankCode(sellerInfo.getIdCard())==null){
//            map.put("msg", "银行卡类型错误");
//            map.put("url", "/sell/seller/onSellerRegister");
//            return new ModelAndView("common/error", map);
//        }

//        try {
//            sellerInfo.setSellerId(KeyUtil.genUniqueKey());
//            sellerInfo.setKey(UUID.randomUUID().toString());
//            sellerInfo.setAudit(SellerStatusEnum.NOT_PASS.getCode());
//            sellerInfo.setPassword(passwordEncoder.encodePassword(sellerInfo.getPassword(),sellerInfo.getUsername()));
//            sellerService.save(sellerInfo);
//        }catch (Exception e){
//            e.printStackTrace();
//            map.put("msg", "修改失败请重试");
//            map.put("url", "/sell/seller/onSellerRegister");
//            return new ModelAndView("common/error", map);
//        }
//        map.put("msg", "修改成功");
//        map.put("url", "/sell/seller/onSellerLogin");
    }


}
