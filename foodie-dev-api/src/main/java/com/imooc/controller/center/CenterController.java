package com.imooc.controller.center;

import com.imooc.pojo.Users;
import com.imooc.service.center.CenterUserService;
import com.imooc.utils.IMOOCJSONResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Api(value = "用户中心", tags = {"用户中心展示相关接口"})
@RequestMapping("center")
public class CenterController {
    @Autowired
    private CenterUserService userService;

    @ApiOperation(value = "获取用户信息", notes = "获取用户信息", httpMethod = "GET")
    @GetMapping("userInfo")
    public IMOOCJSONResult userInfo(@ApiParam(name = "userId", value = "用户id", required = true) @RequestParam String userId) {
        Users users = userService.queryByUserId(userId);
        return IMOOCJSONResult.ok(users);
    }
}
