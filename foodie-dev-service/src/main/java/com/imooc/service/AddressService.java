package com.imooc.service;

import com.imooc.pojo.UserAddress;
import com.imooc.pojo.bo.AddressBO;

import java.util.List;

public interface AddressService {
    /**
     * 查询用户收货地址
     *
     * @param userId
     * @return
     */
    public List<UserAddress> queryAll(String userId);

    /**
     * 用户新增地址
     *
     * @param addressBO
     */
    public void addNewUserAddress(AddressBO addressBO);

    /**
     * 修改用户收货地址
     *
     * @param addressBO
     */
    public void updateUserAddress(AddressBO addressBO);

    /**
     * 根据用户id和地址id删除用户收货地址
     */
    public void deleteUserAddress(String userId, String addressId);

    /**
     * 根据用户id和地址id删除用户收货地址
     */
    public void updateUserAddressIsDefault(String userId, String addressId);

    /**
     * 根据用户id和地址id查询用户收货地址
     *
     * @param userId
     * @param addressId
     * @return
     */
    public UserAddress queryUserAddress(String userId, String addressId);
}
