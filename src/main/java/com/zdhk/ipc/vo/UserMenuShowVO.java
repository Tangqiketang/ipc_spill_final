package com.zdhk.ipc.vo;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class UserMenuShowVO implements Serializable  {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "菜单名")
    private String name;
    @ApiModelProperty(value = "菜单链接")
    private String url;
    @ApiModelProperty(value = "子菜单")
    private List<UserMenuShowVO> childrenList = new ArrayList<UserMenuShowVO>();



}
