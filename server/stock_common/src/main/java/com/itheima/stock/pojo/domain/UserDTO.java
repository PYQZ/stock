package com.itheima.stock.pojo.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDTO {
    private Long id;
    private String username;
    private String password;
    private String phone;
    private String email;
    private String nickName;
    private String realName;
    private int sex;
    private int createWhere;
    private int status;
    private LocalDateTime createdTime;
}
