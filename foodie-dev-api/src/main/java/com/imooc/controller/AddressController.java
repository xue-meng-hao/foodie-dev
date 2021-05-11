package com.imooc.controller;

import com.imooc.pojo.UserAddress;
import com.imooc.pojo.bo.AddressBO;
import com.imooc.service.AddressService;
import com.imooc.utils.IMOOCJSONResult;
import com.imooc.utils.MobileEmailUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 用户在确认订单里，可针对收货地址做以下操作
 * 1. 查询用户创建的收货地址列表
 * 2. 新增收货地址
 * 3. 删除收货地址
 * 4. 修改收货地址
 * 5. 设置默认收货地址
 */
@Api(value = "收货地址相关", tags = {"收货地址相关API"})
@RequestMapping("address")
@RestController
public class AddressController {
    @Autowired
    private AddressService addressService;

    /**
     * 因为隐私相关使用了POST请求
     *
     * @param userId
     * @return
     */
    @ApiOperation(value = "根据用户id查询用户收货地址", notes = "根据用户id查询用户收货地址", httpMethod = "POST")
    @PostMapping("/list")
    public IMOOCJSONResult list(@RequestParam String userId) {
        if (StringUtils.isBlank(userId)) {
            return IMOOCJSONResult.errorMsg("参数不能为空");
        }
        List<UserAddress> userAddresses = addressService.queryAll(userId);
        return IMOOCJSONResult.ok(userAddresses);
    }

    /**
     * 新增用户地址
     *
     * @param addressBO
     * @return
     */
    @ApiOperation(value = "新增用户收货地址", notes = "新增用户收货地址", httpMethod = "POST")
    @PostMapping("/add")
    public IMOOCJSONResult add(@RequestBody AddressBO addressBO) {
        IMOOCJSONResult checkResult = checkAddress(addressBO);
        if (checkResult.getStatus() != 200) {
            return checkResult;
        }
        addressService.addNewUserAddress(addressBO);
        return IMOOCJSONResult.ok();
    }

    /**
     * 修改用户收货地址
     *
     * @param addressBO
     * @return
     */
    @ApiOperation(value = "修改用户收货地址", notes = "修改用户收货地址", httpMethod = "POST")
    @PostMapping("/update")
    public IMOOCJSONResult update(@RequestBody AddressBO addressBO) {
        if (StringUtils.isBlank(addressBO.getAddressId())) {
            return IMOOCJSONResult.errorMsg("修改地址错误：addressId不能为空");
        }
        IMOOCJSONResult checkResult = checkAddress(addressBO);
        if (checkResult.getStatus() != 200) {
            return checkResult;
        }
        addressService.updateUserAddress(addressBO);
        return IMOOCJSONResult.ok();
    }

    /**
     * 删除用户收货地址
     */
    @ApiOperation(value = "删除用户收货地址", notes = "删除用户收货地址", httpMethod = "POST")
    @PostMapping("/delete")
    public IMOOCJSONResult delete(@RequestParam String userId, @RequestParam String addressId) {
        if (StringUtils.isBlank(userId)) {
            return IMOOCJSONResult.errorMsg("userId不能为空");
        }
        if (StringUtils.isBlank(addressId)) {
            return IMOOCJSONResult.errorMsg("addressId不能为空");
        }
        addressService.deleteUserAddress(userId, addressId);
        return IMOOCJSONResult.ok();
    }

    /**
     * 设置默认地址
     *
     * @param userId
     * @param addressId
     * @return
     */
    @ApiOperation(value = "删除用户收货地址", notes = "删除用户收货地址", httpMethod = "POST")
    @PostMapping("/setDefault")
    public IMOOCJSONResult setDefault(@RequestParam String userId, @RequestParam String addressId) {
        if (StringUtils.isBlank(userId)) {
            return IMOOCJSONResult.errorMsg("userId不能为空");
        }
        if (StringUtils.isBlank(addressId)) {
            return IMOOCJSONResult.errorMsg("addressId不能为空");
        }
        addressService.updateUserAddressIsDefault(userId, addressId);
        return IMOOCJSONResult.ok();
    }

    private IMOOCJSONResult checkAddress(AddressBO addressBO) {
        String receiver = addressBO.getReceiver();
        if (StringUtils.isBlank(receiver)) {
            return IMOOCJSONResult.errorMsg("收货人不能为空");
        }
        if (receiver.length() > 12) {
            return IMOOCJSONResult.errorMsg("收货人姓名不能太长");
        }

        String mobile = addressBO.getMobile();
        if (StringUtils.isBlank(mobile)) {
            return IMOOCJSONResult.errorMsg("收货人手机号不能为空");
        }
        if (mobile.length() != 11) {
            return IMOOCJSONResult.errorMsg("收货人手机号长度不正确");
        }
        boolean isMobileOk = MobileEmailUtils.checkMobileIsOk(mobile);
        if (!isMobileOk) {
            return IMOOCJSONResult.errorMsg("收货人手机号格式不正确");
        }

        String province = addressBO.getProvince();
        String city = addressBO.getCity();
        String district = addressBO.getDistrict();
        String detail = addressBO.getDetail();
        if (StringUtils.isBlank(province) ||
                StringUtils.isBlank(city) ||
                StringUtils.isBlank(district) ||
                StringUtils.isBlank(detail)) {
            return IMOOCJSONResult.errorMsg("收货地址信息不能为空");
        }

        return IMOOCJSONResult.ok();
    }
}
