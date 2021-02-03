package com.zdhk.ipc.entity;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 描述:
 *
 * @auther WangMin
 * @create 2020-11-13 10:07
 */
@Data
public class WebsocketInfo {

    @ApiModelProperty(value = "用户.前端无需关心该字段")
    private String toUserId;

    @ApiModelProperty(value = "消息体")
    private Object content;
    @ApiModelProperty(value = "类型 1表示报警事件")
    private Integer type;

}
