package com.itheima.stock.service.impl;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.itheima.stock.constant.StockConstant;
import com.itheima.stock.mapper.*;
import com.itheima.stock.pojo.domain.*;
import com.itheima.stock.pojo.entity.SysRolePermission;
import com.itheima.stock.pojo.entity.SysUser;
import com.itheima.stock.pojo.vo.PermissionInfo;
import com.itheima.stock.service.UserService;
import com.itheima.stock.utils.IdWorker;
import com.itheima.stock.vo.req.LoginReqVo;
import com.itheima.stock.vo.req.PermissionAddVo;
import com.itheima.stock.vo.resp.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;



import java.awt.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;



@Service("userService")
@Slf4j
public class UserServiceImpl implements UserService {
    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private SysPermissionMapper sysPermissionMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private IdWorker idWorker;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private StockRtInfoMapper stockRtInfoMapper;
    @Autowired
    private SysRoleMapper sysRoleMapper;
    @Autowired
    private SysRolePermissionMapper sysRolePermissionMapper;

    @Override
    public SysUser getUserByUserName(String userName) {
        SysUser user = sysUserMapper.findByUserName(userName);
        return user;
    }

    @Override
    public R<LoginRespVo> login(LoginReqVo vo) {
        if (vo == null || StringUtils.isBlank(vo.getUsername()) || StringUtils.isBlank(vo.getPassword())) {
            return R.error(ResponseCode.DATA_ERROR.getMessage());
        }
        if (StringUtils.isBlank(vo.getCode()) || StringUtils.isBlank(vo.getSessionId())) {
            return R.error(ResponseCode.DATA_ERROR);
        }
        String rCode = (String) redisTemplate.opsForValue().get(StockConstant.CHECH_PREFIX + vo.getSessionId());
        if (StringUtils.isBlank(rCode) || !rCode.equalsIgnoreCase(vo.getCode())) {
            return R.error(ResponseCode.CHECK_CODE_ERROR);
        }
        SysUser user = this.sysUserMapper.findByUserName(vo.getUsername());
        if (user == null || !passwordEncoder.matches(vo.getPassword(), user.getPassword())) {
            return R.error(ResponseCode.USERNAME_OR_PASSWORD_ERROR.getMessage());
        }
        if (!passwordEncoder.matches(vo.getPassword(), user.getPassword())) {
            return R.error(ResponseCode.USERNAME_OR_PASSWORD_ERROR);
        }
        //List<String> permissions = sysUserPermissionMapper.findPermissionsByUserId(user.getId());
        List<String> permissions = sysPermissionMapper.findPermissionsByUserId(user.getId());
        List<String> validPermissions = permissions.stream().
                filter(Objects::nonNull).
                filter(permission -> !permission.trim().isEmpty()).
                collect(Collectors.toList());
        List<PermissionInfo> allPermissions = sysPermissionMapper.getAllPermissionsFromDatabase();
        // 构建目录-菜单树
        PermissionTreeBuilder permissionTreeBuilder = new PermissionTreeBuilder();
        List<PermissionInfo> menus = permissionTreeBuilder.buildPermissionTree(allPermissions);


        LoginRespVo loginRespVo = new LoginRespVo();

        loginRespVo.setId(user.getId());
        loginRespVo.setUsername(user.getUsername());
        loginRespVo.setPhone(user.getPhone());
        loginRespVo.setNickName(user.getNickName());
        loginRespVo.setRealName(user.getRealName());
        loginRespVo.setSex(user.getSex());
        loginRespVo.setStatus(user.getStatus());
        loginRespVo.setEmail(user.getEmail());
        loginRespVo.setMenus(menus);
        loginRespVo.setPermissions(validPermissions);
        return R.ok(loginRespVo);

    }
    @Override
    public R<List<PermissionInfo>> getSysPermissionTreeInfo() {
        List<PermissionInfo> allPermissions = sysPermissionMapper.getAllPermissionsFromDatabase();
        PermissionTreeBuilder permissionTreeBuilder = new PermissionTreeBuilder();
        List<PermissionInfo> childrens = permissionTreeBuilder.buildPermissionTree(allPermissions);
        if(childrens.size()>0){
            return R.ok(childrens);
        }else {
            return  R.error(ResponseCode.NO_RESPONSE_DATA.getMessage());
        }

    }

    @Override
    public Map<String, Object> addRoleAndPermission(String name, String description, List<Long> permissionsIds) {
        Long id = idWorker.nextId();
        int result1 = sysRoleMapper.addNameDescription(id,name,description);

        int result2 = sysRolePermissionMapper.addRolePermissionId(id,permissionsIds);
        Map<String , Object> map = new HashMap<>();
        if(result1>0 && result2>0){
            map.put("code",1);
            map.put("msg","操作成功");
        }else {
            map.put("code",0);
            map.put("msg","操作失败");
        }
        return map;
    }

    @Override
    public Map<String, Object> deleteRole(Long roleId) {
        HashMap<String, Object> map = new HashMap<>();
        try {
            sysRoleMapper.deletRoleInfo(roleId);
            map.put("code",1);
            map.put("msg","操作成功");
        } catch (Exception e) {
            map.put("code",0);
            map.put("msg","操作失败");
        }return map;
    }

