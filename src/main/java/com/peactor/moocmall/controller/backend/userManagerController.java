package com.peactor.moocmall.controller.backend;

import com.peactor.moocmall.common.Const;
import com.peactor.moocmall.common.ServerResponse;
import com.peactor.moocmall.pojo.User;
import com.peactor.moocmall.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/manager/user")
public class userManagerController {

    @Autowired
    private IUserService iUserService;

    @RequestMapping(value = "/login.do", method = RequestMethod.POST)
    public ServerResponse<User> login(HttpSession session, String username, String password) {
        ServerResponse<User> response = iUserService.login(username, password);
        if(response.isSuccess()) {
            User user = response.getData();
            if(user.getRole().equals(Const.Role.ROLE_ADMIN)) {
                session.setAttribute(Const.CURRENT_USER, user);
                return response;
            } else {
                return ServerResponse.error("不是管理员,无法登录");
            }
        }
        return response;
    }

}
