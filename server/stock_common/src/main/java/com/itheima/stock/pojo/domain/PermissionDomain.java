package com.itheima.stock.pojo.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PermissionDomain {
    private Long id;
    private String code;
    private String title;
    private String icon;
    private String perms;
    private String url;
    private String method;
    private String name;
    private Long pid;
    private Integer orderNum;
    private Integer type;
    private Integer status;
    private Date createTime;
    private Date updateTime;
    private Integer deleted;

}
