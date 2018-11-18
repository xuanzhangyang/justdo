package com.yuhao.sell.controller;

import com.jpay.ext.kit.PaymentKit;
import com.jpay.secure.RSAUtils;
import com.jpay.weixin.api.WxPayApi;
import com.yuhao.sell.config.WeChatAccountConfig;
import com.yuhao.sell.enums.OrderStatusEnum;
import com.yuhao.sell.enums.PayStatusEnum;
import com.yuhao.sell.model.OrderMaster;
import com.yuhao.sell.model.SellerInfo;
import com.yuhao.sell.service.OrderMasterService;
import com.yuhao.sell.service.SellerService;
import com.yuhao.sell.utils.CardUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * TransferAccountController
 *
 * @author CYH
 * @date 2018/4/21
 */
@RequestMapping("/wxtrans")
@Controller
@Slf4j
public class TransferAccountController {
	private static final String mch_id="1500080462";//商户号
	private static final String certPath="/home/apiclient_cert.p12";//证书路径
	private static final String partnerKey="76172cce2a199e6d2e0c4588ac8d0e48";//密钥

    @Autowired
    private SellerService sellerService;

    @Autowired
    private OrderMasterService orderMasterService;

    @Autowired
    private WeChatAccountConfig weChatAccountConfig;


    /**
     * 企业付款到银行卡
     */
    public String payBank(String name,String account,String amount,String bankCode) throws Exception {

            //假设获取到的RSA加密公钥为PUBLIC_KEY(PKCS#8格式)
            final String PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAsuA6fen8a59YCDnvSEMa8RCDjpZr0Pqj1NrdVATvcE5W0fq+qiM78D9P58fwtxp35sFigy5srWxxhmme7Q0lbIJLr9KdmC/Pqs4lJappWuvoZI9rKobQxgGSLIREj/2hftGYfRdWWUvtRpg71Nvijv0GZw3ff00fRP9Av/bBSc0CbDBBOePzGV4eDCG13b0a2JxeHBupxNYX2JgpVIFdDfSZRPrv+QozUOj1mA+CeRpN5T8BXzgnzZLv5Ck74d0//WqlHb7uyS2PLbElsen2HxM9wkFxw95dbFd7VLqrrqtH6sFqkUiWL5RhB7HNJG7oZS1OZvqzPJzLT9+bM152EQIDAQAB";
            Map<String, String> params = new HashMap<String, String>();
            params.put("mch_id", mch_id);
            log.info( "weChatAccountConfig.getMchId: " +mch_id);
            params.put("partner_trade_no", System.currentTimeMillis()+"");
            params.put("nonce_str", System.currentTimeMillis()+"");
            /**
             * 收款方银行卡号
             */
             params.put("enc_bank_no", RSAUtils.encryptByPublicKeyByWx("6217907500001471850", PUBLIC_KEY));
            log.info( "Acount:  " +account);
            /**
             * 收款方用户名
             */

            params.put("enc_true_name", RSAUtils.encryptByPublicKeyByWx("王思林", PUBLIC_KEY));
            log.info( "Name:  " +name);
            /**
             * 收款方开户行
             */
            params.put("bank_code", "1026");

            params.put("amount", "1");
            params.put("desc", "微信转账");
            params.put("sign", PaymentKit.createSign(params, partnerKey));

            log.info( "weChatAccountConfig.getMchKey: "+partnerKey );
            log.info( "Sign=  "+PaymentKit.createSign(params,partnerKey) );
            log.info( "params=  "+params );

            String payBank = WxPayApi.payBank(params ,certPath, mch_id);

            return payBank;


    }


