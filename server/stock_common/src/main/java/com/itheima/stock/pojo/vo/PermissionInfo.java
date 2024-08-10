package com.itheima.stock.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 权限表
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PermissionInfo implements Serializable {
    private String id;
    private String title;
    private String icon;
    private String path;
    private String name;
    private String pid;  // 父权限ID
    private List<PermissionInfo> children = new ArrayList<>();

    private static final long serialVersionUID = 1L;
}