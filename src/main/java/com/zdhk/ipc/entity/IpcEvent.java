package com.zdhk.ipc.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author Wang Min
 * @since 2021-01-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="IpcEvent对象", description="")
public class IpcEvent extends Model<IpcEvent> {

private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Integer eventType;

    private String eventDesc;

    private String eventDevice;

    private String commonVideoUrl;

    private String commonPicUrl;

    private String detectVideoUrl;

    private String detectPicUrl;

    private Integer isHide;

    private Date eventTime;

    private Date createTime;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
