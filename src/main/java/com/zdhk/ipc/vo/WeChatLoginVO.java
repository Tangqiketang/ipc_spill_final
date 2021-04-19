package com.zdhk.ipc.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author WangMin
 * @description 微信登录后用户信息
 * @date 2021/2/25
 */
@ApiModel(description = "微信登录后用户信息", value = "微信登录后用户信息")
@Data
public class WeChatLoginVO {

    @ApiModelProperty(value = "用户唯一标识openId")
    private String openId;

    @ApiModelProperty(value = "登录用户token")
    private String token;

    @ApiModelProperty(value = "登录用户token过期时间")
    private long tokenExpire;

}