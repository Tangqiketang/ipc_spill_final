package com.zdhk.ipc.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.util.Date;
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
 * @since 2021-01-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="TOrder对象", description="")
public class TOrder extends Model<TOrder> {

private static final long serialVersionUID=1L;

    private Long id;

    private String name;

    private Date createTime;

    private String userName;

    @TableField(exist = false)
    private Integer rowNum;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
