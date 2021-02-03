package com.zdhk.ipc.dto;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="Sms对象", description="")
public class SmsDto {
    private static final long serialVersionUID = 1L;
    private String code;

}
