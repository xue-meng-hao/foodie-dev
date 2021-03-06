package com.imooc.controller.center;

import com.imooc.controller.BaseController;
import com.imooc.pojo.vo.OrderStatusCountsVO;
import com.imooc.service.center.MyOrdersService;
import com.imooc.utils.IMOOCJSONResult;
import com.imooc.utils.PagedGridResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@Api(value = "用户中心订单", tags = {"用户中心订单相关接口"})
@RequestMapping("myorders")
public class MyOrdersController extends BaseController {
    @Autowired
    private MyOrdersService myOrdersService;

    @ApiOperation(value = "查询订单信息", notes = "查询订单信息", httpMethod = "POST")
    @PostMapping("/query")
    public IMOOCJSONResult query(@ApiParam(name = "userId", value = "用户id", required = true) @RequestParam String userId,
                                 @ApiParam(name = "orderStatus", value = "订单状态", required = false) @RequestParam Integer orderStatus,
                                 @ApiParam(name = "page", value = "查询第几页", required = false) @RequestParam Integer page,
                                 @ApiParam(name = "pageSize", value = "分页的每一页显示多少条", required = false) @RequestParam Integer pageSize) {
        if (userId == null) {
            return IMOOCJSONResult.errorMsg("未查询到用户信息");
        }
        if (page == null) {
            page = 1;
        }
        if (pageSize == null) {
            pageSize = COMMENT_PAGE_SIZE;
        }
        PagedGridResult gridResult = myOrdersService.queryMyOrders(userId, orderStatus, page, pageSize);
        return IMOOCJSONResult.ok(gridResult);
    }


    // 商家发货没有后端，所以这个接口仅仅只是用于模拟
    @ApiOperation(value = "商家发货", notes = "商家发货", httpMethod = "GET")
    @GetMapping("/deliver")
    public IMOOCJSONResult deliver(
            @ApiParam(name = "orderId", value = "订单id", required = true)
            @RequestParam String orderId) throws Exception {

        if (StringUtils.isBlank(orderId)) {
            return IMOOCJSONResult.errorMsg("订单ID不能为空");
        }
        myOrdersService.updateDeliverOrderStatus(orderId);
        return IMOOCJSONResult.ok();
    }

    @ApiOperation(value = "用户确认收货", notes = "用户确认收货", httpMethod = "POST")
    @PostMapping("/confirmReceive")
    public IMOOCJSONResult confirmReceive(
            @ApiParam(name = "orderId", value = "订单id", required = true)
            @RequestParam String orderId,
            @ApiParam(name = "userId", value = "用户id", required = true)
            @RequestParam String userId) throws Exception {

        IMOOCJSONResult checkResult = checkUserOrder(userId, orderId);
        if (checkResult.getStatus() != HttpStatus.OK.value()) {
            return checkResult;
        }

        boolean res = myOrdersService.updateReceiveOrderStatus(orderId);
        if (!res) {
            return IMOOCJSONResult.errorMsg("订单确认收货失败！");
        }

        return IMOOCJSONResult.ok();
    }

    @ApiOperation(value = "用户删除订单", notes = "用户删除订单", httpMethod = "POST")
    @PostMapping("/delete")
    public IMOOCJSONResult delete(
            @ApiParam(name = "orderId", value = "订单id", required = true)
            @RequestParam String orderId,
            @ApiParam(name = "userId", value = "用户id", required = true)
            @RequestParam String userId) throws Exception {

        IMOOCJSONResult checkResult = checkUserOrder(userId, orderId);
        if (checkResult.getStatus() != HttpStatus.OK.value()) {
            return checkResult;
        }

        boolean res = myOrdersService.deleteOrder(userId, orderId);
        if (!res) {
            return IMOOCJSONResult.errorMsg("订单删除失败！");
        }

        return IMOOCJSONResult.ok();
    }

    @ApiOperation(value = "查询不同状态订单数", notes = "查询不同状态订单数", httpMethod = "POST")
    @PostMapping("/statusCounts")
    public IMOOCJSONResult statusCounts(@RequestParam String userId) {
        OrderStatusCountsVO oderStatusCounts = myOrdersService.getOderStatusCounts(userId);
        return IMOOCJSONResult.ok(oderStatusCounts);
    }

    @ApiOperation(value = "查询订单动向", notes = "查询订单动向", httpMethod = "POST")
    @PostMapping("/trend")
    public IMOOCJSONResult trend(@RequestParam String userId, @RequestParam Integer page, @RequestParam Integer pageSize) {
        PagedGridResult myOrderTrend = myOrdersService.getMyOrderTrend(userId, page, pageSize);
        return IMOOCJSONResult.ok(myOrderTrend);
    }
}
