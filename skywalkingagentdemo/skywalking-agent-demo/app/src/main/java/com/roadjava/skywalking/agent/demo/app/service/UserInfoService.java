package com.roadjava.skywalking.agent.demo.app.service;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.roadjava.skywalking.agent.demo.app.entity.UserInfoDO;
import com.roadjava.skywalking.agent.demo.app.mapper.UserInfoMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author zhaodaowen
 */
@Service
@Slf4j
public class UserInfoService {

    @Resource
    private UserInfoMapper userInfoMapper;

    public List<UserInfoDO> selectUserList(String userName) {
        LambdaQueryWrapper<UserInfoDO> lqw = new LambdaQueryWrapper<>();
        lqw.like(UserInfoDO::getUserName,userName);
        List<UserInfoDO> userDOS = userInfoMapper.selectList(lqw);
        log.info("selectList result:{}",userDOS);
        return userDOS;
    }
}