    @Override
    public Map<String, Object> updateRolesInfo(Long roleId, Integer status) {
        HashMap<String, Object> map = new HashMap<>();
        try {
            sysRoleMapper.updateRoleInfo(roleId,status);
            map.put("code",1);
            map.put("msg","操作成功");
        } catch (Exception e) {
            map.put("code",0);
            map.put("msg","操作失败");
        }return map;
    }

    @Override
    public R<List<PermissionDomain>> getAllQueryPermissionsInfo() {
        List<PermissionDomain> results = sysPermissionMapper.getAllPermissions();
        return R.ok(results);
    }

    @Override
    public R<List<UserTitleLevelDomain>> getTitleLevelTree() {
        List<UserTitleLevelDomain> infos = sysPermissionMapper.getTitleLevelTree();
        UserTitleLevelDomain topLevelMenu = new UserTitleLevelDomain();
        topLevelMenu.setId(Long.valueOf(0));
        topLevelMenu.setTitle("顶级菜单");
        topLevelMenu.setLevel(0);
        infos.add(0,topLevelMenu);
        return R.ok(infos);


    }

    @Override
    public Map<String, Object> addPermission(Integer type, String title, Long pid, String url, String name, String icon, String perms, String method, String code, Integer orderNum) {
        Long id = idWorker.nextId();
        int result = sysPermissionMapper.addPermission(id,type,title, pid, url, name, icon, perms, method, code, orderNum);
        Map<String, Object> responseMap = new HashMap<>();
        if (result > 0) {
            responseMap.put("code", 1);
            responseMap.put("msg", "添加成功");
        } else {
            responseMap.put("code", 0);
            responseMap.put("msg", "添加失败");
        }
        return responseMap;
    }

    @Override
    public Map<String, Object> updatePermission(Long id, Integer type, String title, Long pid, String url, Integer type1, String title1, Long pid1, String url1, String name, String icon, String perms, String method, String code, Integer orderNum) {
        Map<String, Object> responseMap = new HashMap<>();

        try {
            sysPermissionMapper.updatePermission(id,type,title, pid, url, name, icon, perms, method, code, orderNum);
            responseMap.put("code",1);
            responseMap.put("msg","添加成功");
        } catch (Exception e) {
            responseMap.put("code",0);
            responseMap.put("msg","添加失败");
        }return responseMap;
    }

    @Override
    public Map<String, Object> deletePermission(Long permissionId) {
        Map<String, Object> responseMap = new HashMap<>();
        try {
            sysPermissionMapper.deletePermission(permissionId);
            responseMap.put("code",1);
            responseMap.put("msg","添加成功");
        } catch (Exception e) {
            responseMap.put("code",0);
            responseMap.put("msg","添加失败");
        }return responseMap;
    }

//    private List<PermissionInfo> buildMenuTree(List<String> permissions) {
//        // 转换 SysPermission 到 Permission
//        List<PermissionInfo> allPermissions = permissions.stream().map(this::convertToPermission).collect(Collectors.toList());
//
//        // 根据 pid 组织权限树
//        Map<String, List<PermissionInfo>> permissionMap = allPermissions.stream()
//                .collect(Collectors.groupingBy(PermissionInfo::getPid));
//
//        // 找到根节点（pid 为 "0" 的权限）
//        List<PermissionInfo> rootPermissions = permissionMap.getOrDefault("0", new ArrayList<>());
//
//        // 递归设置 children
//        for (PermissionInfo rootPermission : rootPermissions) {
//            setChildren(rootPermission, permissionMap);
//        }
//
//        return rootPermissions;
//    }
//
//    private PermissionInfo convertToPermission(PermissionInfo sysPermission) {
//        PermissionInfo permission = new PermissionInfo();
//        permission.setId(sysPermission.getId());
//        permission.setTitle(sysPermission.getTitle());
//        permission.setIcon(sysPermission.getIcon());
//        permission.setPath(sysPermission.getUrl());
//        permission.setName(sysPermission.getCode());
//        permission.setPid(sysPermission.getPid());
//        permission.setType(sysPermission.getType());
//        permission.setCode(sysPermission.getCode());
//        return permission;
//    }

