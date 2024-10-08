package com.itheima.stock.mapper;

import com.itheima.stock.pojo.domain.UserRolePageQueryDomain;
import com.itheima.stock.pojo.entity.SysRole;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author py-qz
* @description 针对表【sys_role(角色表)】的数据库操作Mapper
* @createDate 2024-06-20 15:00:05
* @Entity com.itheima.stock.pojo.entity.SysRole
*/
public interface SysRoleMapper {

    int deleteByPrimaryKey(Long id);

    int insert(SysRole record);

    int insertSelective(SysRole record);

    SysRole selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SysRole record);

    int updateByPrimaryKey(SysRole record);

    List<UserRolePageQueryDomain> getUserInfoByPage();

    int addNameDescription(@Param("id") Long id,@Param("name") String name, @Param("description") String description);

    void deletRoleInfo(@Param("roleId") Long roleId);

    void updateRoleInfo(@Param("roleId") Long roleId,@Param("status") Integer status);
}
