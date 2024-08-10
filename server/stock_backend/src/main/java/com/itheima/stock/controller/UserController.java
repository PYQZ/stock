package com.itheima.stock.controller;

import com.itheima.stock.pojo.domain.*;
import com.itheima.stock.pojo.entity.SysUser;
import com.itheima.stock.pojo.vo.PermissionInfo;
import com.itheima.stock.service.UserService;
import com.itheima.stock.vo.req.LoginReqVo;
import com.itheima.stock.vo.req.PermissionAddVo;
import com.itheima.stock.vo.req.PermissionIdAddVo;
import com.itheima.stock.vo.resp.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@Api(value = "用户认证相关接口定义",tags = "用户功能-用户登录功能")
public class UserController {
    @Autowired
    private UserService userService;

    //条件综合查询用户分页信息，条件包含：分页信息 用户创建日期范围
    @PostMapping("/users")
    public R<PageResult> getStockPageInfoByMultiCondition(
            @RequestParam(name = "pageNum", required = false, defaultValue = "1") Integer pageNum,
            @RequestParam(name = "pageSize", required = false, defaultValue = "20") Integer pageSize)
        {
        return userService.getStockPageInfoByMultiCondition(pageNum, pageSize);
    }
    @PostMapping("/user")
    public ResponseEntity<Map> addUserInformation(@RequestBody UserDTO userDTO){
        String msg = userService.addUserInformation(
                userDTO.getUsername(), userDTO.getPassword(), userDTO.getPhone(),
                userDTO.getEmail(), userDTO.getNickName(), userDTO.getRealName(),
                userDTO.getSex(), userDTO.getCreateWhere(), userDTO.getStatus()
        );
        Map<String, Object> response = new HashMap<>();
        if ("添加成功".equals(msg)) {
            response.put("code",1);
            response.put("msg", "操作成功");
        } else {
            response.put("code",0);
            response.put("msg", msg);
        }

        return ResponseEntity.ok(response);

    }

    //获取用户具有的角色信息，以及所有角色信息
    //？空，没想到思路


    @GetMapping("/user/{username}")
    @ApiOperation(value = "根据用户名查询用户信息",notes = "用户信息查询",response = SysUser.class)
    @ApiImplicitParam(paramType = "path",name = "userName",value = "用户名",required = true)
    public SysUser getUserByUserName(@PathVariable("username") String userName){
        return userService.getUserByUserName(userName);
    }
    @PostMapping("/login")
    @ApiOperation(value = "用户登录功能",notes = "用户登录",response = R.class)
    public R<LoginRespVo> login(@RequestBody LoginReqVo vo){
        R<LoginRespVo> r = this.userService.login(vo);
        return r;
    }
    @GetMapping("/captcha")
    @ApiOperation(value = "验证码生成功能",response = R.class)
    public R<Map> getCaptchaCode(){
        return userService.getCaptchaCode();
    }

    @DeleteMapping("/user")
    public Map deletUsers(@RequestBody List<Long> userIds){
        Boolean result = userService.deleteUsersById(userIds);
        HashMap<Object, Object> userMap = new HashMap<>();
        if(result){
            userMap.put("code",1);
            userMap.put("msg","操作成功");
        }else{
            userMap.put("code",0);
            userMap.put("msg","操作失败");
        }
        return userMap;
    }
    //根据用户id查询用户信息
    @GetMapping("/user/info/{userId}")
    public R<InquireUserInfoByRoleId> inquireUserInfoById(@PathVariable("userId") String userId){
        //userId是临时给的，起因是账号id和实际id不匹配，具体情况后续再思考一下
        userId="1247515643591397376";
        return userService.inquireUserInfoById(userId);
    }
    //根据用户id修改用户信息
    @PutMapping("/user")
    public Map<String, Object> updateUserInfoById(@RequestBody InquireUserInfoById inquireUserInfoById){
        Map<String, Object> infos = userService.updateUserInfoById(inquireUserInfoById.getId(),inquireUserInfoById.getUsername(),inquireUserInfoById.getPhone(),
                                        inquireUserInfoById.getEmail(),inquireUserInfoById.getNickName(),
                                        inquireUserInfoById.getRealName(),inquireUserInfoById.getSex(),
                                        inquireUserInfoById.getCreateWhere(),
                                        inquireUserInfoById.getStatus() );
        return infos;
    }
    @PostMapping("/roles")
    public R<PageResult> getUserPageInfo(@RequestParam(name = "pageNum",required = false,defaultValue = "1") Integer pageNum,@RequestParam(name = "pageSize",required = false, defaultValue = "10") Integer pageSize){
        return userService.getUserPageInfo(pageNum,pageSize);
    }
    //添加角色回显权限选项功能
    @GetMapping("/permissions/tree/all")
    public R<List<PermissionInfo>> getSysPermissionTreeInfo(){
        return userService.getSysPermissionTreeInfo();
    }

    //根据角色id删除角色信息
    @DeleteMapping("/role/{roleId}")
    public Map<String, Object> deleteRole(@PathVariable("roleId") Long roleId){
        return userService.deleteRole(roleId);
    }
    @PostMapping("/role/{roleId}/{status}")
    public Map<String, Object> updateRoleStatus(@PathVariable("roleId") Long roleId,@PathVariable("status") Integer status){
        return userService.updateRolesInfo(roleId,status);
    }
    @GetMapping("/permissions")
    public R<List<PermissionDomain>> getAllPermissionsInfo(){
        return userService.getAllQueryPermissionsInfo();
    }
    //添加权限时回显权限树,仅仅显示目录和菜单
    @GetMapping("/permissions/tree")
    public R<List<UserTitleLevelDomain>> getTitleLevelTree(){
        return userService.getTitleLevelTree();
    }
    @PostMapping("/permission")
    public Map<String,Object> addPermission(@RequestBody PermissionAddVo vo){
        return userService.addPermission(
                vo.getType(),
                vo.getTitle(),
                vo.getPid(),
                vo.getUrl(),
                vo.getName(),
                vo.getIcon(),
                vo.getPerms(),
                vo.getMethod(),
                vo.getCode(),
                vo.getOrderNum()
        );
    }
    @PutMapping("/permission")
    public Map<String, Object> updatePermission(@RequestBody PermissionIdAddVo vo){
        return userService.updatePermission(
                vo.getId(),
                vo.getType(),
                vo.getTitle(),
                vo.getPid(),
                vo.getUrl(),
                vo.getType(),
                vo.getTitle(),
                vo.getPid(),
                vo.getUrl(),
                vo.getName(),
                vo.getIcon(),
                vo.getPerms(),
                vo.getMethod(),
                vo.getCode(),
                vo.getOrderNum()
        );
    }
    @DeleteMapping("/permission/{permissionId}")
    public Map<String, Object> deletePermission(@PathVariable("permissionId") Long permissionId){
        return userService.deletePermission(permissionId);
    }
    //添加角色和角色关联权限
    //-------------------------------错误----------------------------------
    @PostMapping("/role")
    public Map<String ,Object> addRoleAndPermission(@RequestBody AddRolePermission addRolePermission){
        Map<String ,Object> infos = userService.addRoleAndPermission(addRolePermission.getName(),addRolePermission.getDescription(),addRolePermission.getPermissionsIds());
        return infos;
    }


}
