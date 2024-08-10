package com.itheima.stock.pojo.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InquireUserInfoByRoleId {
    private String id;
    private String username;
    private String phone;
    private String nickName;
    private String realName;
    private Integer sex;
    private Integer status;
    private String email;
}
