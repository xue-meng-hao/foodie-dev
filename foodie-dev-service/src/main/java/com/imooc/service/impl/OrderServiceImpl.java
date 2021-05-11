package com.imooc.service.impl;

import com.imooc.enums.OrderStatusEnum;
import com.imooc.enums.YesOrNo;
import com.imooc.mapper.OrderItemsMapper;
import com.imooc.mapper.OrderStatusMapper;
import com.imooc.mapper.OrdersMapper;
import com.imooc.pojo.*;
import com.imooc.pojo.bo.SubmitOrderBO;
import com.imooc.pojo.vo.MerchantOrdersVO;
import com.imooc.pojo.vo.OrderVO;
import com.imooc.service.AddressService;
import com.imooc.service.ItemService;
import com.imooc.service.OrderService;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private Sid sid;
    @Autowired
    private OrdersMapper ordersMapper;

    @Autowired
    private AddressService addressService;

    @Autowired
    private ItemService itemService;

    @Autowired
    private OrderItemsMapper orderItemsMapper;

    @Autowired
    private OrderStatusMapper orderStatusMapper;


    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public OrderVO createOrder(SubmitOrderBO submitOrderBo) {
        String userId = submitOrderBo.getUserId();
        String addressId = submitOrderBo.getAddressId();
        String itemSpecIds = submitOrderBo.getItemSpecIds();
        Integer payMethod = submitOrderBo.getPayMethod();
        String leftMsg = submitOrderBo.getLeftMsg();
        //邮费设置为0
        Integer postAmount = 0;


        UserAddress userAddress = addressService.queryUserAddress(userId, addressId);
        //保存新订单
        String orderId = sid.nextShort();
        Orders order = new Orders();
        order.setId(orderId);
        //根据addressId查出用户地址
        order.setUserId(userId);
        order.setReceiverName(userAddress.getReceiver());
        StringBuffer receiverAddressSb = new StringBuffer();
        receiverAddressSb.append(userAddress.getProvince());
        receiverAddressSb.append(userAddress.getCity());
        receiverAddressSb.append(userAddress.getDistrict());
        receiverAddressSb.append(userAddress.getDetail());
        String receiverAddress = receiverAddressSb.toString();
        order.setReceiverAddress(receiverAddress);
        order.setReceiverMobile(userAddress.getMobile());


        order.setPostAmount(postAmount);

        order.setPayMethod(payMethod);
        order.setExtand(leftMsg);

        order.setIsComment(YesOrNo.NO.type);
        order.setIsDelete(YesOrNo.NO.type);

        order.setCreatedTime(new Date());
        order.setUpdatedTime(new Date());
        //根据itemSpecId保存商品信息表
        String[] itemSpecArr = itemSpecIds.split(",");
        Integer totalAmount = 0;
        Integer realPayAmount = 0;
        for (String itemSpecId : itemSpecArr) {
            //商品数量，需要从购物车的redis缓存中取出，暂未使用redis所以设置为1
            Integer buyCounts = 1;
            //根据规格id查询具体价格
            ItemsSpec itemsSpec = itemService.queryItemSpecById(itemSpecId);
            Integer priceNormal = itemsSpec.getPriceNormal();
            Integer priceDiscount = itemsSpec.getPriceDiscount();
            //单价*商品数量(商品数量从redis的缓存中获取)
            totalAmount += priceNormal * buyCounts;
            realPayAmount += priceDiscount * buyCounts;

            //根据规格id取商品信息及图片
            String itemId = itemsSpec.getItemId();
            Items item = itemService.queryItemById(itemId);

            String imgUrl = itemService.queryItemMainImgById(itemId);

            //循环保存商品信息到数据库
            OrderItems orderItem = new OrderItems();
            String subOrderId = sid.nextShort();
            orderItem.setId(subOrderId);
            orderItem.setOrderId(orderId);
            orderItem.setItemId(itemId);
            orderItem.setItemName(item.getItemName());
            orderItem.setItemImg(imgUrl);
            orderItem.setBuyCounts(buyCounts);
            orderItem.setItemSpecId(itemSpecId);
            orderItem.setItemSpecName(itemsSpec.getName());
            orderItem.setPrice(itemsSpec.getPriceDiscount());
            orderItemsMapper.insert(orderItem);
            //提交每个商品时，商品库存减少
            itemService.decreaseItemSpecStock(itemSpecId, buyCounts);
        }
        order.setTotalAmount(totalAmount);
        order.setRealPayAmount(realPayAmount);

        ordersMapper.insert(order);
        //保存订单状态表
        OrderStatus waitPayOrderStatus = new OrderStatus();
        waitPayOrderStatus.setOrderId(orderId);
        waitPayOrderStatus.setOrderStatus(OrderStatusEnum.WAIT_PAY.type);
        waitPayOrderStatus.setCreatedTime(new Date());

        orderStatusMapper.insert(waitPayOrderStatus);

        //构建商户订单，用于传给支付中心
        MerchantOrdersVO merchantOrdersVO = new MerchantOrdersVO();
        merchantOrdersVO.setMerchantOrderId(orderId);
        merchantOrdersVO.setMerchantUserId(userId);
        merchantOrdersVO.setAmount(realPayAmount + postAmount);
        merchantOrdersVO.setPayMethod(payMethod);

        OrderVO orderVO = new OrderVO();
        orderVO.setOrderId(orderId);
        orderVO.setMerchantOrdersVO(merchantOrdersVO);

        return orderVO;
    }


    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void updateOderStatus(String oderId, Integer orderStatus) {
        OrderStatus paidStatus = new OrderStatus();
        paidStatus.setOrderId(oderId);
        paidStatus.setOrderStatus(orderStatus);
        paidStatus.setPayTime(new Date());
        orderStatusMapper.updateByPrimaryKeySelective(paidStatus);
    }

    @Override
    public OrderStatus queryOrderStatusInfo(String orderId) {
        OrderStatus orderStatus = orderStatusMapper.selectByPrimaryKey(orderId);
        return orderStatus;
    }
}
