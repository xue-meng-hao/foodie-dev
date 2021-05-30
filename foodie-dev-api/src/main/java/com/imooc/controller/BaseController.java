package com.imooc.controller;


import com.imooc.pojo.Orders;
import com.imooc.service.center.MyOrdersService;
import com.imooc.utils.IMOOCJSONResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.io.File;

@Controller
public class BaseController {
    public static final Integer COMMENT_PAGE_SIZE = 10;

    public static final Integer PAGE_SIZE = 20;

    public static final String FOODIE_SHOPCART = "shopcart";

    @Autowired
    public MyOrdersService myOrdersService;
    //    微信支付成功->通知到支付中心-->天天吃货平台
    //                      |-->回调通知的URL
//    String payRetrurnUrl = "http://localhost:8088/orders/notifyMerchantOrderPaid";
    String payRetrurnUrl = "http://192.168.17.128:8088/orders/notifyMerchantOrderPaid";

    String paymentURL = "http://payment.t.mukewang.com/foodie-payment/payment/createMerchantOrder";
    //用户上传头像的目录
    public static final String IMAGE_USER_FACE_PATH = "D:" + File.separator + "workspaces" + File.separator + "images" +
            File.separator + "foodie" + File.separator + "faces";

    /**
     * 用于验证用户和订单是否有关联关系，避免非法用户调用
     *
     * @return
     */
    public IMOOCJSONResult checkUserOrder(String userId, String orderId) {
        Orders order = myOrdersService.queryMyOrder(userId, orderId);
        if (order == null) {
            return IMOOCJSONResult.errorMsg("订单不存在！");
        }
        return IMOOCJSONResult.ok(order);
    }
}
