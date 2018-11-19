package com.peactor.moocmall.service;

import com.peactor.moocmall.common.ServerResponse;
import com.peactor.moocmall.pojo.User;

public interface IUserService {

    /**
     * 用户登录，验证用户名是否存在，验证用户名和密码是否正确，清除密码信息，返回user对象
     * @param username
     * @param password
     * @return
     */
    ServerResponse<User> login(String username, String password);

    /**
     * 注册用户
     * @param user
     * @return
     */
    ServerResponse<String> register(User user);

    /**
     * 校验对应类型的数据是否存在
     * （用户名 / email）
     * @param str
     * @param type
     * @return
     */
    ServerResponse<String> checkValid(String str, String type);

    /**
     * 查询该用户的安全问题
     * @param username
     * @return
     */
    ServerResponse<String> selectQuestion(String username);

    /**
     * 校验问题答案
     * @param username
     * @param question
     * @param answer
     * @return
     */
    ServerResponse<String> checkAnswer(String username, String question, String answer);

    /**
     * 验证是否存在该邮箱
     * @param email
     * @return
     */
    boolean checkEmail(String email);

    /**
     * 校验用户名, true为存在用户名，false为不存在用户名
     * @param username
     * @return
     */
    boolean checkUsername(String username);

    /**
     * ,校验参数,token,重置密码
     * @param username
     * @param newPassword
     * @param forgetToken
     * @return
     */
    ServerResponse<String> forgetResetPassword(String username, String newPassword, String forgetToken);

    ServerResponse<String> resetPassword(User user, String oldPassword, String newPassword);

    ServerResponse<User> updateInfomation(User user);

    boolean checkEmailOther(int id, String email);

    ServerResponse<User> getInfomation(Integer id);

    ServerResponse checkAdminRole(User user);
}
