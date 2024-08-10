package com.itheima.stock.mapper;

import com.itheima.stock.pojo.domain.InquireUserInfoById;
import com.itheima.stock.pojo.domain.InquireUserInfoByRoleId;
import com.itheima.stock.pojo.domain.UserDTO;
import com.itheima.stock.pojo.entity.SysUser;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;


import java.util.List;

/**
* @author py-qz
* @description 针对表【sys_user(用户表)】的数据库操作Mapper
* @createDate 2024-06-20 15:00:05
* @Entity com.itheima.stock.pojo.entity.SysUser
*/
public interface SysUserMapper {

    int deleteByPrimaryKey(Long id);

    int insert(SysUser record);

    int insertSelective(SysUser record);

    SysUser selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SysUser record);

    int updateByPrimaryKey(SysUser record);
    SysUser findByUserName(@Param("name") String userName);

    int addUserInfo(UserDTO userDTO);

    @Delete({
            "<script>",
            "DELETE FROM sys_user WHERE id IN",
            "<foreach item='id' collection='userIds' open='(' separator=',' close=')'>",
            "#{id}",
            "</foreach>",
            "</script>"
    })
    void deleteUserByIds(@Param("userIds") List<Long> userIds);

    InquireUserInfoByRoleId inquireUserInfo(@Param("userId") String userId);

    @Update("UPDATE sys_user SET username = #{username}, phone = #{phone}, email = #{email}, nick_name = #{nickName}, real_name = #{realName}, sex = #{sex}, create_where = #{createWhere}, status = #{status} WHERE id = #{id}")
    int updateUserInfoById(@Param("id") Long id, @Param("username") String username, @Param("phone") String phone,
                           @Param("email") String email, @Param("nickName") String nickName, @Param("realName") String realName,
                           @Param("sex") Integer sex, @Param("createWhere") Integer createWhere, @Param("status") Integer status);



//    int addUserInfo(@Param("username")String username, @Param("password")String password, @Param("phone") String phone, @Param("email") String email, @Param("nickName") String nickName, @Param("realName") String realName, @Param("sex") int sex, @Param("createWhere") int createWhere, @Param("status") int status);
}
