package com.roadjava.skywalking.agent.demo.app.controller;


import com.roadjava.skywalking.agent.demo.app.entity.UserInfoDO;
import com.roadjava.skywalking.agent.demo.app.service.UserInfoService;
import lombok.Data;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author zhaodaowen
 */
@RestController
@RequestMapping("/userInfo")
public class UserInfoController {
    @Resource
    private UserInfoService userInfoService;

    @GetMapping("/list")
    public List<UserInfoDO> selectUserList(String userName) {
        return userInfoService.selectUserList(userName);
    }

    public boolean execute() {
        return false;
    }
}
