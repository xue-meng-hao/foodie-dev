package com.imooc.service.center;

import com.imooc.pojo.Users;
import com.imooc.pojo.bo.center.CenterUserBO;

public interface CenterUserService {
    /**
     * 根据userId查询user
     *
     * @param userId
     * @return
     */
    public Users queryByUserId(String userId);

    /**
     * 修改用户信息
     *
     * @param userId
     * @param centerUserBO
     */
    public Users updateUserInfo(String userId, CenterUserBO centerUserBO);

    /**
     * 修改用户头像
     *
     * @param userId
     * @param faceUrl
     */
    public Users updateUserFace(String userId, String faceUrl);
}