    @Scheduled(cron = "0 0 23 * * ?")
//    @GetMapping("/start")
//@Scheduled(cron = "0/30 * * * * ?")
    public void transferAccount() throws Exception {
	String PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAsuA6fen8a59YCDnvSEMa8RCDjpZr0Pqj1NrdVATvcE5W0fq+qiM78D9P58fwtxp35sFigy5srWxxhmme7Q0lbIJLr9KdmC/Pqs4lJappWuvoZI9rKobQxgGSLIREj/2hftGYfRdWWUvtRpg71Nvijv0GZw3ff00fRP9Av/bBSc0CbDBBOePzGV4eDCG13b0a2JxeHBupxNYX2JgpVIFdDfSZRPrv+QozUOj1mA+CeRpN5T8BXzgnzZLv5Ck74d0//WqlHb7uyS2PLbElsen2HxM9wkFxw95dbFd7VLqrrqtH6sFqkUiWL5RhB7HNJG7oZS1OZvqzPJzLT9+bM152EQIDAQAB";
	Map<String, String> params = new HashMap<String, String>();
	List<SellerInfo> sellerInfos = sellerService.findAll();

	for (SellerInfo sellerInfo : sellerInfos) {
//		System.out.println( sellerInfo );
		List<OrderMaster> orderMasters = orderMasterService.findBySellerIdAndOrderStatusAndPayStatusAndIsEnd( sellerInfo.getSellerId(),
				OrderStatusEnum.FINISHED.getCode(), PayStatusEnum.SUCCESS.getCode(), 0 );
		BigDecimal sum = new BigDecimal( 0 );
		for (OrderMaster orderMaster : orderMasters) {
			sum = sum.add( orderMaster.getOrderAmount() );
//				orderMaster.getOrderAmount();
//			System.out.println( "订单号是" + orderMaster.getOrderId() + "金额是：" + sum.floatValue() );
		}
		//	log.error("今日金额少于1元:",sum.floatValue(),sellerInfo.getName(),sellerInfo.getIdCard());
		//1.进行转账
		String bankCode = CardUtil.getInstance().getBankCode( sellerInfo.getIdCard() );
		log.info( "bankcode= "+ bankCode);
		if (bankCode == null) {
			log.error( "银行卡类型出错:", sellerInfo.getName(), sellerInfo.getIdCard() );
			continue;
		}
		if (sum.intValue() < 1) {
//			log.error( "今日金额少于1元:", sum.floatValue(), sellerInfo.getName(), sellerInfo.getIdCard() );
			continue;
            }
			//进行抽点付款
//            String response="";
//            try {
//                 response= payBank(sellerInfo.getName(),sellerInfo.getIdCard(),(sum.floatValue()*(1-sellerInfo.getPoint()))+"",bankCode);
//				   response= payBank("王思林","6217907500001471850","2",CardUtil.getInstance().getBankCode("6217907500001471850"));
//                 log.info( "sellerInfo.getName: "+sellerInfo.getName()+" sellerInfo.getIdCard :"+sellerInfo.getIdCard()+" sum:"+(sum.floatValue()*(1-sellerInfo.getPoint()))+"  banckCode "+bankCode );
//                 log.info( "Respoense:"+response);


//                log.info( "已转账用户" );
//            } catch (Exception e) {
//                log.error("支付出错:",sellerInfo.getName(),sellerInfo.getIdCard());
//                e.printStackTrace();
//                continue;
//            }

			params.put( "mch_id", mch_id );
			params.put( "partner_trade_no", System.currentTimeMillis() + "" );
			params.put( "nonce_str", System.currentTimeMillis() + "" );
			params.put( "enc_bank_no", RSAUtils.encryptByPublicKeyByWx( sellerInfo.getIdCard(), PUBLIC_KEY ) );//收款方银行卡号
			log.info( "IdCard= "+ sellerInfo.getIdCard());
			params.put( "enc_true_name", RSAUtils.encryptByPublicKeyByWx( sellerInfo.getName(), PUBLIC_KEY ) );//收款方用户名
			log.info( "name= "+ sellerInfo.getName());
			params.put( "bank_code",bankCode );//收款方开户行
			int amount = (int)((sum.floatValue() * (1 - sellerInfo.getPoint()) ))-1;
			amount = amount*100;
			params.put( "amount", amount+"" );
			log.info( "金额是:"+amount+"" );
			params.put( "desc", "微信转账" );
			params.put( "sign", PaymentKit.createSign( params, partnerKey ) );
			log.info( "params= "+params );

			String payBank = WxPayApi.payBank( params, certPath, mch_id );
			log.info( payBank );
			//2.判断是否支付成功
			if (payBank.contains( "支付成功" )) {
				//3.更新订单状态
				for (OrderMaster orderMaster : orderMasters) {
					orderMaster.setIsEnd( 1 );
					orderMasterService.endOrder( orderMaster );
				}
				log.error( "支付成功:", sellerInfo.getName(), sellerInfo.getIdCard() );
			} else {
				log.error( "支付失败:", sellerInfo.getName(), sellerInfo.getIdCard() );
			}

			log.error( "支付信息:", payBank );

		}

	}

}
