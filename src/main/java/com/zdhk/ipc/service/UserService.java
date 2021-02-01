package com.zdhk.ipc.service;


import com.zdhk.ipc.entity.SysPermission;

import java.util.List;

public interface UserService {

    boolean isSuperAdmin(Long id);

    List<SysPermission> getAllMenuList();

    List<SysPermission> getUserMenuListByUserId(Long id);
}
