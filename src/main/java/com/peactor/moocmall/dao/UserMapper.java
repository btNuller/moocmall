package com.peactor.moocmall.dao;

import com.peactor.moocmall.common.ServerResponse;
import com.peactor.moocmall.pojo.User;
import org.apache.ibatis.annotations.Param;

public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    int countByUsername(String username);

    int countByEmail(String email);

    String selectQuestion(String username);

    User selectLogin(@Param("username") String username,@Param("password") String password);

    int checkAnswer(@Param("username") String username, @Param("question") String question, @Param("answer") String answer);

    int resetPassword(@Param("username") String username, @Param("password") String password);

    int checkPassword(@Param("oldPassword") String oldPassword, @Param("id") Integer id);

    int countByEmailOther(@Param("id") int id, @Param("email") String email);
}