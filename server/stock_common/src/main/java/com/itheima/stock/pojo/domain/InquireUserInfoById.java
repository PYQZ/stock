package com.itheima.stock.pojo.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InquireUserInfoById {
    private Long id;
    private String username;
    private String phone;
    private String email;
    private String nickName;
    private String realName;
    private Integer sex;
    private Integer createWhere;
    private Integer status;

}
