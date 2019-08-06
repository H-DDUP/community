package com.xisan517.community.mapper;

import com.xisan517.community.model.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;


@Mapper
public interface UserMapper {
    @Insert("insert into user (name,account_id,token,gmt_create,gmt_modified) values(#{name},#{accountId},#{token},#{gmtCreate},#{gmtModified})")
    void insert(User user);

    @Select("SELECT * FROM `user`")
    List<User> findAllUser();

    @Select("select * from user where token=#{token}")
    User findByToken(@Param("token") String token);
}
