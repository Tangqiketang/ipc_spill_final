package com.zdhk.ipc.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zdhk.ipc.entity.SysPermission;

import java.util.List;

/**
 * <p>
 * 菜单权限表 Mapper 接口
 * </p>
 *
 * @author Wang Min
 * @since 2020-12-02
 */
public interface SysPermissionExtMapper extends BaseMapper<SysPermission> {

    List<SysPermission> getMenuListByUserId(Long id);
}
