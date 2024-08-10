package com.itheima.stock.vo.req;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PermissionAddVo {
    private Integer type;
    private String title;
    private Long pid;
    private String url;
    private String name;
    private String icon;
    private String perms;
    private String method;
    private String code;
    private Integer orderNum;

}
