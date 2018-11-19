package com.peactor.moocmall.service.impl;

import com.peactor.moocmall.common.Const;
import com.peactor.moocmall.common.ServerResponse;
import com.peactor.moocmall.common.TokenCache;
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
            } else if(type.equals(Const.EMAIL)) {
                if(checkEmail(str)) {
                    return ServerResponse.error("邮箱已存在");
                }
            } else {
                return ServerResponse.error("参数错误");
            }
        }
        return ServerResponse.success("校验成功");
    }

    /**
     * 查询该用户的安全问题
     * @param username
     * @return
     */
    @Override
    public ServerResponse<String> selectQuestion(String username) {
        ServerResponse<String> valid = checkValid(username, Const.USERNAME);
        if(valid.isSuccess()) {
            //用户不存在
            return ServerResponse.error("用户不存在");
        }
        String question = userMapper.selectQuestion(username);
        if(StringUtils.isBlank(question)) {
            return ServerResponse.error("找回密码的问题不存在");
        }
        return ServerResponse.success(question);
    }

    /**
     * 校验问题答案
     * @param username
     * @param question
     * @param answer
     * @return
     */
    @Override
    public ServerResponse<String> checkAnswer(String username, String question, String answer) {
        int resultCount = userMapper.checkAnswer(username, question, answer);
        if(0 == resultCount) {
            return ServerResponse.error("问题答案错误");
        }
        String forgetToken = UUID .randomUUID().toString();
        TokenCache.setKey(TokenCache.TOKEN_PREFIX + username, forgetToken);
        return ServerResponse.success(forgetToken);
    }

    /**
     * 验证是否存在该邮箱
     * @param email
     * @return
     */
    public boolean checkEmail(String email) {
        return 0 < userMapper.countByEmail(email);
    }

    /**
     * 校验用户名, true为存在用户名，false为不存在用户名
     * @param username
     * @return
     */
    public boolean checkUsername(String username) {
        return 0 < userMapper.countByUsername(username);
    }

    @Override
    public ServerResponse<String> forgetResetPassword(String username, String newPassword, String forgetToken) {
        if(StringUtils.isBlank(forgetToken)) {
            return ServerResponse.error("参数错误,token需要传递");
        }
        ServerResponse<String> valid = checkValid(username, Const.USERNAME);
        if(valid.isSuccess()) {
            return ServerResponse.error("用户不存在");
        }
        String token = TokenCache.getKey(TokenCache.TOKEN_PREFIX + username);
        if(StringUtils.isBlank(token)) {
            return ServerResponse.error("token无效或过期");
        }
        if(StringUtils.equals(forgetToken, token)) {
            String password = MD5Util.MD5EncodeUtf8(newPassword);
            int rowCount = userMapper.resetPassword(username, password);
            if(rowCount > 0) {
                return ServerResponse.success("修改密码成功");
            }
        } else {
            return ServerResponse.error("Token校验失败,请重新获取重置密码的token");
        }
        return ServerResponse.error("修改密码失败");
    }

    @Override
    public ServerResponse<String> resetPassword(User user, String oldPassword, String newPassword) {
        //防止横向越权,验证旧密码,一定要指定的是这个用户
        int rowcount = userMapper.checkPassword(MD5Util.MD5EncodeUtf8(oldPassword), user.getId());
        if(0 == rowcount) {
            return ServerResponse.error("旧密码错误");
        }
        user.setPassword(MD5Util.MD5EncodeUtf8(newPassword));
        int updateCount = userMapper.updateByPrimaryKeySelective(user);
        if(updateCount > 0) {
            return ServerResponse.success("密码更新成功");
        }
        return ServerResponse.error("密码更新失败");
    }

    /**
     * username不能被更新,新的demal是否已经存在,并且存在的email如果相同的话,不能是当前用户的
     * @param user
     * @return
     */
    @Override
    public ServerResponse<User> updateInfomation(User user) {
        if(checkEmailOther(user.getId(), user.getEmail())) {
            return ServerResponse.error("已经存在该邮箱,请更换email再尝试");
        }
        User record = new User();
        record.setId(user.getId());
        record.setEmail(user.getEmail());
        record.setPhone(user.getPhone());
        record.setQuestion(user.getQuestion());
        record.setAnswer(user.getAnswer());
        int updateCount = userMapper.updateByPrimaryKeySelective(record);
        if(0 < updateCount) {
            return ServerResponse.success("更新个人信息成功");
        }
        return ServerResponse.error("更新个人信息失败");
    }

    /**
     * 检验除了该账号的邮箱是否存在
     * @param id
     * @param email
     * @return
     */
    public boolean checkEmailOther(int id, String email) {
        return 0 < userMapper.countByEmailOther(id, email);
    }

    @Override
    public ServerResponse<User> getInfomation(Integer id) {
        User user = userMapper.selectByPrimaryKey(id);
        if(null == user) {
            return ServerResponse.error("找不到当前用户");
        }
        user.setPassword(StringUtils.EMPTY);
        return ServerResponse.success("获取用户信息成功");
    }

    @Override
    public ServerResponse checkAdminRole(User user) {
        if(null != user && Const.Role.ROLE_ADMIN == user.getRole().intValue()) {
            return ServerResponse.success();
        } else {
            return ServerResponse.error("找不到当前用户");
        }
    }
}
