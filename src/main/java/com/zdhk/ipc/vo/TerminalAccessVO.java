package com.zdhk.ipc.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(description = "设备登录后用户信息",value = "设备登录后用户信息")
@Data
public class TerminalAccessVO {
    @ApiModelProperty(value = "登录用户token")
    private String token;

    @ApiModelProperty(value = "登录用户token过期时间")
    private long tokenExpire;

    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "登录账号")
    private String username;

    @ApiModelProperty(value = "车牌号")
    private String plate;

}
