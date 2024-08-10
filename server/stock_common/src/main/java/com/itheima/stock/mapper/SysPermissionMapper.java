package com.itheima.stock.mapper;

import com.itheima.stock.pojo.domain.PermissionDomain;
import com.itheima.stock.pojo.domain.UserTitleLevelDomain;
import com.itheima.stock.pojo.entity.SysPermission;
import com.itheima.stock.pojo.vo.PermissionInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author py-qz
* @description 针对表【sys_permission(权限表（菜单）)】的数据库操作Mapper
* @createDate 2024-06-20 15:00:05
* @Entity com.itheima.stock.pojo.entity.SysPermission
*/
public interface SysPermissionMapper {




    int deleteByPrimaryKey(Long id);

    int insert(SysPermission record);

    int insertSelective(SysPermission record);

    SysPermission selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SysPermission record);

    int updateByPrimaryKey(SysPermission record);


    List<String> findPermissionsByUserId(@Param("id") Long id);

    List<PermissionInfo> getAllPermissionsFrom();


    List<PermissionInfo> getAllPermissionsFromDatabase();

    List<PermissionDomain> getAllPermissions();

    List<UserTitleLevelDomain> getTitleLevelTree();

    int addPermission(@Param("id") Long id,@Param("type") Integer type,@Param("title") String title,@Param("pid") Long pid,@Param("url") String url,@Param("name") String name,@Param("icon") String icon,@Param("perms") String perms,@Param("method") String method,@Param("code") String code,@Param("orderNum") Integer orderNum);

    int updatePermission(@Param("id") Long id,@Param("type") Integer type,@Param("title") String title,@Param("pid") Long pid,@Param("url") String url,@Param("name") String name,@Param("icon") String icon,@Param("perms") String perms,@Param("method") String method,@Param("code") String code,@Param("orderNum") Integer orderNum);

    void deletePermission(@Param("permissionId") Long permissionId);
}
