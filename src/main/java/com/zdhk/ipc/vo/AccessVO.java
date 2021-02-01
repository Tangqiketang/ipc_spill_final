package com.zdhk.ipc.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@ApiModel(description = "登录后用户信息",value = "登录后用户信息")
@Data
public class AccessVO {
    @ApiModelProperty(value = "登录用户token")
    private String token;

    @ApiModelProperty(value = "登录用户token过期时间")
    private long tokenExpire;

    @ApiModelProperty(value = "登录用户名")
    private String userName;

    @ApiModelProperty(value = "头像")
    private String avatar;

    @ApiModelProperty(value = "手机号")
    private String phone;

    @ApiModelProperty(value = "姓名")
    private String realName;

    @ApiModelProperty(value = "菜单")
    private List<UserMenuShowVO> menuList;
}
