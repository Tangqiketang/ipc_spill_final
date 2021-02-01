package com.zdhk.ipc.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.time.LocalDateTime;
import java.io.Serializable;
import java.util.Date;

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
public class IpcCamera extends Model<IpcCamera> {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "状态 0离线 1在线")
    private Integer cameraStatus;

    @NotBlank(message = "相机名称不能为空")
    @Size(min=1, max=20)
    private String cameraName;

    @NotBlank
    @ApiModelProperty(value = "设备厂家")
    private String cameraCompany;

    @ApiModelProperty(value = "设备型号")
    private String cameraModelType;

    @ApiModelProperty(value = "连接参数")
    private String cameraUrl;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
