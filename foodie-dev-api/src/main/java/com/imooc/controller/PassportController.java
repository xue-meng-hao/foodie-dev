package com.imooc.controller;

import com.imooc.pojo.bo.UserBO;
import com.imooc.pojo.Users;
import com.imooc.service.UserService;
import com.imooc.utils.CookieUtils;
import com.imooc.utils.IMOOCJSONResult;
import com.imooc.utils.JsonUtils;
import com.imooc.utils.MD5Utils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * tag是主题，value是说明
 */
@Api(value = "注册登录", tags = {"用于注册登录的相关接口"})
@RestController
@RequestMapping("passport")
public class PassportController {
    @Autowired
    private UserService userService;

    @ApiOperation(value = "用于验证用户名是否可用", notes = "用户名是否存在", httpMethod = "GET")
    @GetMapping("/usernameIsExist")
    public IMOOCJSONResult userNameIsEist(@RequestParam String username) {
        /**
         * 判断用户名不能为空
         */
        if (StringUtils.isBlank(username)) {
            return IMOOCJSONResult.errorMsg("用户名为空");
        }
        /**
         * 查找注册的用户名是否存在
         */
        boolean isExist = userService.queryUserNameIsExist(username);
        if (isExist) {
            return IMOOCJSONResult.errorMsg("用户名已存在");
        }

        return IMOOCJSONResult.ok();
    }

    /**
     * 注册用户
     *
     * @param userBO 用户数据的封装
     * @return
     * @RequestBody 用于传入前端传的json格式数据
     */
    @PostMapping("/regist")
    public IMOOCJSONResult regist(@RequestBody UserBO userBO, HttpServletRequest request, HttpServletResponse response) {
        String userName = userBO.getUsername();
        String password = userBO.getPassword();
        String confirmPassword = userBO.getConfirmPassword();
        //判断用户名是否为空
        if (StringUtils.isBlank(userName)) {
            return IMOOCJSONResult.errorMsg("用户名为空");
        }
        //判断密码是否为空
        if (StringUtils.isBlank(password)) {
            return IMOOCJSONResult.errorMsg("密码为空");
        }
        //判断确认密码是否为空
        if (StringUtils.isBlank(confirmPassword)) {
            return IMOOCJSONResult.errorMsg("确认密码为空");
        }
        //判断密码长度
        if (password.length() < 8) {
            return IMOOCJSONResult.errorMsg("密码长度不得少于8");
        }
        //判断两次密码是否相同
        if (!password.equals(confirmPassword)) {
            return IMOOCJSONResult.errorMsg("两次密码不一致!");
        }
        //判断用户名是否已经存在
        boolean isExist = userService.queryUserNameIsExist(userName);
        if (isExist) {
            return IMOOCJSONResult.errorMsg("用户名已存在！");
        }
        Users usersResult = userService.createUser(userBO);
        Users newResult = setPropertiesNull(usersResult);
        CookieUtils.setCookie(request, response, "user", JsonUtils.objectToJson(newResult), true);
        // TODO 生成用户token，存入redis会话
        // TODO 同步购物车数据
        return IMOOCJSONResult.ok("注册成功");
    }

    @PostMapping("/login")
    public IMOOCJSONResult login(@RequestBody UserBO userBO, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String username = userBO.getUsername();
        String password = userBO.getPassword();
        //判断用户名是否为空
        if (StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
            return IMOOCJSONResult.errorMsg("用户名或密码不能为空");
        }
        //实现登录
        final Users result = userService.queryUserForLogin(username, MD5Utils.getMD5Str(password));
        if (result == null) {
            return IMOOCJSONResult.errorMsg("用户名或密码错误！");
        }
        Users newResult = setPropertiesNull(result);
        CookieUtils.setCookie(request, response, "user", JsonUtils.objectToJson(newResult), true);
        // TODO 生成用户token，存入redis会话
        // TODO 同步购物车数据
        return IMOOCJSONResult.ok(newResult);
    }

    /**
     * 将重要信息隐藏
     *
     * @param result
     * @return
     */
    public Users setPropertiesNull(Users result) {
        result.setPassword(null);
        result.setMobile(null);
        result.setEmail(null);
        result.setCreatedTime(null);
        result.setRealname(null);
        return result;
    }

    @ApiOperation(value = "用户退出登录", notes = "用户退出登录", httpMethod = "POST")
    @PostMapping("/logout")
    public IMOOCJSONResult userNameIsEist(@RequestParam String userId, HttpServletRequest request, HttpServletResponse response) {
        CookieUtils.deleteCookie(request, response, "user");
        // TODO 使用分布式时需要清空分布式会话
        // TODO 有购物车需要清空购物车信息
        return IMOOCJSONResult.ok();
    }

}
