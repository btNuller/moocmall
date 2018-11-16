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

    User selectLogin(@Param("username") String username,@Param("password") String password);

    int countByEmail(String email);

    ServerResponse<String> selectQuestion(String username);

    int checkAnswer(@Param("username") String username, @Param("question") String question, @Param("answer") String answer);
}