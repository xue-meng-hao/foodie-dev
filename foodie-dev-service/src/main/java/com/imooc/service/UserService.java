package com.imooc.service;

import com.imooc.pojo.bo.UserBO;
import com.imooc.pojo.Users;

public interface UserService {
    /**
     * 判断用户名是否存在
     */
    public boolean queryUserNameIsExist(String UserName);

    /**
     * 创建用户
     *
     * @param userBO
     * @return
     */
    public Users createUser(UserBO userBO);

    /**
     * 根据用户名和密码查询账号是否存在
     *
     * @param username
     * @param password
     * @return
     */
    public Users queryUserForLogin(String username, String password);
}
