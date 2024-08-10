package com.itheima.stock.vo.resp;

import com.itheima.stock.pojo.domain.UserTitleLevelDomain;
import com.itheima.stock.pojo.vo.PermissionInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PermissionTreeBuilder {
    public static List<PermissionInfo> buildPermissionTree(List<PermissionInfo> allPermissions) {
        Map<String, List<PermissionInfo>> permissionMap = new HashMap<>();
        for (PermissionInfo permission : allPermissions) {
            permissionMap.computeIfAbsent(permission.getPid(), k -> new ArrayList<>()).add(permission);
        }
        return buildTree(permissionMap, "0");
    }

    private static List<PermissionInfo> buildTree(Map<String, List<PermissionInfo>> permissionMap, String parentId) {
        List<PermissionInfo> permissions = permissionMap.get(parentId);
        if (permissions == null) {
            return new ArrayList<>();
        }
        for (PermissionInfo permission : permissions) {
            List<PermissionInfo> children = buildTree(permissionMap, permission.getId());
            permission.setChildren(children);
        }
        return permissions;
    }


}
