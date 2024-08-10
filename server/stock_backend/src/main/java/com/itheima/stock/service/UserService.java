package com.itheima.stock.service;

import com.itheima.stock.pojo.domain.InquireUserInfoByRoleId;
import com.itheima.stock.pojo.domain.PermissionDomain;
import com.itheima.stock.pojo.domain.UserTitleLevelDomain;
import com.itheima.stock.pojo.entity.SysUser;
import com.itheima.stock.pojo.vo.PermissionInfo;
import com.itheima.stock.vo.req.LoginReqVo;
import com.itheima.stock.vo.req.PermissionAddVo;
import com.itheima.stock.vo.resp.LoginRespVo;
import com.itheima.stock.vo.resp.PageResult;
import com.itheima.stock.vo.resp.R;

import java.util.List;
import java.util.Map;

public interface UserService {
    SysUser getUserByUserName(String userName);

    R<LoginRespVo> login(LoginReqVo vo);

    R<Map> getCaptchaCode();

    R<PageResult> getStockPageInfoByMultiCondition(Integer pageNum, Integer pageSize);

    String addUserInformation(String username, String password, String phone, String email, String nickName, String realName, int sex, int createWhere, int status);


    Boolean deleteUsersById(List<Long> userIds);


    R<InquireUserInfoByRoleId> inquireUserInfoById(String  userId);

    Map updateUserInfoById(Long id, String username, String phone, String email, String nickName, String realName, Integer sex, Integer createWhere, Integer status);

    R<PageResult> getUserPageInfo(Integer pageNum, Integer pageSize);


    R<List<PermissionInfo>> getSysPermissionTreeInfo();

    Map<String, Object> addRoleAndPermission(String name, String description, List<Long> permissionsIds);

    Map<String, Object> deleteRole(Long roleId);

    Map<String, Object> updateRolesInfo(Long roleId, Integer status);

    R<List<PermissionDomain>> getAllQueryPermissionsInfo();


    R<List<UserTitleLevelDomain>> getTitleLevelTree();


    Map<String, Object> addPermission(Integer type, String title, Long pid, String url, String name, String icon, String perms, String method, String code, Integer orderNum);

    Map<String, Object> updatePermission(Long id, Integer type, String title, Long pid, String url, Integer type1, String title1, Long pid1, String url1, String name, String icon, String perms, String method, String code, Integer orderNum);

    Map<String, Object> deletePermission(Long permissionId);
}