    private void setChildren(PermissionInfo parent, Map<String, List<PermissionInfo>> permissionMap) {
        List<PermissionInfo> children = permissionMap.get(parent.getId());
        if (children != null) {
            parent.setChildren(children);
            for (PermissionInfo child : children) {
                setChildren(child, permissionMap);
            }
        } else {
            parent.setChildren(null); // 设置为空时，前端展示 null
        }
    }

//    @Override
//    public R<LoginRespVo> login(LoginReqVo vo) {
//        if(vo==null|| StringUtils.isBlank(vo.getUsername())||StringUtils.isBlank(vo.getPassword())){
//            return R.error(ResponseCode.DATA_ERROR.getMessage());
//        }
//        if(StringUtils.isBlank(vo.getCode())||StringUtils.isBlank(vo.getSessionId())){
//            return R.error(ResponseCode.DATA_ERROR);
//        }
//        String rCode = (String) redisTemplate.opsForValue().get(StockConstant.CHECH_PREFIX + vo.getSessionId());
//        if(StringUtils.isBlank(rCode)||!rCode.equalsIgnoreCase(vo.getCode())){
//            return R.error(ResponseCode.CHECK_CODE_ERROR);
//        }
//        SysUser user = this.sysUserMapper.findByUserName(vo.getUsername());
//        if(user==null||!passwordEncoder.matches(vo.getPassword(), user.getPassword())){
//            return R.error(ResponseCode.USERNAME_OR_PASSWORD_ERROR.getMessage());
//        }
//        if(!passwordEncoder.matches(vo.getPassword(),user.getPassword())){
//            return R.error(ResponseCode.USERNAME_OR_PASSWORD_ERROR);
//        }
//        LoginRespVo loginRespVo = new LoginRespVo();
//        BeanUtils.copyProperties(user,loginRespVo);
//        return R.ok(loginRespVo);
//    }

    @Override
    public R<Map> getCaptchaCode() {
        try {
            // 确保参数是正数
            int width = 250;
            int height = 100;
            int codeCount = 4;
            int lineCount = 5;

            if (width <= 0 || height <= 0 || codeCount <= 0 || lineCount <= 0) {
                throw new IllegalArgumentException("Width, height, codeCount and lineCount must be positive");
            }

            LineCaptcha captcha = CaptchaUtil.createLineCaptcha(width, height, codeCount, lineCount);
            captcha.setBackground(Color.lightGray);
            String code = captcha.getCode();
            String imageBase64 = captcha.getImageBase64();
            String sessionId = String.valueOf(idWorker.nextId());
            redisTemplate.opsForValue().set("CK:" + sessionId, code, 5, TimeUnit.MINUTES);

            HashMap<String, String> info = new HashMap<>();
            info.put("sessionId", sessionId);
            info.put("imageData", imageBase64);

            return R.ok(info);

        } catch (IllegalArgumentException e) {
            log.error("Error generating captcha: " + e.getMessage(), e);
            throw e;
        }

    }

    @Override
    public R<PageResult> getStockPageInfoByMultiCondition(Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<UserInfoDomain> infos = stockRtInfoMapper.getStockPageInfoByMultiCondition();
        if (CollectionUtils.isEmpty(infos)) {
            return R.error(ResponseCode.NO_RESPONSE_DATA.getMessage());
        }
        PageResult<UserInfoDomain> stockUserDomaininPageRrsult = new PageResult<>(new PageInfo<>(infos));
        return R.ok(stockUserDomaininPageRrsult);
    }

    @Override
    public String addUserInformation(String username, String password, String phone, String email, String nickName, String realName, int sex, int createWhere, int status) {
        UserDTO userDTO = new UserDTO();
        Long id = idWorker.nextId();
        userDTO.setId(id);
        userDTO.setUsername(username);
        userDTO.setPassword(password);
        userDTO.setPhone(phone);
        userDTO.setEmail(email);
        userDTO.setNickName(nickName);
        userDTO.setRealName(realName);
        userDTO.setSex(sex);
        userDTO.setCreateWhere(createWhere);
        userDTO.setStatus(status);
        LocalDateTime now = LocalDateTime.now();
        userDTO.setCreatedTime(now);
        int result = sysUserMapper.addUserInfo(userDTO);
        try {
            if (result > 0) {
                return "添加成功";
            } else {
                return "添加失败";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "出现异常: " + e.getMessage();
        }
    }

    @Override
    public Boolean deleteUsersById(List<Long> userIds) {
        try {
            sysUserMapper.deleteUserByIds(userIds);
            return true;
        } catch (Exception e) {
            return false;
        }

    }

    @Override
    public R<InquireUserInfoByRoleId> inquireUserInfoById(String userId) {
        InquireUserInfoByRoleId result = sysUserMapper.inquireUserInfo(userId);
        return R.ok(result);
    }

    @Override
    public Map updateUserInfoById(Long id, String username, String phone, String email, String nickName, String realName, Integer sex, Integer createWhere, Integer status) {
        int result = sysUserMapper.updateUserInfoById(id,username,phone,email,nickName,realName,sex,createWhere,status);
        HashMap<Object, Object> userMap = new HashMap<>();
        if(result>0){
            userMap.put("code",1);
            userMap.put("msg","操作成功");
        }else{
            userMap.put("code",0);
            userMap.put("msg","操作失败");
        }
//        userMap.put("code",1);
//        userMap.put("msg","操作成功");
        return userMap;
    }

    //分页查询有问题，目前只有一页数据
    @Override
    public R<PageResult> getUserPageInfo(Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        List<UserRolePageQueryDomain> infos = sysRoleMapper.getUserInfoByPage();
        if(CollectionUtils.isEmpty(infos)){
            return R.error(ResponseCode.NO_RESPONSE_DATA.getMessage());
        }
        PageResult<UserRolePageQueryDomain> pageResult = new PageResult<>(new PageInfo<>(infos));
        return R.ok(pageResult);

    }




}
