package com.itheima.stock.mapper;

import com.itheima.stock.pojo.entity.SysRolePermission;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author py-qz
* @description 针对表【sys_role_permission(角色权限表)】的数据库操作Mapper
* @createDate 2024-06-20 15:00:05
* @Entity com.itheima.stock.pojo.entity.SysRolePermission
*/
public interface SysRolePermissionMapper {

    int deleteByPrimaryKey(Long id);

    int insert(SysRolePermission record);

    int insertSelective(SysRolePermission record);

    SysRolePermission selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SysRolePermission record);

    int updateByPrimaryKey(SysRolePermission record);

    int addRolePermissionId(@Param("id") Long id,@Param("permissionsIds") List<Long> permissionsIds);
}
