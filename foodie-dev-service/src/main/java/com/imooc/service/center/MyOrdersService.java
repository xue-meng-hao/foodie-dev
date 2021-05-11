package com.imooc.service.center;

import com.imooc.pojo.Orders;
import com.imooc.pojo.vo.OrderStatusCountsVO;
import com.imooc.utils.PagedGridResult;

public interface MyOrdersService {
    /**
     * 查询我的订单列表
     *
     * @param userId
     * @param orderStatus
     * @param page
     * @param pageSize
     * @return
     */
    public PagedGridResult queryMyOrders(String userId, Integer orderStatus, Integer page, Integer pageSize);

    /**
     * 更新订单状态为已发货
     *
     * @param orderId
     */
    public void updateDeliverOrderStatus(String orderId);

    /**
     * 查询我的订单
     *
     * @param userId
     * @param orderId
     * @return
     */
    public Orders queryMyOrder(String userId, String orderId);

    /**
     * 逻辑删除订单
     *
     * @param userId
     * @param orderId
     * @return
     */
    public boolean deleteOrder(String userId, String orderId);

    /**
     * 更新订单状态为收货
     *
     * @param orderId
     * @return
     */
    public boolean updateReceiveOrderStatus(String orderId);

    /**
     * 查询订单状态总数
     *
     * @param userId
     * @return
     */
    public OrderStatusCountsVO getOderStatusCounts(String userId);

    public PagedGridResult getMyOrderTrend(String userId, Integer page, Integer pageSize);


}
