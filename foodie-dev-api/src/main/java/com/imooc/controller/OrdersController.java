package com.imooc.controller;

import com.imooc.enums.OrderStatusEnum;
import com.imooc.enums.PayMethod;
import com.imooc.pojo.OrderStatus;
import com.imooc.pojo.bo.SubmitOrderBO;
import com.imooc.pojo.vo.MerchantOrdersVO;
import com.imooc.pojo.vo.OrderVO;
import com.imooc.service.OrderService;
import com.imooc.utils.CookieUtils;
import com.imooc.utils.IMOOCJSONResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@Api(value = "订单相关", tags = {"订单相关接口"})
@RestController
@RequestMapping("orders")
public class OrdersController extends BaseController {
    private Logger LOGGER = LoggerFactory.getLogger(OrdersController.class);

    @Autowired
    private OrderService orderService;

    @Autowired
    private RestTemplate restTemplate;


    @ApiOperation(value = "创建订单", notes = "创建订单")
    @PostMapping("/create")
    public IMOOCJSONResult create(@RequestBody SubmitOrderBO submitOrderBo, HttpServletRequest request, HttpServletResponse response) {
        LOGGER.info(submitOrderBo.toString());
        /* 判断支付方式是否正确 */
        if (submitOrderBo.getPayMethod() != PayMethod.WEIXIN.type && submitOrderBo.getPayMethod() != PayMethod.ALIPAY.type) {
            return IMOOCJSONResult.errorMsg("支付方式不支持");
        }
        //1. 创建订单
        String orderId = "";
        MerchantOrdersVO merchantOrdersVO = null;
        try {
            OrderVO orderVO = orderService.createOrder(submitOrderBo);
            if (orderVO != null) {
                orderId = orderVO.getOrderId();
                merchantOrdersVO = orderVO.getMerchantOrdersVO();
                merchantOrdersVO.setAmount(1);
                merchantOrdersVO.setReturnUrl(payRetrurnUrl);
            }
        } catch (Exception e) {
            return IMOOCJSONResult.errorMsg("库存不足");
        }
        //2. 创建订单后，移除购物车中商品
        //TODO 整合redis之后，完善购物车中已结算商品移除购物车，并同步到前端cookie中
//        CookieUtils.setCookie(request, response, FOODIE_SHOPCART, "");
        //3. 向支付中心发送当前订单，用于保存支付中心的订单数据

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("imoocUserId", "imooc");
        headers.add("password", "imooc");
        HttpEntity<MerchantOrdersVO> entity = new HttpEntity<>(merchantOrdersVO, headers);

        ResponseEntity<IMOOCJSONResult> resultResponseEntity = restTemplate.postForEntity(paymentURL, entity, IMOOCJSONResult.class);
        IMOOCJSONResult paymentResult = resultResponseEntity.getBody();
        if (paymentResult.getStatus() != 200) {
            return IMOOCJSONResult.errorMsg("支付中心创建失败!");
        }
        return IMOOCJSONResult.ok(orderId);
    }

    /**
     * 支付中心支付成功后的回调方法
     *
     * @param merchantOrderId
     * @return
     */
    @PostMapping("notifyMerchantOrderPaid")
    public Integer notifyMerchantOrderPaid(String merchantOrderId) {
        orderService.updateOderStatus(merchantOrderId, OrderStatusEnum.WAIT_DELIVER.type);
        return HttpStatus.OK.value();
    }

    @PostMapping("getPaidOrderInfo")
    public IMOOCJSONResult getPaidOrderInfo(String orderId) {
        OrderStatus orderStatus = orderService.queryOrderStatusInfo(orderId);
        return IMOOCJSONResult.ok(orderStatus);
    }
}
