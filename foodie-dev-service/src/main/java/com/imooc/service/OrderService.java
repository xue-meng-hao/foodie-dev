package com.imooc.service;

import com.imooc.pojo.OrderStatus;
import com.imooc.pojo.bo.SubmitOrderBO;
import com.imooc.pojo.vo.OrderVO;

public interface OrderService {
    /**
     * 创建订单相关信息
     *
     * @param submitOrderBo
     */
    public OrderVO createOrder(SubmitOrderBO submitOrderBo);

    /**
     * 修改订单状态
     *
     * @param oderId
     * @param orderStatus
     */
    public void updateOderStatus(String oderId, Integer orderStatus);

    /**
     * 根据订单id查询订单状态
     *
     * @param orderId
     * @return
     */
    public OrderStatus queryOrderStatusInfo(String orderId);

}
