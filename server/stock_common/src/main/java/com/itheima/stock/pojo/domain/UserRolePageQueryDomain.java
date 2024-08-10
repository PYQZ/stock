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
public class UserRolePageQueryDomain {
    private Long id;
    private String name;
    private String description;
    private Integer status;
    private Date createTime;
    private Date updateTime;
    private Integer deleted;

}
