package com.imooc.service.impl;

import com.imooc.enums.YesOrNo;
import com.imooc.mapper.UserAddressMapper;
import com.imooc.pojo.UserAddress;
import com.imooc.pojo.bo.AddressBO;
import com.imooc.service.AddressService;
import org.n3r.idworker.Sid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class AddressServiceImpl implements AddressService {
    @Autowired
    private UserAddressMapper userAddressMapper;
    @Autowired
    private Sid sid;

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<UserAddress> queryAll(String userId) {
        UserAddress userAddress = new UserAddress();
        userAddress.setUserId(userId);
        List<UserAddress> userAddresses = userAddressMapper.select(userAddress);
        return userAddresses;
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public void addNewUserAddress(AddressBO addressBO) {
        //判断用户是否存在默认地址，若不存在，则设置为默认地址
        Integer isDefault = 0;
        List<UserAddress> userAddresses = this.queryAll(addressBO.getUserId());
        if (userAddresses == null || userAddresses.isEmpty()) {
            isDefault = 1;
        }
        String addressId = sid.nextShort();
        UserAddress newAddress = new UserAddress();
        BeanUtils.copyProperties(addressBO, newAddress);
        newAddress.setId(addressId);
        newAddress.setIsDefault(isDefault);
        newAddress.setCreatedTime(new Date());
        newAddress.setUpdatedTime(new Date());
        userAddressMapper.insert(newAddress);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void updateUserAddress(AddressBO addressBO) {
        String addressId = addressBO.getAddressId();
//         = addressBO.getAddressId();
        UserAddress pendingAddress = new UserAddress();
        BeanUtils.copyProperties(addressBO, pendingAddress);
        pendingAddress.setId(addressId);
        pendingAddress.setUpdatedTime(new Date());
        userAddressMapper.updateByPrimaryKeySelective(pendingAddress);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void deleteUserAddress(String userId, String addressId) {
        UserAddress address = new UserAddress();
        address.setId(addressId);
        address.setUserId(userId);
        userAddressMapper.delete(address);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void updateUserAddressIsDefault(String userId, String addressId) {
        //1.将默认设置为非默认
        UserAddress queryAddress = new UserAddress();
        queryAddress.setUserId(userId);
        queryAddress.setIsDefault(YesOrNo.YES.type);
        List<UserAddress> list = userAddressMapper.select(queryAddress);
        for (UserAddress address : list) {
            address.setIsDefault(YesOrNo.NO.type);
            userAddressMapper.updateByPrimaryKeySelective(address);
        }

        //2.将非默认设置为默认
        UserAddress selectAddress = new UserAddress();
        selectAddress.setUserId(userId);
        selectAddress.setId(addressId);
        selectAddress.setIsDefault(YesOrNo.YES.type);
        userAddressMapper.updateByPrimaryKeySelective(selectAddress);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public UserAddress queryUserAddress(String userId, String addressId) {
        UserAddress singleAddress = new UserAddress();
        singleAddress.setId(addressId);
        singleAddress.setUserId(userId);
        UserAddress userAddress = userAddressMapper.selectOne(singleAddress);
        return userAddress;
    }

}
