package com.imooc.service.impl;

import com.imooc.enums.Sex;
import com.imooc.mapper.UsersMapper;
import com.imooc.pojo.bo.UserBO;
import com.imooc.pojo.Users;
import com.imooc.service.UserService;
import com.imooc.utils.DateUtil;
import com.imooc.utils.MD5Utils;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UsersMapper mapper;
    /**
     * 用于生成唯一id
     */
    @Autowired
    private Sid sid;
    /**
     * 用户默认头像
     */
    public static final String USER_FACE = "http://up.enterdesk.com/edpic/31/c3/fd/31c3fdc63511cabedd6415d121fa2d58.jpg";

    /**
     * 判断用户名是否存在
     *
     * @param username
     * @return
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public boolean queryUserNameIsExist(String username) {
        Example userExample = new Example(Users.class);
        Example.Criteria userCriteria = userExample.createCriteria();
        userCriteria.andEqualTo("username", username);
        //通过条件进行查询
        Users result = mapper.selectOneByExample(userExample);
        return result != null;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public Users createUser(UserBO userBO) {
        Users users = new Users();
        String userId = sid.nextShort();
        users.setId(userId);
        users.setUsername(userBO.getUsername());
        try {
            //使用MD5加密
            users.setPassword(MD5Utils.getMD5Str(userBO.getPassword()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        users.setNickname(userBO.getUsername());
        //设置用户头像
        users.setFace(USER_FACE);
        //默认生日日期
        users.setBirthday(DateUtil.stringToDate("1999-01-01"));
        users.setSex(Sex.secret.type);
        users.setCreatedTime(new Date());
        users.setUpdatedTime(new Date());
        mapper.insert(users);
        return users;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public Users queryUserForLogin(String username, String password) {
        Example userExample = new Example(Users.class);
        Example.Criteria userCriteria = userExample.createCriteria();
        userCriteria.andEqualTo("username", username);
        userCriteria.andEqualTo("password", password);
        Users result = mapper.selectOneByExample(userExample);
        return result;
    }
}
