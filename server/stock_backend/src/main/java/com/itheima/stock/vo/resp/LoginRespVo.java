package com.itheima.stock.vo.resp;

import com.itheima.stock.pojo.vo.PermissionInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginRespVo {
    private Long id;
    private String phone;
    /**
     * 用户名
     */
    private String username;
    /**
     * 昵称
     */
    private String nickName;
    private String realName;
    private int sex;
    private int status;
    private String email;
    private List<PermissionInfo> menus;
    private List<String> permissions;

}
