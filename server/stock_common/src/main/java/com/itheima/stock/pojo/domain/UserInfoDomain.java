package com.itheima.stock.pojo.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserInfoDomain {
    private Long id;
    private String username;
    private String password;
    private String phone;
    private String realName;
    private String nickName;
    private String email;
    private int status;
    private int sex;
    private int deleted;
    private long createId;
    private long updateId;
    private int createWhere;
    private Date createTime;
    private Date updateTime;
    private String createUserName;
    private String updateUserName;


}
