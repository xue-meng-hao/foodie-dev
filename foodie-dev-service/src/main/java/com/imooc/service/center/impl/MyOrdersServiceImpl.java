package com.imooc.service.center.impl;

import com.github.pagehelper.PageHelper;
import com.imooc.enums.OrderStatusEnum;
import com.imooc.enums.YesOrNo;
import com.imooc.mapper.OrderStatusMapper;
import com.imooc.mapper.OrdersMapper;
import com.imooc.mapper.OrdersMapperCustom;
import com.imooc.pojo.OrderStatus;
import com.imooc.pojo.Orders;
import com.imooc.pojo.vo.MyOrdersVO;
import com.imooc.pojo.vo.OrderStatusCountsVO;
import com.imooc.service.BaseService;
import com.imooc.service.center.MyOrdersService;
import com.imooc.utils.PagedGridResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MyOrdersServiceImpl extends BaseService implements MyOrdersService {
    @Autowired
    private OrdersMapperCustom ordersMapperCustom;
    @Autowired
    private OrderStatusMapper orderStatusMapper;
    @Autowired
    private OrdersMapper ordersMapper;

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public PagedGridResult queryMyOrders(String userId, Integer orderStatus, Integer page, Integer pageSize) {
        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);

        if (orderStatus != null) {
            map.put("orderStatus", orderStatus);
        }

        PageHelper.startPage(page, pageSize);
        List<MyOrdersVO> myOrdersVOS = ordersMapperCustom.queryMyOrders(map);
        return setterPagedGrid(myOrdersVOS, page);
    }


    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public void updateDeliverOrderStatus(String orderId) {
        OrderStatus orderStatus = new OrderStatus();
        orderStatus.setOrderId(orderId);
        orderStatus.setOrderStatus(OrderStatusEnum.WAIT_DELIVER.type);
        orderStatusMapper.updateByPrimaryKeySelective(orderStatus);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public Orders queryMyOrder(String userId, String orderId) {
        Orders order = new Orders();
        order.setUserId(userId);
        order.setId(orderId);
        order.setIsDelete(YesOrNo.NO.type);
        return ordersMapper.selectOne(order);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public boolean deleteOrder(String userId, String orderId) {
        Orders orders = new Orders();
        orders.setUserId(userId);
        orders.setUpdatedTime(new Date());
        orders.setId(orderId);
        orders.setIsDelete(YesOrNo.YES.type);
        int i = ordersMapper.updateByPrimaryKeySelective(orders);
        return i == 1;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public boolean updateReceiveOrderStatus(String orderId) {
        OrderStatus orderStatus = new OrderStatus();
        orderStatus.setSuccessTime(new Date());
        Example example = new Example(OrderStatus.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("orderId", orderId);
        criteria.andEqualTo("orderStatus", OrderStatusEnum.WAIT_RECEIVE.type);
        orderStatus.setOrderStatus(OrderStatusEnum.SUCCESS.type);
        int i = orderStatusMapper.updateByExampleSelective(orderStatus, example);
        return i == 1;
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public OrderStatusCountsVO getOderStatusCounts(String userId) {
        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);
        map.put("orderStatus", OrderStatusEnum.WAIT_PAY.type);
        int waitPayCount = ordersMapperCustom.getMyOrderStatusCounts(map);
        map.put("orderStatus", OrderStatusEnum.WAIT_DELIVER.type);
        int waitDeliverCount = ordersMapperCustom.getMyOrderStatusCounts(map);
        map.put("orderStatus", OrderStatusEnum.WAIT_RECEIVE.type);
        int waitReceiveCount = ordersMapperCustom.getMyOrderStatusCounts(map);
        map.put("orderStatus", OrderStatusEnum.SUCCESS.type);
        map.put("isComment", YesOrNo.NO.type);
        int waitCommentCount = ordersMapperCustom.getMyOrderStatusCounts(map);
        OrderStatusCountsVO orderStatusCountsVO = new OrderStatusCountsVO(
                waitPayCount,
                waitDeliverCount,
                waitReceiveCount,
                waitCommentCount
        );
        return orderStatusCountsVO;
    }

    @Override
    public PagedGridResult getMyOrderTrend(String userId, Integer page, Integer pageSize) {
        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);
        PageHelper.startPage(page, pageSize);
        List<OrderStatus> myOrderTrend = ordersMapperCustom.getMyOrderTrend(map);
        PagedGridResult result = setterPagedGrid(myOrderTrend, page);
        return result;
    }
}
