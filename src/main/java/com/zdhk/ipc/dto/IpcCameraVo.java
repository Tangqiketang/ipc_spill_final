package com.zdhk.ipc.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * <p>
 *
 * </p>
 *
 * @author Wang Min
 * @since 2020-11-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="IpcCamera对象", description="")
public class IpcCameraVo  {

    private static final long serialVersionUID=1L;


    @NotBlank(message = "相机名称不能为空")
    @Size(min=1, max=20)
    private String cameraName;


    @ApiModelProperty(value = "设备型号")
    private String cameraModelType;

    @ApiModelProperty(value = "连接参数")
    private String cameraUrl;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private long createTime;



}
