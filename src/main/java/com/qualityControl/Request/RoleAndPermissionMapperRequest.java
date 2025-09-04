package com.qualityControl.Request;

import lombok.Data;

import java.util.List;

@Data
public class RoleAndPermissionMapperRequest {
    String roleName;
    List<RolePermission> permissions;

}