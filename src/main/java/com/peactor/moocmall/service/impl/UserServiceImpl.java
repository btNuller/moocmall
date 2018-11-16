package com.peactor.moocmall.service.impl;

import com.peactor.moocmall.common.Const;
import com.peactor.moocmall.common.ServerResponse;
import com.peactor.moocmall.dao.UserMapper;
import com.peactor.moocmall.pojo.User;
import com.peactor.moocmall.service.IUserService;
import com.peactor.moocmall.util.MD5Util;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    private UserMapper userMapper;

    /**
     * 用户登录，验证用户名是否存在，验证用户名和密码是否正确，清除密码信息，返回user对象
     * @param username 用户名
     * @param password 密码
     * @return
     */
    @Override
    public ServerResponse<User> login(String username, String password) {
        if(!checkUsername(username)){
            return ServerResponse.error("用户名不存在");
        }

        String md5Password = MD5Util.MD5EncodeUtf8(password);
        User user = userMapper.selectLogin(username, md5Password);
        if(null == user) {
            return ServerResponse.error("用户名或密码错误");
        }
        //清除密码信息
        user.setPassword(StringUtils.EMPTY);
        return ServerResponse.success("登录成功", user);
    }

    /**
     * 注册用户
     * @param user
     * @return
     */
    @Override
    public ServerResponse<String> register(User user) {
        ServerResponse<String> validResponse = checkValid(user.getUsername(), Const.USERNAME);
        if(!validResponse.isSuccess()) {
            return validResponse;
        }
        validResponse = checkValid(user.getEmail(), Const.EMAIL);
        if(!validResponse.isSuccess()) {
            return validResponse;
        }
        user.setRole(Const.Role.ROLE_CUSTOMER);
        user.setPassword(MD5Util.MD5EncodeUtf8(user.getPassword()));
        int resultCount = userMapper.insert(user);
        if(0 == resultCount) {
            return ServerResponse.error("注册失败");
        }
        return ServerResponse.success("注册成功");
    }

    /**
     * 校验对应类型的数据是否存在
     * （用户名 / email）
     * @param str
     * @param type
     * @return
     */
    @Override
    public ServerResponse<String> checkValid(String str, String type) {
        if(StringUtils.isNotEmpty(str)) {
            if(type.equals(Const.USERNAME)) {
                if(checkUsername(str)) {
                    return ServerResponse.error("用户名已存在");
                }
            }
            if(type.equals(Const.EMAIL)) {
                if(checkEmail(str)) {
                    return ServerResponse.error("邮箱已存在");
                }
            }
        } else {
            return ServerResponse.error("参数错误");
        }
        return ServerResponse.success("校验成功");
    }

    @Override
    public ServerResponse<String> selectQuestion(String username) {
        return userMapper.selectQuestion(username);
    }

    @Override
    public ServerResponse<String> checkAnswer(String username, String question, String answer) {
        int resultCount = userMapper.checkAnswer(username, question, answer);
        if(0 == resultCount) {
            return ServerResponse.error("问题答案错误");
        }
        String forgetToken = UUID.randomUUID().toString();
        //TODO next
        return null;
    }

    /**
     * 验证是否存在该邮箱
     * @param email
     * @return
     */
    private boolean checkEmail(String email) {
        return 0 < userMapper.countByEmail(email);
    }

    /**
     * 校验用户名, true为存在用户名，false为不存在用户名
     * @param username
     * @return
     */
    private boolean checkUsername(String username) {
        return 0 < userMapper.countByUsername(username);
    }
}
