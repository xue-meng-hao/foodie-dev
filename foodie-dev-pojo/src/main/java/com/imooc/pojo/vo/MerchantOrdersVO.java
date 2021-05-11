package com.imooc.pojo.vo;

public class MerchantOrdersVO {

    private String merchantOrderId;         // 商户订单号
    private String merchantUserId;          // 商户方的发起用户的用户主键id
    private Integer amount;                 // 实际支付总金额（包含商户所支付的订单费邮费总额）
    private Integer payMethod;              // 支付方式 1:微信   2:支付宝
    private String returnUrl;               // 支付成功后的回调地址（学生自定义）

    public String getMerchantOrderId() {
        return merchantOrderId;
    }

    /**
     * 设置商户订单号
     *
     * @param merchantOrderId
     */
    public void setMerchantOrderId(String merchantOrderId) {
        this.merchantOrderId = merchantOrderId;
    }

    public String getMerchantUserId() {
        return merchantUserId;
    }

    /**
     * 设置用户id
     *
     * @param merchantUserId
     */
    public void setMerchantUserId(String merchantUserId) {
        this.merchantUserId = merchantUserId;
    }

    public Integer getAmount() {
        return amount;
    }

    /**
     * 设置付款金额
     *
     * @param amount
     */
    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Integer getPayMethod() {
        return payMethod;
    }

    public void setPayMethod(Integer payMethod) {
        this.payMethod = payMethod;
    }

    public String getReturnUrl() {
        return returnUrl;
    }

    public void setReturnUrl(String returnUrl) {
        this.returnUrl = returnUrl;
    }
}