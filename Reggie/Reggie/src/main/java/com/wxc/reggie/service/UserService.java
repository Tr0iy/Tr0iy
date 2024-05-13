package com.wxc.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wxc.reggie.common.R;
import com.wxc.reggie.entity.User;

import javax.servlet.http.HttpSession;
import java.util.Map;

public interface UserService extends IService<User> {
    R<String> sendMsg(User user, HttpSession session);

    R<User> login(Map map, HttpSession session);
}
