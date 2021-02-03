package com.zdhk.ipc.dto;


import com.zdhk.ipc.entity.SysPermission;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class UserMenuDto implements Serializable  {
    private static final long serialVersionUID = 1L;
    
    private Integer id;

    private Integer parentId;

    private String name;

    private String url;

    private List<UserMenuDto> childrenList = new ArrayList<UserMenuDto>();


    public UserMenuDto(SysPermission sysPermission) {
        this.name = sysPermission.getName();
        this.url = sysPermission.getUrl();
        this.id = sysPermission.getId();
        this.parentId = sysPermission.getParentId();
    }
}
