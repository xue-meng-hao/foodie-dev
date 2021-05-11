package com.imooc.controller;

import com.imooc.pojo.bo.ShopcartBO;
import com.imooc.utils.IMOOCJSONResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Api(value = "购物车接口", tags = {"购物车相关"})
@RestController
@RequestMapping("shopcart")
public class ShopcartController {
    private final Logger LOGGER = LoggerFactory.getLogger(ShopcartController.class);

    @ApiOperation(value = "添加到购物车", notes = "将商品添加到购物车", httpMethod = "POST")
    @PostMapping("/add")
    public IMOOCJSONResult add(@RequestParam String userId, @RequestBody ShopcartBO shopcartBO, HttpServletRequest request, HttpServletResponse response) {
        if (StringUtils.isBlank(userId)) {
            IMOOCJSONResult.errorMsg("用户id为空");
        }
        //TODO 前端将购物车数据传入，在登录状态下后端存入redis缓存中
        LOGGER.info(shopcartBO.toString());
        return IMOOCJSONResult.ok();
    }

    @ApiOperation(value = "从购物车中删除商品", notes = "从购物车中删除商品", httpMethod = "POST")
    @PostMapping("/del")
    public IMOOCJSONResult del(@RequestParam String userId, @RequestParam String itemSpecId, HttpServletRequest request, HttpServletResponse response) {
        if (StringUtils.isBlank(userId)) {
            return IMOOCJSONResult.errorMsg("参数不能为空");
        }
        if (StringUtils.isBlank(itemSpecId)) {
            return IMOOCJSONResult.errorMsg("参数不能为空");
        }
        //TODO 用户在前端删除商品数据，若用户已登录，则在后端也删除
        LOGGER.info("delete success!");
        return IMOOCJSONResult.ok();
    }
}
