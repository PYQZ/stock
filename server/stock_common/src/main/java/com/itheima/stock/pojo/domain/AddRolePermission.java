package com.itheima.stock.pojo.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddRolePermission {
    private String name;
    private String description;
    private List<Long> permissionsIds;
}
