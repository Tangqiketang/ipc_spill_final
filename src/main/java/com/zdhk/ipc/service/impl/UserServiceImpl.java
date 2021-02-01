package com.zdhk.ipc.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zdhk.ipc.constant.ConfigConst;
import com.zdhk.ipc.entity.SysPermission;
import com.zdhk.ipc.entity.SysRole;
import com.zdhk.ipc.mapper.SysPermissionExtMapper;
import com.zdhk.ipc.mapper.SysPermissionMapper;
import com.zdhk.ipc.mapper.SysRoleMapper;
import com.zdhk.ipc.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 描述:
 *
 * @auther WangMin
 * @create 2020-12-02 17:10
 */
@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Resource
    private SysRoleMapper sysRoleMapper;
    @Resource
    private SysPermissionMapper sysPermissionMapper;
    @Resource
    private SysPermissionExtMapper sysPermissionExtMapper;

    @Override
    public boolean isSuperAdmin(Long userId) {
        boolean flag = false;
        List<SysRole> roleList = sysRoleMapper.selectList(new LambdaQueryWrapper<SysRole>().inSql(SysRole::getId,"select role_id from sys_user_role where user_id ="+userId));
        if(roleList != null && roleList.size() > 0){
            for (SysRole role : roleList){
                if(role.getRoleCode().equals(ConfigConst.SUPER_ADMIN)){
                    flag =true;
                    break;
                }
            }
        }
        return flag;
    }

    @Override
    public List<SysPermission> getAllMenuList() {
        return sysPermissionMapper.selectList(new LambdaQueryWrapper<SysPermission>().eq(SysPermission::getDelFlag,0).in(SysPermission::getMenuType,1,0).orderByAsc(SysPermission::getParentId,SysPermission::getId));
    }

    @Override
    public List<SysPermission> getUserMenuListByUserId(Long id) {
        return sysPermissionExtMapper.getMenuListByUserId(id);
    }
}
